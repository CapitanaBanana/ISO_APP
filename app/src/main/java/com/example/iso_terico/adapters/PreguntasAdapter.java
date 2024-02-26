package com.example.iso_terico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iso_terico.R;
import com.example.iso_terico.models.Pregunta;
import com.example.iso_terico.models.Ranking;

import java.util.List;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.PreguntasViewHolder> {
    Context context;
    List<Ranking> rankingList;

    public PreguntasAdapter(Context context, List<Ranking> rankingList){
        this.context =context;
        this.rankingList = rankingList;
    }
    @NonNull
    @Override
    public PreguntasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new PreguntasViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_list, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull PreguntasViewHolder holder, int position){
        Ranking pos = rankingList.get(holder.getAdapterPosition());
        holder.tUsuario.setText(pos.getUsuario());
        holder.tPuntaje.setText(pos.getPuntaje());
    }

    @Override
    public int getItemCount(){return rankingList.size();}

    public static class PreguntasViewHolder extends RecyclerView.ViewHolder{
        TextView tUsuario, tPuntaje;
        public PreguntasViewHolder(@NonNull View itemView){
            super(itemView);
            tUsuario = itemView.findViewById(R.id.usuario);
            tPuntaje = itemView.findViewById(R.id.puntaje);
        }
    }

}
