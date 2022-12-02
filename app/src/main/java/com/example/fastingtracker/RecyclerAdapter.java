package com.example.fastingtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Pair<Integer, Integer>> list;
    Context context;

    public RecyclerAdapter(ArrayList<Pair<Integer, Integer>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.custom_layout, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.pairText.setText(list.get(position).first + "-" + list.get(position).second);

        int pos = holder.getAdapterPosition();

        switch (position) {
            case 0:
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
                break;

            case 1:
                holder.difficulty.setText("Medium");

                holder.fastingHours.setText(list.get(position).first + " - hours of fasting");
                holder.eatingHours.setText(list.get(position).second + " - hours of eating");

                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow));

                holder.image.setImageResource(R.drawable.medium);
                break;

            case 2:
                holder.difficulty.setText("Hard");

                holder.fastingHours.setText(list.get(position).first + " - hours of fasting");
                holder.eatingHours.setText(list.get(position).second + " - hours of eating");

                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange));

                holder.image.setImageResource(R.drawable.hard);
                break;

            case 3:
                holder.difficulty.setText("Hardcore");

                holder.fastingHours.setText("Do fasting for\n a whole day 🔥");
                holder.eatingHours.setVisibility(View.INVISIBLE);

                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));

                holder.image.setImageResource(R.drawable.hardcore_fasting);
                break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FastingActivity.class);
                i.putExtra("first", list.get(pos).first + "");
                i.putExtra("second", list.get(pos).second + "");

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pairText, difficulty, fastingHours, eatingHours;
        CardView cardView;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);

            pairText = itemView.findViewById(R.id.pairText);

            fastingHours = itemView.findViewById(R.id.fastingHour);
            eatingHours = itemView.findViewById(R.id.eatingHour);

            difficulty = itemView.findViewById(R.id.difficulty);

            image = itemView.findViewById(R.id.image);
        }
    }
}


