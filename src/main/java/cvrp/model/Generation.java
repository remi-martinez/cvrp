package cvrp.model;

import java.util.ArrayList;
import java.util.Collections;

public class Generation {

    /**
     * @param g
     * @param quantityMax Par défaut la quantité Max est la constante Vehicle.QUANTITY_MAX mais
     *                    pour certaines conditions on peux la modifier afin de remplir les camions à moitié au début par exemple
     * @return
     */
    public static Graph fillVehicle(Graph g, int quantityMax) {
        int indexVehicle = 0;

        //On tire aléatoirement des noeuds pour les mettre dans des tournées aléatoirement
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

}
