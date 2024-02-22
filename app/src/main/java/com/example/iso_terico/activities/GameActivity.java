package com.example.iso_terico.activities;

import static com.example.iso_terico.activities.MainMenuActivity.EXTRA_PREGUNTA;

import static java.lang.Math.random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iso_terico.R;
import com.example.iso_terico.adapters.PreguntasAdapter;
import com.example.iso_terico.models.Pregunta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Confetti;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.core.models.Size;

public class GameActivity extends AppCompatActivity {
    private TextView textViewPregunta;
    private List<Pregunta> preguntasList;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private Pregunta actual;
    private TextView explicacion;
    private Button aceptar;
    private Button siguiente;
    private KonfettiView konfettiView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        textViewPregunta=findViewById(R.id.textViewPregunta);
        radioGroup= findViewById(R.id.radioGroup);
        explicacion= findViewById(R.id.explicacion);

        aceptar= findViewById(R.id.aceptar);
        siguiente=findViewById(R.id.siguiente);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        preguntasList = new ArrayList<>();
        if (intent != null && intent.hasExtra(EXTRA_PREGUNTA)) {
            preguntasList= (List<Pregunta>) intent.getSerializableExtra(EXTRA_PREGUNTA);
        }
        Collections.shuffle(preguntasList);
        iniciarJuego();

        konfettiView = findViewById(R.id.konfettiView);

    }
    public void iniciarJuego(){
        if (!preguntasList.isEmpty()) {
            preguntar();
        } else {
            Toast.makeText(this, "No hay más preguntas de este tema!", Toast.LENGTH_SHORT).show();
        }
    }
    public void preguntar(){
        if (!preguntasList.isEmpty()) {
            actual = preguntasList.remove(preguntasList.size() - 1);
            textViewPregunta.setText(actual.getPregunta());
            List<String> respuestas = actual.getRespustas();
            Collections.shuffle(respuestas);
            radioButton1.setText(respuestas.get(0));
            radioButton2.setText(respuestas.get(1));
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            if (respuestas.size()>2){
                radioButton3.setText(respuestas.get(2));
                radioButton3.setVisibility(View.VISIBLE);
            }
            if (respuestas.size()>3){
                radioButton4.setText(respuestas.get(3));
                radioButton4.setVisibility(View.VISIBLE);
            }
        }

    }

    public void aceptar(View view){
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (selectedRadioButtonId!=-1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            if (selectedRadioButton.getText().equals(actual.getRespuesta_correcta())) {
                Toast.makeText(this, "BIEN!", Toast.LENGTH_SHORT).show();
                explode();
            }
            else
                Toast.makeText(this, "MAL!", Toast.LENGTH_SHORT).show();
            explicacion.setText(actual.getExplicacion());
            radioButton1.setVisibility(View.GONE);
            radioButton2.setVisibility(View.GONE);
            radioButton3.setVisibility(View.GONE);
            radioButton4.setVisibility(View.GONE);

            explicacion.setVisibility(View.VISIBLE);
            aceptar.setVisibility(View.GONE);
            siguiente.setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(this, "Debe seleccionar una respuesta!", Toast.LENGTH_SHORT).show();

    }

    public void siguiente(View view){
        explicacion.setVisibility(View.GONE);
        radioGroup.clearCheck();
        iniciarJuego();
        aceptar.setVisibility(View.VISIBLE);
        siguiente.setVisibility(View.GONE);
    }
    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Drawable drawableEstrellita = getResources().getDrawable(R.drawable.estrellita);
        Shape.DrawableShape estrellitaShape = new Shape.DrawableShape(
                drawableEstrellita,
                true,  // Puedes ajustar si quieres aplicar tinte o no
                true   // Puedes ajustar si quieres aplicar alpha o no
        );
        konfettiView.start(
                new PartyFactory(emitterConfig)

                        .spread(360)
                        .shapes(Arrays.asList(estrellitaShape))
                        .colors(Arrays.asList(0xf5f242, 0xf5b342, 0xf57e42, 0xf54542))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0))
                        .build());
    }
}
