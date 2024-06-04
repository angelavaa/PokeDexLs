package com.angelaavalos.pokedexls.models;


import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Trainer {
    private String name;
    private int money;
    private Map<String, Integer> items;
    private List<Pokemon> capturedPokemons;

    public Trainer() {
        this.items = new HashMap<>();
        this.capturedPokemons = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public List<Pokemon> getCapturedPokemons() {
        return capturedPokemons;
    }

    public void setCapturedPokemons(List<Pokemon> capturedPokemons) {
        this.capturedPokemons = capturedPokemons;
    }
}
