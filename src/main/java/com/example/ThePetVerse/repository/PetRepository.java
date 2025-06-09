package com.example.ThePetVerse.repository;

import com.example.ThePetVerse.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer>{
}
