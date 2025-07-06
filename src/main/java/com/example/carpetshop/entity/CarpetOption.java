package com.example.carpetshop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "carpet_option")
public class CarpetOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_carpetcoloropt")
    private CarpetColorOption carpetColorOption;


    @ManyToOne
    @JoinColumn(name = "id_size")
    private Size size;

    private Double price;

    private Double discount;

    private Integer stock;

    // Getters/setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarpetColorOption getCarpetColorOption() {
        return carpetColorOption;
    }

    public void setCarpetColorOption(CarpetColorOption carpetColorOption) {
        this.carpetColorOption = carpetColorOption;
    }


    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
