package cvrp.model;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CsvExporter {
    public Metaheuristic metaheuristic;
    public Graph graph;
    public String pathFile;

    private final static String[] COLUMNS_HEADERS = new String[]{
            "Nom fichier", "Nombre de clients", "Algorithme de base", "Fitness de base",
            "Nombre de vehicules minimum", "Metaheuristiques", "Fitness resultat",
            "Vehicules resultat", "Nombre iterations"
    };
    private final static String[] COLUMNS_SIMULATED_ANNEALING = new String[]{"Variation", "Temperature"};
    private final static String[] COLUMNS_TABU = new String[]{"Taille liste"};


    public CsvExporter(Metaheuristic metaheuristic, Graph graph) {
        this.metaheuristic = metaheuristic;
        this.graph = graph;
        this.pathFile = new File("").getAbsolutePath() + "\\files\\exports\\";
    }

    public String writeCsv(CsvData csvData) {
        List<String[]> stringArray = new ArrayList<>();



        return this.writeCsv(stringArray);

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
}
