package com.hkmc.aws.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@AllArgsConstructor
@Data
public class Car {
    private Optional<Insurance> insurance;
}
