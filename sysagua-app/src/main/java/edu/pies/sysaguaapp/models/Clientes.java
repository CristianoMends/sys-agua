package edu.pies.sysaguaapp.models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)

public class Clientes {
    private Long id;
    private String name;
    private Address address;
    private String phone;
    private String cnpj;
    private Boolean active;
    // Getters e Setters
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public void setAddress(Address address) {
        this.address = address;
    }
    public Address getAddress() {
        return address;
    }

    public void setActive(Boolean active) {
    this.active = active;
    }
    public boolean getActive(){
        return active;
    }
    
}
