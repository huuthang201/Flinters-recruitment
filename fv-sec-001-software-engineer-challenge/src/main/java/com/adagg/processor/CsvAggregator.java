package com.adagg.processor;

import com.adagg.model.CampaignStats;
import com.adagg.util.RowValidator;
import com.adagg.util.RowValidator.ValidRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvAggregator {
    private static final int BUFFER_SIZE = 8 * 1024 * 1024;
    private static final String EXPECTED_HEADER = "campaign_id,date,impressions,clicks,spend,conversions";

    private final RowValidator rowValidator;
    private final Map<String, CampaignStats> campaigns;

    public CsvAggregator(RowValidator rowValidator) {
        this.rowValidator = rowValidator;
        this.campaigns = new HashMap<>();
    }

    public void process(Path inputPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(Files.newInputStream(inputPath), StandardCharsets.UTF_8),
            BUFFER_SIZE
        )) {
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("Input CSV is empty");
            }
            if (!EXPECTED_HEADER.equals(header.trim())) {
                throw new IOException("Invalid CSV header: " + header);
            }

            long lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] tokens = line.split(",", -1);
                rowValidator.validate(tokens, lineNumber).ifPresent(this::accumulate);
            }
        }
    }

    public List<CampaignStats> getTop10ByCTR() {
        return campaigns.values().stream()
            .sorted(Comparator.comparingDouble(CampaignStats::getCTR).reversed()
                .thenComparing(CampaignStats::getCampaignId))
            .limit(10)
            .toList();
    }

    public List<CampaignStats> getTop10ByCPA() {
        return campaigns.values().stream()
            .filter(campaign -> campaign.getCPA() != null)
            .sorted(Comparator.comparing(CampaignStats::getCPA)
                .thenComparing(CampaignStats::getCampaignId))
            .limit(10)
            .toList();
    }

    public int getCampaignCount() {
        return campaigns.size();
    }

    private void accumulate(ValidRow row) {
        campaigns.computeIfAbsent(row.campaignId(), CampaignStats::new)
            .accumulate(row.impressions(), row.clicks(), row.spend(), row.conversions());
    }
}
