package cvrp.model;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class Simulation {

    public static void prepareSimu(Algorithm algorithm, int iterationCount, float variation, int temperature ,int tabuListSize) {
        CsvExporter csvExporter;
        if(Algorithm.SIMULATED_ANNEALING.equals(algorithm)){
            csvExporter = new CsvExporter(algorithm, iterationCount, variation, temperature);
        }else{
            csvExporter = new CsvExporter(algorithm, iterationCount, tabuListSize);
        }
        CSVWriter writer = csvExporter.createCsv();

        long start = System.nanoTime();
        System.out.println("[SIMULATION] Lancement de tous les fichiers avec les parametres " + csvExporter);

        File directory = new File(new File("").getAbsolutePath() + "\\files\\imports\\");
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
        File file = new File(new File("").getAbsolutePath() + "\\files\\imports\\" + fileName);
        Graph _graph = new Graph(file);
        Graph graph = Generation.randomGeneration(_graph);

        CsvData csvData = CsvDataBuilder.builder()
                .fileName(fileName)
                .clientCount(graph.getClientCount())
                .baseFitness(graph.getFitness())
                .minVehicleCount(graph.getMinVehicles())
                .metaheuristic(csvExporter.getAlgorithm())
                .iterationCount(csvExporter.getIterationCount())
                .variation(csvExporter.getVariation())
                .tabuListSize(csvExporter.getTabuListSize())
                .temperature(csvExporter.getTemperature())
                .build();

        // Ici : on lance la simulation
        Metaheuristic m = new Metaheuristic(graph);
        Graph optimizedGraph = null;
        int fitnessCompare = 99999999;

        long startLocal, stopLocal;
        long executionTime = 0;

        startLocal = System.currentTimeMillis();

        if(csvExporter.getAlgorithm() == Algorithm.SIMULATED_ANNEALING) {
            //Comme nous n'avons pas la solution optimal avec recuit nous le lançons plusieurs fois pour obtenir un résultat significatif
            for (int i = 0; i<3; i++){
                Graph simulatedGraph = m.simulatedAnnealing(csvExporter.getIterationCount(), csvExporter.getVariation(), csvExporter.getTemperature());

                // Fin de la simulation
                if(simulatedGraph.getFitness() < fitnessCompare){
                    optimizedGraph = simulatedGraph;
                    stopLocal = System.currentTimeMillis();
                    executionTime = Math.abs(startLocal - stopLocal);
                }
            }
        } else {
            optimizedGraph = m.tabuSearch(csvExporter.getIterationCount(), csvExporter.getTabuListSize());

            // Fin de la simulation
            stopLocal = System.currentTimeMillis();
            executionTime = Math.abs(startLocal - stopLocal);
        }

        System.out.println(" - simulation terminee (" + executionTime + "ms).");

        csvData.setResultFitness(optimizedGraph.getFitness());
        csvData.setMinVehicleCountResult(optimizedGraph.getVehicles().size());
        csvData.setExecutionTime(executionTime);

        csvExporter.writeLine(csvData, writer);
    }
}
