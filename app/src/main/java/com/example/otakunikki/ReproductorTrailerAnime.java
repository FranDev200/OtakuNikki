package com.example.otakunikki;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReproductorTrailerAnime extends AppCompatActivity {

    private WebView wbTrailer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reproductor_trailer_anime);
        wbTrailer = findViewById(R.id.wbTrailer);

        // Configurar WebView
        WebSettings webSettings = wbTrailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        wbTrailer.setWebViewClient(new WebViewClient());

        // Obtener la URL del intent
        String trailerUrl = getIntent().getStringExtra("Trailer");
        if (trailerUrl != null) {
            wbTrailer.loadUrl(trailerUrl);
        }
        else {
            //Toast.makeText(this, "No se pudo cargar el tráiler", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no hay tráiler
        }
    }
}