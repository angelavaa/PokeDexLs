package com.angelaavalos.pokedexls.models;


import java.util.ArrayList;
import java.util.List;

public class Trainer {
    private String name;
    private int money;
    private List<String> items;
    private List<Pokemon> capturedPokemons;

    public Trainer() {
        this.items = new ArrayList<>();
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

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<Pokemon> getCapturedPokemons() {
        return capturedPokemons;
    }

    public void setCapturedPokemons(List<Pokemon> capturedPokemons) {
        this.capturedPokemons = capturedPokemons;
    }
}
