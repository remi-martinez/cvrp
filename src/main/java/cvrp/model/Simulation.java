package cvrp.model;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Simulation {

    public static void prepareSimu(Algorithm algorithm, int iterationCount, float variation, int tabuListSize) {
        CsvExporter csvExporter;
        if(Algorithm.SIMULATED_ANNEALING.equals(algorithm)){
            csvExporter = new CsvExporter(algorithm, iterationCount, variation);
        }else{
            csvExporter = new CsvExporter(algorithm, iterationCount, tabuListSize);
        }
        CSVWriter writer = csvExporter.createCsv();

        long start = System.nanoTime();
        System.out.println("[SIMULATION] Lancement de tous les fichiers les parametres " + csvExporter);

        File directory = new File(new File("").getAbsolutePath() + "\\files\\");
        FilenameFilter filter = (f, name) -> name.endsWith(".txt");
        String[] pathnames = directory.list(filter);

        assert pathnames != null;
        int index = 1;
        int totalSize = pathnames.length;
        for (String file : pathnames) {
            System.out.printf("[SIMULATION] (%s) Lecture fichier %s", index++ + "/" + totalSize, file);
            try {
                Simulation.startSimu(file, csvExporter, writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long stop = System.nanoTime();
        System.out.printf("[SIMULATION] Simulation terminee (temps total = %.2fs).%n", Math.abs(start - stop) / 1000000000.0f);
    }

    public static void startSimu(String fileName, CsvExporter csvExporter, CSVWriter writer) throws IOException {
        File file = new File(new File("").getAbsolutePath() + "\\files\\" + fileName);
        Graph _graph = new Graph(file);
        Graph graph = Generation.fillVehicle(_graph, Vehicle.QUANTITY_MAX); // Cette valeur peut être modifié pour remplir les véhicules à l'initialisation à moitié par exemple.

        long startLocal, stopLocal;

        CsvData csvData = CsvDataBuilder.builder()
                .fileName(fileName)
                .clientCount(graph.getClientCount())
                .baseFitness(graph.getFitness())
                .minVehicleCount(graph.getMinVehicles())
                .metaheuristic(csvExporter.getAlgorithm())
                .iterationCount(csvExporter.getIterationCount())
                .variation(csvExporter.getVariation())
                .tabuListSize(csvExporter.getTabuListSize())
                .build();


        // Ici : on lance la simulation
        Metaheuristic m = new Metaheuristic(graph);
        Graph optimizedGraph;

        startLocal = System.currentTimeMillis();
        if(csvExporter.getAlgorithm() == Algorithm.SIMULATED_ANNEALING) {
            optimizedGraph = m.simulatedAnnealing(csvExporter.getIterationCount(), csvExporter.getVariation());
        } else {
            optimizedGraph = m.tabuSearch(csvExporter.getIterationCount(), csvExporter.getTabuListSize());
        }

        // Fin de la simulation
        stopLocal = System.currentTimeMillis();
        long executionTime = Math.abs(startLocal- stopLocal);
        System.out.println(" - simulation terminee (" + executionTime + "ms).");
        csvData.setResultFitness(optimizedGraph.getFitness());
        csvData.setMinVehicleCountResult(optimizedGraph.getMinVehicles());
        csvData.setExecutionTime(executionTime);

        csvExporter.writeLine(csvData, writer);
    }
}
