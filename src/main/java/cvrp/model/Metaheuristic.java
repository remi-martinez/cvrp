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

    /**
     * Algorithme du recuit simulé
     *
     * @param maxIteration Nombres d'itération par température
     * @param variation variation de la température
     * @return Graph g
     */
    public Graph simulatedAnnealing(int maxIteration, float variation, int temp) {
        ArrayList<Vehicle> currentSolution;
        double latestFitness = this.graph.getFitness();
        double temperature = (double) temp;
        Graph g;
        int nbTemp = (int)(Math.log(Math.log(0.8) / Math.log(0.01))/Math.log(variation) )* 3; //Nb changement de temperature
        for (int i = 0; i < nbTemp; i++){
            for (int j = 0; j < maxIteration; j++) {
                currentSolution = this.graph.cloneVehicles();
                int rand = RANDOM.nextInt(2);
                switch(rand){
                    case 0 :
                        g = t.generateIntraRelocateNeighbor();
                        break;
                    case 1 :
                        g = t.generateExtraRelocateNeighbor();
                        break;
                    case 2:
                        g = t.generateExchangeNeighbor();
                        break;
                    default:
                        g = t.generateTwoOptNeighbor();
                        break;
                }
                double currentTotalFitness = g.getFitness();
                double delta = currentTotalFitness - latestFitness;
                if (delta < 0 || (RANDOM.nextDouble() < Math.exp(-delta / temperature))) {
                    latestFitness = currentTotalFitness;
                    this.graph = g;
                } else {
                    this.graph.setVehicles(currentSolution);
                }
                temperature = variation * temperature;
            }
        }
        return this.graph;
    }

    public Graph simulatedAnnealing(int maxIteration, float variation){
        return this.simulatedAnnealing(maxIteration, variation, (int)this.graph.getInitialTemperature());
    }

    public Graph test(){
        return this.graph = t.generateIntraRelocateNeighbor();
    }
}
