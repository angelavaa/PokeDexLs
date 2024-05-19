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

import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Trainer;
import com.angelaavalos.pokedexls.network.api.TrainerRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SecondFragment extends Fragment {

    private TextView trainerNameTextView;
    private TextView trainerMoneyTextView;
    private TextView trainerItemsTextView;
    private EditText editTrainerName;
    private Button saveTrainerNameButton;

    private Trainer currentTrainer;

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        trainerNameTextView = view.findViewById(R.id.trainerName);
        trainerMoneyTextView = view.findViewById(R.id.trainerMoney);
        trainerItemsTextView = view.findViewById(R.id.trainerItems);
        editTrainerName = view.findViewById(R.id.editTrainerName);
        saveTrainerNameButton = view.findViewById(R.id.saveTrainerNameButton);

        saveTrainerNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = editTrainerName.getText().toString().trim();
                if (!newName.isEmpty()) {
                    if (currentTrainer != null) { // Verifica que currentTrainer no sea null
                        currentTrainer.setName(newName);
                        saveTrainerData();
                    } else {
                        Toast.makeText(getActivity(), "No se ha cargado la información del entrenador", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                    trainerItemsTextView.setText("Items: " + (trainer.getItems() != null ? trainer.getItems().toString() : "[]"));
                } else {
                    currentTrainer = new Trainer(); // Inicializar currentTrainer si es null
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    private void saveTrainerData() {
        TrainerRepository repository = new TrainerRepository();
        repository.saveTrainer(currentTrainer);
        Toast.makeText(getActivity(), "Nombre actualizado", Toast.LENGTH_SHORT).show();
        editTrainerName.setText("");
        trainerNameTextView.setText(currentTrainer.getName());
    }
}
