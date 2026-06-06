# Coding Tasks

## TASK-01: `src/main/java/com/adagg/model/CampaignStats.java`

Purpose: Represent mutable aggregate metrics for one campaign and calculate CTR and CPA from totals.

Fields:

```java
private final String campaignId;
private long totalImpressions;
private long totalClicks;
private double totalSpend;
private long totalConversions;
```

Methods:

```java
public CampaignStats(String campaignId)
public void accumulate(long impressions, long clicks, double spend, long conversions)
public String getCampaignId()
public long getTotalImpressions()
public long getTotalClicks()
public double getTotalSpend()
public long getTotalConversions()
public double getCTR()
public Double getCPA()
```

Implementation workflow:

1. Create class in `com.adagg.model`.
2. Add private fields for campaign ID and mutable totals.
3. Initialize `campaignId` through constructor.
4. Implement `accumulate` by adding each numeric row value to the current totals.
5. Implement `getCTR` to return `0.0` when impressions are zero.
6. Implement `getCPA` to return `null` when conversions are zero.
7. Add simple getters for all fields.

Dependencies: None.

## TASK-02: `src/main/java/com/adagg/util/ArgParser.java`

Purpose: Parse and validate CLI arguments and return typed input and output paths.

Fields and records:

```java
public record ParsedArgs(Path inputPath, Path outputDir) {}
```

Methods:

```java
public ParsedArgs parse(String[] args)
```

Implementation workflow:

1. Create class in `com.adagg.util`.
2. Iterate over `args` by index.
3. Recognize only `--input` and `--output`.
4. Validate each recognized argument has a following value.
5. Reject unknown arguments.
6. Validate both required options are present.
7. Convert argument values to `Path`.
8. Validate input path exists and is a regular file.
9. If output path exists, validate it is a directory.
10. If output path does not exist, create directories.
11. Return `ParsedArgs`.
12. Throw `IllegalArgumentException` or `IOException` with clear messages on failure.

Dependencies: None.

## TASK-03: `src/main/java/com/adagg/util/RowValidator.java`

Purpose: Validate raw CSV tokens and return a typed row only when all row-level business rules pass.

Fields and records:

```java
public record ValidRow(
    String campaignId,
    String date,
    long impressions,
    long clicks,
    double spend,
    long conversions
) {}
```

Methods:

```java
public Optional<ValidRow> validate(String[] tokens, long lineNumber)
```

Implementation workflow:

1. Create stateless class in `com.adagg.util`.
2. Validate token count equals `6`.
3. Trim all token values.
4. Validate `campaign_id` is not empty.
5. Validate `date` matches `YYYY-MM-DD`.
6. Parse `impressions` as `long`.
7. Parse `clicks` as `long`.
8. Parse `spend` as `double`.
9. Parse `conversions` as `long`.
10. Validate all numeric values are non-negative.
11. On failure, print warning to stderr with line number and reason.
12. On success, return `Optional.of(new ValidRow(...))`.
13. On failure, return `Optional.empty()`.

Dependencies: None.

## TASK-04: `src/main/java/com/adagg/processor/CsvAggregator.java`

Purpose: Stream the input CSV, validate rows, aggregate campaign totals, and expose ranking lists.

Fields:

```java
private static final int BUFFER_SIZE = 8 * 1024 * 1024;
private final RowValidator rowValidator;
private final Map<String, CampaignStats> campaigns;
```

Methods:

```java
public CsvAggregator(RowValidator rowValidator)
public void process(Path inputPath) throws IOException
public List<CampaignStats> getTop10ByCTR()
public List<CampaignStats> getTop10ByCPA()
public int getCampaignCount()
```

Implementation workflow:

1. Create class in `com.adagg.processor`.
2. Initialize a `HashMap<String, CampaignStats>`.
3. Open input with `BufferedReader` and 8MB buffer.
4. Read first line.
5. Validate expected header.
6. Iterate remaining lines with line number tracking.
7. Split each line into tokens.
8. Call `RowValidator.validate(tokens, lineNumber)`.
9. If validator returns empty, continue to next line.
10. If validator returns a row, use `computeIfAbsent` for the campaign ID.
11. Call `accumulate` with typed row values.
12. Implement CTR ranking by sorting map values by `getCTR()` descending, tie-breaking by campaign ID, and limiting to 10.
13. Implement CPA ranking by filtering `getCPA() != null`, sorting by CPA ascending, tie-breaking by campaign ID, and limiting to 10.
14. Implement campaign count as map size.

Dependencies: TASK-01, TASK-03.

## TASK-05: `src/main/java/com/adagg/writer/CsvWriter.java`

Purpose: Write ranked campaign lists to CSV files using exact column order and decimal formatting.

Fields:

```java
private static final String HEADER = "campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA";
```

Methods:

```java
public void write(Path outputFile, List<CampaignStats> campaigns) throws IOException
```

Implementation workflow:

1. Create class in `com.adagg.writer`.
2. Open the target output file with a writer.
3. Write the header line.
4. Iterate campaign stats list.
5. Format `total_spend` with exactly 2 decimal places.
6. Format `CTR` with exactly 4 decimal places.
7. Format `CPA` with exactly 2 decimal places when non-null.
8. Write empty CPA field when CPA is null.
9. Write each campaign as one CSV line.
10. Flush and close using try-with-resources.

Dependencies: TASK-01.

## TASK-06: `src/main/java/com/adagg/Main.java`

Purpose: Wire CLI parsing, aggregation, output writing, and timing summary into the runnable application entry point.

Fields and methods:

```java
public static void main(String[] args)
```

Implementation workflow:

1. Capture start time with `System.currentTimeMillis()`.
2. Instantiate `ArgParser`.
3. Parse arguments.
4. Instantiate `RowValidator`.
5. Instantiate `CsvAggregator`.
6. Process input path.
7. Get top 10 by CTR.
8. Get top 10 by CPA.
9. Instantiate `CsvWriter`.
10. Write `top10_ctr.csv` into output directory.
11. Write `top10_cpa.csv` into output directory.
12. Capture end time.
13. Print processed campaign count to stdout.
14. Print elapsed time to stdout.
15. Catch startup, read, and write failures.
16. Print clear failure message to stderr.
17. Exit with status code `1` on failure.

Dependencies: TASK-02, TASK-03, TASK-04, TASK-05.

## TASK-07: `src/test/java/com/adagg/processor/CsvAggregatorTest.java`

Purpose: Verify aggregation, metrics, malformed row handling, and ranking behavior with JUnit 5.

Fields and methods:

```java
@Test void aggregatesRowsByCampaignId()
@Test void calculatesCtrAndCpaFromTotals()
@Test void returnsZeroCtrWhenImpressionsAreZero()
@Test void returnsNullCpaWhenConversionsAreZero()
@Test void skipsMalformedRows()
@Test void sortsTop10CtrDescending()
@Test void excludesZeroConversionsFromCpaRanking()
@Test void sortsTop10CpaAscending()
```

Implementation workflow:

1. Create test class under `src/test/java/com/adagg/processor`.
2. Use temporary CSV files for test inputs.
3. Include valid header in test CSVs.
4. Test duplicate `campaign_id` rows aggregate into one record.
5. Test CTR and CPA use aggregate totals.
6. Test zero impressions returns CTR `0.0`.
7. Test zero conversions returns CPA `null`.
8. Test malformed rows are skipped without stopping aggregation.
9. Test CTR ranking sort order and limit.
10. Test CPA ranking excludes zero-conversion campaigns.
11. Test CPA ranking sort order and limit.

Dependencies: TASK-01, TASK-03, TASK-04.

## TASK-08: `README.md`

Purpose: Document setup, usage, dependencies, performance results, and project structure for submission.

Fields and methods: Not applicable.

Implementation workflow:

1. Preserve challenge context where useful.
2. Add Java 21 prerequisite.
3. Add Maven prerequisite.
4. Add build command.
5. Add run command with fat JAR path.
6. Document output files and schemas.
7. Document libraries used.
8. Document project structure.
9. Document processing time after benchmark is run.
10. Document peak memory usage after measurement is available.
11. Document edge-case behavior.
12. Mention `../PROMPTS.md` for AI prompt history.

Dependencies: TASK-01 through TASK-07 for final performance and behavior details.
