package com.adagg;

import com.adagg.model.CampaignStats;
import com.adagg.processor.CsvAggregator;
import com.adagg.util.RowValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CsvAggregatorTest {
    private static final String HEADER = "campaign_id,date,impressions,clicks,spend,conversions";

    @TempDir
    Path tempDir;

    @Test
    void aggregatesRowsByCampaignId() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-a,2026-01-01,100,10,50.00,2",
            "campaign-a,2026-01-02,200,20,75.00,3"
        );

        CampaignStats campaign = aggregator.getTop10ByCTR().getFirst();

        assertEquals(1, aggregator.getCampaignCount());
        assertEquals("campaign-a", campaign.getCampaignId());
        assertEquals(300, campaign.getTotalImpressions());
        assertEquals(30, campaign.getTotalClicks());
        assertEquals(125.00, campaign.getTotalSpend(), 0.0001);
        assertEquals(5, campaign.getTotalConversions());
    }

    @Test
    void calculatesCtrAndCpaFromTotals() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-a,2026-01-01,100,10,40.00,2",
            "campaign-a,2026-01-02,300,30,60.00,3"
        );

        CampaignStats campaign = aggregator.getTop10ByCTR().getFirst();

        assertEquals(0.10, campaign.getCTR(), 0.0001);
        assertEquals(20.00, campaign.getCPA(), 0.0001);
    }

    @Test
    void returnsZeroCtrWhenImpressionsAreZero() throws IOException {
        CsvAggregator aggregator = processCsv("campaign-a,2026-01-01,0,10,15.00,1");

        assertEquals(0.0, aggregator.getTop10ByCTR().getFirst().getCTR(), 0.0001);
    }

    @Test
    void returnsNullCpaWhenConversionsAreZero() throws IOException {
        CsvAggregator aggregator = processCsv("campaign-a,2026-01-01,100,10,15.00,0");

        assertNull(aggregator.getTop10ByCTR().getFirst().getCPA());
    }

    @Test
    void skipsMalformedRows() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-a,2026-01-01,100,10,15.00,1",
            "campaign-b,2026-01-02,-1,10,15.00,1",
            ",2026-01-03,100,10,15.00,1",
            "campaign-c,invalid-date,100,10,15.00,1",
            "campaign-d,2026-01-04,100,not-a-number,15.00,1"
        );

        assertEquals(1, aggregator.getCampaignCount());
        assertEquals("campaign-a", aggregator.getTop10ByCTR().getFirst().getCampaignId());
    }

    @Test
    void sortsTop10CtrDescending() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-01,2026-01-01,100,1,10.00,1",
            "campaign-02,2026-01-01,100,2,10.00,1",
            "campaign-03,2026-01-01,100,3,10.00,1",
            "campaign-04,2026-01-01,100,4,10.00,1",
            "campaign-05,2026-01-01,100,5,10.00,1",
            "campaign-06,2026-01-01,100,6,10.00,1",
            "campaign-07,2026-01-01,100,7,10.00,1",
            "campaign-08,2026-01-01,100,8,10.00,1",
            "campaign-09,2026-01-01,100,9,10.00,1",
            "campaign-10,2026-01-01,100,10,10.00,1",
            "campaign-11,2026-01-01,100,11,10.00,1"
        );

        List<CampaignStats> top10 = aggregator.getTop10ByCTR();

        assertEquals(10, top10.size());
        assertEquals("campaign-11", top10.getFirst().getCampaignId());
        assertEquals("campaign-02", top10.getLast().getCampaignId());
    }

    @Test
    void excludesZeroConversionsFromCpaRanking() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-a,2026-01-01,100,10,15.00,0",
            "campaign-b,2026-01-01,100,10,20.00,2"
        );

        List<CampaignStats> topCpa = aggregator.getTop10ByCPA();

        assertEquals(1, topCpa.size());
        assertEquals("campaign-b", topCpa.getFirst().getCampaignId());
    }

    @Test
    void sortsTop10CpaAscending() throws IOException {
        CsvAggregator aggregator = processCsv(
            "campaign-01,2026-01-01,100,10,110.00,1",
            "campaign-02,2026-01-01,100,10,100.00,1",
            "campaign-03,2026-01-01,100,10,90.00,1",
            "campaign-04,2026-01-01,100,10,80.00,1",
            "campaign-05,2026-01-01,100,10,70.00,1",
            "campaign-06,2026-01-01,100,10,60.00,1",
            "campaign-07,2026-01-01,100,10,50.00,1",
            "campaign-08,2026-01-01,100,10,40.00,1",
            "campaign-09,2026-01-01,100,10,30.00,1",
            "campaign-10,2026-01-01,100,10,20.00,1",
            "campaign-11,2026-01-01,100,10,10.00,1"
        );

        List<CampaignStats> top10 = aggregator.getTop10ByCPA();

        assertEquals(10, top10.size());
        assertEquals("campaign-11", top10.getFirst().getCampaignId());
        assertEquals("campaign-02", top10.getLast().getCampaignId());
    }

    private CsvAggregator processCsv(String... rows) throws IOException {
        Path inputFile = tempDir.resolve("ad_data.csv");
        String content = HEADER + System.lineSeparator() + String.join(System.lineSeparator(), rows);
        Files.writeString(inputFile, content);

        CsvAggregator aggregator = new CsvAggregator(new RowValidator());
        aggregator.process(inputFile);
        return aggregator;
    }
}
