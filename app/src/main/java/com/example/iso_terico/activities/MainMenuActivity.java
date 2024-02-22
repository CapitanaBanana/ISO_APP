package com.example.iso_terico.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.iso_terico.R;
import com.example.iso_terico.adapters.PreguntasAdapter;
import com.example.iso_terico.models.GoogleSheetsResponse;
import com.example.iso_terico.models.IGoogleSheets;
import com.example.iso_terico.models.Pregunta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainMenuActivity extends AppCompatActivity {
    public static final String EXTRA_PREGUNTA= "com.example.iso_terico.PREGUNTA";
    private List<Pregunta> preguntasList;
    private IGoogleSheets iGoogleSheets;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        progressBar = findViewById(R.id.progressBar);
        preguntasList = new ArrayList<>();

        // Initialize the Retrofit instance and create an instance of the IGoogleSheets interface
        Retrofit retrofit = GoogleSheetsResponse.getClientGetMethod("https://script.google.com/");
        iGoogleSheets = retrofit.create(IGoogleSheets.class);
    }
    protected void onResume() {
        preguntasList.clear();
        super.onResume();
    }

    public void memoria(View view){

        llamadoLoad("memoria");
    }
    public void buffer(View view){
        llamadoLoad("cache");
    }
    public void entrada_salida(View view){
        llamadoLoad("entrada_salida");
    }
    public void archivos(View view){
        llamadoLoad("archivos");
    }
    public void todos(View view){

    }

    private void loadDataFromGoogleSheets(String clase){
        String pathUrl;
        try{
            pathUrl = "https://script.google.com/macros/s/AKfycbwhxEY35SqaccXgyVNVR_q4jhpA1fsN5ngwoWc_-_zQJuEkoJBopc-Fq6Xu60OHM087/exec?spreadsheetId=1eCEHGeCz1Nb-CxZFvduh0J3-eTRTEim_8iURbV5Ytcc&sheet="+clase;
            iGoogleSheets.getPreguntas(pathUrl).enqueue(new Callback<String>() {
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response){
                    try {
                        assert response.body() != null;
                        JSONObject responseObject = new JSONObject(response.body());
                        JSONArray preguntasArray = responseObject.getJSONArray("preguntas");
                        for (int i = 0; i < preguntasArray.length(); i++) {
                            JSONObject object = preguntasArray.getJSONObject(i);
                            String id = object.getString("id");
                            String pregunt = object.getString("pregunta");
                            String respuesta_correcta = object.getString("respuesta_correcta");
                            String explicacion = object.getString("explicacion");
                            String inc1 = object.getString("inc1");
                            String inc2 = object.getString("inc2");
                            String inc3 = object.getString("inc3");

                            Pregunta pregunta = new Pregunta(id, pregunt, respuesta_correcta, explicacion, inc1, inc2, inc3);
                            preguntasList.add(pregunta);
                        }
                        progressBar.setVisibility(View.GONE);
                        iniciarJuego(preguntasList);
                    } catch (JSONException je){
                        Log.e("error", "fallo3");
                        je.printStackTrace();
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
    private void llamadoLoad(String s){
        try {
            if (tieneConexion()) {
                progressBar.setVisibility(View.VISIBLE); // Oculta el ProgressBar
                loadDataFromGoogleSheets(s);
            }
            else{
                progressBar.setVisibility(View.GONE); // Oculta el ProgressBar
                Toast.makeText(this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde Google Sheets: " + e.getMessage());
        }
    }
    private boolean tieneConexion() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    private void iniciarJuego(List<Pregunta> preguntas){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_PREGUNTA, (Serializable) preguntas);
        startActivity(intent);
    }
}