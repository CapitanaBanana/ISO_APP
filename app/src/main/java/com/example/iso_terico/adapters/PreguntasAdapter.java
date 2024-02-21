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

import java.util.List;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.PreguntasViewHolder> {
    Context context;
    List<Pregunta> preguntaList;

    public PreguntasAdapter(Context context, List<Pregunta> preguntaList){
        this.context =context;
        this.preguntaList = preguntaList;
    }
    @NonNull
    @Override
    public PreguntasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return new PreguntasViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_list, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull PreguntasViewHolder holder, int position){
        Pregunta pos = preguntaList.get(holder.getAdapterPosition());
        String pregunta = pos.getPregunta();
        holder.tId.setText(pos.getId());
        holder.tPregunta.setText(pos.getPregunta());
        holder.tRespuestaCorrecta.setText(pos.getRespuesta_correcta());
    }

    @Override
    public int getItemCount(){return preguntaList.size();}

    public static class PreguntasViewHolder extends RecyclerView.ViewHolder{
        TextView tId, tPregunta, tRespuestaCorrecta;
        public PreguntasViewHolder(@NonNull View itemView){
            super(itemView);
            tId = itemView.findViewById(R.id.id);
            tPregunta = itemView.findViewById(R.id.pregunta);
            tRespuestaCorrecta = itemView.findViewById(R.id.respuestaCorrecta);
        }
    }

}
