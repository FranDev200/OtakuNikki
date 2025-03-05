package com.example.otakunikki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfil extends AppCompatActivity {
    private ImageButton btnAnyadir;
    private GridView miGridView;
    private AdaptadorPerfilesGridView miAdaptadorPerfilesGridView;
    private List<Perfil> miListaPerfiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_perfil);

        btnAnyadir = findViewById(R.id.btnAnyadir);
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

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirAnyadirPerfil();
            }
        });
    }

    public void AbrirMenuPrincipal(){
        Intent intent = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivity(intent);
    }

    public void abrirAnyadirPerfil(){
        Intent intent = new Intent(getApplicationContext(), AnyadirPerfil.class);
        startActivity(intent);
    }
}