package com.example.carpetshop.dto;

import java.util.List;

public class CarpetDetailDTO {
    private Long id;
    private String name;
    private String origin;
    private String shortDescription;
    private List<String> imageUrls;
    private List<String> sizes;
    private List<String> types;
    private List<String> colors;
    private List<VariantOptionDTO> variantOptions;


    public List<VariantOptionDTO> getVariantOptions() {
        return variantOptions;
    }

    public void setVariantOptions(List<VariantOptionDTO> variantOptions) {
        this.variantOptions = variantOptions;
    }
    public CarpetDetailDTO() {}

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }
    // Getters, setters...
}

