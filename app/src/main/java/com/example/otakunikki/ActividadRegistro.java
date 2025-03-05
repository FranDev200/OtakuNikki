package com.example.otakunikki;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActividadRegistro extends AppCompatActivity {
    private String[] paises = {"--Seleccion un pais--","España", "Estados Unidos", "Japón"};
    private Spinner spnRegion;
    private TextView tvPaisSeleccionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registro);
        spnRegion = findViewById(R.id.spnRegion);
        tvPaisSeleccionado = findViewById(R.id.tvPaisSeleccionado);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnRegion.setAdapter(adapter);
        spnRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(paises[position].contentEquals("--Seleccion un pais--")){
                    tvPaisSeleccionado.setText(paises[position]);
                }
                if(paises[position].contentEquals("España")){
                    tvPaisSeleccionado.setText((paises[position]));
                }
                if(paises[position].contentEquals("Estados Unidos")){
                    tvPaisSeleccionado.setText((paises[position]));
                }
                if(paises[position].contentEquals("Japón")){
                    tvPaisSeleccionado.setText((paises[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}