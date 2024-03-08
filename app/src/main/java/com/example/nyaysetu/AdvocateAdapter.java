package com.example.nyaysetu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdvocateAdapter extends RecyclerView.Adapter<AdvocateAdapter.ViewHolder> {
    private ArrayList<AdvocateModel> list;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advocate_detail_row , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current AdvocateModel from the list
        AdvocateModel model = list.get(position);

        // Set the name and experience TextViews
        holder.name.setText(model.getName());
        holder.experience.setText(model.getExperience());
//        Toast.makeText(context, "Rating", Toast.LENGTH_SHORT).show();

        // Set the rating for the RatingBar
        holder.rate.setRating(model.getRatingBar());

        holder.rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(b){
                    model.setRatingBar(v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name , experience;
        RatingBar rate ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        
            name = itemView.findViewById(R.id.nameOfAdvocate);
            experience = itemView.findViewById(R.id.experienceOfAdvocate);
            rate = itemView.findViewById(R.id.ratingBar);
        }

    }
    public AdvocateAdapter(ArrayList<AdvocateModel> list, Context context) {
        this.list = list;
        this.context = context;
    }




}
