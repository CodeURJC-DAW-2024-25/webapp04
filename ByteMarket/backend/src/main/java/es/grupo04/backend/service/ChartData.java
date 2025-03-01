package es.grupo04.backend.service;

public class ChartData {
    private int month;
    private String type;
    private int quantity;

    public ChartData(int month, String type, int quantity) {
        this.month = month;
        this.type = type;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void addOne() {
        quantity++;
    }
}