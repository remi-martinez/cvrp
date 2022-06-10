package cvrp.model;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

public class Simulation {

    public static void prepareSimu(Algorithm algorithm, List<Algorithm> baseAlgorithms, int iterationCount, float variation) {
        Simulation.prepareSimu(algorithm, baseAlgorithms, iterationCount, variation, 0); // Simulated annealing
    }

    public static void prepareSimu(Algorithm algorithm, List<Algorithm> baseAlgorithms, int iterationCount, int tabuListSize) {
        Simulation.prepareSimu(algorithm, baseAlgorithms, iterationCount, 0, tabuListSize); // Tabu
    }

    public static void prepareSimu(Algorithm algorithm, List<Algorithm> baseAlgorithms, int iterationCount, float variation, int tabuListSize) {
        CsvExporter csvExporter;
        if (Algorithm.SIMULATED_ANNEALING.equals(algorithm)) {
            csvExporter = new CsvExporter(algorithm, baseAlgorithms.get(0), iterationCount, variation);
        } else {
            csvExporter = new CsvExporter(algorithm, baseAlgorithms.get(0), iterationCount, tabuListSize);
        }
        CSVWriter writer = csvExporter.createCsv();

        for (Algorithm baseAlgorithm : baseAlgorithms) {
            csvExporter.setBaseAlgorithm(baseAlgorithm);

            long start = System.nanoTime();
            System.out.println("[SIMULATION] Lancement de tous les fichiers les parametres " + csvExporter);

            File directory = new File(new File("").getAbsolutePath() + "\\files\\");
            FilenameFilter filter = (f, name) -> name.endsWith(".txt");
            String[] pathnames = directory.list(filter);

            assert pathnames != null;
            long startLocal, stopLocal;
            int index = 1;
            int totalSize = pathnames.length;
            for (String file : pathnames) {
                startLocal = System.nanoTime();
                System.out.printf("[SIMULATION] (%s) Lecture fichier %s", index++ + "/" + totalSize, file);

                try {
                    Simulation.startSimu(file, csvExporter, writer);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                stopLocal = System.nanoTime();
                System.out.println(" - simulation terminee (" + Math.abs(startLocal - stopLocal) / 1000000 + "ms).");
            }

            long stop = System.nanoTime();
            System.out.printf("[SIMULATION] Simulation terminee (temps total = %.2fs).%n", Math.abs(start - stop) / 1000000000.0f);

        }

        try {
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startSimu(String fileName, CsvExporter csvExporter, CSVWriter writer) throws IOException {
        File file = new File(new File("").getAbsolutePath() + "\\files\\" + fileName);
        boolean isRandomized = Algorithm.RANDOM.equals(csvExporter.getBaseAlgorithm());
        Graph _graph = new Graph(file);
        Graph graph = Generation.graphGeneration(_graph, isRandomized);

        CsvData csvData = CsvDataBuilder.builder()
                .fileName(fileName)
                .clientCount(graph.getClientCount())
                .baseFitness(graph.getFitness())
                .minVehicleCount(graph.getMinVehicles())
                .baseAlgorithm(csvExporter.getBaseAlgorithm())
                .metaheuristic(csvExporter.getAlgorithm())
                .iterationCount(csvExporter.getIterationCount())
                .variation(csvExporter.getVariation())
                .tabuListSize(csvExporter.getTabuListSize())
                .build();


        // Ici : on lance la simulation
        Metaheuristic m = new Metaheuristic(graph);
        Graph optimizedGraph;

        if(csvExporter.getAlgorithm() == Algorithm.SIMULATED_ANNEALING) {
            optimizedGraph = m.simulatedAnnealing(csvExporter.getIterationCount(), csvExporter.getVariation());
        } else {
            optimizedGraph = m.tabuSearch(csvExporter.getIterationCount(), csvExporter.getTabuListSize());
        }

        // Fin de la simulation
        csvData.setResultFitness(optimizedGraph.getFitness());
        csvData.setMinVehicleCountResult(optimizedGraph.getMinVehicles());

        csvExporter.writeLine(csvData, writer);
    }
}
