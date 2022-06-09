package cvrp.model;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;


public class CsvExporter {
    private String pathFile;
    private Algorithm algorithm;
    private int iterationCount;
    private float variation;
    private int tabuListSize;


    private final static String[] COLUMNS_HEADERS = new String[]{
            "Nom fichier", "Nb clients", "Fitness de base",
            "Nb vehicules min", "Metaheuristique", "Fitness resultat",
            "Vehicules resultat", "Nombre iterations"
    };
    private final static String[] COLUMNS_SIMULATED_ANNEALING = new String[]{"Variation (µ)"};
    private final static String[] COLUMNS_TABU = new String[]{"Taille liste tabou"};


    public CsvExporter(Algorithm algorithm, int iterationCount) {
        this.algorithm = algorithm;
        this.iterationCount = iterationCount;
        this.pathFile = new File("").getAbsolutePath() + "\\files\\exports\\";
    }

    public CsvExporter(Algorithm algorithm, int iterationCount, float variation) {
        this(algorithm, iterationCount);
        this.variation = variation;
    }

    public CsvExporter(Algorithm algorithm, int iterationCount, int tabuListSize) {
        this(algorithm, iterationCount);
        this.tabuListSize = tabuListSize;
    }


    public CSVWriter createCsv() {
        CSVWriter writer = null;
        try {
            String strVariation = variation == 0f ? "" : String.valueOf(variation);
            String strTabuListSize = tabuListSize == 0 ? "" : String.valueOf(tabuListSize);
            String fileName = this.pathFile + String.format("%s_%d_%s%s.csv", algorithm, iterationCount, strVariation, strTabuListSize);

            if(new File(fileName).exists()) {
                throw new RuntimeException("Le fichier " + fileName + " existe deja. Creation annulee.");
            }

            writer = new CSVWriter(new FileWriter(fileName),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);

            // Colonnes de base + recuit OU colonnes de base + tabou
            String[] allColumns = (algorithm == Algorithm.SIMULATED_ANNEALING)
                    ? Utils.concatenate(COLUMNS_HEADERS, COLUMNS_SIMULATED_ANNEALING)
                    : Utils.concatenate(COLUMNS_HEADERS, COLUMNS_TABU);
            writer.writeNext(allColumns);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Fichier en cours d'utilisation.");
        }
        return writer;
    }
    public void writeLine(CsvData csvData, CSVWriter writer) {
        try {
            if(writer == null) return;
            writer.writeNext(csvData.getRowForAlgorithm(this.algorithm));
            writer.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String writeCsv(List<String[]> stringArray) {
        String fileName = "";
        try {
            fileName = getDateTime() + ".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(this.pathFile + fileName),
                    CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.RFC4180_LINE_END);
            writer.writeAll(Collections.singleton(COLUMNS_HEADERS));
            writer.writeAll(stringArray);
            writer.flush();
            writer.close();
        } catch(IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public float getVariation() {
        return variation;
    }

    public void setVariation(float variation) {
        this.variation = variation;
    }

    public int getTabuListSize() {
        return tabuListSize;
    }

    public void setTabuListSize(int tabuListSize) {
        this.tabuListSize = tabuListSize;
    }

    @Override
    public String toString() {
        return "CsvExporter{" +
                "algorithm=" + algorithm +
                ", iterationCount=" + iterationCount +
                ", variation=" + variation +
                ", tabuListSize=" + tabuListSize +
                '}';
    }
}
