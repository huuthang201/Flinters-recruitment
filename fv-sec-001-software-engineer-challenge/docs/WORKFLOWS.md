# Workflows

## CLI Startup Flow

1. Capture `System.currentTimeMillis()` as the application start time.
2. Parse command-line arguments.
3. Validate `--input` is present.
4. Validate `--output` is present.
5. Validate argument values are present and non-empty.
6. Validate input path exists.
7. Validate input path is a regular file.
8. Validate output path.
9. Create output directory if it does not exist.
10. If any startup validation fails, print a clear message to stderr and call `System.exit(1)`.
11. Continue to CSV processing only after arguments and paths are valid.

## CSV Streaming Flow

1. Open the input file using `BufferedReader` with an 8MB buffer.
2. Read the first line.
3. Validate the first line is the expected CSV header.
4. If the header is missing or invalid, fail with a clear error.
5. Skip the header after validation.
6. Initialize line counter at `1` for the header.
7. Read each remaining line in a loop.
8. Increment line counter before validating each data row.
9. Split the row into raw tokens.
10. Pass raw tokens and line number to the row validation flow.
11. Pass only fully validated rows to aggregation.
12. Continue until `readLine()` returns `null`.
13. Close the reader with try-with-resources.

## Data Preprocessing Flow

For each row before aggregation:

1. Trim whitespace from all tokens.
2. Validate token count is exactly `6`; otherwise skip and warn.
3. Validate `campaign_id` is non-empty; otherwise skip and warn.
4. Validate `impressions` parses as a non-negative `long`; otherwise skip and warn.
5. Validate `clicks` parses as a non-negative `long`; otherwise skip and warn.
6. Validate `spend` parses as a non-negative `double`; otherwise skip and warn.
7. Validate `conversions` parses as a non-negative `long`; otherwise skip and warn.
8. Validate `date` matches `YYYY-MM-DD` format; otherwise skip and warn.
9. Return a typed validated row only when all checks pass.
10. Return an empty result for invalid rows.

## Aggregation Flow

1. Maintain a `HashMap<String, CampaignStats>` keyed by `campaign_id`.
2. For every validated row, get or create the campaign stats record.
3. Accumulate row values into campaign totals.

Aggregation operation:

```text
HashMap.computeIfAbsent(campaignId, CampaignStats::new)
    .accumulate(impressions, clicks, spend, conversions)
```

The map holds only one mutable aggregate per campaign and never stores raw input rows.

## Ranking Flow

CTR ranking:

1. Stream values from the aggregation `HashMap`.
2. Sort by CTR descending.
3. Break ties by `campaign_id` ascending.
4. Limit to 10 campaigns.
5. Collect to a list for writing.

CPA ranking:

1. Stream values from the aggregation `HashMap`.
2. Filter out campaigns where CPA is `null`.
3. Sort by CPA ascending.
4. Break ties by `campaign_id` ascending.
5. Limit to 10 campaigns.
6. Collect to a list for writing.

## Output Flow

For each output file:

1. Resolve output file path inside the output directory.
2. Open a writer with try-with-resources.
3. Write the header line.
4. Iterate the ranked campaign list.
5. Format each field using the required decimal precision.
6. Write one CSV line per campaign.
7. Flush the writer.
8. Close the writer automatically.

Output files:

| File | Source List |
|---|---|
| `top10_ctr.csv` | Top 10 by CTR descending |
| `top10_cpa.csv` | Top 10 by CPA ascending after excluding null CPA |

## Error Handling Flow

| Failure Point | Behavior |
|---|---|
| Missing `--input` | Print clear error to stderr and exit `1` |
| Missing `--output` | Print clear error to stderr and exit `1` |
| Unknown argument | Print clear error to stderr and exit `1` |
| Missing argument value | Print clear error to stderr and exit `1` |
| Input file missing | Print clear error to stderr and exit `1` |
| Input path not regular file | Print clear error to stderr and exit `1` |
| Output path is existing file | Print clear error to stderr and exit `1` |
| Output directory cannot be created | Print clear error to stderr and exit `1` |
| Empty input file | Print clear error to stderr and exit `1` |
| Invalid header | Print clear error to stderr and exit `1` |
| Malformed data row | Print warning to stderr with line number and reason; skip row; continue |
| Read error | Print clear error to stderr and exit `1` |
| Write error | Print clear error to stderr and exit `1` |

## Timing Flow

1. Capture start time at the beginning of `Main`.
2. Parse and validate arguments.
3. Process CSV.
4. Write output files.
5. Capture end time after output is complete.
6. Print elapsed time to stdout.
7. Print total campaigns processed to stdout.

Summary output should include:

```text
Processed campaigns: <count>
Elapsed time: <milliseconds> ms
```
