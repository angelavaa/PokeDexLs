package com.angelaavalos.pokedexls.models;


import android.util.Log;

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
        this.items = new HashMap<>();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            this.items.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }

    public List<Pokemon> getCapturedPokemons() {
        return capturedPokemons;
    }

    public void setCapturedPokemons(List<Pokemon> capturedPokemons) {
        this.capturedPokemons = capturedPokemons;
    }

    public boolean hasItem(String itemName) {
        itemName = itemName.toLowerCase();
        Log.d("Trainer", "Checking item: " + itemName + " count: " + items.getOrDefault(itemName, 0));
        return items.getOrDefault(itemName, 0) > 0;
    }

    public void useItem(String itemName) {
        itemName = itemName.toLowerCase();
        int count = items.getOrDefault(itemName, 0);
        if (count > 0) {
            items.put(itemName, count - 1);
            Log.d("Trainer", "Using item: " + itemName + ". Remaining: " + (count - 1));
        }
    }

    public void addCapturedPokemon(Pokemon pokemon) {
        if (capturedPokemons.size() < 6) {
            capturedPokemons.add(pokemon);
        }
    }

    public void removeCapturedPokemon(Pokemon pokemon) {
        capturedPokemons.remove(pokemon);
    }
}

