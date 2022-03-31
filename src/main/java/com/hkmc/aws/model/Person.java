package com.hkmc.aws.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@AllArgsConstructor
@Data
public class Person {

    private Optional<Car> car;
    private int age;
}
