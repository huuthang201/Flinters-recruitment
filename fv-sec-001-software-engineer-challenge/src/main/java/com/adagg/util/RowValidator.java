package com.adagg.util;

import java.util.Optional;
import java.util.regex.Pattern;

public class RowValidator {
    private static final int EXPECTED_TOKEN_COUNT = 6;
    private static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public record ValidRow(
        String campaignId,
        String date,
        long impressions,
        long clicks,
        double spend,
        long conversions
    ) {}

    public Optional<ValidRow> validate(String[] tokens, long lineNumber) {
        if (tokens.length != EXPECTED_TOKEN_COUNT) {
            warn(lineNumber, "expected 6 columns but found " + tokens.length);
            return Optional.empty();
        }

        String campaignId = tokens[0].trim();
        String date = tokens[1].trim();
        String impressionsValue = tokens[2].trim();
        String clicksValue = tokens[3].trim();
        String spendValue = tokens[4].trim();
        String conversionsValue = tokens[5].trim();

        if (campaignId.isEmpty()) {
            warn(lineNumber, "campaign_id is empty");
            return Optional.empty();
        }
        if (!DATE_PATTERN.matcher(date).matches()) {
            warn(lineNumber, "date must match YYYY-MM-DD");
            return Optional.empty();
        }

        Long impressions = parseNonNegativeLong(impressionsValue, "impressions", lineNumber);
        if (impressions == null) {
            return Optional.empty();
        }

        Long clicks = parseNonNegativeLong(clicksValue, "clicks", lineNumber);
        if (clicks == null) {
            return Optional.empty();
        }

        Double spend = parseNonNegativeDouble(spendValue, "spend", lineNumber);
        if (spend == null) {
            return Optional.empty();
        }

        Long conversions = parseNonNegativeLong(conversionsValue, "conversions", lineNumber);
        if (conversions == null) {
            return Optional.empty();
        }

        return Optional.of(new ValidRow(campaignId, date, impressions, clicks, spend, conversions));
    }

    private Long parseNonNegativeLong(String value, String fieldName, long lineNumber) {
        try {
            long parsed = Long.parseLong(value);
            if (!Double.isFinite(parsed) || parsed < 0) {
                warn(lineNumber, fieldName + " must be a finite non-negative number");
                return null;
            }
            return parsed;
        } catch (NumberFormatException e) {
            warn(lineNumber, fieldName + " must be a non-negative integer");
            return null;
        }
    }

    private Double parseNonNegativeDouble(String value, String fieldName, long lineNumber) {
        try {
            double parsed = Double.parseDouble(value);
            if (parsed < 0) {
                warn(lineNumber, fieldName + " must be non-negative");
                return null;
            }
            return parsed;
        } catch (NumberFormatException e) {
            warn(lineNumber, fieldName + " must be a non-negative number");
            return null;
        }
    }

    private void warn(long lineNumber, String reason) {
        System.err.println("Warning: line " + lineNumber + " skipped: " + reason);
    }
}
