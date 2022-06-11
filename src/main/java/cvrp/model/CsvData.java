package cvrp.model;

public class CsvData {
    private String fileName; // Nom du fichier
    private int clientCount; // Nombre de clients
    private double baseFitness; // Fitness de base
    private int minVehicleCount; // Nombre de véhicules minimum de base
    private Algorithm metaheuristic; // Recuit ou Tabu (type d'algo)
    private double resultFitness; // Fitness résultat après simulation
    private int minVehicleCountResult; // Nombre de véhicules minimum après simulation
    private int iterationCount; // Nombre d'itérations
    private long executionTime; // Temps d'éxécution de l'algorithme

    // For simulated annealing
    private double variation;
    private int temperature;

    // For tabu
    private int tabuListSize;

    public CsvData(String fileName,
                   int clientCount,
                   double baseFitness,
                   int minVehicleCount,
                   Algorithm metaheuristic,
                   double resultFitness,
                   int minVehicleCountResult,
                   int iterationCount,
                   double variation,
                   int temperature,
                   int tabuListSize,
                   long executionTime
    ) {
        this.fileName = fileName;
        this.clientCount = clientCount;
        this.baseFitness = baseFitness;
        this.minVehicleCount = minVehicleCount;
        this.metaheuristic = metaheuristic;
        this.resultFitness = resultFitness;
        this.minVehicleCountResult = minVehicleCountResult;
        this.iterationCount = iterationCount;
        this.variation = variation;
        this.tabuListSize = tabuListSize;
        this.executionTime = executionTime;
        this.temperature = temperature;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public double getBaseFitness() {
        return baseFitness;
    }

    public void setBaseFitness(double baseFitness) {
        this.baseFitness = baseFitness;
    }

    public int getMinVehicleCount() {
        return minVehicleCount;
    }

    public void setMinVehicleCount(int minVehicleCount) {
        this.minVehicleCount = minVehicleCount;
    }

    public Algorithm getMetaheuristic() {
        return metaheuristic;
    }

    public void setMetaheuristic(Algorithm metaheuristic) {
        this.metaheuristic = metaheuristic;
    }

    public double getResultFitness() {
        return resultFitness;
    }

    public void setResultFitness(double resultFitness) {
        this.resultFitness = resultFitness;
    }

    public int getMinVehicleCountResult() {
        return minVehicleCountResult;
    }

    public void setMinVehicleCountResult(int minVehicleCountResult) {
        this.minVehicleCountResult = minVehicleCountResult;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount) {
        this.iterationCount = iterationCount;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }

    public int getTabuListSize() {
        return tabuListSize;
    }

    public void setTabuListSize(int tabuListSize) {
        this.tabuListSize = tabuListSize;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "CsvData{" +
                "fileName='" + fileName + '\'' +
                ", clientCount=" + clientCount +
                ", baseFitness=" + baseFitness +
                ", minVehicleCount=" + minVehicleCount +
                ", metaheuristic=" + metaheuristic +
                ", resultFitness=" + resultFitness +
                ", vehicleCountResult=" + minVehicleCountResult +
                ", iterationCount=" + iterationCount +
                ", variation=" + variation +
                ", temperature=" + temperature +
                ", tabuListSize=" + tabuListSize +
                ", executionTime" + executionTime +
                '}';
    }

    public String[] getRowForAlgorithm(Algorithm algorithm) {
        String[] rows = new String[]{fileName, clientCount + "", (double)Math.round(baseFitness * 100.0) / 100.0 + "", minVehicleCount + "",
                Utils.removeAccents(String.valueOf(metaheuristic)), (double)Math.round(resultFitness * 100.0) / 100.0 + "", minVehicleCountResult + "",
                iterationCount + "", executionTime +"", (double)Math.round((baseFitness - resultFitness) * 100.0) / 100.0 + ""};

        int i = 0;
        for (String row : rows) {
            rows[i++] = row.replace(".",","); // Comma instead of dot
        }

        if (algorithm == Algorithm.SIMULATED_ANNEALING) {
            return Utils.concatenate(rows, new String[]{(double)Math.round(variation * 10.0)/10.0 + "", String.valueOf(temperature)});
        } else {
            return Utils.concatenate(rows, new String[]{tabuListSize + ""});
        }
    }
}
