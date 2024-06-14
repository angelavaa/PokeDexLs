package com.angelaavalos.pokedexls.ui.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CapturedPokemonAdapter extends RecyclerView.Adapter<CapturedPokemonAdapter.ViewHolder> {
    private List<Pokemon> capturedPokemons;
    private OnPokemonRemovedListener onPokemonRemovedListener;

    public interface OnPokemonRemovedListener {
        void onPokemonRemoved(Pokemon pokemon);
    }

    public CapturedPokemonAdapter(List<Pokemon> capturedPokemons, OnPokemonRemovedListener onPokemonRemovedListener) {
        this.capturedPokemons = capturedPokemons;
        this.onPokemonRemovedListener = onPokemonRemovedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.captured_pokemon_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = capturedPokemons.get(position);
        holder.pokemonName.setText(pokemon.getName());

        // Cargar la imagen del Pokémon
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getSprites().getFrontDefault())
                .into(holder.pokemonImage);

        holder.removeButton.setOnClickListener(v -> {
            if (onPokemonRemovedListener != null) {
                onPokemonRemovedListener.onPokemonRemoved(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return capturedPokemons.size();
    }

    public void removePokemon(Pokemon pokemon) {
        int position = capturedPokemons.indexOf(pokemon);
        if (position != -1) {
            capturedPokemons.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName;
        ImageView pokemonImage;
        Button removeButton;

        ViewHolder(View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}

