package com.example.vnomobile.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpriteButtonsAdapter extends RecyclerView.Adapter<SpriteButtonsAdapter.ListOfSpriteViewHolder> {

    private CharacterButton[] buttons;
    private CharacterState state;
    private UIDesign design;

    private int lastItemSelectedPosition = -1;

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
        holder.bind(buttons[position], position == lastItemSelectedPosition);
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
            itemView.setOnClickListener(this);
        }

        public void bind(CharacterButton button, boolean selected) {
            this.characterButton = button;
            log.debug("Selected: {}", selected);
            Bitmap selectedBackground = design.getEmoteSelectBitmaps()[selected ? 1 : 0];
            this.spriteButtonImage.setImageBitmap(mergeTwoBitmaps(selectedBackground, characterButton.getButtonBitmap()));
        }


        @Override
        public void onClick(View v) {
            state.setSpriteName(characterButton.getAssociatedSpriteName());
            int saveLastPos = lastItemSelectedPosition;
            lastItemSelectedPosition = getAdapterPosition();
            notifyItemChanged(saveLastPos);
            notifyItemChanged(lastItemSelectedPosition);
        }

        private Bitmap mergeTwoBitmaps(Bitmap bitmap1, Bitmap bitmap2) {
            Bitmap result = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(bitmap1, new Matrix(), null);
            int xoffset = (bitmap1.getWidth() - bitmap2.getWidth()) / 2;
            int yoffset = (bitmap1.getHeight() - bitmap2.getHeight()) / 2;
            canvas.drawBitmap(bitmap2, xoffset, yoffset, null);
            return result;
        }
    }
}
