package com.example.ThePetVerse.model;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

@Entity
@Table(name="tb_pet")
public class Pet {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String age;
    @Column(nullable = false)
    private String size;
    @Column(nullable = false)
    private Double weight;
    @Column(nullable = false)
    private String breed;
    @Column(nullable = false)
    private String animal;
    @Column(nullable = false)
    private Boolean castration;

    public Pet() {
    }

    public Pet(Integer id, String name, String age, String size, Double weight, String breed, String animal, Boolean castration) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.size = size;
        this.weight = weight;
        this.breed = breed;
        this.animal = animal;
        this.castration = castration;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public Boolean getCastration() {
        return castration;
    }

    public void setCastration(Boolean castration) {
        this.castration = castration;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", size='" + size + '\'' +
                ", weight=" + weight +
                ", breed='" + breed + '\'' +
                ", animal='" + animal + '\'' +
                ", castration=" + castration +
                '}';
    }
}
