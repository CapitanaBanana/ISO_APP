package com.example.iso_terico;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder> {

    private JSONArray jsonArray;

    public RecyclerUserAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView pregunta;
        public TextView respuesta;

        public ViewHolder(View rootView) {
            super(rootView);
            pregunta = rootView.findViewById(R.id.pregunta);
            respuesta = rootView.findViewById(R.id.respuesta);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            String idPlusUsername = jsonObject.getString("id") + " - " + jsonObject.getString("username");
            holder.pregunta.setText(idPlusUsername);
            holder.respuesta.setText(jsonObject.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
            // Log o mensaje de error adicional
            holder.pregunta.setText("Error al cargar");
            holder.respuesta.setText("Error al cargar");
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }
}