package com.adagg.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ArgParser {
    public record ParsedArgs(Path inputPath, Path outputDir) {}

    public ParsedArgs parse(String[] args) throws IOException {
        String inputValue = null;
        String outputValue = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if ("--input".equals(arg)) {
                inputValue = readValue(args, ++i, "--input");
            } else if ("--output".equals(arg)) {
                outputValue = readValue(args, ++i, "--output");
            } else {
                throw new IllegalArgumentException("Unknown argument: " + arg);
            }
        }

        if (inputValue == null) {
            throw new IllegalArgumentException("Missing required argument: --input");
        }
        if (outputValue == null) {
            throw new IllegalArgumentException("Missing required argument: --output");
        }

        Path inputPath = Path.of(inputValue);
        Path outputDir = Path.of(outputValue);

        validateInputPath(inputPath);
        validateOutputDir(outputDir);

        return new ParsedArgs(inputPath, outputDir);
    }

    private String readValue(String[] args, int valueIndex, String optionName) {
        if (valueIndex >= args.length || args[valueIndex].isBlank() || args[valueIndex].startsWith("--")) {
            throw new IllegalArgumentException("Missing value for argument: " + optionName);
        }
        return args[valueIndex];
    }

    private void validateInputPath(Path inputPath) {
        if (!Files.exists(inputPath)) {
            throw new IllegalArgumentException("Input file does not exist: " + inputPath);
        }
        if (!Files.isRegularFile(inputPath)) {
            throw new IllegalArgumentException("Input path is not a regular file: " + inputPath);
        }
    }

    private void validateOutputDir(Path outputDir) throws IOException {
        if (Files.exists(outputDir)) {
            if (!Files.isDirectory(outputDir)) {
                throw new IllegalArgumentException("Output path is not a directory: " + outputDir);
            }
            return;
        }

        Files.createDirectories(outputDir);
    }
}
