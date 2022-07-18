package xyz.udalny.vnomobile.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SimpleTextViewHolder extends RecyclerView.ViewHolder {

    TextView textView;

    public SimpleTextViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(android.R.id.text1);
    }

    public void bind(String text) {
        textView.setText(text);
    }
}
