package com.prospecta.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import com.prospecta.exception.CsvProcessingException;

import java.io.*;
import java.util.*;

@Service
public class CsvProcessingService {

    private Map<String, String> cellValuesMap = new HashMap<>(); // Stores cell values by reference
    private Set<String> cellsBeingEvaluated = new HashSet<>(); // Tracks cells currently being evaluated

    public ByteArrayInputStream processCsvFile(InputStream inputStream) {
        List<String[]> processedCsvData = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(bufferedReader);

            int rowNumber = 1;
            for (CSVRecord record : csvRecords) {
                String[] processedRow = new String[record.size()];
                char columnChar = 'A';

                for (int columnIndex = 0; columnIndex < record.size(); columnIndex++) {
                    String cellValue = record.get(columnIndex);
                    String cellReference = generateCellReference(rowNumber, columnIndex);

                    String processedCellValue = processCellValue(cellValue, cellReference);
                    cellValuesMap.put(cellReference, processedCellValue);

                    processedRow[columnIndex] = processedCellValue;
                    columnChar++;
                }
                processedCsvData.add(processedRow);
                rowNumber++;
            }
        } catch (IOException e) {
            throw new CsvProcessingException("Failed to process CSV file", e);
        }

        try {
            return convertToCsvFile(processedCsvData);
        } catch (IOException e) {
            throw new CsvProcessingException("Failed to convert processed data to CSV", e);
        }
    }

    private String processCellValue(String cellValue, String cellReference) {
        if (cellValue.startsWith("=")) {
            if (cellsBeingEvaluated.contains(cellReference)) {
                return "ERROR: Circular Reference"; // Detect circular reference
            }

            cellsBeingEvaluated.add(cellReference); // Mark cell as being evaluated
            String evaluatedValue = evaluateFormula(cellValue.substring(1)); // Remove '=' and evaluate
            cellsBeingEvaluated.remove(cellReference); // Remove after evaluation
            return evaluatedValue;
        } else {
            return cellValue; // Return the literal value
        }
    }

    private String generateCellReference(int rowNumber, int columnIndex) {
        char columnChar = (char) ('A' + columnIndex);
        return columnChar + String.valueOf(rowNumber);
    }

    private String evaluateFormula(String formula) {
        try {
            for (String reference : cellValuesMap.keySet()) {
                if (formula.contains(reference)) {
                    String referenceValue = cellValuesMap.get(reference);
                    if (referenceValue == null || referenceValue.startsWith("ERROR")) {
                        return "ERROR"; // Handle error in reference
                    }
                    formula = formula.replace(reference, referenceValue);
                }
            }

            double evaluationResult = evaluateExpression(formula);

            if (evaluationResult == Math.floor(evaluationResult)) {
                return String.valueOf((int) evaluationResult); // Return integer value
            } else {
                return String.valueOf(evaluationResult); // Return decimal value
            }
        } catch (ArithmeticException e) {
            return "ERROR: Division by zero";
        } catch (RuntimeException e) {
            return "ERROR";
        }
    }

    private double evaluateExpression(final String expression) {
        return new Object() {
            int position = -1, currentChar;

            void nextCharacter() {
                currentChar = (++position < expression.length()) ? expression.charAt(position) : -1;
            }

            boolean consumeCharacter(int characterToConsume) {
                while (currentChar == ' ') nextCharacter();
                if (currentChar == characterToConsume) {
                    nextCharacter();
                    return true;
                }
                return false;
            }

            double parse() {
                nextCharacter();
                double result = parseExpression();
                if (position < expression.length()) throw new RuntimeException("Unexpected: " + (char) currentChar);
                return result;
            }

            double parseExpression() {
                double result = parseTerm();
                for (; ; ) {
                    if (consumeCharacter('+')) result += parseTerm();
                    else if (consumeCharacter('-')) result -= parseTerm();
                    else return result;
                }
            }

            double parseTerm() {
                double result = parseFactor();
                for (; ; ) {
                    if (consumeCharacter('*')) result *= parseFactor();
                    else if (consumeCharacter('/')) {
                        double divisor = parseFactor();
                        if (divisor == 0) throw new ArithmeticException("Division by zero");
                        result /= divisor;
                    } else return result;
                }
            }

            double parseFactor() {
                if (consumeCharacter('+')) return parseFactor();
                if (consumeCharacter('-')) return -parseFactor();

                double result;
                int startPosition = this.position;
                if (consumeCharacter('(')) {
                    result = parseExpression();
                    consumeCharacter(')');
                } else if ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') {
                    while ((currentChar >= '0' && currentChar <= '9') || currentChar == '.') nextCharacter();
                    result = Double.parseDouble(expression.substring(startPosition, this.position));
                } else if (currentChar >= 'A' && currentChar <= 'Z') {
                    while (currentChar >= 'A' && currentChar <= 'Z') nextCharacter();
                    String variable = expression.substring(startPosition, this.position);
                    result = Double.parseDouble(cellValuesMap.get(variable));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) currentChar);
                }

                if (consumeCharacter('^')) result = Math.pow(result, parseFactor());
                return result;
            }
        }.parse();
    }

    private ByteArrayInputStream convertToCsvFile(List<String[]> data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream))) {
            for (String[] row : data) {
                printWriter.println(String.join(",", row));
            }
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
