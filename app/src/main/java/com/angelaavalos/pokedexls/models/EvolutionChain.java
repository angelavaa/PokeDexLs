package com.angelaavalos.pokedexls.models;

import java.util.List;

public class EvolutionChain {
    private Chain chain;

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public static class Chain {
        private Species species;
        private List<EvolvesTo> evolves_to;

        public Species getSpecies() {
            return species;
        }

        public void setSpecies(Species species) {
            this.species = species;
        }

        public List<EvolvesTo> getEvolvesTo() {
            return evolves_to;
        }

        public void setEvolvesTo(List<EvolvesTo> evolves_to) {
            this.evolves_to = evolves_to;
        }

        public static class EvolvesTo {
            private Species species;
            private List<EvolvesTo> evolves_to;

            public Species getSpecies() {
                return species;
            }

            public void setSpecies(Species species) {
                this.species = species;
            }

            public List<EvolvesTo> getEvolvesTo() {
                return evolves_to;
            }

            public void setEvolvesTo(List<EvolvesTo> evolves_to) {
                this.evolves_to = evolves_to;
            }
        }
    }

    public static class Species {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
