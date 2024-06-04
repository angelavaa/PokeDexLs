package com.angelaavalos.pokedexls.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.angelaavalos.pokedexls.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Map<String, Integer> items;

    public ItemAdapter(Map<String, Integer> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String itemName = (String) items.keySet().toArray()[position];
        int itemQuantity = items.get(itemName);
        holder.itemName.setText(itemName + " " + itemQuantity);

        // Convertir itemName a minúsculas para asegurar que coincida con el nombre del archivo
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pokedexls.appspot.com/items/" + itemName.toLowerCase() + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.itemImage))
                .addOnFailureListener(e -> holder.itemImage.setImageResource(R.drawable.ic_pokedex));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}