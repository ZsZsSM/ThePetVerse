package com.example.ThePetVerse.controller;

import com.example.ThePetVerse.model.Pet;
import com.example.ThePetVerse.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    PetService petService;

    @GetMapping
    public List<Pet>getAllPet(){
        return this.petService.findAllPet();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPet(@PathVariable Integer id){
        Optional<Pet> pet= this.petService.findByIDPet(id);
        if(pet==null || pet.get().getId()==0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The pet with the id" +id+ " was not found.");
        }
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<?> addPet(@Validated @RequestBody Pet pet, BindingResult result){
        if(result.hasErrors()){
            Map<String,String> errors= new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
            }
        Optional <Pet> petOp=this.petService.findByIDPet(pet.getId());
        if(petOp.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The pet with the id" + pet.getId() + "already exists");
        }
        Pet petSave=this.petService.savePet(pet);
        return ResponseEntity.status(HttpStatus.CREATED).body(petSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable Integer id){
        Optional<Pet> petOp=this.petService.findByIDPet(id);
        if(!petOp.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The pet with the id: "+id+" ISNT registred");
        }
        this.petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUser(@Validated @PathVariable Integer id, @RequestBody Pet petEdit, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        Optional<Pet> userOp = this.petService.findByIDPet(id);
        if (!userOp.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The pet with the id " + id + " ISNT registred");
        }
        return ResponseEntity.ok(this.petService.editPet(id, petEdit));
    }


}



