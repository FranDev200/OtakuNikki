package com.example.otakunikki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfil extends AppCompatActivity {
    private GridView miGridView;
    private AdaptadorPerfilesGridView miAdaptadorPerfilesGridView;
    private List<Perfil> miListaPerfiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_perfil);

        miGridView = findViewById(R.id.gvListaUsuarios);
        miListaPerfiles = new ArrayList<Perfil>();
        miAdaptadorPerfilesGridView = new AdaptadorPerfilesGridView(getApplicationContext(), R.layout.item_perfiles, miListaPerfiles );
        miGridView.setAdapter(miAdaptadorPerfilesGridView);

        miListaPerfiles.add(new Perfil("https://i.blogs.es/0f7b87/solo-leveling/500_333.webp", "Mario", null));
        miAdaptadorPerfilesGridView.notifyDataSetChanged();

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbrirMenuPrincipal();
            }
        });
    }

    public void AbrirMenuPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(intent);
    }
}