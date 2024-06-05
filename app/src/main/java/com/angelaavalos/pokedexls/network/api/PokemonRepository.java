package com.angelaavalos.pokedexls.network.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.angelaavalos.pokedexls.models.EvolutionChain;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.models.PokemonResponse;
import com.angelaavalos.pokedexls.models.PokemonSpecies;
import com.angelaavalos.pokedexls.network.GsonRequest;
import com.angelaavalos.pokedexls.network.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PokemonRepository {

    public void getPokemonList(final Context context, int limit, int offset, final Response.Listener<List<Pokemon>> listener, final Response.ErrorListener errorListener) {
        String url = "https://pokeapi.co/api/v2/pokemon?limit=" + limit + "&offset=" + offset;
        GsonRequest<PokemonResponse> request = new GsonRequest<>(
                Request.Method.GET,
                url,
                new TypeToken<PokemonResponse>() {},
                response -> {
                    List<Pokemon> allPokemons = new ArrayList<>();
                    for (Pokemon pokemon : response.getResults()) {
                        String urlDetails = pokemon.getUrl();
                        Log.d("PokemonRepository", "Pokemon: " + pokemon.getName() + " URL: " + urlDetails);
                        if (urlDetails != null && !urlDetails.isEmpty()) {
                            GsonRequest<Pokemon> detailRequest = new GsonRequest<>(
                                    Request.Method.GET,
                                    urlDetails,
                                    new TypeToken<Pokemon>() {},
                                    pokemonDetails -> {
                                        // Asignar la URL original al objeto detallado
                                        pokemonDetails.setUrl(urlDetails);
                                        allPokemons.add(pokemonDetails);
                                        fetchEvolutionChain(context, pokemonDetails, allPokemons, listener, response.getResults().size(), errorListener);
                                    },
                                    errorListener
                            );
                            VolleySingleton.getInstance(context).addToRequestQueue(detailRequest);
                        } else {
                            Log.e("PokemonRepository", "URL is null or empty for Pokemon: " + pokemon.getName());
                        }
                    }
                    if (allPokemons.size() == response.getResults().size()) {
                        listener.onResponse(allPokemons);
                    }
                },
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    private void fetchEvolutionChain(final Context context, final Pokemon pokemon, final List<Pokemon> allPokemons, final Response.Listener<List<Pokemon>> listener, int totalPokemons, final Response.ErrorListener errorListener) {
        if (pokemon.getUrl() == null || pokemon.getUrl().isEmpty()) {
            Log.e("PokemonRepository", "URL is null or empty for Pokemon: " + pokemon.getName());
            return;
        }

        // Extraer ID del Pokémon desde la URL
        String[] urlParts = pokemon.getUrl().split("/");
        if (urlParts.length < 2) {
            Log.e("PokemonRepository", "Invalid URL for Pokemon: " + pokemon.getName());
            return;
        }
        String pokemonId = urlParts[urlParts.length - 1];
        Log.d("PokemonRepository", "Pokemon ID extracted: " + pokemonId);

        // Obtener el ID de la cadena evolutiva desde el endpoint species
        String speciesUrl = "https://pokeapi.co/api/v2/pokemon-species/" + pokemonId + "/";

        GsonRequest<PokemonSpecies> speciesRequest = new GsonRequest<>(
                Request.Method.GET,
                speciesUrl,
                new TypeToken<PokemonSpecies>() {},
                speciesResponse -> {
                    String evolutionChainUrl = speciesResponse.getEvolutionChain().getUrl();
                    Log.d("PokemonRepository", "Evolution Chain URL: " + evolutionChainUrl);

                    GsonRequest<EvolutionChain> evolutionRequest = new GsonRequest<>(
                            Request.Method.GET,
                            evolutionChainUrl,
                            new TypeToken<EvolutionChain>() {},
                            evolutionChain -> {
                                pokemon.setEvolutionChain(evolutionChain);
                                try {
                                    int typePokemon = pokemon.getTypePokemon();
                                    Log.d("PokemonRepository", "Type Pokemon for " + pokemon.getName() + ": " + typePokemon);
                                } catch (IllegalStateException e) {
                                    Log.e("PokemonRepository", "Error determining type_pokemon for " + pokemon.getName() + ": " + e.getMessage());
                                }
                                if (allPokemons.size() == totalPokemons) {
                                    listener.onResponse(allPokemons);
                                }
                            },
                            error -> {
                                Log.e("PokemonLoadError", "Error: " + error.toString());
                                // Manejar el error aquí y continuar
                                if (allPokemons.size() == totalPokemons) {
                                    listener.onResponse(allPokemons);
                                }
                            }
                    );
                    VolleySingleton.getInstance(context).addToRequestQueue(evolutionRequest);
                },
                error -> {
                    Log.e("PokemonLoadError", "Error: " + error.toString());
                    // Manejar el error aquí y continuar
                    if (allPokemons.size() == totalPokemons) {
                        listener.onResponse(allPokemons);
                    }
                }
        );
        VolleySingleton.getInstance(context).addToRequestQueue(speciesRequest);
    }

}





