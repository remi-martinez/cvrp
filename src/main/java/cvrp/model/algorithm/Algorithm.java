package cvrp.model.algorithm;

public enum Algorithm {
    FILL_VEHICLE("Remplissage camion"),
    RANDOM("Aléatoire"),
    TEST("Test"),
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

    @Override
    public String toString() {
        return getAlgorithmName();
    }
}
