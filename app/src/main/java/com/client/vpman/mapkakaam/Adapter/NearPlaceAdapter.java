package com.client.vpman.mapkakaam.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.client.vpman.mapkakaam.Model.ModelData;
import com.client.vpman.mapkakaam.R;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class NearPlaceAdapter extends RecyclerView.Adapter<NearPlaceAdapter.Myholder>
{

    Context context;
    List<ModelData> list;

    public NearPlaceAdapter(Context context, List<ModelData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NearPlaceAdapter.Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.near_place_adapter, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearPlaceAdapter.Myholder holder, int position) {

        String photoUrl="https://maps.googleapis.com/maps/api/place/photo?maxwidth="+list.get(position).getWidth()+"&maxheight="+list.get(position).getHeight()+"&photoreference="+list.get(position).getPhoto_reference()+"&key=AIzaSyB7tnG5pbO6Lh0M7TqoYIcTzRYYGR9SUhk";

        Log.d("wqou",photoUrl);

        Glide.with(context).load(photoUrl).into(holder.imageView);
        holder.textView.setText(list.get(position).getVicinity());

        Log.d("jh3ru",list.get(position).getVicinity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        MaterialTextView textView;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageData);
            textView=itemView.findViewById(R.id.Address);
        }
    }
}
