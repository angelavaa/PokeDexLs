package com.angelaavalos.pokedexls.ui.adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public CapturedPokemonAdapter(List<Pokemon> capturedPokemons) {
        this.capturedPokemons = capturedPokemons;
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

        // Verificar si el nombre de la pokeball es null antes de usarlo
        if (pokemon.getPokeballName() != null) {
            String pokeballName = pokemon.getPokeballName().toLowerCase(); // Asegurarse de que el nombre esté en minúsculas
            Log.d("CapturedPokemonAdapter", "Pokeball name: " + pokeballName);
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pokedexls.appspot.com/items/" + pokeballName + ".png");
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.pokeballImage))
                    .addOnFailureListener(e -> {
                        Log.e("CapturedPokemonAdapter", "Failed to load image for pokeball: " + pokeballName);
                        holder.pokeballImage.setImageResource(R.drawable.ic_pokedex);
                    });
        } else {
            Log.e("CapturedPokemonAdapter", "Pokeball name is null for pokemon: " + pokemon.getName());
            holder.pokeballImage.setImageResource(R.drawable.ic_pokedex); // Imagen de placeholder si no hay pokeballName
        }
    }

    @Override
    public int getItemCount() {
        return capturedPokemons.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName;
        ImageView pokemonImage;
        ImageView pokeballImage;

        ViewHolder(View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);
            pokeballImage = itemView.findViewById(R.id.pokeballImage);
        }
    }
}
