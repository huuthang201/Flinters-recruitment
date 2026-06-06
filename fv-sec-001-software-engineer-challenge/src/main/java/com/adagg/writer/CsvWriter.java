package com.adagg.writer;

import com.adagg.model.CampaignStats;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class CsvWriter {
    private static final String HEADER = "campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA";

    public void write(Path outputFile, List<CampaignStats> campaigns) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();

            for (CampaignStats campaign : campaigns) {
                writer.write(formatCampaign(campaign));
                writer.newLine();
            }

            writer.flush();
        }
    }

    private String formatCampaign(CampaignStats campaign) {
        Double cpa = campaign.getCPA();
        String cpaValue = cpa == null ? "" : String.format(Locale.US, "%.2f", cpa);

        return String.join(",",
            campaign.getCampaignId(),
            Long.toString(campaign.getTotalImpressions()),
            Long.toString(campaign.getTotalClicks()),
            String.format(Locale.US, "%.2f", campaign.getTotalSpend()),
            Long.toString(campaign.getTotalConversions()),
            String.format(Locale.US, "%.4f", campaign.getCTR()),
            cpaValue
        );
    }
}
