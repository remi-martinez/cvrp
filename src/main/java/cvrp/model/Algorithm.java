package cvrp.model;

public enum Algorithm {
    RANDOM("Aléatoire"),
    SIMULATED_ANNEALING("Recuit simulé"),
    TABU("Tabou");

    private final String algorithmName;

    Algorithm(String nomAlgoTourne)
    {
        this.algorithmName = nomAlgoTourne;
    }

    public String getAlgorithmName()
    {
        return algorithmName;
    }
}
