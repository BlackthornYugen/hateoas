package com.example.demo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.example.demo.Animal.ANIMAL_TYPE_HUMAN;

@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ANIMAL_TYPE_HUMAN)
@NoArgsConstructor
@AllArgsConstructor
public class Human extends Animal {
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Passport> passports;

    @Builder
    Human(Long id, String name, byte[] dna, List<Passport> passports) {
        super(id, name, dna, ANIMAL_TYPE_HUMAN);
        this.passports = passports;
    }
}