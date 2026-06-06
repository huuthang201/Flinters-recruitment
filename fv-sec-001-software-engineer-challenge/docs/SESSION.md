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
| Current status | TASK-01 complete; TASK-02 ready to start |

## Task Progress

| Task ID | File | Status | Notes |
|---|---|---|---|
| TASK-01 | `src/main/java/com/adagg/model/CampaignStats.java` | ✅ Complete | Core aggregate model generated |
| TASK-02 | `src/main/java/com/adagg/util/ArgParser.java` | 🟡 In Progress | CLI parsing and path validation |
| TASK-03 | `src/main/java/com/adagg/util/RowValidator.java` | ⬜ Pending | Per-row validation and typed row record |
| TASK-04 | `src/main/java/com/adagg/processor/CsvAggregator.java` | ⬜ Pending | Streaming, validation integration, aggregation, ranking |
| TASK-05 | `src/main/java/com/adagg/writer/CsvWriter.java` | ⬜ Pending | Output CSV writing and formatting |
| TASK-06 | `src/main/java/com/adagg/Main.java` | ⬜ Pending | Application wiring and timing summary |
| TASK-07 | `src/test/java/com/adagg/processor/CsvAggregatorTest.java` | ⬜ Pending | JUnit 5 tests for business behavior |
| TASK-08 | `README.md` | ⬜ Pending | Final implementation README and benchmark notes |

## Expected Final File Tree

```text
fv-sec-001-software-engineer-challenge/
  .gitignore
  README.md
  ad_data.csv
  ad_data.csv.zip
  docs/
    BUSINESS-LOGIC.md
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
            processor/
              CsvAggregatorTest.java
ROMPTS.md
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
- Confirm whether the implementation should treat simple comma splitting as sufficient because the input schema does not mention quoted CSV fields.
- Confirm whether output CPA should be an empty CSV field when conversions are zero in `top10_ctr.csv`.

## Next Action

Wait for instruction before starting **TASK-02: `util/ArgParser.java`**.
