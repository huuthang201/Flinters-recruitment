# Business Logic

## Aggregation By Campaign

Aggregation by `campaign_id` means every valid input row with the same `campaign_id` contributes to one final campaign record.

For each campaign, sum these values across all valid rows:

- `total_impressions`
- `total_clicks`
- `total_spend`
- `total_conversions`

The `date` field is used only for validation. It is not part of the output and does not split aggregation groups.

## CTR Formula

Click-through rate is calculated from aggregated totals:

```text
CTR = total_clicks / total_impressions
```

Rules:

- Use campaign-level totals, not per-row averages.
- Return `0.0` when `total_impressions = 0`.
- Output with 4 decimal places.
- Example: `0.0500`.

## CPA Formula

Cost per acquisition is calculated from aggregated totals:

```text
CPA = total_spend / total_conversions
```

Rules:

- Use campaign-level totals, not per-row averages.
- Return `null` when `total_conversions = 0`.
- Output with 2 decimal places when present.
- Example: `20.00`.

## `top10_ctr.csv` Rules

- Include all campaigns, including campaigns with zero conversions.
- Sort by CTR descending.
- Select the first 10 campaigns after sorting.
- If fewer than 10 campaigns exist, output all available campaigns.
- If CTR values tie, sort tied campaigns by `campaign_id` ascending for deterministic output.

## `top10_cpa.csv` Rules

- Exclude campaigns where CPA is `null`.
- Campaigns with zero conversions must not appear in this file.
- Sort by CPA ascending because lower CPA is more efficient.
- Select the first 10 campaigns after filtering and sorting.
- If fewer than 10 eligible campaigns exist, output all eligible campaigns.
- If CPA values tie, sort tied campaigns by `campaign_id` ascending for deterministic output.

## Output Columns And Formatting

Both CSV files must use this exact column order:

```text
campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA
```

Formatting rules:

| Column | Formatting |
|---|---|
| `campaign_id` | Raw campaign identifier after trimming |
| `total_impressions` | Base-10 integer with no decimal places |
| `total_clicks` | Base-10 integer with no decimal places |
| `total_spend` | Decimal with exactly 2 places |
| `total_conversions` | Base-10 integer with no decimal places |
| `CTR` | Decimal with exactly 4 places |
| `CPA` | Decimal with exactly 2 places when present; empty field when `null` |

## Data Preprocessing Rules

Apply these rules to each row before aggregation:

| Field | Rule |
|---|---|
| All tokens | Trim leading and trailing whitespace |
| Token count | Must equal 6 |
| `campaign_id` | Must be a non-empty string after trimming |
| `date` | Must match `YYYY-MM-DD` format |
| `impressions` | Must parse as a non-negative integer |
| `clicks` | Must parse as a non-negative integer |
| `spend` | Must parse as a non-negative float |
| `conversions` | Must parse as a non-negative integer |

If any validation fails:

- Skip the row.
- Log a warning to stderr with line number and reason.
- Continue processing later rows.
- Never crash the program because of a bad row.

## Malformed Rows

A row is malformed when any of these conditions is true:

- Wrong column count.
- Non-numeric value in `impressions`, `clicks`, `spend`, or `conversions`.
- Negative value in `impressions`, `clicks`, `spend`, or `conversions`.
- Empty `campaign_id` after trimming.
- Invalid `date` format.

Malformed rows are skipped silently from the business result perspective. They are logged to stderr only and must never crash the program.
