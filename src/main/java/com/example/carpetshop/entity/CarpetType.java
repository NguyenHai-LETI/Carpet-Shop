package com.example.carpetshop.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "carpet_type")
public class CarpetType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carpet_id", nullable = false)
    private Carpet carpet;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    // Constructors
    public CarpetType() {}

    public CarpetType(Carpet carpet, Type type) {
        this.carpet = carpet;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Carpet getCarpet() {
        return carpet;
    }

    public void setCarpet(Carpet carpet) {
        this.carpet = carpet;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
