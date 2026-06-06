# AI Prompts Log

This file documents all AI prompts used during development. Prompts are recorded raw and unedited for submission review.

## PROMPT-01

```text
opencode run "# PROMPT-01 — Generate Planning Files for Agentic Coding Session

I have a software engineering challenge. Before writing any code, analyze the requirements thoroughly and generate the following planning files for an agentic coding session.

---

## Application Type

- This is a **console application (CLI only)** — pure terminal input/output
- No GUI, no web server, no REST API
- User interacts solely via command-line arguments
- All progress/errors printed to stdout/stderr

## Language & Stack

- Java 21
- Maven (fat JAR via `maven-assembly-plugin`)
- No external CSV libraries — use `BufferedReader` for memory-efficient streaming
- JUnit 5 for testing
- Run via: `java -jar target/ad-performance-aggregator-1.0.0-jar-with-dependencies.jar --input ad_data.csv --output results/`

---

## File 1 — `docs/SKILLS.md`

Agent context & hard constraints. Include:

- Project overview: what it does, input CSV schema, output files
- Tech stack with reasoning for each choice
- Memory efficiency rules (file is ~1GB — NEVER load all into memory, stream line by line)
- Code style rules: SRP per class, meaningful names, no dead code, private fields
- CLI contract: argument names, validation behavior
- Package structure: `com.adagg` with subpackages `model/`, `processor/`, `writer/`, `util/`
- Edge cases table: situation → expected behavior (cover: zero conversions, zero impressions, malformed rows, missing file, empty file, duplicate campaign_id rows, negative values, invalid date format, empty campaign_id)
- Metric formulas: CTR and CPA with precision rules
- Output CSV format: columns, sort order, decimal formatting per column
- Definition of Done checklist

---

## File 2 — `docs/BUSINESS-LOGIC.md`

Pure business rules, no implementation detail. Include:

- What "aggregation by campaign_id" means (multiple rows → one record)
- Exact formula for CTR: `total_clicks / total_impressions`, 4 decimal places, return `0.0` if impressions = 0
- Exact formula for CPA: `total_spend / total_conversions`, 2 decimal places, return `null` if conversions = 0
- Rules for `top10_ctr.csv`: sort by CTR descending, top 10, include all campaigns
- Rules for `top10_cpa.csv`: sort by CPA ascending (lowest = most efficient), top 10, EXCLUDE campaigns with zero conversions
- Output column order and formatting for both files
- Data preprocessing rules (applied per row before aggregation):
  - Trim whitespace from all tokens
  - Validate `campaign_id` is non-empty string
  - Validate `impressions`, `clicks`, `conversions` are non-negative integers
  - Validate `spend` is a non-negative float
  - Validate `date` matches format `YYYY-MM-DD`
  - If any validation fails → skip row, log warning to stderr with line number and reason
- What "malformed row" means: wrong column count, non-numeric values, negative values, empty campaign_id, invalid date format
- Malformed rows are skipped silently (logged to stderr only, never crash the program)

---

## File 3 — `docs/WORKFLOWS.md`

Step-by-step runtime flow of the entire application. Include:

- **CLI startup flow:** parse args → validate `--input` and `--output` presence → check input file exists → create output dir if not exists → `exit(1)` with clear message on any failure
- **CSV streaming flow:** open `BufferedReader` with 8MB buffer → read first line → validate it's the header → skip it → iterate remaining lines with line counter
- **Data preprocessing flow** (per row, before aggregation):
  - Trim whitespace from all tokens
  - Validate token count == 6, else skip + warn
  - Validate `campaign_id` non-empty, else skip + warn
  - Validate `impressions`, `clicks`, `conversions` parse as non-negative long, else skip + warn
  - Validate `spend` parses as non-negative double, else skip + warn
  - Validate `date` matches `YYYY-MM-DD` format, else skip + warn
  - Only pass fully validated row to aggregation step
- **Aggregation flow:** `HashMap.computeIfAbsent(campaignId, CampaignStats::new).accumulate(impressions, clicks, spend, conversions)`
- **Ranking flow:** stream HashMap values → for CTR: sort descending, limit 10 → for CPA: filter out null CPA first, sort ascending, limit 10
- **Output flow:** for each output file: write header line → iterate list → format each field → write line → flush → close
- **Error handling flow:** what happens at each failure point (bad args, missing file, bad row, write error)
- **Timing flow:** capture `System.currentTimeMillis()` at start → process → print elapsed time and total campaigns processed at end

---

## File 4 — `docs/TASKS.md`

Detailed coding task breakdown. For each task include:

- Task ID: TASK-01, TASK-02, ...
- Target file path
- Purpose (one sentence)
- Fields & methods with signatures
- Step-by-step implementation workflow (pseudocode, not actual code)
- Dependencies on other tasks

Tasks to cover:

- **TASK-01:** `model/CampaignStats.java` — mutable POJO with fields: `campaignId`, `totalImpressions`, `totalClicks`, `totalSpend`, `totalConversions`. Methods: `accumulate()`, `getCTR()`, `getCPA()`
- **TASK-02:** `util/ArgParser.java` — parse `--input`/`--output` args, validate both present and input file exists, create output dir, return `ParsedArgs` record
- **TASK-03:** `util/RowValidator.java` — stateless validator, takes raw `String[]` tokens, returns validated & typed row or returns empty `Optional` on failure
- **TASK-04:** `processor/CsvAggregator.java` — open `BufferedReader`, skip header, call `RowValidator` per line, accumulate into `HashMap`, expose `getTop10ByCTR()` and `getTop10ByCPA()`
- **TASK-05:** `writer/CsvWriter.java` — write `List<CampaignStats>` to CSV with correct column order and decimal formatting
- **TASK-06:** `Main.java` — wire `ArgParser` → `CsvAggregator` → `CsvWriter`, print timing summary
- **TASK-07:** `CsvAggregatorTest.java` — JUnit 5 tests covering: basic aggregation, CTR/CPA formulas, zero impressions, zero conversions, malformed rows skipped, top10 sort order, CPA excludes zero conversions
- **TASK-08:** `README.md` — setup instructions, how to run, libraries used, processing time, peak memory usage, project structure

---

## File 5 — `docs/SESSION.md`

Live session state. Include:

- Session info: project name, language, build tool, date, current status
- Task progress table: Task ID \| File \| Status (⬜ Pending / 🟡 In Progress / ✅ Done / 🔴 Blocked) \| Notes
- Full expected file/folder tree of the final project
- Decisions log table: Decision \| Reason
- Open questions/blockers section
- Next action pointer (which task to start first)

---

## File 6 — `./PROMPTS.md` *(root of project, not in `docs/`)*

Prompts log for submission. Include:

- A header explaining this file documents all AI prompts used during development
- First entry (this) must be this exact prompt pasted verbatim as `PROMPT-01`
- Leave placeholder sections for `PROMPT-02`, `PROMPT-03`, etc. to be filled as coding progresses

---

## Rules

- Do **NOT** write any Java source code yet
- Generate all 6 files completely before stopping
- After generating, print a summary table of all files created and confirm which TASK to start with
- Wait for my instruction before proceeding to code

---

## Here is the README

```
/FlintersExam/recruitment/fv-sec-001-software-engineer-challenge/README.md
``` "
```

## PROMPT-02

```text
opencode run "In folder fv-sec-001-software-engineer-challenge, read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-01** — generate `src/main/java/com/adagg/model/CampaignStats.java`. After done, update `docs/SESSION.md` to mark TASK-01 as ✅ and set TASK-02 as 🟡. Append this prompt verbatim as **PROMPT-02** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-03

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-02** — generate `src/main/java/com/adagg/util/ArgParser.java`. After done, update `docs/SESSION.md` to mark TASK-02 as ✅ and set TASK-03 as 🟡. Append this prompt verbatim as **PROMPT-03** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-04

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-03** — generate `src/main/java/com/adagg/util/RowValidator.java`. After done, update `docs/SESSION.md` to mark TASK-03 as ✅ and set TASK-04 as 🟡. Append this prompt verbatim as **PROMPT-04** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-05

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-04** — generate `src/main/java/com/adagg/processor/CsvAggregator.java`. After done, update `docs/SESSION.md` to mark TASK-04 as ✅ and set TASK-05 as 🟡. Append this prompt verbatim as **PROMPT-05** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-06

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-05** — generate `src/main/java/com/adagg/writer/CsvWriter.java`. After done, update `docs/SESSION.md` to mark TASK-05 as ✅ and set TASK-06 as 🟡. Append this prompt verbatim as **PROMPT-06** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-07

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-06** — generate `src/main/java/com/adagg/Main.java`. After done, update `docs/SESSION.md` to mark TASK-06 as ✅ and set TASK-07 as 🟡. Append this prompt verbatim as **PROMPT-07** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-08

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-07** — generate `src/test/java/com/adagg/CsvAggregatorTest.java`. After done, update `docs/SESSION.md` to mark TASK-07 as ✅ and set TASK-08 as 🟡. Append this prompt verbatim as **PROMPT-08** to `PROMPTS.md`. Then wait for my instruction."
```

## PROMPT-09

```text
opencode run "Read `docs/SKILLS.md`, `docs/BUSINESS-LOGIC.md`, `docs/WORKFLOWS.md`, `docs/TASKS.md`, and `docs/SESSION.md` carefully. Then start **TASK-08** — generate `docs/INSTRUCTIONS.md` including: setup instructions, how to run, libraries used, processing time placeholder, peak memory usage placeholder, and full project structure. After done, update `docs/SESSION.md` to mark TASK-08 as ✅ and all tasks as ✅. Append this prompt verbatim as **PROMPT-09** to `PROMPTS.md`. Then wait for my instruction."
```
