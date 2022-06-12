package cvrp.model.entity;


import static java.lang.Integer.parseInt;

public class Client {

    private int id;
    private int posX;
    private int posY;
    private int quantity;

    public Client(int id, int posX, int posY, int quantity) {
        this.id = id;
        this.posX = posX;
        this.posY = posY;
        this.quantity = quantity;
    }

    public Client(String[] line) {
        this.id = parseInt(line[0]);
        this.posX = parseInt(line[1]);
        this.posY = parseInt(line[2]);
        this.quantity = parseInt(line[3]);
    }

    public int getId() {
        return id;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", posX=" + posX +
                ", posY=" + posY +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}
