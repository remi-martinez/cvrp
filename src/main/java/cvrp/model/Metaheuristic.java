package cvrp.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Metaheuristic {

    public Graph graph;
    public TransfoElementaire t;
    private static Random RANDOM = new Random();

    public Metaheuristic(Graph graph){
        this.graph = graph;
        this.t = new TransfoElementaire(graph);
    }

    public Graph tabuSearch(int maximumIteration, int tabuLength) {
        ArrayList<Vehicle> bestSolution = this.graph.cloneVehicles();
        double latestFitness = this.graph.getFitness();
        double bestFitness = latestFitness;
        Queue<ArrayList<Vehicle>> tabuList = new LinkedList<>();

        ArrayList<ArrayList<Vehicle>> neighbors;

        for (int i = 0; i < maximumIteration; i++) {
            neighbors = t.generateNeighbors();
            ArrayList<Vehicle> min = neighbors.stream()
                    .filter(neighbor -> !tabuList.contains(neighbor))
                    .min((solution1, solution2) -> (int) (Graph.getFitness(solution1) - Graph.getFitness(solution2)))
                    .get();

            for (Vehicle v: min) {
                System.out.print(v.getQuantity() + "-");
            }

            double newRouteLength = Graph.getFitness(min);
            if (newRouteLength > latestFitness) {
                if (tabuList.size() == tabuLength) {
                    tabuList.remove();
                }
                tabuList.add(Graph.cloneVehicles(min));
            }

            latestFitness = newRouteLength;
            if (newRouteLength < bestFitness) {
                bestFitness = newRouteLength;
                bestSolution = Graph.cloneVehicles(min);
            }

            this.graph.setVehicles(min);
        }
        this.graph.setVehicles(bestSolution);
        return this.graph;
    }

    public Graph simulatedAnnealing(int maxIteration, float variation) {
        ArrayList<Vehicle> currentSolution;
        double latestFitness = this.graph.getFitness();
        double temperature = this.graph.getInitialTemperature();
        Graph g;

        for (int i = 0; i < maxIteration; i++) {
            currentSolution = this.graph.cloneVehicles();
            if (RANDOM.nextBoolean()) {
                g = t.randomNeighbor();
            } else {
                g = t.randomNeighbor2();
            }
            double currentTotalFitness = g.getFitness();
            double delta = latestFitness - currentTotalFitness;
            if (delta > 0 || (RANDOM.nextDouble() < Math.exp(delta / temperature))) {
                System.out.println(this.graph.getInitialTemperature());
                latestFitness = currentTotalFitness;
            } else {
                this.graph.setVehicles(currentSolution);
            }
            temperature = variation * temperature;
//            System.out.println(delta);
        }
        return this.graph;
    }
}
