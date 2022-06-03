package cvrp.model;

import java.util.ArrayList;
import java.util.Collections;

public class Generation {

    public static Graph graphGeneration(Graph g, boolean randomized) {
        int capacityVehicle = 100;
        int indexVehicle = 0;

        //On tire aléatoirement des noeuds pour les mettre dans des tournées aléatoirement
        ArrayList<Client> clients = (ArrayList<Client>) g.getClientList().clone();
        Client depot = clients.remove(0);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        if(randomized){
            Collections.shuffle(clients); //On randomize une seule fois la collection pour éviter de tirer un aléatoire à chaque fois
        }

        int index = 0;

        vehicles.add(new Vehicle(depot));
        Vehicle vehicle = vehicles.get(indexVehicle);

        while (clients.size() != 0) {
            Client c = clients.get(index);
            if ((int) vehicle.getVisit().stream().mapToDouble(Client::getQuantity).sum() + c.getQuantity() > capacityVehicle) {
                vehicles.add(new Vehicle(depot));
                indexVehicle++;
                vehicle = vehicles.get(indexVehicle);
            }
            vehicle.add(c);
            clients.remove(index);
        }

//        //Affichage des distances des véhicules
//        for (Vehicle v: vehicles) {
//            System.out.println("Quantité = " + v.getQuantity());
//            Client prev = g.getWarehouse();
//            for (Client cl : v.getVisit()){
//                System.out.println(v.calculateDistance(prev, cl));
//                prev = cl;
//            }
//            System.out.println(v.getLength());
//            System.out.println("-----------*--********************");
//
//        }

        g.setVehicles(vehicles);
        return g;
    }
}
