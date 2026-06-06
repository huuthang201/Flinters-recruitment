# Session State

## Session Info

| Field | Value |
|---|---|
| Project name | Ad Performance Aggregator |
| Challenge | FV-SEC001 Software Engineer Challenge |
| Application type | Console application, CLI only |
| Language | Java 21 |
| Build tool | Maven |
| Date | 2026-06-07 |
| Current status | All tasks complete |

## Task Progress

| Task ID | File | Status | Notes |
|---|---|---|---|
| TASK-01 | `src/main/java/com/adagg/model/CampaignStats.java` | ✅ Complete | Core aggregate model generated |
| TASK-02 | `src/main/java/com/adagg/util/ArgParser.java` | ✅ Complete | CLI parsing and path validation generated |
| TASK-03 | `src/main/java/com/adagg/util/RowValidator.java` | ✅ Complete | Per-row validation and typed row record generated |
| TASK-04 | `src/main/java/com/adagg/processor/CsvAggregator.java` | ✅ Complete | Streaming, validation integration, aggregation, ranking generated |
| TASK-05 | `src/main/java/com/adagg/writer/CsvWriter.java` | ✅ Complete | Output CSV writing and formatting generated |
| TASK-06 | `src/main/java/com/adagg/Main.java` | ✅ Complete | Application wiring and timing summary generated |
| TASK-07 | `src/test/java/com/adagg/CsvAggregatorTest.java` | ✅ Complete | JUnit 5 tests for business behavior generated |
| TASK-08 | `docs/INSTRUCTIONS.md` | ✅ Complete | Final implementation instructions and benchmark placeholders generated |

## Expected Final File Tree

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

## Decisions Log

| Decision | Reason |
|---|---|
| Use Java 21 | LTS runtime with modern language features and strong standard library |
| Use Maven | Standard Java build and test workflow; supports fat JAR packaging |
| Use `maven-assembly-plugin` | Produces the requested `jar-with-dependencies` artifact |
| Use `BufferedReader` with 8MB buffer | Efficient line-by-line processing for approximately 1GB CSV input |
| Store only `HashMap<String, CampaignStats>` | Keeps memory proportional to unique campaigns, not total rows |
| Skip malformed rows with stderr warning | Preserves processing continuity while surfacing data issues |
| Return null CPA for zero conversions | Matches challenge requirement to ignore or return null and enables CPA exclusion |
| Use deterministic tie-breaker by `campaign_id` | Makes output stable across JVM runs and map iteration order |
| Use fixed decimal output formatting | Ensures expected result precision for CTR, CPA, and spend |

## Open Questions And Blockers

- No current blockers.

## Next Action

Wait for further instruction.
