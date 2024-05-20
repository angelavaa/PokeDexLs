package com.angelaavalos.pokedexls.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Trainer;
import com.angelaavalos.pokedexls.network.api.TrainerRepository;
import com.angelaavalos.pokedexls.ui.adapters.StoreItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ThirdFragment extends Fragment {

    private TextView trainerMoneyTextView;
    private RecyclerView storeItemsRecyclerView;

    private Trainer currentTrainer;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        trainerMoneyTextView = view.findViewById(R.id.trainerMoney);
        storeItemsRecyclerView = view.findViewById(R.id.storeItemsRecyclerView);

        storeItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                    storeItemsRecyclerView.setAdapter(new StoreItemAdapter(trainer, ThirdFragment.this::buyItem));
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
