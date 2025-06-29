package com.example.ThePetVerse.service;

import com.example.ThePetVerse.model.Pet;
import com.example.ThePetVerse.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet savePet(Pet pet){
        return this.petRepository.save(pet);
    }

    public List<Pet> findAllPet(){
        return  this.petRepository.findAll();
    }

    public Optional<Pet> findByIDPet(Integer id){
        return this.petRepository.findById(id);
    }

    public void deletePet(Integer id){
        this.petRepository.deleteById(id);
    }

    public Pet editPet(Integer id, Pet petEdit) {
        Optional<Pet> petOp = this.petRepository.findById(id);
        if (petOp.isPresent()) {
            // Asegúrate que el objeto petEdit tenga el id correcto
            petEdit.setId(id);
            // Guarda el objeto completamente nuevo en la base (reemplaza el anterior)
            return this.petRepository.save(petEdit);
        }
        return null;
    }

}
