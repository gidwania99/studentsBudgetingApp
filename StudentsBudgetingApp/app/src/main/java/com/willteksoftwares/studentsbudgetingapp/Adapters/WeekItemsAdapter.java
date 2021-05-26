package com.willteksoftwares.studentsbudgetingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.willteksoftwares.studentsbudgetingapp.Model.Data;
import com.willteksoftwares.studentsbudgetingapp.R;

import java.util.List;

public class WeekItemsAdapter extends RecyclerView.Adapter<WeekItemsAdapter.ViewHolder> {

    private Context mContext;
    private List<Data> myDataList;


    public WeekItemsAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public WeekItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout,parent,false);
        return new WeekItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekItemsAdapter.ViewHolder holder, int position) {

        final Data data = myDataList.get(position);

        holder.item.setText("Item: "+data.getItem());
        holder.amount.setText("Spent: "+data.getAmount());
        holder.date.setText("On "+data.getDate());
        holder.note.setText("Note: "+data.getNotes());


        switch (data.getItem()) {
            case "Transport":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.ic_food);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Education":
                holder.imageView.setImageResource(R.drawable.ic_education);
                break;
            case "Charity":
                holder.imageView.setImageResource(R.drawable.ic_consultancy);
                break;
            case "Apparel and Services":
                holder.imageView.setImageResource(R.drawable.ic_shirt);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.ic_health);
                break;
            case "Personal Expenses":
                holder.imageView.setImageResource(R.drawable.ic_personalcare);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.ic_house);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item,amount,date,note;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            note = itemView.findViewById(R.id.notes);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
