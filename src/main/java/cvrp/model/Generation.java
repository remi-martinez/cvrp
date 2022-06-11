package cvrp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Generation {

    private static Random RANDOM = new Random();


    /**
     * @param g
     * @param quantityMax Par défaut la quantité Max est la constante Vehicle.QUANTITY_MAX mais
     *                    pour certaines conditions on peux la modifier afin de remplir les camions à moitié au début par exemple
     * @return
     */
    public static Graph fillVehicle(Graph g, int quantityMax) {
        int indexVehicle = 0;

        ArrayList<Client> clients = (ArrayList<Client>) g.getClientList().clone();
        Client depot = clients.remove(0);
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        int index = 0;

        vehicles.add(new Vehicle(depot));
        Vehicle vehicle = vehicles.get(indexVehicle);

        while (clients.size() != 0) {
            Client c = clients.get(index);
            if ((int) vehicle.getVisit().stream().mapToDouble(Client::getQuantity).sum() + c.getQuantity() > quantityMax) {
                vehicles.add(new Vehicle(depot));
                indexVehicle++;
                vehicle = vehicles.get(indexVehicle);
            }
            vehicle.add(c);
            clients.remove(index);
        }
        g.setVehicles(vehicles);
        return g;
    }

    public static Graph fillVehicle(Graph g){
        return Generation.fillVehicle(g, Vehicle.QUANTITY_MAX);
    }

    public static Graph randomGeneration(Graph g){
        int indexVehicle = 0;

        ArrayList<Client> clients = (ArrayList<Client>) g.getClientList().clone();
        Client depot = clients.remove(0);
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        Collections.shuffle(clients);

        int index = 0;

        vehicles.add(new Vehicle(depot));
        Vehicle vehicle = vehicles.get(indexVehicle);

        while (clients.size() != 0) {
            Client c = clients.get(index);
            if ((int) vehicle.getVisit().stream().mapToDouble(Client::getQuantity).sum() + c.getQuantity() > Vehicle.QUANTITY_MAX) {
                vehicles.add(new Vehicle(depot));
                indexVehicle++;
                vehicle = vehicles.get(indexVehicle);
            }
            vehicle.add(c);
            clients.remove(index);
        }
        g.setVehicles(vehicles);
        return g;
    }

}
