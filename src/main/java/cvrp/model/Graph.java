package cvrp.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Graph {
    private ArrayList<Client> clientList = new ArrayList<>();
    private ArrayList<Client> newMap = new ArrayList<>();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private Client warehouse;
    private int minVehicles;

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
        this.minVehicles = nbTotalPackages / 100; // 100 de quantité par véhicule donc on prévoit large pour des petites tournées

        //Génération aléatoire du graph
        Graph g =  Generation.graphGeneration(this, true);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
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

    public int getMinVehicles() {
        return minVehicles;
    }

    public void setMinVehicles(int minVehicles) {
        this.minVehicles = minVehicles;
    }

    public Client getWarehouse() {
        return getClientList().get(0);
    }

    public double getFitness() {
        return getVehicles().stream().mapToDouble(Vehicle::getLength).sum();
    }

    public double getInitialTemperature(){
        return -getFitness() / Math.log(0.8);
    }

    public ArrayList<Vehicle> cloneCurrentSolution() {
        return (ArrayList<Vehicle>) vehicles.stream().map(Vehicle::clone).collect(Collectors.toList());
    }

}
