package com.angelaavalos.pokedexls.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angelaavalos.pokedexls.R;
import com.angelaavalos.pokedexls.models.Trainer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.StoreItemViewHolder> {
    private Trainer currentTrainer;
    private Map<String, Integer> items;
    private ItemPurchaseListener listener;

    public interface ItemPurchaseListener {
        void onItemPurchase(String itemName, int itemPrice);
    }

    public StoreItemAdapter(Trainer currentTrainer, ItemPurchaseListener listener) {
        this.currentTrainer = currentTrainer;
        this.listener = listener;
        items = new HashMap<>();
        items.put("Pokeball", 200);
        items.put("Superball", 500);
        items.put("Ultraball", 1500);
        items.put("Masterball", 100000);
    }

    @NonNull
    @Override
    public StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item, parent, false);
        return new StoreItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
        String itemName = (String) items.keySet().toArray()[position];
        int itemPrice = items.get(itemName);
        holder.itemName.setText(itemName);
        holder.itemPrice.setText("Precio: " + itemPrice);

        // Convertir itemName a minúsculas para asegurar que coincida con el nombre del archivo
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://pokedexls.appspot.com/items/" + itemName.toLowerCase() + ".png");
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.itemImage))
                .addOnFailureListener(e -> holder.itemImage.setImageResource(R.drawable.ic_pokedex)); // Imagen de placeholder

        holder.buyButton.setOnClickListener(v -> listener.onItemPurchase(itemName, itemPrice));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class StoreItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemPrice;
        ImageView itemImage;
        Button buyButton;

        StoreItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.storeItemName);
            itemPrice = itemView.findViewById(R.id.storeItemPrice);
            itemImage = itemView.findViewById(R.id.storeItemImage);
            buyButton = itemView.findViewById(R.id.buyStoreItemButton);
        }
    }
}
