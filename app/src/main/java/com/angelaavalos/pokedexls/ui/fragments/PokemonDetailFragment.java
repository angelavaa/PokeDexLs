package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.EvolutionChain;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.models.Trainer;
import com.angelaavalos.pokedexls.network.api.TrainerRepository;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.List;
import java.util.Random;

public class PokemonDetailFragment extends Fragment {
    private static final String ARG_POKEMON_JSON = "pokemon_json";

    private Pokemon pokemon;
    private TextView textViewCaptureItem;
    private String[] pokeballs = {"Pokeball", "Superball", "Ultraball", "Masterball"};

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
        TextView textViewType = view.findViewById(R.id.textViewPokemonType);
        TextView textViewAbility = view.findViewById(R.id.textViewPokemonAbility);
        TextView textViewEvolutionStage = view.findViewById(R.id.textViewEvolutionStage);
        LinearLayout linearLayoutStats = view.findViewById(R.id.linearLayoutStats);
        textViewCaptureItem = view.findViewById(R.id.textViewCaptureItem);

        if (pokemon != null) {
            Glide.with(this)
                    .load(pokemon.getSprites().getFrontDefault())
                    .into(imageViewFront);

            Glide.with(this)
                    .load(pokemon.getSprites().getBackDefault())
                    .into(imageViewBack);

            textViewName.setText(pokemon.getName());


            StringBuilder types = new StringBuilder();
            for (Pokemon.Type type : pokemon.getTypes()) {
                types.append(type.getType().getName()).append(" ");
            }
            textViewType.setText(types.toString().trim());

            String ability = selectAbility(pokemon.getAbilities());
            textViewAbility.setText(ability);

            // Determinar y mostrar la etapa evolutiva
            String evolutionStage = getEvolutionStage(pokemon);
            textViewEvolutionStage.setText(evolutionStage);

            linearLayoutStats.removeAllViews();
            for (Pokemon.Stat stat : pokemon.getStats()) {
                View statView = createStatView(stat);
                linearLayoutStats.addView(statView);
            }

            textViewCaptureItem.setText((pokemon.getCaptureItem() != null ? pokemon.getCaptureItem() : "N/A"));
        }

        Button captureButton = view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCaptureDialog();
            }
        });

        return view;
    }

    private void showCaptureDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Selecciona el ítem de captura")
                .setItems(pokeballs, (dialog, which) -> {
                    String selectedPokeball = pokeballs[which];
                    attemptCapturePokemon(selectedPokeball.toLowerCase());
                })
                .show();
    }

    private void attemptCapturePokemon(String selectedPokeball) {
        if (pokemon == null) return;

        TrainerRepository trainerRepository = new TrainerRepository();
        trainerRepository.getTrainerReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trainer trainer = snapshot.getValue(Trainer.class);
                if (trainer != null) {
                    Log.d("Trainer", "Trainer loaded: " + trainer.getName());
                    Log.d("Trainer", "Trainer items: " + trainer.getItems());

                    if (trainer.getCapturedPokemons().size() >= 6) {
                        Toast.makeText(getContext(), "No puedes capturar más de 6 Pokémon", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (trainer.isPokemonCaptured(pokemon.getName())) {
                        Toast.makeText(getContext(), pokemon.getName() + " ya ha sido capturado!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!trainer.hasItem(selectedPokeball)) {
                        Toast.makeText(getContext(), "No tienes suficiente " + selectedPokeball, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double captureProbability = calculateCaptureProbability(selectedPokeball, pokemon.getTypePokemon());
                    double randomValue = Math.random();

                    if (randomValue <= captureProbability) {
                        trainer.useItem(selectedPokeball); // Usar el ítem
                        pokemon.setCaptureItem(selectedPokeball); // Guardar el ítem de captura
                        pokemon.setPokeballName(selectedPokeball); // Guardar el nombre de la pokeball
                        Log.d("PokemonDetailFragment", "Captured pokeball: " + selectedPokeball);
                        trainer.addCapturedPokemon(pokemon);
                        trainer.setMoney(trainer.getMoney() + calculateCaptureMoney(pokemon.getTypePokemon()));
                        trainerRepository.saveTrainer(trainer);
                        Toast.makeText(getContext(), pokemon.getName() + " capturado!", Toast.LENGTH_SHORT).show();
                        textViewCaptureItem.setText("Item de captura: " + selectedPokeball);
                    } else {
                        Toast.makeText(getContext(), pokemon.getName() + " escapó!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error al capturar Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double calculateCaptureProbability(String pokeball, int typePokemon) {
        double baseProbability = (600.0 - typePokemon) / 600.0;
        switch (pokeball) {
            case "pokeball":
                return baseProbability * 1;
            case "superball":
                return baseProbability * 1.5;
            case "ultraball":
                return baseProbability * 2;
            case "masterball":
                return 1.0;
            default:
                return 0;
        }
    }

    private int calculateCaptureMoney(int typePokemon) {
        return 400 + 100 * typePokemon;
    }

    private String getEvolutionStage(Pokemon pokemon) {
        if (pokemon.getEvolutionChain() == null) {
            return "Unknown";
        }

        EvolutionChain.Chain chain = pokemon.getEvolutionChain().getChain();
        return determineStage(chain, pokemon.getName(), 1);
    }

    private String determineStage(Object chainPart, String pokemonName, int currentStage) {
        EvolutionChain.Species species;
        List<EvolutionChain.Chain.EvolvesTo> evolvesToList;

        if (chainPart instanceof EvolutionChain.Chain) {
            species = ((EvolutionChain.Chain) chainPart).getSpecies();
            evolvesToList = ((EvolutionChain.Chain) chainPart).getEvolvesTo();
        } else if (chainPart instanceof EvolutionChain.Chain.EvolvesTo) {
            species = ((EvolutionChain.Chain.EvolvesTo) chainPart).getSpecies();
            evolvesToList = ((EvolutionChain.Chain.EvolvesTo) chainPart).getEvolvesTo();
        } else {
            return "Unknown";
        }

        if (species.getName().equalsIgnoreCase(pokemonName)) {
            switch (currentStage) {
                case 1: return "First Evolution";
                case 2: return "Second Evolution";
                case 3: return "Third Evolution";
                default: return "Unknown";
            }
        }

        for (EvolutionChain.Chain.EvolvesTo evolvesTo : evolvesToList) {
            String stage = determineStage(evolvesTo, pokemonName, currentStage + 1);
            if (!stage.equals("Unknown")) {
                return stage;
            }
        }
        return "Unknown";
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
