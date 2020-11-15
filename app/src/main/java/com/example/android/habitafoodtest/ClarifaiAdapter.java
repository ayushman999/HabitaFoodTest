package com.example.android.habitafoodtest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClarifaiAdapter extends RecyclerView.Adapter<ClarifaiAdapter.ClarifaiHolder> {
    ArrayList<Elements> item;

    public ClarifaiAdapter(ArrayList<Elements> item) {
        this.item = item;
    }

    @NonNull
    @Override
    public ClarifaiHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementView= LayoutInflater.from(parent.getContext()).inflate(R.layout.clarifai_row_elements,parent,false);
        ClarifaiHolder holder=new ClarifaiHolder(elementView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClarifaiHolder holder, int position) {
        holder.probability.setText(item.get(position).getProbability());
        holder.item.setText(item.get(position).getItem());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ClarifaiHolder extends RecyclerView.ViewHolder {
        TextView item;
        TextView probability;
        public ClarifaiHolder(@NonNull View itemView) {
            super(itemView);
            item=(TextView) itemView.findViewById(R.id.item);
            probability=(TextView) itemView.findViewById(R.id.probability);

        }
    }
}
