package com.adagg.model;

public class CampaignStats {
    private final String campaignId;
    private long totalImpressions;
    private long totalClicks;
    private double totalSpend;
    private long totalConversions;

    public CampaignStats(String campaignId) {
        this.campaignId = campaignId;
    }

    public void accumulate(long impressions, long clicks, double spend, long conversions) {
        totalImpressions += impressions;
        totalClicks += clicks;
        totalSpend += spend;
        totalConversions += conversions;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public long getTotalImpressions() {
        return totalImpressions;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public double getTotalSpend() {
        return totalSpend;
    }

    public long getTotalConversions() {
        return totalConversions;
    }

    public double getCTR() {
        if (totalImpressions == 0) {
            return 0.0;
        }
        return (double) totalClicks / totalImpressions;
    }

    public Double getCPA() {
        if (totalConversions == 0) {
            return null;
        }
        return totalSpend / totalConversions;
    }
}
