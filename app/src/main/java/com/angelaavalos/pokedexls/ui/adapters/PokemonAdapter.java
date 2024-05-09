package com.angelaavalos.pokedexls.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private List<Pokemon> pokemonList;

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getName());

        String imageUrl = pokemon.getImageUrl();
        Log.d("LoadImage", "Loading image from URL: " + imageUrl);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.error_image)
                    .into(holder.pokemonImage);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.default_image)
                    .into(holder.pokemonImage);
        }
    }


    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public void updateData(List<Pokemon> newPokemonList) {
        pokemonList.clear();
        pokemonList.addAll(newPokemonList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonName;
        ImageView pokemonImage;

        ViewHolder(View itemView) {
            super(itemView);
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);
        }
    }
}

