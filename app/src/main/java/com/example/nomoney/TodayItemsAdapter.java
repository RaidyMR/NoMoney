package com.example.nomoney;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TodayItemsAdapter extends RecyclerView.Adapter<TodayItemsAdapter.ViewHolder>{

    private Context mContext;
    private List<Data> myDataList;
    private String postid;
    private String note;
    private int amount;
    private String item;

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
        holder.date.setText("Tanggal: " + data.getDate());
        holder.note.setText("Catatan: " + data.getNote());

        switch (data.getItem()){
            case "Transportasi":
                holder.imageView.setImageResource(R.drawable.ic_transport);
                break;
            case "Makanan":
                holder.imageView.setImageResource(R.drawable.ic_food);
                break;
            case "Hiburan":
                holder.imageView.setImageResource(R.drawable.ic_entertainment);
                break;
            case "Lainnya":
                holder.imageView.setImageResource(R.drawable.ic_other);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postid = data.getId();
                note = data.getNote();
                amount = data.getAmount();
                item = data.getItem();

                updateData();
            }
        });
    }

    private void updateData() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View myView = inflater.inflate(R.layout.update_layout, null);

        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();

        final TextView mItem = myView.findViewById(R.id.item);
        final EditText mAmount = myView.findViewById(R.id.amount);
        final EditText mNote = myView.findViewById(R.id.note);

        mItem.setText(item);

        mAmount.setText(String.valueOf(amount));
        mAmount.setSelection(String.valueOf(amount).length());

        mNote.setText(note);
        mNote.setSelection(note.length());

        Button updateBtn = myView.findViewById(R.id.update);
        Button deleteBtn = myView.findViewById(R.id.delete);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = Integer.parseInt(mAmount.getText().toString());
                note = mNote.getText().toString();

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                String date = dateFormat.format(cal.getTime());

                Data data = new Data(item, date, postid, note, amount);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dompet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(postid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "sukses diubah", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "gagal diubah, " + task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dompet").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                reference.child(postid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "sukses dihapus", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "gagal dihapus, " + task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.dismiss();
            }
        });

        dialog.show();
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
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
