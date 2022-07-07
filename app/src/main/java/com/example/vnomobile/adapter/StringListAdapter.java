package com.example.vnomobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnomobile.R;
import com.example.vnomobile.adapter.viewholder.ListOfServersViewHolder;
import com.example.vnomobile.adapter.viewholder.SimpleTextViewHolder;

import java.util.List;

public class StringListAdapter extends RecyclerView.Adapter<SimpleTextViewHolder> {

    private List<String> stringList;

    public StringListAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public SimpleTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SimpleTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleTextViewHolder holder, int position) {
        holder.bind(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }
}
