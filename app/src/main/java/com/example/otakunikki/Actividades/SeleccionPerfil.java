package com.example.otakunikki.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.otakunikki.Adaptadores.AdaptadorPerfilesGridView;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;

import java.util.ArrayList;
import java.util.List;

public class SeleccionPerfil extends AppCompatActivity {
    private ImageButton btnAnyadir;
    private GridView miGridView;
    private AdaptadorPerfilesGridView miAdaptadorPerfilesGridView;
    private List<Perfil> listaperfiles;
    private String idioma;
    private TextView tvEscogePerfil, tvAgregarPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_perfil);

        SharedPreferences infoIdioma = getSharedPreferences("Idiomas", Context.MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");
        btnAnyadir = findViewById(R.id.btnAnyadir);
        miGridView = findViewById(R.id.gvListaUsuarios);
        tvEscogePerfil = findViewById(R.id.tvEscogePerfil);
        tvAgregarPerfil = findViewById(R.id.tvAgregarPerfil);

        Traductor.traducirTexto(tvEscogePerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvEscogePerfil.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(tvAgregarPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvAgregarPerfil.setText(textoTraducido);
            }
        });

        miGridView.setAdapter(miAdaptadorPerfilesGridView);
        listaperfiles = new ArrayList<Perfil>();

        /**RECOGEMOS EL INTENT DE LA CLASE REGISTRO**/
        Usuario usuario = getIntent().getParcelableExtra("Usuario");

        SharedPreferences preferences = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(usuario.getEmail() != null){
            editor.putString("email", usuario.getEmail());
            editor.putString("region", usuario.getIdioma());
        }
        editor.apply();


        miAdaptadorPerfilesGridView = new AdaptadorPerfilesGridView(getApplicationContext(), R.layout.item_perfiles,usuario.getListaPerfiles());

        miGridView.setAdapter(miAdaptadorPerfilesGridView);

        miAdaptadorPerfilesGridView.notifyDataSetChanged();

        miGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Perfil perfil = usuario.getListaPerfiles().get(position);

                // Guardar en SharedPreferences
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PerfilSeleccionado", perfil.getNombrePerfil());
                editor.putInt("ImagenPerfil", perfil.getImagenPerfil());
                editor.apply();


                AbrirMenuPrincipal();
                finish();
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