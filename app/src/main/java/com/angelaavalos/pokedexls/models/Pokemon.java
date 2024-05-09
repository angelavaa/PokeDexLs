package com.angelaavalos.pokedexls.models;

public class Pokemon {
    private String name;
    private String url;
    private Sprites sprites;

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

    public String getImageUrl() {
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    public static class Sprites {
        private String front_default;

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }
    }
}


