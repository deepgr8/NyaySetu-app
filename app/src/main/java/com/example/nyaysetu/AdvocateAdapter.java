package com.example.nyaysetu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdvocateAdapter extends RecyclerView.Adapter<AdvocateAdapter.ViewHolder> {
    private ArrayList<AdvocateModel> list;
    private Context context;

    public AdvocateAdapter(ArrayList<AdvocateModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdvocateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advocate_detail_row , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvocateAdapter.ViewHolder holder, int position) {
      AdvocateModel model = list.get(position);
        Log.d("nnn", "onBindViewHolder: "+model.getName());
      holder.name.setText(model.getName());
      holder.experience.setText(model.getExperience());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name , experience;
        RatingBar rate ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameOfAdvocate);
            experience = itemView.findViewById(R.id.experienceOfAdvocate);
            rate = itemView.findViewById(R.id.ratingBar);
        }

    }
}
