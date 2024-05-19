package com.angelaavalos.pokedexls.network.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.models.PokemonResponse;
import com.angelaavalos.pokedexls.network.GsonRequest;
import com.angelaavalos.pokedexls.network.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class PokemonRepository {
    private static final String URL = "https://pokeapi.co/api/v2/pokemon?limit=100";

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
                        GsonRequest<Pokemon> detailRequest = new GsonRequest<>(
                                Request.Method.GET,
                                urlDetails,
                                new TypeToken<Pokemon>() {},
                                pokemonDetails -> {
                                    allPokemons.add(pokemonDetails);
                                    if (allPokemons.size() == response.getResults().size()) {
                                        listener.onResponse(allPokemons);
                                    }
                                },
                                errorListener
                        );
                        VolleySingleton.getInstance(context).addToRequestQueue(detailRequest);
                    }
                },
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


}


