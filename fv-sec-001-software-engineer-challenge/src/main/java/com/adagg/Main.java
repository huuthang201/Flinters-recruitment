package com.adagg;

import com.adagg.processor.CsvAggregator;
import com.adagg.util.ArgParser;
import com.adagg.util.ArgParser.ParsedArgs;
import com.adagg.util.RowValidator;
import com.adagg.writer.CsvWriter;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        try {
            ArgParser argParser = new ArgParser();
            ParsedArgs parsedArgs = argParser.parse(args);

            RowValidator rowValidator = new RowValidator();
            CsvAggregator aggregator = new CsvAggregator(rowValidator);
            aggregator.process(parsedArgs.inputPath());

            CsvWriter csvWriter = new CsvWriter();
            Path topCtrOutput = parsedArgs.outputDir().resolve("top10_ctr.csv");
            Path topCpaOutput = parsedArgs.outputDir().resolve("top10_cpa.csv");

            csvWriter.write(topCtrOutput, aggregator.getTop10ByCTR());
            csvWriter.write(topCpaOutput, aggregator.getTop10ByCPA());

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("Processed campaigns: " + aggregator.getCampaignCount());
            System.out.println("Elapsed time: " + elapsedTime + " ms");
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
