package cvrp.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Client> clientList = new ArrayList<>();
    private ArrayList<Client> newMap = new ArrayList<>();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Client warehouse;


    public Graph(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int nbTotalPackages = 0;

        //On skip la premiere ligne
        br.readLine();
        while ((st = br.readLine()) != null) {
            String[] line = st.split(";");
            Client Client = new Client(line);
            getClientList().add(Client);
            nbTotalPackages += Integer.parseInt(line[3]);
        }
        int minVehicles = nbTotalPackages / 100; // 100 de quantité par véhicule donc on prévoit large pour des petites tournées
        //Génération aléatoire du graph
        randomGeneration(minVehicles);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public ArrayList<Client> getClientList() {
        return clientList;
    }

    public void setClientList(ArrayList<Client> clientList) {
        this.clientList = clientList;
    }

    public ArrayList<Client> getNewMap() {
        return newMap;
    }

    public void setNewMap(ArrayList<Client> newMap) {
        this.newMap = newMap;
    }

    public Graph randomGeneration(int minVehicles) {
        int nbVehicles = minVehicles * 2; //On prend le double du nombre de vehicules minimum pour commencer aléatoirement
        int capacityVehicle = 100;
        int indexVehicle = 0;

        //On tire aléatoirement des noeuds pour les mettre dans des tournées aléatoirement
        Client depot = getClientList().get(0); //On récupère le dépot
        ArrayList<Client> Clients = (ArrayList<Client>) getClientList().clone();
        Clients.remove(depot);
//        Collections.shuffle(Clients); //On randomize une seule fois la collection pour éviter de tirer un aléatoire à chaque fois
        int index = 0;

        vehicles.add(new Vehicle(depot));
        Vehicle vehicle = vehicles.get(indexVehicle);

        while (Clients.size() != 0) {
            Client c = Clients.get(index);
            if ((int) vehicle.getVisit().stream().mapToDouble(Client::getQuantity).sum() >= capacityVehicle) {
                vehicles.add(new Vehicle(depot));
                indexVehicle++;
                vehicle = vehicles.get(indexVehicle);
            }
            vehicle.add(c);
            Clients.remove(index);
        }

        return this;
    }

    public Client getWarehouse() {
        return getClientList().get(0);
    }

    public double getFitness() {
        return getVehicles().stream().mapToDouble(Vehicle::getLength).sum();
    }

}
