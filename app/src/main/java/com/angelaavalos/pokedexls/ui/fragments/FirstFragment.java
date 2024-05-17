package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.ui.adapters.PokemonAdapter;
import com.angelaavalos.pokedexls.network.api.PokemonRepository;
import com.angelaavalos.pokedexls.R;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment implements PokemonAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private SearchView searchView;

    public FirstFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);

        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));

        adapter = new PokemonAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

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

    @Override
    public void onItemClick(Pokemon pokemon) {
        if (pokemon != null) {
            PokemonDetailFragment detailFragment = PokemonDetailFragment.newInstance(pokemon);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, detailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
