package cvrp.model;

public final class CsvDataBuilder {
    // For simulated annealing
    private double variation;
    // For tabu
    private int tabuListSize;
    private String fileName; // Nom du fichier
    private int clientCount; // Nombre de clients
    private double baseFitness; // Fitness de base
    private int minVehicleCount; // Nombre de véhicules minimum de base
    private Algorithm baseAlgorithm; // Algorithme de base (RANDOM ou FILL_TRUCK)
    private Algorithm metaheuristic; // Recuit ou Tabu (type d'algo)
    private double resultFitness; // Fitness résultat après simulation
    private int minVehicleCountResult; // Nombre de véhicules minimum après simulation
    private int iterationCount; // Nombre d'itérations

    private CsvDataBuilder() {
    }

    public static CsvDataBuilder builder() {
        return new CsvDataBuilder();
    }

    public CsvDataBuilder variation(double variation) {
        this.variation = variation;
        return this;
    }

    public CsvDataBuilder tabuListSize(int tabuListSize) {
        this.tabuListSize = tabuListSize;
        return this;
    }

    public CsvDataBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public CsvDataBuilder clientCount(int clientCount) {
        this.clientCount = clientCount;
        return this;
    }

    public CsvDataBuilder baseFitness(double baseFitness) {
        this.baseFitness = baseFitness;
        return this;
    }

    public CsvDataBuilder minVehicleCount(int minVehicleCount) {
        this.minVehicleCount = minVehicleCount;
        return this;
    }

    public CsvDataBuilder baseAlgorithm(Algorithm baseAlgorithm) {
        this.baseAlgorithm = baseAlgorithm;
        return this;
    }

    public CsvDataBuilder metaheuristic(Algorithm metaheuristic) {
        this.metaheuristic = metaheuristic;
        return this;
    }

    public CsvDataBuilder resultFitness(double resultFitness) {
        this.resultFitness = resultFitness;
        return this;
    }

    public CsvDataBuilder minVehicleCountResult(int minVehicleCountResult) {
        this.minVehicleCountResult = minVehicleCountResult;
        return this;
    }

    public CsvDataBuilder iterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
        return this;
    }

    public CsvData build() {
        return new CsvData(fileName, clientCount, baseFitness, minVehicleCount, baseAlgorithm, metaheuristic, resultFitness, minVehicleCountResult, iterationCount, variation, tabuListSize);
    }
}
