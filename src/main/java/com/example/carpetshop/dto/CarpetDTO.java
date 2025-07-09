package com.example.carpetshop.dto;

import java.util.List;

public class CarpetDTO {
    private Long id;
    private String name;
    private String origin;
    private String shortDescription;
    private String imageUrl;
    private String hoverImageUrl;
    private Double minPrice;
    private List<String> colors;
    private List<String> types;

    public CarpetDTO() {}

    public CarpetDTO(Long id, String name, String origin, String shortDescription, String imageUrl, String hoverImageUrl, Double minPrice, List<String> colors, List<String> types) {
        this.id = id;
        this.name = name;
        this.origin = origin;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.hoverImageUrl = hoverImageUrl;
        this.minPrice = minPrice;
        this.colors = colors;
        this.types = types;
    }

    public CarpetDTO(Long id, String name, String origin, String shortDescription, String imageUrl, String hoverImageUrl, Double minPrice) {
        this(id, name, origin, shortDescription, imageUrl, hoverImageUrl, minPrice, null, null);
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getOrigin() { return origin; }
    public String getShortDescription() { return shortDescription; }
    public String getImageUrl() { return imageUrl; }
    public String getHoverImageUrl() { return hoverImageUrl; }
    public Double getMinPrice() { return minPrice; }
    public List<String> getColors() { return colors; }
    public List<String> getTypes() { return types; }
    public void setColors(List<String> colors) { this.colors = colors; }
    public void setTypes(List<String> types) { this.types = types; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }
    public void setHoverImageUrl(String hoverImageUrl) { this.hoverImageUrl = hoverImageUrl; }
}

