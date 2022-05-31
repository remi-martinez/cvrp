package cvrp.model;


import java.util.ArrayList;

public class Vehicle {

    final int QUANTITY_MAX = 100;

    private ArrayList<Client> visit = new ArrayList<>();
    private double length = 0;
    private int quantity = 0;
    private Client warehouse;

    public Vehicle(Client warehouse) {
        this.warehouse = warehouse;
    }

    public void add(Client c) {
        add(visit.size(), c);
    }

    public void add(Integer i, Client c) {
        visit.add(i, c);

        if (visit.size() <= 1) {
            this.length = 2 * calculateDistance(warehouse, c);
        } else {
            Client prevClient, nextClient;
            if (i == 0) {
                prevClient = this.warehouse;
                nextClient = this.visit.get(i);
            } else if (i == visit.size()) {
                prevClient = this.visit.get(i - 1);
                nextClient = this.warehouse;
            } else {
                prevClient = this.visit.get(i - 1);
                nextClient = this.visit.get(i);
            }

            //On ajoute la distance du précédent au niveau client ainsi que du niveau client au suivant.
            this.length += calculateDistance(prevClient, c) + calculateDistance(c, nextClient);
            //On retire l'ancienne distance
            this.length -= calculateDistance(prevClient, nextClient);
        }

        //On rajoute à la quantité de la visite le nouveau client
        this.quantity += c.getQuantity();
    }

    public Client remove(Integer i) {
        Client c = visit.get(i);
        if (visit.size() <= 1) {
            this.length = 0;
        } else {
            Client prevClient, nextClient;
            if (i == 0) {
                prevClient = this.warehouse;
                nextClient = this.visit.get(i + 1);
            } else if (i == this.visit.size()) {
                prevClient = this.visit.get(i - 1);
                nextClient = this.warehouse;
            } else {
                prevClient = this.visit.get(i - 1);
                nextClient = this.visit.get(i);
            }

            //On retire la distance avec le point précedent et suivant
            this.length -= calculateDistance(prevClient, c) + calculateDistance(c, nextClient);
            //On ajoute la distance entre ces deux points
            this.length += calculateDistance(prevClient, nextClient);
        }

        //Mise a jour de la quantité
        this.quantity -= c.getQuantity();

        visit.remove(c);
        return c;
    }

    private double calculateDistance(final Client startClient, final Client endCLient) {
        return Math.sqrt(Math.pow(endCLient.getPosX() - startClient.getPosX(), 2) + Math.pow(endCLient.getPosY() - startClient.getPosY(), 2));
    }

    public ArrayList<Client> getVisit() {
        return visit;
    }

    public void setVisit(ArrayList<Client> visit) {
        this.visit = visit;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Client getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Client warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public Vehicle clone() {
        Vehicle clone = new Vehicle(warehouse);
        visit.stream().forEach(clone::add);
        return clone;
    }
}
