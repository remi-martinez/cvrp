package cvrp.model;

public final class CsvDataBuilder {
    // For simulated annealing
    private double variation;
    private int temperature;
    // For tabu
    private int tabuListSize;
    private String fileName; // Nom du fichier
    private int clientCount; // Nombre de clients
    private double baseFitness; // Fitness de base
    private int minVehicleCount; // Nombre de véhicules minimum de base
    private Algorithm metaheuristic; // Recuit ou Tabu (type d'algo)
    private double resultFitness; // Fitness résultat après simulation
    private int minVehicleCountResult; // Nombre de véhicules minimum après simulation
    private int iterationCount; // Nombre d'itérations
    private long executionTime; // Temps d'éxecution


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

    public CsvDataBuilder executionTime(long executionTime){
        this.executionTime = executionTime;
        return this;
    }

    public CsvDataBuilder temperature(int temperature){
        this.temperature = temperature;
        return this;
    }

    public CsvData build() {
        return new CsvData(fileName, clientCount, baseFitness, minVehicleCount, metaheuristic, resultFitness, minVehicleCountResult, iterationCount, variation, temperature, tabuListSize, executionTime);
    }
}
