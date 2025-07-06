package com.example.carpetshop.entity;

import jakarta.persistence.*;

@Entity
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_carpetcoloropt")
    private CarpetColorOption carpetColorOption;

    private String url;
    private boolean isMain;
    private boolean isHover;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public boolean isHover() {
        return isHover;
    }

    public void setHover(boolean hover) {
        isHover = hover;
    }
}
