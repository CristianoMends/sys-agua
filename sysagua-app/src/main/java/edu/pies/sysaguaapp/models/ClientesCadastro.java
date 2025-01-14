package edu.pies.sysaguaapp.models;
import java.time.LocalDate;

public class ClientesCadastro {
    private Long id;
    private String name;
    public static class Address {
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
    private Address address;
    private String phone;
    private LocalDate createdAt;
    private boolean active;
    private String cnpj;

    // Getters e Setters
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    public String getCnpj() {
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
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
