package com.example.carpetshop.dto;


public class CarpetDTO {
    private Long id;
    private String name;
    private String origin;
    private String shortDescription;
    private String imageUrl;
    private String hoverImageUrl;
    private Double minPrice;




    public CarpetDTO() {}

    public CarpetDTO(Long id, String name, String origin, String shortDescription, String imageUrl, String hoverImageUrl) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.hoverImageUrl = hoverImageUrl;
    }

    public CarpetDTO(Long id, String name, String origin, String shortDescription, String imageUrl, String hoverImageUrl, Double minPrice) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.hoverImageUrl = hoverImageUrl;
        this.minPrice = minPrice;
    }



    // Getters

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHoverImageUrl() {
        return hoverImageUrl;
    }
    public void setHoverImageUrl(String hoverImageUrl) {
        this.hoverImageUrl = hoverImageUrl;
    }

}

