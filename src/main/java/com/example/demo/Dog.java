package com.example.demo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.example.demo.Animal.ANIMAL_TYPE_DOG;

@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ANIMAL_TYPE_DOG)
@NoArgsConstructor
@AllArgsConstructor
public class Dog extends Animal {
    private String breed;
    private boolean isChipped;

    @Builder
    public Dog(Long id, String name, byte[] signature, String breed, boolean isChipped) {
        super(id, name, signature, ANIMAL_TYPE_DOG);
        this.breed = breed;
        this.isChipped = isChipped;
    }
}