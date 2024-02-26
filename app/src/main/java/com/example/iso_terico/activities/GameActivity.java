package com.example.iso_terico.activities;

import static com.example.iso_terico.activities.MainMenuActivity.EXTRA_PREGUNTA;

import static java.lang.Math.random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iso_terico.R;
import com.example.iso_terico.adapters.PreguntasAdapter;
import com.example.iso_terico.models.Pregunta;
import com.example.iso_terico.models.PuntajeCalculator;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Rotation;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Confetti;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.xml.KonfettiView;
import nl.dionsegijn.konfetti.core.models.Size;

public class GameActivity extends AppCompatActivity {
    public static final String EXTRA_PUNTAJE= "com.example.iso_terico.PUNTAJE";
    private TextView textViewPregunta, explicacion, correcto, incorrecto,textViewRacha, respuestaCorrecta;
    private List<Pregunta> preguntasList;
    private RadioGroup radioGroup;
    private RadioButton radioButton1,radioButton2,radioButton3,radioButton4;
    private Pregunta actual;
    private Button aceptar, siguiente;
    private KonfettiView konfettiView = null;
    private int racha=0, puntaje=0;
    private LocalDateTime tiempoInicio, tiempoFin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        textViewPregunta=findViewById(R.id.textViewPregunta);
        radioGroup= findViewById(R.id.radioGroup);
        explicacion= findViewById(R.id.explicacion);
        respuestaCorrecta= findViewById(R.id.respuestaCorrecta);
        correcto=findViewById(R.id.correcto);
        incorrecto=findViewById(R.id.incorrecto);
        textViewRacha=findViewById(R.id.racha);
        aceptar= findViewById(R.id.aceptar);
        siguiente=findViewById(R.id.siguiente);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        textViewRacha.setText("Racha: "+racha);

        preguntasList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_PREGUNTA)) {
            preguntasList= (List<Pregunta>) intent.getSerializableExtra(EXTRA_PREGUNTA);
        }
        Collections.shuffle(preguntasList);
        iniciarJuego();

        konfettiView = findViewById(R.id.konfettiView);

    }
    private void iniciarJuego(){
        if (!preguntasList.isEmpty()) {
            preguntar();
        } else {
                Intent intent = new Intent(this, RankingActivity.class);
                intent.putExtra(EXTRA_PUNTAJE, puntaje);
                startActivity(intent);
        }
    }
    private void preguntar() {
        if (!preguntasList.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tiempoInicio= LocalDateTime.now();
            }
            actual = preguntasList.remove(preguntasList.size() - 1);
            textViewPregunta.setText(actual.getPregunta());
            List<String> respuestas = actual.getRespustas();
            Collections.shuffle(respuestas);
            radioButton1.setText(respuestas.get(0));
            radioButton2.setText(respuestas.get(1));
            radioButton1.setVisibility(View.VISIBLE);
            radioButton2.setVisibility(View.VISIBLE);
            if (respuestas.size() > 2) {
                radioButton3.setText(respuestas.get(2));
                radioButton3.setVisibility(View.VISIBLE);
            }
            if (respuestas.size() > 3) {
                radioButton4.setText(respuestas.get(3));
                radioButton4.setVisibility(View.VISIBLE);
            }
        }
    }

    public void aceptar(View view){
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        if (selectedRadioButtonId!=-1) {
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                tiempoFin=LocalDateTime.now();
            if (selectedRadioButton.getText().equals(actual.getRespuesta_correcta())) {
                correcto.setVisibility(View.VISIBLE);
                correcto.startAnimation(aniSlide);
                racha+=1;
                explode();
                long diferenciaEnSegundos = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    diferenciaEnSegundos = Duration.between(tiempoInicio, tiempoFin).getSeconds();
                }
                puntaje+= PuntajeCalculator.calcularPuntajePorPregunta(diferenciaEnSegundos);

            }
            else{
                incorrecto.setVisibility(View.VISIBLE);
                incorrecto.startAnimation(aniSlide);
                racha=0;
                rain();
            }
            if(!actual.getExplicacion().equals(".")){
                explicacion.setVisibility(View.VISIBLE);
                explicacion.setText(actual.getExplicacion());
            }
            respuestaCorrecta.setVisibility(View.VISIBLE);
            respuestaCorrecta.setText(actual.getRespuesta_correcta());

            radioButton1.setVisibility(View.GONE);
            radioButton2.setVisibility(View.GONE);
            radioButton3.setVisibility(View.GONE);
            radioButton4.setVisibility(View.GONE);
            aceptar.setVisibility(View.GONE);
            siguiente.setVisibility(View.VISIBLE);
            textViewRacha.setText("Racha: "+racha);
        }
        else
            Toast.makeText(this, "Debe seleccionar una respuesta!", Toast.LENGTH_SHORT).show();

    }

    public void siguiente(View view){
        explicacion.setVisibility(View.GONE);
        respuestaCorrecta.setVisibility(View.GONE);
        radioGroup.clearCheck();
        iniciarJuego();
        aceptar.setVisibility(View.VISIBLE);
        siguiente.setVisibility(View.GONE);
        correcto.setVisibility(View.GONE);
        incorrecto.setVisibility(View.GONE);
    }

    private void explode() {
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
                        .sizes(new Size(20, 2f,10f))
                        .shapes(estrellitaShape)
                        .colors(Arrays.asList(0xf5f242, 0xf5b342, 0xf57e42, 0xf54542))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0))
                        .build());
    }
    private void rain() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        Drawable drawableCaquita = getResources().getDrawable(R.drawable.caquita);
        Shape.DrawableShape caquitaShape = new Shape.DrawableShape(
                drawableCaquita,
                false,  // Puedes ajustar si quieres aplicar tinte o no
                true   // Puedes ajustar si quieres aplicar alpha o no
        );
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .sizes(new Size(30, 2f,10f))
                        .rotation((new Rotation(true, 3f, 0.5f, 2f, 0f)))
                        .spread(Spread.ROUND)
                        .shapes(caquitaShape)
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.0, -0.5).between(new Position.Relative(1.0, -0.1)))
                        .build());
    }

}
