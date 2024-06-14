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
import android.widget.Toast;

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
    private OnPokemonReleaseListener releaseListener;

    public interface OnPokemonReleaseListener {
        void onPokemonRelease(Pokemon pokemon);
    }

    public CapturedPokemonAdapter(List<Pokemon> capturedPokemons, OnPokemonReleaseListener releaseListener) {
        this.capturedPokemons = capturedPokemons;
        this.releaseListener = releaseListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.captured_pokemon_item, parent, false);
        return new ViewHolder(view, releaseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = capturedPokemons.get(position);
        holder.pokemonName.setText(pokemon.getName());

        // Cargar la imagen del Pokémon
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getSprites().getFrontDefault())
                .into(holder.pokemonImage);

        // Cargar la imagen de la pokeball
        if (pokemon.getPokeballName() != null) {
            String pokeballName = pokemon.getPokeballName().toLowerCase();
            Log.d("CapturedPokemonAdapter", "Pokeball name: " + pokeballName);
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pokedexls.appspot.com/items/" + pokeballName + ".png");
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.pokeballImage))
                    .addOnFailureListener(e -> {
                        Log.e("CapturedPokemonAdapter", "Failed to load image for pokeball: " + pokeballName);
                        holder.pokeballImage.setImageResource(R.drawable.ic_pokedex);
                    });
        } else {
            Log.e("CapturedPokemonAdapter", "Pokeball name is null for pokemon: " + pokemon.getName());
            holder.pokeballImage.setImageResource(R.drawable.ic_pokedex);
        }

        holder.releaseButton.setOnClickListener(v -> {
            try {
                releaseListener.onPokemonRelease(pokemon);
            } catch (IllegalStateException e) {
                Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return capturedPokemons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName;
        ImageView pokemonImage;
        ImageView pokeballImage;
        Button releaseButton;

        ViewHolder(View itemView, final OnPokemonReleaseListener releaseListener) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);
            pokeballImage = itemView.findViewById(R.id.pokeballImage);
            releaseButton = itemView.findViewById(R.id.releaseButton);
        }
    }
}


