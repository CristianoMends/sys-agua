package edu.pies.sysaguaapp.models;

public class Produto {
    private String name;
    private String brand;
    private String line;
    private double cost;
    private String ncm;

    public Produto(String name, String brand, String line, double cost, String ncm) {
        this.name = name;
        this.brand = brand;
        this.line = line;
        this.cost = cost;
        this.ncm = ncm;
    }

    // Getters e setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getLine() { return line; }
    public void setLine(String line) { this.line = line; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }
}

