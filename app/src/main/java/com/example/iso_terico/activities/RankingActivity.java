package com.example.iso_terico.activities;

import static com.example.iso_terico.activities.GameActivity.EXTRA_PUNTAJE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iso_terico.R;
import com.example.iso_terico.adapters.PreguntasAdapter;
import com.example.iso_terico.models.GoogleSheetsResponse;
import com.example.iso_terico.models.IGoogleSheets;
import com.example.iso_terico.models.Pregunta;
import com.example.iso_terico.models.Ranking;
import com.example.iso_terico.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RankingActivity extends AppCompatActivity {
    private String tema;
    private IGoogleSheets iGoogleSheets;
    private ProgressBar progressBar;
    private List<Ranking> rankingList;
    private RecyclerView recyclerView;
    private String puntaje;
    private LinearLayout pregunta;
    private LinearLayout usuario;
    private String textoIngresado;
    private Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Intent intent = getIntent();
        progressBar = findViewById(R.id.progressBar);
        rankingList= new ArrayList<>();
        if (intent != null && intent.hasExtra(EXTRA_PUNTAJE)) {
            puntaje = String.valueOf(intent.getIntExtra(EXTRA_PUNTAJE, 0));
            tema= MainMenuActivity.tema;
        }
        recyclerView = findViewById(R.id.recyclerView);
        pregunta= findViewById(R.id.pregunta);
        pregunta.setVisibility(View.VISIBLE);
        TextView puntajeTextView = findViewById(R.id.puntaje);
        puntajeTextView.setText("TU PUNTAJE ES: "+ puntaje);

    }

    public void guardarPuntuacion(View view){
        pregunta.setVisibility(View.GONE);
        usuario= findViewById(R.id.usuario);
        usuario.setVisibility(View.VISIBLE);
    }
    public void volverAlMenu(View view){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }
    public void registrarPuntajeUsuario(View view){
        EditText editText = findViewById(R.id.nombreDeUsuario);
        textoIngresado = editText.getText().toString().trim();
        if (textoIngresado.isEmpty()){
            editText.setError("Dale pibe, meté algo!");
        }
        else {
            usuario.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Retrofit retrofit = GoogleSheetsResponse.getClientGetMethod("https://script.google.com/");
            iGoogleSheets = retrofit.create(IGoogleSheets.class);
            loadDataFromGoogleSheets(tema);
        }
    }

    private void loadDataFromGoogleSheets(String temaActual){
        String pathUrl;
        try{
            pathUrl = "";
            iGoogleSheets.getPreguntas(pathUrl).enqueue(new Callback<String>() {
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response){
                    try {
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray rankingArray = responseObject.getJSONArray("ranking");
                        for (int i = 0; i < rankingArray.length(); i++) {
                            JSONObject object = rankingArray.getJSONObject(i);
                            String tema = object.getString("tema");
                            if (tema.equals(temaActual)){
                                String id = object.getString("id");
                                String usuario = object.getString("usuario");
                                String puntaje = object.getString("puntaje");
                                Ranking ranking = new Ranking(id,tema, usuario, puntaje);
                                rankingList.add(ranking);
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        mostrarRanking(rankingList);
                    } catch (JSONException e){
                        Log.e("error", "fallo3",e);
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("error", "fallo2");
                }
            });
        } catch (Exception e) {
            Log.e("error", "fallo1");
            throw new RuntimeException(e);
        }
    }
    private void mostrarRanking(List<Ranking> ranking){
        Ranking r= ranking.stream().filter(ranking1 -> ranking1.getUsuario().equals(textoIngresado)).findAny().orElse(null);
        if (r==null){
            int maxId;
            if (!ranking.isEmpty()){
                Ranking rankingConMaxId = Collections.max(ranking, Comparator.comparingInt(r1 -> Integer.parseInt(r1.getId())));
                maxId= Integer.parseInt(rankingConMaxId.getId())+1;
            }
            else
                maxId=1;
            Ranking nuevo=new Ranking(String.valueOf(maxId) ,tema, textoIngresado, puntaje);
            ranking.add(nuevo);
            registrarPuntaje(nuevo);
        }
        else if (Integer.parseInt(r.getPuntaje())< Integer.parseInt(puntaje)){
            ranking.remove(r);
            Ranking nuevo=new Ranking(r.getId() ,tema, textoIngresado, puntaje);
            ranking.add(nuevo);
            registrarPuntaje(nuevo);
        }
        progressBar.setVisibility(View.GONE);
        LinearLayout rank = findViewById(R.id.ranking);
        ranking.stream()
                .sorted(Comparator.comparingInt((Ranking ra) -> Integer.parseInt(ra.getPuntaje())).reversed())
                .collect(Collectors.toList());
        Collections.sort(ranking, new Comparator<Ranking>() {
            @Override
            public int compare(Ranking registro1, Ranking registro2) {
                return Integer.compare(Integer.parseInt(registro2.getPuntaje()), Integer.parseInt(registro1.getPuntaje()));
            }
        });
        rank.setVisibility(View.VISIBLE);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(RankingActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        PreguntasAdapter rankingAdapter = new PreguntasAdapter(RankingActivity.this, rankingList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(rankingAdapter);


    }

    private void registrarPuntaje(Ranking r){
        AsyncTask.execute(() ->{
            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("")
                        .build();
                IGoogleSheets iGoogleSheets1 = retrofit.create(IGoogleSheets.class);

                String jsonRequest = "{\n" +
                        "  \"spreadsheet_id\": \"" + Common.GOOGLE_SHEET_ID + "\",\n" +
                        "  \"sheet\": \"" + Common.SHEET_NAME + "\",\n" +
                        "  \"rows\": [\n" +
                        "    \"" + r.getId() + "\",\n" +
                        "    \"" + r.getTema() + "\",\n" +
                        "    \"" + r.getUsuario() + "\",\n" +
                        "    \"" + r.getPuntaje() + "\"\n" +
                        "  ],\n" +
                        "  \"rowIndex\": " + String.valueOf(Integer.parseInt(r.getId())+1) + "\n" +
                        "}";

                Call<String> call = iGoogleSheets1.getStringRequestBody(jsonRequest);
                Response<String> response = call.execute();
                int code = response.code();
                if (code == 200) {
                    Log.e("bien", "joya");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}