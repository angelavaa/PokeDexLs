package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Pokemon;
import com.angelaavalos.pokedexls.models.Trainer;
import com.angelaavalos.pokedexls.network.api.TrainerRepository;
import com.angelaavalos.pokedexls.ui.adapters.CapturedPokemonAdapter;
import com.angelaavalos.pokedexls.ui.adapters.ItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {
    private TextView trainerNameTextView;
    private TextView trainerMoneyTextView;
    private EditText editTrainerName;
    private Button saveTrainerNameButton;
    private Button editTrainerNameButton;
    private RecyclerView trainerItemsRecyclerView;
    private RecyclerView capturedPokemonsRecyclerView;
    private Trainer currentTrainer;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        trainerNameTextView = view.findViewById(R.id.trainerName);
        trainerMoneyTextView = view.findViewById(R.id.trainerMoney);
        editTrainerName = view.findViewById(R.id.editTrainerName);
        saveTrainerNameButton = view.findViewById(R.id.saveTrainerNameButton);
        editTrainerNameButton = view.findViewById(R.id.editTrainerNameButton);
        trainerItemsRecyclerView = view.findViewById(R.id.trainerItemsRecyclerView);
        capturedPokemonsRecyclerView = view.findViewById(R.id.capturedPokemonsRecyclerView);

        editTrainerNameButton.setOnClickListener(v -> {
            editTrainerName.setVisibility(View.VISIBLE);
            saveTrainerNameButton.setVisibility(View.VISIBLE);
            editTrainerNameButton.setVisibility(View.GONE);
        });

        saveTrainerNameButton.setOnClickListener(v -> {
            String newName = editTrainerName.getText().toString().trim();
            if (!newName.isEmpty()) {
                if (currentTrainer != null) {
                    currentTrainer.setName(newName);
                    saveTrainerData();
                } else {
                    Toast.makeText(getActivity(), "No se ha cargado la información del entrenador", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        trainerItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        capturedPokemonsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTrainerData();

        return view;
    }

    private void loadTrainerData() {
        TrainerRepository repository = new TrainerRepository();
        repository.getTrainerReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Trainer trainer = snapshot.getValue(Trainer.class);
                if (trainer != null) {
                    currentTrainer = trainer;
                    trainerNameTextView.setText(trainer.getName());
                    trainerMoneyTextView.setText("Dinero: " + trainer.getMoney());
                    trainerItemsRecyclerView.setAdapter(new ItemAdapter(trainer.getItems()));
                    capturedPokemonsRecyclerView.setAdapter(new CapturedPokemonAdapter(trainer.getCapturedPokemons(), pokemon -> releasePokemon(pokemon)));
                } else {
                    currentTrainer = new Trainer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void releasePokemon(Pokemon pokemon) {
        try {
            currentTrainer.removeCapturedPokemon(pokemon);
            saveTrainerData();
            Toast.makeText(getActivity(), pokemon.getName() + " ha sido liberado", Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTrainerData() {
        TrainerRepository repository = new TrainerRepository();
        repository.saveTrainer(currentTrainer);
        Toast.makeText(getActivity(), "Datos actualizados", Toast.LENGTH_SHORT).show();
        editTrainerName.setText("");
        editTrainerName.setVisibility(View.GONE);
        saveTrainerNameButton.setVisibility(View.GONE);
        editTrainerNameButton.setVisibility(View.VISIBLE);
        trainerNameTextView.setText(currentTrainer.getName());
    }
}
