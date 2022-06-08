package cvrp.model;

public class CsvData {
    private String fileName;
    private String clientName;
    private Algorithm baseAlgorithm;
    private double baseFitness;
    private int minVehicleCount;
    private Metaheuristic metaheuristic;
    private double resultFitness;
    private int vehicleCountResult;
    private int iterationCount;

    // For simulated annealing
    private double variation;
    private double temperature;

    // For tabu
    private int tabuListSize;

    // Constructor without extra params
    public CsvData(String fileName,
                   String clientName,
                   Algorithm baseAlgorithm,
                   double baseFitness,
                   int minVehicleCount,
                   Metaheuristic metaheuristic,
                   double resultFitness,
                   int vehicleCountResult,
                   int iterationCount) {
        this(fileName, clientName, baseAlgorithm, baseFitness, minVehicleCount, metaheuristic, resultFitness, vehicleCountResult, iterationCount, 0d, 0d, 0);
    }

   // Constructor for Tabu
    public CsvData(String fileName,
                   String clientName,
                   Algorithm baseAlgorithm,
                   double baseFitness,
                   int minVehicleCount,
                   Metaheuristic metaheuristic,
                   double resultFitness,
                   int vehicleCountResult,
                   int iterationCount,
                   int tabuListSize) {
        this(fileName, clientName, baseAlgorithm, baseFitness, minVehicleCount, metaheuristic, resultFitness, vehicleCountResult, iterationCount, 0d, 0d, tabuListSize);
    }

    // Constructor for simulated annealing
    public CsvData(String fileName,
                   String clientName,
                   Algorithm baseAlgorithm,
                   double baseFitness,
                   int minVehicleCount,
                   Metaheuristic metaheuristic,
                   double resultFitness,
                   int vehicleCountResult,
                   int iterationCount,
                   double variation,
                   double temperature) {
        this(fileName, clientName, baseAlgorithm, baseFitness, minVehicleCount, metaheuristic, resultFitness, vehicleCountResult, iterationCount, variation, temperature, 0);

    }

    // Full constructor
    public CsvData(String fileName,
                   String clientName,
                   Algorithm baseAlgorithm,
                   double baseFitness,
                   int minVehicleCount,
                   Metaheuristic metaheuristic,
                   double resultFitness,
                   int vehicleCountResult,
                   int iterationCount,
                   double variation,
                   double temperature,
                   int tabuListSize) {
        this.fileName = fileName;
        this.clientName = clientName;
        this.baseAlgorithm = baseAlgorithm;
        this.baseFitness = baseFitness;
        this.minVehicleCount = minVehicleCount;
        this.metaheuristic = metaheuristic;
        this.resultFitness = resultFitness;
        this.vehicleCountResult = vehicleCountResult;
        this.iterationCount = iterationCount;
        this.variation = variation;
        this.temperature = temperature;
        this.tabuListSize = tabuListSize;
    }
}
