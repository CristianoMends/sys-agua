package edu.pies.sysaguaapp.models;

public class Address {
    private String number;
    private String street;
    private String neighborhood;
    private String city;
    private String state;
    // Getters and setters for address properties
    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreet() {
        return street;
    }
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    public String getNeighborhood() {
        return neighborhood;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCity() {
        return city;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getState() {
        return state;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }
}
