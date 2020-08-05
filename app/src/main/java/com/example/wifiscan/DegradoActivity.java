package com.example.wifiscan;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DegradoActivity extends AppCompatActivity {
    private ImageView imageView;
    private String[] titoli = {"CHEEEKEEE BREEEKEEEEE", "Sesso!", "MilaniAni", "EEEEH SEECONDO MEEEEH", "Frociarenzo", "Le corna belle... Il pelo luungo...", "Marcello Bello"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degrado);

        // numero casuale tra 0 e titoli.length - 1
        final int sceltaTitolo = new Random().nextInt(titoli.length);

        // sceglie un titolo a caso
        setTitle(titoli[sceltaTitolo]);

        // modifica l'immagini dell'ImageView
        imageView = findViewById(R.id.imgDegrado);
        imageView.setImageResource(R.drawable.cheeki_breeki);

        // prendo la root view per poter modificare il colore dopo
        final View root = imageView.getRootView();


        // modifica il colore di sfondo ogni 2 secondi
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // devo usare runOnUiThread perchè
                // solo il therd in modalità UI può modificare gli oggetti dell'UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Random rand = new Random();

                        root.setBackgroundColor(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                    }
                });
            }
        }, 2000, 2000);
    }
}