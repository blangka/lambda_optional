package com.hkmc.aws.model;

import lombok.Data;

@Data
public class Dish {

    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public enum Type {
        MEAT,
        FISH,
        OTHER
    }
}
