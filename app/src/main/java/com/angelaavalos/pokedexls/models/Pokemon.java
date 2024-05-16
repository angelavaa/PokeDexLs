package com.angelaavalos.pokedexls.models;

import java.util.List;

public class Pokemon {
    private String name;
    private String url;
    private Sprites sprites;
    private List<Type> types;
    private String description;
    private List<Ability> abilities;
    private List<Stat> stats;

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
}
