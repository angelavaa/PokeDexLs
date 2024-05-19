package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ThirdFragment extends Fragment {

    private TextView trainerMoneyTextView;
    private TextView pokeballPriceTextView;
    private TextView superballPriceTextView;
    private TextView ultraballPriceTextView;
    private TextView masterballPriceTextView;
    private Button buyPokeballButton;
    private Button buySuperballButton;
    private Button buyUltraballButton;
    private Button buyMasterballButton;

    private Trainer currentTrainer;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        trainerMoneyTextView = view.findViewById(R.id.trainerMoney);
        pokeballPriceTextView = view.findViewById(R.id.pokeballPrice);
        superballPriceTextView = view.findViewById(R.id.superballPrice);
        ultraballPriceTextView = view.findViewById(R.id.ultraballPrice);
        masterballPriceTextView = view.findViewById(R.id.masterballPrice);
        buyPokeballButton = view.findViewById(R.id.buyPokeballButton);
        buySuperballButton = view.findViewById(R.id.buySuperballButton);
        buyUltraballButton = view.findViewById(R.id.buyUltraballButton);
        buyMasterballButton = view.findViewById(R.id.buyMasterballButton);

        pokeballPriceTextView.setText("Precio: 200");
        superballPriceTextView.setText("Precio: 500");
        ultraballPriceTextView.setText("Precio: 1500");
        masterballPriceTextView.setText("Precio: 100000");

        buyPokeballButton.setOnClickListener(v -> buyItem("Pokeball", 200));
        buySuperballButton.setOnClickListener(v -> buyItem("Superball", 500));
        buyUltraballButton.setOnClickListener(v -> buyItem("Ultraball", 1500));
        buyMasterballButton.setOnClickListener(v -> buyItem("Masterball", 100000));

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
                    trainerMoneyTextView.setText("Dinero: " + trainer.getMoney());
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

    private void buyItem(String itemName, int itemPrice) {
        if (currentTrainer.getMoney() >= itemPrice) {
            currentTrainer.setMoney(currentTrainer.getMoney() - itemPrice);
            currentTrainer.getItems().add(itemName);
            saveTrainerData();
            Toast.makeText(getActivity(), itemName + " comprado!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Dinero insuficiente", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTrainerData() {
        TrainerRepository repository = new TrainerRepository();
        repository.saveTrainer(currentTrainer);
        trainerMoneyTextView.setText("Dinero: " + currentTrainer.getMoney());
    }
}
