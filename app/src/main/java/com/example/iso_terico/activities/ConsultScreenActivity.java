package com.example.iso_terico.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iso_terico.R;
import com.example.iso_terico.adapters.PreguntasAdapter;
import com.example.iso_terico.models.IGoogleSheets;
import com.example.iso_terico.models.Pregunta;
import com.example.iso_terico.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultScreenActivity  extends AppCompatActivity {
    IGoogleSheets iGoogleSheets;
    private List<Pregunta> preguntasList;

    private RecyclerView recyclerPreguntas;
    private ProgressBar progressBar;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_screen_activity);
        progressBar= findViewById(R.id.progressBar);
        recyclerPreguntas=findViewById(R.id.recyclerPreguntas);
        preguntasList = new ArrayList<>();

        iGoogleSheets = Common.IGSGetMethodClient(Common.BASE_URL);
        loadDataFromGoogleSheets();

    }

    private void loadDataFromGoogleSheets(){
        String pathUrl;

        try{
            pathUrl = "https://script.google.com/macros/s/AKfycbwhxEY35SqaccXgyVNVR_q4jhpA1fsN5ngwoWc_-_zQJuEkoJBopc-Fq6Xu60OHM087/exec?spreadsheetId=1eCEHGeCz1Nb-CxZFvduh0J3-eTRTEim_8iURbV5Ytcc&sheet=preguntas";
            iGoogleSheets.getPersonas(pathUrl).enqueue(new Callback<String>() {
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response){
                    try {
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray preguntasArray = responseObject.getJSONArray("preguntas");
                        setPreguntasAdapter(preguntasList);
                        for (int i = 0; i < preguntasArray.length(); i++) {
                            JSONObject object = preguntasArray.getJSONObject(i);
                            String id = object.getString("id");
                            String pregunt = object.getString("pregunta");
                            String respuesta_correcta = object.getString("respuesta");

                            ArrayList<String> respuestas_incorrectas = new ArrayList<>();

                            Pregunta pregunta = new Pregunta(id, pregunt, respuesta_correcta, respuestas_incorrectas);
                            preguntasList.add(pregunta);
                            progressBar.setVisibility(View.GONE);
                        }
                        int size = preguntasList.size();
                    } catch (JSONException je){
                        Log.e("error", "fallo3");
                        je.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("error", "fallo2");
                }

                private void setPreguntasAdapter(List<Pregunta> preguntaList){
                    LinearLayoutManager manager = new LinearLayoutManager(ConsultScreenActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);

                    PreguntasAdapter preguntasAdapter = new PreguntasAdapter(ConsultScreenActivity.this, preguntaList);
                    recyclerPreguntas.setLayoutManager(manager);
                    recyclerPreguntas.setAdapter(preguntasAdapter);
                }
            });
        } catch (Exception e) {
            Log.e("error", "fallo1");
            throw new RuntimeException(e);
        }
    }
    public void actualizarListado(View view){
        loadDataFromGoogleSheets();
    }
}
