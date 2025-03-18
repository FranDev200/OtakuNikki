package com.example.otakunikki.Clases;

import android.util.Log;

import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.nl.translate.Translation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class Traductor {

    // Método para traducir texto
    public static void traducirTexto(String texto, String idiomaOrigen, String idiomaDestino, TraduccionCallback callback) {
        // Establecer las opciones del traductor
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(idiomaOrigen)  // Establecer el idioma de origen
                .setTargetLanguage(idiomaDestino)  // Establecer el idioma de destino
                .build();

        // Crear un traductor con las opciones
        Translator translator = Translation.getClient(options);

        // Descargar el modelo si es necesario
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(aVoid -> {
                    // Traducir el texto
                    translator.translate(texto)
                            .addOnSuccessListener(callback::onTextoTraducido)  // Si la traducción es exitosa
                            .addOnFailureListener(e -> {
                                Log.e("ML Kit", "Error al traducir: " + e.getMessage());
                                callback.onTextoTraducido(texto);  // En caso de error, devolver el texto original
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("ML Kit", "Error al descargar el modelo: " + e.getMessage());
                    callback.onTextoTraducido(texto);  // En caso de error, devolver el texto original
                });
    }

    // Interfaz de Callback para manejar la respuesta de la traducción
    public interface TraduccionCallback {
        void onTextoTraducido(String textoTraducido);
    }
}

