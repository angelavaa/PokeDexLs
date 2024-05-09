package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.ui.adapters.PokemonAdapter;
import com.angelaavalos.pokedexls.network.api.PokemonRepository;
import com.angelaavalos.pokedexls.R;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;

    public FirstFragment() {
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new PokemonAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadPokemonData();

        return view;
    }

    private void loadPokemonData() {
        PokemonRepository repository = new PokemonRepository();
        repository.getPokemonList(getActivity(), new Response.Listener<List<Pokemon>>() {
            @Override
            public void onResponse(List<Pokemon> pokemons) {
                adapter.updateData(pokemons);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PokemonLoadError", "Error: " + error.toString());
            }
        });
    }
}
