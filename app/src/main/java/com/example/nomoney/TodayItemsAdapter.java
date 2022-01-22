package com.example.nomoney;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodayItemsAdapter extends RecyclerView.Adapter<TodayItemsAdapter.ViewHolder>{

    private Context mContext;
    private List<Data> myDataList;

    public TodayItemsAdapter(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public TodayItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.retrieve_layout, parent, false);
        return new TodayItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodayItemsAdapter.ViewHolder holder, int position) {

        final Data data = myDataList.get(position);
        holder.item.setText("Item: " + data.getItem());
        holder.amount.setText("Nominal: " + data.getAmount());
        holder.date.setText("Hari ini: " + data.getDate());
        holder.note.setText("Catatan: " + data.getNote());

        // kalo datanya udh nggk null bisa dinyalain nih
//        switch (data.getItem()){
//            case "Transportasi":
//                holder.imageView.setImageResource(R.drawable.ic_transport);
//                break;
//            case "Makanan":
//                holder.imageView.setImageResource(R.drawable.ic_food);
//                break;
//            case "Hiburan":
//                holder.imageView.setImageResource(R.drawable.ic_entertainment);
//                break;
//            case "Lainnya":
//                holder.imageView.setImageResource(R.drawable.ic_other);
//                break;
//        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView item, amount, date, note;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.nominal);
            date = itemView.findViewById(R.id.tanggal);
            note = itemView.findViewById(R.id.catatan);
        }
    }
}
