package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

public class PokemonDetailFragment extends Fragment {

    private static final String ARG_POKEMON_JSON = "pokemon_json";

    private Pokemon pokemon;

    public static PokemonDetailFragment newInstance(Pokemon pokemon) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POKEMON_JSON, new Gson().toJson(pokemon));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String pokemonJson = getArguments().getString(ARG_POKEMON_JSON);
            pokemon = new Gson().fromJson(pokemonJson, Pokemon.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_detail, container, false);

        ImageView imageViewFront = view.findViewById(R.id.imageViewPokemonFront);
        ImageView imageViewBack = view.findViewById(R.id.imageViewPokemonBack);
        TextView textViewName = view.findViewById(R.id.textViewPokemonName);
        TextView textViewDescription = view.findViewById(R.id.textViewPokemonDescription);
        TextView textViewType = view.findViewById(R.id.textViewPokemonType);
        TextView textViewAbility = view.findViewById(R.id.textViewPokemonAbility);
        LinearLayout linearLayoutStats = view.findViewById(R.id.linearLayoutStats);

        if (pokemon != null) {
            Glide.with(this)
                    .load(pokemon.getSprites().getFrontDefault())
                    .into(imageViewFront);

            Glide.with(this)
                    .load(pokemon.getSprites().getBackDefault())
                    .into(imageViewBack);

            textViewName.setText(pokemon.getName());
            textViewDescription.setText(pokemon.getDescription());

            StringBuilder types = new StringBuilder();
            for (Pokemon.Type type : pokemon.getTypes()) {
                types.append(type.getType().getName()).append(" ");
            }
            textViewType.setText(types.toString().trim());

            // Seleccionar habilidad
            String ability = selectAbility(pokemon.getAbilities());
            textViewAbility.setText(ability);

            // Mostrar estadísticas
            linearLayoutStats.removeAllViews();
            for (Pokemon.Stat stat : pokemon.getStats()) {
                View statView = createStatView(stat);
                linearLayoutStats.addView(statView);
            }
        }

        return view;
    }

    private String selectAbility(List<Pokemon.Ability> abilities) {
        Random random = new Random();
        for (Pokemon.Ability ability : abilities) {
            if (ability.isHidden() && random.nextInt(100) < 25) {
                return ability.getAbility().getName();
            }
        }
        int index = random.nextInt(abilities.size());
        return abilities.get(index).getAbility().getName();
    }

    private View createStatView(Pokemon.Stat stat) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.stat_item, null);

        TextView textViewStatName = view.findViewById(R.id.textViewStatName);
        ProgressBar progressBarStat = view.findViewById(R.id.progressBarStat);

        textViewStatName.setText(stat.getStat().getName());
        progressBarStat.setProgress(stat.getBaseStat());

        return view;
    }
}
