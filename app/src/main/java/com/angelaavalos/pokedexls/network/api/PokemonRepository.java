package com.angelaavalos.pokedexls.network.api;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.models.PokemonResponse;
import com.angelaavalos.pokedexls.network.GsonRequest;
import com.angelaavalos.pokedexls.network.VolleySingleton;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class PokemonRepository {
    private static final String URL = "https://pokeapi.co/api/v2/pokemon?limit=100";

    public void getPokemonList(Context context, final Response.Listener<List<Pokemon>> listener, Response.ErrorListener errorListener) {
        GsonRequest<PokemonResponse> request = new GsonRequest<>(
                Request.Method.GET,
                URL,
                new TypeToken<PokemonResponse>() {},
                response -> listener.onResponse(response.getResults()),
                errorListener
        );
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

}


