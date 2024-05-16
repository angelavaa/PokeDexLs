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
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    public PokemonAdapter(List<Pokemon> pokemonList, OnItemClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_item, parent, false);
        return new ViewHolder(view, listener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.pokemonName.setText(pokemon.getName());

        String frontImageUrl = pokemon.getSprites().getFrontDefault();
        Log.d("LoadImage", "Loading front image from URL: " + frontImageUrl);
        if (frontImageUrl != null && !frontImageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(frontImageUrl)
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
        PokemonAdapter adapter;

        ViewHolder(View itemView, final OnItemClickListener listener, PokemonAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            pokemonName = itemView.findViewById(R.id.pokemonName);
            pokemonImage = itemView.findViewById(R.id.pokemonImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(adapter.pokemonList.get(position));
                        }
                    }
                }
            });
        }
    }
}
