package com.angelaavalos.pokedexls.network.api;

import com.angelaavalos.pokedexls.models.Trainer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrainerRepository {
    private DatabaseReference databaseReference;

    public TrainerRepository() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("trainers");
    }

    public void saveTrainer(Trainer trainer) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(userId).setValue(trainer);
    }

    public DatabaseReference getTrainerReference() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return databaseReference.child(userId);
    }
}
