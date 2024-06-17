package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private boolean isLoading = false;
    private int limit = 15;
    private int offset = 0;
    private boolean isSearching = false;  // Variable para verificar si se está realizando una búsqueda

    public FirstFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                searchPokemon(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    isSearching = false;
                    adapter.getFilter().filter("");  // Limpiar el filtro y cargar la lista paginada
                    offset = 0; // Reiniciar el offset
                    loadPokemonData();  // Cargar los datos paginados
                }
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading && !isSearching && layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getItemCount() - 1) {
                    // Llegamos al final de la lista, cargar más Pokémon solo si no estamos buscando
                    loadMorePokemonData();
                }
            }
        });

        loadPokemonData();

        return view;
    }

    private void loadPokemonData() {
        isLoading = true;
        PokemonRepository repository = new PokemonRepository();
        repository.getPokemonList(getActivity(), limit, offset, new Response.Listener<List<Pokemon>>() {
            @Override
            public void onResponse(List<Pokemon> pokemons) {
                adapter.setData(pokemons); // Reemplazar datos cuando offset es 0
                offset += limit;
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PokemonLoadError", "Error: " + error.toString());
                isLoading = false;
            }
        });
    }

    private void loadMorePokemonData() {
        isLoading = true;
        PokemonRepository repository = new PokemonRepository();
        repository.getPokemonList(getActivity(), limit, offset, new Response.Listener<List<Pokemon>>() {
            @Override
            public void onResponse(List<Pokemon> pokemons) {
                adapter.updateData(pokemons); // Agregar datos a la lista existente
                offset += limit;
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PokemonLoadError", "Error: " + error.toString());
                isLoading = false;
            }
        });
    }

    private void searchPokemon(String query) {
        isLoading = true;
        isSearching = true;  // Indicar que se está realizando una búsqueda
        PokemonRepository repository = new PokemonRepository();
        repository.searchPokemon(getActivity(), query, new Response.Listener<List<Pokemon>>() {
            @Override
            public void onResponse(List<Pokemon> pokemons) {
                adapter.setData(pokemons); // Reemplazar datos en el adaptador
                isLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PokemonSearchError", "Error: " + error.toString());
                Toast.makeText(getActivity(), "No se encontraron Pokémon", Toast.LENGTH_SHORT).show();
                isLoading = false;
                isSearching = false;  // Restablecer el estado de búsqueda en caso de error
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
