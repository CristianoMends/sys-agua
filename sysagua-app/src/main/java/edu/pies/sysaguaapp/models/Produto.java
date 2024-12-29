package edu.pies.sysaguaapp.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Produto {
    private Long id;
    private String name;
    private String unit;
    private Double price;
    private Double cost;
    private String brand;
    private String category;
    private String line;
    private String ncm;
    private String registeredAt;
    private String updatedAt;

    @JsonCreator
    public Produto(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name,
            @JsonProperty("unit") String unit,
            @JsonProperty("price") Double price,
            @JsonProperty("cost") Double cost,
            @JsonProperty("brand") String brand,
            @JsonProperty("category") String category,
            @JsonProperty("line") String line,
            @JsonProperty("ncm") String ncm,
            @JsonProperty("registeredAt") String registeredAt,
            @JsonProperty("updateAt") String updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.cost = cost;
        this.brand = brand;
        this.category = category;
        this.line = line;
        this.ncm = ncm;
        this.registeredAt = registeredAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRegisteredAt() { return registeredAt; }
    public void setRegisteredAt(String registeredAt) { this.registeredAt = registeredAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}

