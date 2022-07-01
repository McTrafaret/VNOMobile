package com.example.vnomobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.CharacterState;
import com.example.vnomobile.R;
import com.example.vnomobile.resource.CharacterButton;
import com.example.vnomobile.resource.UIDesign;

public class SpriteButtonsAdapter extends RecyclerView.Adapter<SpriteButtonsAdapter.ListOfSpriteViewHolder> {

    private CharacterButton[] buttons;
    private CharacterState state;
    private UIDesign design;

    public SpriteButtonsAdapter(CharacterButton[] buttons, UIDesign design, CharacterState state) {
        this.buttons = buttons;
        this.state = state;
        this.design = design;
    }

    @NonNull
    @Override
    public ListOfSpriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_sprite_button, parent, false);
        return new ListOfSpriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfSpriteViewHolder holder, int position) {
        holder.bind(buttons[position]);
    }

    @Override
    public int getItemCount() {
        return buttons.length;
    }

    public class ListOfSpriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView spriteButtonImage;
        CharacterButton characterButton;

        public ListOfSpriteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.spriteButtonImage = itemView.findViewById(R.id.sprite_button_image);
        }

        public void bind(CharacterButton button) {
            this.characterButton = button;
            this.spriteButtonImage.setImageBitmap(characterButton.getButtonBitmap());
        }


        @Override
        public void onClick(View v) {
            state.setSpriteName(characterButton.getAssociatedSpriteName());
        }
    }
}
