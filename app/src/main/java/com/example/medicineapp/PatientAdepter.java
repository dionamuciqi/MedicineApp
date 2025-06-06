package com.example.medicineapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PatientAdepter extends RecyclerView.Adapter<PatientAdepter.ViewHolder> {

    Context context;
    ArrayList<PatientModel> arrayList = new ArrayList<>();
    int userId;  // Ruaj userId për përdorim

    // Konstruktor i ri që pranon userId
    public PatientAdepter(Context context, ArrayList<PatientModel> arrayList, int userId) {
        this.context = context;
        this.arrayList = arrayList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public PatientAdepter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_patients_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdepter.ViewHolder holder, int position) {
        holder.tvTitle.setText(arrayList.get(position).getFullname());
        holder.tvDesc.setText(arrayList.get(position).getDiagnostic());

        holder.cardView.setOnLongClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Entry")
                        .setMessage("Are you sure want to delete this entry?")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            MyDbHelper dbHelper = new MyDbHelper(context);
                            String id = String.valueOf(arrayList.get(currentPosition).getId());
                            dbHelper.deletePatient(id);
                            dialogInterface.dismiss();

                            // Rifresko aktivitetin Home duke kaluar userId
                            Intent intent = new Intent(context, Home.class);
                            intent.putExtra("userId", userId);
                            context.startActivity(intent);
                        })
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setIcon(R.drawable.baseline_add_alert_24)
                        .show();
            }
            return true;
        });

        holder.cardView.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Intent intent = new Intent(context, UpdateActivity3.class);
                intent.putExtra("fullname", arrayList.get(currentPosition).getFullname());
                intent.putExtra("diagnostic", arrayList.get(currentPosition).getDiagnostic());
                intent.putExtra("id", arrayList.get(currentPosition).getId());
                intent.putExtra("userId", userId);  // kalojmë userId edhe në UpdateActivity3
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
