package cvrp.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Graph {
    private ArrayList<Client> clientList = new ArrayList<>();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private int minVehicles;

    public Graph(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int nbTotalPackages = 0;

        //On skip la premiere ligne
        br.readLine();

        //On parcourt les autres points
        while ((st = br.readLine()) != null) {
            String[] line = st.split(";");
            Client client = new Client(line);
            this.getClientList().add(client);
            nbTotalPackages += Integer.parseInt(line[3]);
        }
        this.minVehicles = (int)(Math.ceil(nbTotalPackages / (double)Vehicle.QUANTITY_MAX)); // 100 de quantité par véhicule donc on prévoit large pour des petites tournées

        //Génération aléatoire du graph
        Graph g = Generation.graphGeneration(this, false);
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public int getClientCount() {
        return clientList.size();
    }

    public ArrayList<Client> getClientList() {
        return clientList;
    }

    public void setClientList(ArrayList<Client> clientList) {
        this.clientList = clientList;
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

    public static double getFitness(ArrayList<Vehicle> vehicles) {
        return vehicles.stream().mapToDouble(Vehicle::getLength).sum();
    }

    public double getInitialTemperature() {
        return -300 / Math.log(0.8);
    }

    public ArrayList<Vehicle> cloneVehicles() {
        return (ArrayList<Vehicle>) this.vehicles.stream().map(Vehicle::clone).collect(Collectors.toList());
    }

    public static ArrayList<Vehicle> cloneVehicles(ArrayList<Vehicle> vehicles) {
        return (ArrayList<Vehicle>) vehicles.stream().map(Vehicle::clone).collect(Collectors.toList());
    }


}
