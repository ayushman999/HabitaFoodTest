package com.example.android.habitafoodtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SpoonacularAdapter extends RecyclerView.Adapter<SpoonacularAdapter.MyViewHolder> {
    ArrayList<SElements> data=new ArrayList<>();
    Context context;

    public SpoonacularAdapter(ArrayList<SElements> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View elementView= LayoutInflater.from(parent.getContext()).inflate(R.layout.spoonacular_element,parent,false);
        MyViewHolder holder=new MyViewHolder(elementView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SElements current=data.get(position);
        holder.foodName.setText(current.getTitle());
        ArrayAdapter<String> mAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,current.getMissing_list());
        holder.missing.setAdapter(mAdapter);
        ArrayAdapter<String> rAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,current.getAvailable_list());
        holder.required.setAdapter(rAdapter);
        String url=current.getFood_img_url();
        Glide.with(context).load(url).into(holder.foodImage);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName;
        ListView missing;
        ListView required;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage=(ImageView) itemView.findViewById(R.id.element_img);
            foodName=(TextView) itemView.findViewById(R.id.title_name);
            missing=(ListView) itemView.findViewById(R.id.missing_ing_list);
            required=(ListView) itemView.findViewById(R.id.required_ing_list);
        }
    }
}
