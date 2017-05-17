package com.itayc14.uppsaletask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itaycohen on 16.5.2017.
 */
public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.ViewHolder> {

    private View.OnClickListener clickListener;
    private ArrayList<Dish> dishes;
    private Context context;

    public DishesAdapter(List<Dish> dishes, Context context, View.OnClickListener clickListener) {
        this.dishes = (ArrayList)dishes;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public DishesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_pattern, null);
        row.setOnClickListener(clickListener);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(DishesAdapter.ViewHolder holder, int position) {
        holder.dishPrice.setText(dishes.get(position).getPrice()+"₪");
        holder.dishDescription.setText(dishes.get(position).getDescription());
        holder.dishImgView.setImageResource(R.drawable.no_image_exist);
        String imgLink = dishes.get(position).getImgLink();
        if(imgLink != null && !imgLink.isEmpty())
            Glide.with(context).load(imgLink).into(holder.dishImgView);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishPrice;
        TextView dishDescription;
        ImageView dishImgView;

        public ViewHolder(View itemView) {
            super(itemView);
            dishPrice = (TextView)itemView.findViewById(R.id.pattern_dish_Price);
            dishDescription = (TextView)itemView.findViewById(R.id.pattern_dish_description);
            dishImgView = (ImageView)itemView.findViewById(R.id.pattern_img);
        }
    }
}
