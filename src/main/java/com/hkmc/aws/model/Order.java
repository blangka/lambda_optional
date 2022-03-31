package com.hkmc.aws.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {

    private String customer;
    private List<Trade> trades = new ArrayList<>();

    public void addTrade( Trade trade ) {
        trades.add( trade );
    }
}
