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
     * <p>
     * Inter route
     * * Relocate
     * * Exchange
     * * Cross-exchange
     * * Fusion
     * * Inverse de la fusion
     */

    private static Random RANDOM = new Random();

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

    /**
     * RELOCATE INTRA
     * Déplace un point aléatoirement dans sa route
     */
    public Graph generateIntraRelocateNeighbor() {
        int vehicleToModifyIndex = RANDOM.nextInt(this.graph.getVehicles().size());
        Vehicle vehicleToModify = this.graph.getVehicles().get(vehicleToModifyIndex);
        ArrayList<Client> visitToModify = vehicleToModify.getVisit();

        if (visitToModify.size() > 1) {
            int clientToMoveIndex = RANDOM.nextInt(visitToModify.size());

            Client clientToMove = vehicleToModify.remove(clientToMoveIndex);

            int insertIndex;
            while ((insertIndex = RANDOM.nextInt(visitToModify.size() + 1)) == clientToMoveIndex);
            vehicleToModify.add(insertIndex, clientToMove);
        }

        return this.graph;
    }

    /**
     * RELOCATE EXTRA
     * Deplace un point aléatoirement d'une route à une autre
     */
    public Graph generateExtraRelocateNeighbor() {
        Vehicle randomVehicleToModify = this.graph.getVehicles().get(RANDOM.nextInt(this.graph.getVehicles().size()));
        Client randomClientToModify = randomVehicleToModify.remove(RANDOM.nextInt(randomVehicleToModify.getVisit().size()));

        if (randomVehicleToModify.getVisit().size() == 0) {
            this.graph.getVehicles().remove(randomVehicleToModify);
        }

        //On randomize l'ordre des véhicules
        ArrayList<Vehicle> randomizeVehicles = (ArrayList<Vehicle>) this.graph.getVehicles().clone();
        Collections.shuffle(randomizeVehicles);

        boolean clientAdded = false;
        //On parcourt tout les véhicules
        for (Vehicle v : randomizeVehicles) {
            //Sauf le véhicule que l'on modifie et
            if (!v.equals(randomVehicleToModify)) {
                // S'il a la place pour accepter ce nouveau client
                if (v.getQuantity() + randomClientToModify.getQuantity() <= randomVehicleToModify.QUANTITY_MAX) {
                    //Alors on insere le point aléatoirement dans la route
                    v.add(RANDOM.nextInt(v.getVisit().size()), randomClientToModify);
                    clientAdded = true;
                    break;
                }

            }
        }
        if (!clientAdded) {
            Vehicle newVehicle = new Vehicle(this.graph.getWarehouse());
            newVehicle.add(randomClientToModify);
            this.graph.getVehicles().add(newVehicle);
        }

        return this.graph;

    }

    /**
     * Génération de tout les voisins pour tout les points du graph
     *
     * @return neighbors
     */
    public ArrayList<ArrayList<Vehicle>> generateNeighbors() {
        ArrayList<ArrayList<Vehicle>> neighbors = new ArrayList<>();

        ArrayList<Vehicle> storedSolution = this.graph.cloneVehicles();

        for (int vehicleIndex = 0; vehicleIndex < storedSolution.size(); vehicleIndex++) {
            for (int pointIndex = 0; pointIndex < storedSolution.get(vehicleIndex).getVisit().size(); pointIndex++) {
                neighbors.addAll(generateRelocateInternNeighbors(vehicleIndex, pointIndex));
                neighbors.addAll(generateRelocateExternNeighbors(vehicleIndex, pointIndex));
//                neighbors.addAll(generateExchangeInternNeighbors(vehicleIndex, pointIndex));
                this.graph.setVehicles(this.graph.cloneVehicles());
            }
        }

        //On remet la solution sauvegardée au graph
        this.graph.setVehicles(storedSolution);

        return neighbors;

    }


    /**
     *Génération de TOUT les voisins avec RELOCATE INTERNE pour un véhicule et un point donné
     */
    public ArrayList<ArrayList<Vehicle>> generateRelocateInternNeighbors(int vehicleIndex, int pointIndex) {
        ArrayList<ArrayList<Vehicle>> neighbors = new ArrayList<>();
        ArrayList<Vehicle> storedSolution = this.graph.cloneVehicles();
        Vehicle vehicleToModify = this.graph.getVehicles().get(vehicleIndex);

        int size = vehicleToModify.getVisit().size();

        if (size > 1) {
            Client clientToMove = vehicleToModify.remove(pointIndex);

            for (int i = 0; i < size; i++) {
                if (i != pointIndex) {
                    vehicleToModify.add(i, clientToMove);
                    neighbors.add(this.graph.cloneVehicles());
                    vehicleToModify.remove(i);
                }
            }
            //On remet la solution sauvegardée au graph
            this.graph.setVehicles(storedSolution);
        }

        return neighbors;
    }

    /**
     *
     * Génération de TOUT les voisins avec RELOCATE EXTERNE pour un véhicule et un point donné
     * @param vehicleIndex
     * @param pointIndex
     * @return
     */
    public ArrayList<ArrayList<Vehicle>> generateRelocateExternNeighbors(int vehicleIndex, int pointIndex) {
        ArrayList<ArrayList<Vehicle>> neighbors = new ArrayList<>();

        ArrayList<Vehicle> defaultSolution = this.graph.cloneVehicles();

        Vehicle vehicleToModify = this.graph.getVehicles().get(vehicleIndex);
        Client clientToMove = vehicleToModify.remove(pointIndex);
        if (vehicleToModify.getVisit().isEmpty()) {
            this.graph.getVehicles().remove(vehicleIndex);
        }

        ArrayList<Vehicle> intermediateSolution = this.graph.cloneVehicles();

        for (int vehicleInsertIndex = 0; vehicleInsertIndex < intermediateSolution.size(); vehicleInsertIndex++) {

            if (vehicleToModify.getVisit().isEmpty() || vehicleInsertIndex != vehicleIndex) {

                Vehicle vehicleToInsert = this.graph.getVehicles().get(vehicleInsertIndex);

                if (vehicleToInsert.getQuantity() + clientToMove.getQuantity() <= vehicleToModify.QUANTITY_MAX) {
                    ArrayList<Client> clientsToInsert = vehicleToInsert.getVisit();
                    for (int pointInsertIndex = 0; pointInsertIndex <= clientsToInsert.size(); pointInsertIndex++) {
                        vehicleToInsert.add(pointInsertIndex, clientToMove);
                        neighbors.add(this.graph.cloneVehicles());
                        vehicleToInsert.remove(pointInsertIndex);
                    }

                }

            }
        }


        Vehicle newVehicle = new Vehicle(this.graph.getWarehouse());
        newVehicle.add(clientToMove);
        this.graph.getVehicles().add(newVehicle);
        neighbors.add(this.graph.getVehicles());
        this.graph.setVehicles(defaultSolution);

        return neighbors;
    }

    public ArrayList<ArrayList<Vehicle>> generateExchangeInternNeighbors(int vehicleIndex, int client1ToExchangeIndex) {
        ArrayList<ArrayList<Vehicle>> neighbors = new ArrayList<>();
        ArrayList<Vehicle> storedSolution = this.graph.cloneVehicles();
        Vehicle vehicleToModify = this.graph.getVehicles().get(vehicleIndex);

        int size = vehicleToModify.getVisit().size();

        if (size >= 2) {

            for (int client2ToExchangeIndex = 0; client2ToExchangeIndex < size; client2ToExchangeIndex++){
                if(client2ToExchangeIndex != client1ToExchangeIndex){
                    Client client2ToExchange = vehicleToModify.remove(client2ToExchangeIndex);
                    Client client1ToExchange = vehicleToModify.remove(client1ToExchangeIndex);

                    vehicleToModify.add(client1ToExchangeIndex, client2ToExchange);
                    vehicleToModify.add(client2ToExchangeIndex, client1ToExchange);

                    neighbors.add(this.graph.cloneVehicles());
                }

                //On remet la solution sauvegardée au graph
                this.graph.setVehicles(storedSolution);
            }
        }

        return neighbors;
    }
}
