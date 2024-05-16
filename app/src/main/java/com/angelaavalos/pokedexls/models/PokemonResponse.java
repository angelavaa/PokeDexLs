package com.angelaavalos.pokedexls.models;

import com.angelaavalos.pokedexls.models.Pokemon;

import java.util.List;

public class PokemonResponse {
    private List<Pokemon> results;

    public List<Pokemon> getResults() {
        return results;
    }

    public void setResults(List<Pokemon> results) {
        this.results = results;
    }
}


