package com.example.carpetshop.entity;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Carpet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String origin;

    @Column(name = "short_desc", length = 1000)
    private String shortDesc;

    @OneToMany(mappedBy = "carpet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarpetColorOption> colorOptions = new ArrayList<>();

    @OneToMany(mappedBy = "carpet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarpetType> carpetTypes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public List<CarpetColorOption> getColorOptions() {
        return colorOptions;
    }

    public void setColorOptions(List<CarpetColorOption> colorOptions) {
        this.colorOptions = colorOptions;
    }

    public List<CarpetType> getCarpetTypes() {
        return carpetTypes;
    }

    public void setCarpetTypes(List<CarpetType> carpetTypes) {
        this.carpetTypes = carpetTypes;
    }
}
