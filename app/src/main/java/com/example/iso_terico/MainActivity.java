package com.example.iso_terico;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import org.json.JSONArray;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MainActivity";
    private String TABLE_PREGUNTAS = "preguntas";
    private String sheetInJsonURL = "https://script.google.com/macros/s/AKfycbwhxEY35SqaccXgyVNVR_q4jhpA1fsN5ngwoWc_-_zQJuEkoJBopc-Fq6Xu60OHM087/exec?spreadsheetId=1eCEHGeCz1Nb-CxZFvduh0J3-eTRTEim_8iURbV5Ytcc&sheet=preguntas";
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        getUsers();
    }
    private void initList() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        recyclerView.setLayoutManager(llm);

        progressBar.setVisibility(View.GONE);
    }

    public void getUsers() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        // Request a JSONArray response from the provided URL.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, sheetInJsonURL + TABLE_PREGUNTAS, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Continuar con el procesamiento del JSON y la inicialización de la lista
                        RecyclerUserAdapter recyclerUserAdapter = new RecyclerUserAdapter(response);
                        initList();
                        recyclerView.setAdapter(recyclerUserAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String errorMessage = new String(error.networkResponse.data);
                            Log.e(LOG_TAG, "Error en la solicitud, código de estado: " + statusCode + ", Mensaje: " + errorMessage);
                            // Manejar el error según sea necesario
                        } else {
                            Log.e(LOG_TAG, "Error en la solicitud, código de estado desconocido");
                        }
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }
}

