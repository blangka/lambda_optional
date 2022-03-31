package com.hkmc.aws.model;

import lombok.Data;

@Data
public class Transaction {

    private Trader trader;
    private int year;
    private int value;
}
