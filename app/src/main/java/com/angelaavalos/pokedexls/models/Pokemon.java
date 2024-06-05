package com.angelaavalos.pokedexls.models;

import android.util.Log;

import java.util.List;

public class Pokemon {
    private String name;
    private String url;
    private Sprites sprites;
    private List<Type> types;
    private String description;
    private List<Ability> abilities;
    private List<Stat> stats;
    private EvolutionChain evolutionChain;

    @Override
    public String toString() {
        return "Pokemon{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", sprites=" + sprites +
                ", types=" + types +
                ", description='" + description + '\'' +
                ", abilities=" + abilities +
                ", stats=" + stats +
                ", evolutionChain=" + evolutionChain +
                '}';
    }

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

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }
    public EvolutionChain getEvolutionChain() {
        return evolutionChain;
    }

    public void setEvolutionChain(EvolutionChain evolutionChain) {
        this.evolutionChain = evolutionChain;
    }

    public static class Sprites {
        private String front_default;
        private String back_default;

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }

        public String getBackDefault() {
            return back_default;
        }

        public void setBackDefault(String back_default) {
            this.back_default = back_default;
        }
    }

    public static class Type {
        private TypeInfo type;

        public TypeInfo getType() {
            return type;
        }

        public void setType(TypeInfo type) {
            this.type = type;
        }

        public static class TypeInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class Ability {
        private AbilityInfo ability;
        private boolean is_hidden;

        public AbilityInfo getAbility() {
            return ability;
        }

        public void setAbility(AbilityInfo ability) {
            this.ability = ability;
        }

        public boolean isHidden() {
            return is_hidden;
        }

        public void setHidden(boolean is_hidden) {
            this.is_hidden = is_hidden;
        }

        public static class AbilityInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public static class Stat {
        private StatInfo stat;
        private int base_stat;

        public StatInfo getStat() {
            return stat;
        }

        public void setStat(StatInfo stat) {
            this.stat = stat;
        }

        public int getBaseStat() {
            return base_stat;
        }

        public void setBaseStat(int base_stat) {
            this.base_stat = base_stat;
        }

        public static class StatInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    public int getTypePokemon() {
        if (this.evolutionChain == null) {
            Log.e("Pokemon", "Evolution chain is not set for " + this.name);
            // Valor predeterminado o manejo alternativo
            return 0; // O algún valor por defecto
        }

        EvolutionChain.Chain chain = this.evolutionChain.getChain();
        if (chain == null) {
            Log.e("Pokemon", "Chain is not set in evolution chain for " + this.name);
            // Valor predeterminado o manejo alternativo
            return 0; // O algún valor por defecto
        }

        return calculateTypePokemon(chain, this.name);
    }


    private int calculateTypePokemon(EvolutionChain.Chain chain, String pokemonName) {
        String chainName = chain.getSpecies().getName();
        Log.d("PokemonRepository", "Calculating type for: " + pokemonName + ", starting with chain: " + chainName);

        if (chainName.equalsIgnoreCase(pokemonName)) {
            return getRandomValue(20, 80);
        }

        return traverseEvolutionChain(chain.getEvolvesTo(), pokemonName, 1);
    }


    private int traverseEvolutionChain(List<EvolutionChain.Chain.EvolvesTo> evolvesToList, String pokemonName, int evolutionStage) {
        for (EvolutionChain.Chain.EvolvesTo evolvesTo : evolvesToList) {
            String evolvesToName = evolvesTo.getSpecies().getName();
            Log.d("PokemonRepository", "Checking evolvesTo: " + evolvesToName + " at stage: " + evolutionStage);
            if (evolvesToName.equalsIgnoreCase(pokemonName)) {
                switch (evolutionStage) {
                    case 1:
                        return getRandomValue(80, 200);
                    case 2:
                        return getRandomValue(200, 350);
                    case 3:
                    default:
                        return getRandomValue(350, 500);
                }
            } else {
                int typePokemon = traverseEvolutionChain(evolvesTo.getEvolvesTo(), pokemonName, evolutionStage + 1);
                if (typePokemon > 0) {
                    return typePokemon;
                }
            }
        }
        return 0;
    }


    private int getRandomValue(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
