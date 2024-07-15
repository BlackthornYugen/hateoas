package com.example.demo.rest.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dog.class, name = Animal.ANIMAL_TYPE_DOG),
        @JsonSubTypes.Type(value = Human.class, name = Animal.ANIMAL_TYPE_HUMAN)
})
public abstract class Animal {
    public static final String ANIMAL_TYPE_HUMAN = "HUMAN";
    public static final String ANIMAL_TYPE_DOG = "DOG";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private byte[] dna;
    @Column(insertable=false, updatable=false)
    private String type;
}