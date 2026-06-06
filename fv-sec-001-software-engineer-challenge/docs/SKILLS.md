# Agent Skills And Constraints

## Project Overview

This project is a Java 21 console application named **Ad Performance Aggregator**. It reads a large advertising performance CSV file, aggregates metrics by `campaign_id`, and writes two ranked CSV reports:

- `top10_ctr.csv`: top 10 campaigns by highest click-through rate.
- `top10_cpa.csv`: top 10 campaigns by lowest cost per acquisition, excluding campaigns with zero conversions.

Input CSV schema:

| Column | Type | Description |
|---|---|---|
| `campaign_id` | string | Campaign identifier used as the aggregation key |
| `date` | string | Date in `YYYY-MM-DD` format |
| `impressions` | integer | Number of ad impressions |
| `clicks` | integer | Number of ad clicks |
| `spend` | float | Advertising cost in USD |
| `conversions` | integer | Number of conversions |

Output files:

| File | Purpose |
|---|---|
| `top10_ctr.csv` | Campaigns sorted by CTR descending, limited to 10 rows |
| `top10_cpa.csv` | Campaigns with conversions sorted by CPA ascending, limited to 10 rows |

## Tech Stack

| Technology | Reason |
|---|---|
| Java 21 | Stable LTS runtime with strong standard library support for file IO, collections, records, and testing |
| Maven | Standard Java build tool for dependency management, lifecycle commands, and reproducible packaging |
| `maven-assembly-plugin` | Builds a runnable fat JAR matching the challenge run command |
| `BufferedReader` | Enables memory-efficient line-by-line CSV streaming without loading the full file |
| JUnit 5 | Modern Java testing framework for unit and integration-style tests |
| No external CSV libraries | Required by the challenge; implementation must use standard Java IO and simple token parsing |

## Memory Efficiency Rules

The input file is approximately 1GB. The program must never load the full CSV into memory.

- Stream the CSV line by line with `BufferedReader`.
- Use an 8MB buffer for efficient sequential reads.
- Store only aggregated campaign totals in memory, keyed by `campaign_id`.
- Do not store raw rows after validation.
- Do not build a list of all parsed rows.
- Use `HashMap<String, CampaignStats>` for aggregation.
- Keep ranking lists limited to the number of unique campaigns, not the number of rows.
- Close file handles with try-with-resources.

## Code Style Rules

- Keep one clear responsibility per class.
- Use meaningful class, method, and variable names.
- Keep fields private.
- Avoid dead code, commented-out code, and unused helpers.
- Prefer simple control flow over premature abstractions.
- Validate input near the boundary where it enters the system.
- Keep formatting deterministic and explicit.
- Use immutable return types where practical for parsed arguments and validated rows.
- Avoid backward-compatibility shims unless a concrete requirement appears.

## CLI Contract

Run command:

```bash
java -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar --input ad_data.csv --output results/
```

Required arguments:

| Argument | Required | Description |
|---|---:|---|
| `--input` | Yes | Path to the input CSV file |
| `--output` | Yes | Path to the output directory |

Validation behavior:

- If `--input` is missing, print a clear error to stderr and exit with status code `1`.
- If `--output` is missing, print a clear error to stderr and exit with status code `1`.
- If an argument value is missing, print a clear error to stderr and exit with status code `1`.
- If the input file does not exist, print a clear error to stderr and exit with status code `1`.
- If the input path is not a regular file, print a clear error to stderr and exit with status code `1`.
- If the output directory does not exist, create it.
- If the output path exists but is not a directory, print a clear error to stderr and exit with status code `1`.
- Unknown arguments should produce a clear error and exit with status code `1`.

## Package Structure

Base package: `com.adagg`

Expected source layout:

```text
src/main/java/com/adagg/
  Main.java
  model/
    CampaignStats.java
  processor/
    CsvAggregator.java
  writer/
    CsvWriter.java
  util/
    ArgParser.java
    RowValidator.java
```

Expected test layout:

```text
src/test/java/com/adagg/
  processor/
    CsvAggregatorTest.java
```

## Edge Cases

| Situation | Expected Behavior |
|---|---|
| Zero conversions | CPA is `null`; campaign is excluded from `top10_cpa.csv`; CPA column in CTR report may be empty |
| Zero impressions | CTR is `0.0000`; program does not divide by zero |
| Malformed rows | Skip the row, log warning to stderr with line number and reason, continue processing |
| Missing input file | Print clear error to stderr and exit with status code `1` |
| Empty file | Print clear error for missing/invalid header and exit with status code `1` |
| Duplicate `campaign_id` rows | Aggregate all valid rows into one campaign record |
| Negative values | Treat as malformed row; skip and warn |
| Invalid date format | Treat as malformed row; skip and warn |
| Empty `campaign_id` | Treat as malformed row; skip and warn |

## Metric Formulas

CTR:

```text
CTR = total_clicks / total_impressions
```

- Return `0.0` when `total_impressions = 0`.
- Format as 4 decimal places in output.
- Example: `0.0500`.

CPA:

```text
CPA = total_spend / total_conversions
```

- Return `null` when `total_conversions = 0`.
- Format as 2 decimal places in output when present.
- Example: `20.00`.

Spend:

- Aggregate using double precision.
- Format `total_spend` as 2 decimal places in output.

## Output CSV Format

Both output files use this exact column order:

```text
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
```

`top10_ctr.csv` rules:

- Include all campaigns, including campaigns with zero conversions.
- Sort by CTR descending.
- Limit to 10 rows.
- Format `total_spend` with 2 decimal places.
- Format `CTR` with 4 decimal places.
- Format `CPA` with 2 decimal places when present; write an empty field when CPA is `null`.

`top10_cpa.csv` rules:

- Exclude campaigns with zero conversions.
- Sort by CPA ascending.
- Limit to 10 rows.
- Format `total_spend` with 2 decimal places.
- Format `CTR` with 4 decimal places.
- Format `CPA` with 2 decimal places.

If two campaigns have equal ranking values, use `campaign_id` ascending as a deterministic tie-breaker.

## Definition Of Done

- Maven project builds successfully with Java 21.
- Fat JAR is generated by `maven-assembly-plugin`.
- CLI runs with `--input` and `--output` arguments.
- Input CSV is streamed line by line and never loaded fully into memory.
- Header is validated before row processing.
- Valid rows are aggregated by `campaign_id`.
- Malformed rows are skipped and warnings are logged to stderr.
- `top10_ctr.csv` is written with correct sorting, columns, and formatting.
- `top10_cpa.csv` excludes zero-conversion campaigns and is written with correct sorting, columns, and formatting.
- JUnit 5 tests cover aggregation, formulas, malformed rows, sorting, and zero-value edge cases.
- README documents setup, run command, libraries, processing time, memory usage, and structure.
- `../PROMPTS.md` records prompts used during development.
