package com.angelaavalos.pokedexls.models;

public class PokemonSpecies {
    private EvolutionChainInfo evolution_chain;

    public EvolutionChainInfo getEvolutionChain() {
        return evolution_chain;
    }

    public void setEvolutionChain(EvolutionChainInfo evolution_chain) {
        this.evolution_chain = evolution_chain;
    }

    public static class EvolutionChainInfo {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
