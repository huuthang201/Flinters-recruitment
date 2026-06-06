# Ad Performance Aggregator Instructions

This document describes how to set up, run, test, and review the Java 21 Ad Performance Aggregator implementation.

## Overview

The application is a console-only CSV processor. It streams advertising performance data, aggregates valid rows by `campaign_id`, and writes two ranked reports:

| Output file | Ranking |
|---|---|
| `top10_ctr.csv` | Highest click-through rate, descending |
| `top10_cpa.csv` | Lowest cost per acquisition, ascending, excluding zero-conversion campaigns |

## Input CSV

The input file must use this exact header:

```text
campaign_id,date,impressions,clicks,spend,conversions
```

Columns:

| Column | Type | Rule |
|---|---|---|
| `campaign_id` | string | Required after trimming |
| `date` | string | Must match `YYYY-MM-DD` |
| `impressions` | integer | Non-negative |
| `clicks` | integer | Non-negative |
| `spend` | float | Non-negative |
| `conversions` | integer | Non-negative |

Malformed rows are skipped, warnings are printed to stderr with the line number, and processing continues.

## Output CSV Format

Both output files use this exact column order:

```text
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
```

Formatting rules:

| Column | Format |
|---|---|
| `total_impressions` | Integer |
| `total_clicks` | Integer |
| `total_spend` | 2 decimal places |
| `total_conversions` | Integer |
| `CTR` | 4 decimal places |
| `CPA` | 2 decimal places when present; empty when `null` |

## Setup

Prerequisites:

- Java 21
- Maven

Prepare the data:

```bash
unzip ad_data.csv.zip
```

Build the application:

```bash
mvn clean package
```

Note: the current source implementation expects the Maven project file to provide Java 21 configuration, JUnit 5, and `maven-assembly-plugin` for fat JAR packaging.

## Run

Run the application with required `--input` and `--output` arguments:

```bash
java -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar --input ad_data.csv --output results/
```

Successful execution prints:

```text
Processed campaigns: <count>
Elapsed time: <milliseconds> ms
```

Failure behavior:

- Missing or invalid CLI arguments print a clear error to stderr and exit with status code `1`.
- Missing or invalid input files print a clear error to stderr and exit with status code `1`.
- Invalid or empty CSV headers print a clear error to stderr and exit with status code `1`.
- Malformed data rows are skipped with stderr warnings and do not stop processing.

## Tests

Run tests with:

```bash
mvn test
```

The test suite covers aggregation, CTR and CPA formulas, zero-value edge cases, malformed row skipping, top-10 limits, and deterministic sorting.

## Libraries Used

| Library or API | Purpose |
|---|---|
| Java 21 standard library | File IO, path handling, collections, records, formatting, CLI wiring |
| `BufferedReader` | Memory-efficient streaming with an 8MB buffer |
| `HashMap` | Aggregation by campaign ID with one mutable stats object per campaign |
| JUnit 5 | Unit and integration-style tests |
| Maven | Build, test, and package lifecycle |
| `maven-assembly-plugin` | Runnable fat JAR packaging |

No external CSV parsing library is used.

## Performance Placeholders

Processing time for 1GB input: `TBD - benchmark not run yet`

Peak memory usage: `TBD - measurement not run yet`

## Project Structure

```text
fv-sec-001-software-engineer-challenge/
  .gitignore
  README.md
  ad_data.csv
  ad_data.csv.zip
  docs/
    BUSINESS-LOGIC.md
    INSTRUCTIONS.md
    SESSION.md
    SKILLS.md
    TASKS.md
    WORKFLOWS.md
  pom.xml
  results/
    .gitkeep
    top10_ctr.csv
    top10_cpa.csv
  src/
    main/
      java/
        com/
          adagg/
            Main.java
            model/
              CampaignStats.java
            processor/
              CsvAggregator.java
            util/
              ArgParser.java
              RowValidator.java
            writer/
              CsvWriter.java
    test/
      java/
        com/
          adagg/
            CsvAggregatorTest.java
../PROMPTS.md
```

## Prompt Log

AI prompts used during development are recorded in `../PROMPTS.md`.
