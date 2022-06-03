package cvrp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TransfoElementaire {

    /**
     * Intra route
     * * Relocate
     * * Exchange
     * * 2-opt
     *
     * Inter route
     * * Relocate
     * * Exchange
     * * Cross-exchange
     * * Fusion
     * * Inverse de la fusion
     */

    private static final Random RANDOM = new Random();

    private Graph graph;

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public TransfoElementaire(Graph graph) {
        this.graph = graph;
    }

    //    public void twoOpt(Vehicle v)
//    {
//        // Get tour size
//        int size = g.getClientList().size();
//
//        if(g.getNewMap().isEmpty()){
//            g.setNewMap((ArrayList<Client>) g.getClientList().clone());
//        }
//
//        for (int i=0;i<size;i++)
//        {
//            g.getNewMap().set(i, g.getClientList().get(i));
//        }
//
//        // On repète tant que ça ne s'améliore pas
//        int improve = 0;
//        int iteration = 0;
//
//        while ( improve < 800 ){
////            double best_distance = g.calculDistanceTotal();
//            double best_distance = 14;
//
//            for ( int i = 1; i < size - 1; i++ ){
//                for ( int k = i + 1; k < size; k++){
//                    TwoOptSwap(v, i, k );
//                    iteration++;
////                    double new_distance = g.calculDistanceTotal();
//                    double new_distance = 14;
//
//                    if ( new_distance < best_distance ){
//                        // Amélioration
//                        improve = 0;
//
//                        for (int j=0;j<size;j++)
//                        {
//                            g.getClientList().set(j, g.getNewMap().get(j));
//                        }
//
//                        best_distance = new_distance;
//
//                        // On notifie l'affichage
////                        NotifyTourUpdate(g, Double.toString(best_distance), Integer.toString(iteration));
//                    }
//                }
//            }
//            improve ++;
//        }
//    }
//
//    public void TwoOptSwap(Vehicle v, int i, int k )
//    {
//        int size = v.getVisit().size();
//        ArrayList<Client> newVisit = (ArrayList<Client>) v.getVisit().clone();
//
//        for ( int c = 0; c <= i - 1; ++c) {
//            v.getVisit().set( c, newVisit.get(c) );
//        }
//
//        int dec = 0;
//        for ( int c = i; c <= k; ++c ){
//            v.getVisit().set( c, newVisit.get( k - dec ) );
//            dec++;
//        }
//
//        for ( int c = k + 1; c < size; ++c ){
//            v.getVisit().set( c, newVisit.get(c) );
//        }
//    }

    /**
     * Déplacement aléatoire d'un point dans sa route
     */
    public Graph randomNeighbor(){
        int vehicleToModifyIndex = RANDOM.nextInt(this.graph.getVehicles().size());
        Vehicle vehicleToModify = this.graph.getVehicles().get(vehicleToModifyIndex);
        ArrayList<Client> visitToModify = vehicleToModify.getVisit();

        if (visitToModify.size() > 1) {
            int clientToMoveIndex = RANDOM.nextInt(visitToModify.size());

//            System.out.println(vehicleToModify.getLength());
            Client clientToMove = vehicleToModify.remove(clientToMoveIndex);
//            System.out.println(vehicleToModify.getLength());

            int insertIndex;
            while ((insertIndex = RANDOM.nextInt(visitToModify.size() + 1)) == clientToMoveIndex) ;
            vehicleToModify.add(insertIndex, clientToMove);
        }
//        System.out.println(vehicleToModify.getLength());

        return this.graph;
    }

    /**
     *  Deplace un point aléatoirement d'une route à une autre
     *
     */
    public Graph randomNeighbor2() {
        Vehicle randomVehicleToModify = this.graph.getVehicles().get(RANDOM.nextInt(this.graph.getVehicles().size()));
        Client randomClientToModify = randomVehicleToModify.remove(RANDOM.nextInt(randomVehicleToModify.getVisit().size()));

        if(randomVehicleToModify.getVisit().size() == 0){
            this.graph.getVehicles().remove(randomVehicleToModify);
        }

        //On randomize l'ordre des véhicules
        ArrayList<Vehicle> randomizeVehicles = (ArrayList<Vehicle>) this.graph.getVehicles().clone();
        Collections.shuffle(randomizeVehicles);

        boolean clientAdded = false;
        //On parcourt tout les véhicules
        for (Vehicle v: randomizeVehicles) {
            //Sauf le véhicule que l'on modifie et
            if(!v.equals(randomVehicleToModify)){
                // S'il a la place pour accepter ce nouveau client
                if(v.getQuantity() + randomClientToModify.getQuantity() <= randomVehicleToModify.QUANTITY_MAX){
                    //Alors on insere le point aléatoirement dans la route
                    v.add(RANDOM.nextInt(v.getVisit().size()), randomClientToModify);
                    clientAdded = true;
                    break;
                }

            }
        }
        if(!clientAdded){
            Vehicle newVehicle = new Vehicle(this.graph.getWarehouse());
            newVehicle.add(randomClientToModify);
            this.graph.getVehicles().add(newVehicle);
        }

        return this.graph;

    }

    public Graph simulatedAnnealing(int maxIteration, float variation){
        ArrayList<Vehicle> currentSolution;
        double latestFitness = this.graph.getFitness();
        double temperature = this.graph.getInitialTemperature();
        //double variation = 0.90;

        for (int i = 0; i < maxIteration; i++) {
            currentSolution = this.graph.cloneCurrentSolution();
            if (RANDOM.nextBoolean()) {
                this.graph = randomNeighbor();
            } else {
                this.graph = randomNeighbor2();
            }
            double currentTotalFitness = this.graph.getFitness();
            if (currentTotalFitness < latestFitness || (RANDOM.nextDouble() < Math.exp((latestFitness - currentTotalFitness) / temperature))) {
                latestFitness = currentTotalFitness;
            } else {
                this.graph.setVehicles(currentSolution);
            }
            temperature = variation * temperature;
        }
        return this.graph;
    }

}
