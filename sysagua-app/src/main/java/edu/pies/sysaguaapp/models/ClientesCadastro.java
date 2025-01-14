package edu.pies.sysaguaapp.models;

import javax.xml.crypto.Data;

public class ClientesCadastro {
    private Long id;
    private String name;
    public static class Address {
        private Long number;
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
        public void setNumber(Long number) {
            this.number = number;
        }
        public Long getNumber() {
            return number;
        }
    }
    private Address address;
    private Long phone;
    private Data createdAt;
    private boolean active;
    private Long cnpj;

    // Getters e Setters
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
    public long getPhone() {
        return phone;
    }

    public void setCnpj(long cnpj) {
        this.cnpj = cnpj;
    }
    public long getCnpj() {
        return cnpj;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isActive() {
        return active;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    public Address getAddress() {
        return address;
    }
    public void setCreatedAt(Data createdAt) {
        this.createdAt = createdAt;
    }
    public Data getCreatedAt() {
        return createdAt;
    }
}
