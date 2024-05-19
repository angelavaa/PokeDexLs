package com.angelaavalos.pokedexls.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> implements Filterable{
    private List<Pokemon> pokemonList;
    private List<Pokemon> pokemonListFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pokemon pokemon);
    }

    public PokemonAdapter(List<Pokemon> pokemonList, OnItemClickListener listener) {
        this.pokemonList = pokemonList;
        this.pokemonListFull = new ArrayList<>(pokemonList);
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
        int initialSize = pokemonList.size();
        pokemonList.addAll(newPokemonList);
        pokemonListFull.addAll(newPokemonList); // Actualiza la lista completa
        notifyItemRangeInserted(initialSize, newPokemonList.size());
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

    @Override
    public Filter getFilter() {
        return pokemonFilter;
    }

    private Filter pokemonFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pokemon> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(pokemonListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Pokemon pokemon : pokemonListFull) {
                    if (pokemon.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(pokemon);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pokemonList.clear();
            pokemonList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
