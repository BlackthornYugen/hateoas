package com.example.demo.rest.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.example.demo.rest.entities.Animal.ANIMAL_TYPE_DOG;

@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ANIMAL_TYPE_DOG)
@NoArgsConstructor
@AllArgsConstructor
public class Dog extends Animal {
    private String breed;
    private boolean isChipped;

    @Builder
    private Dog(Long id, String name, byte[] dna, String breed, boolean isChipped) {
        super(id, name, dna, ANIMAL_TYPE_DOG);
        this.breed = breed;
        this.isChipped = isChipped;
    }
}