package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.Fragmentos.FragmentInfoUsuario;
import com.example.otakunikki.Fragmentos.FragmentInicio;
import com.example.otakunikki.Fragmentos.FragmentoListas;
import com.example.otakunikki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipal extends AppCompatActivity {
    private BottomNavigationView menu_navegador;
    private String idioma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        SharedPreferences infoIdioma = getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");

        menu_navegador = findViewById(R.id.menu_navegador);
        traducirBottomNavigationView(menu_navegador, idioma);

        cargarFragment(new FragmentInicio());
        menu_navegador.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.mnu_home){
                    cargarFragment(new FragmentInicio());
                }
                else if(item.getItemId() == R.id.mnu_foro){

                }else if(item.getItemId() == R.id.mnu_explorar){
                    abrirExplorar();
                }else if(item.getItemId() == R.id.mnu_listas){
                    cargarFragment(new FragmentoListas());
                }else if(item.getItemId() == R.id.mnu_cuenta){
                    cargarFragment(new FragmentInfoUsuario());
                }

                return true;
            }
        });

    }

    public void abrirExplorar(){
        Intent intent = new Intent(getApplicationContext(), Explorar.class);
        startActivityForResult(intent, 1); // El 1 es para ponerle un id a la actividad
        //Utilizo startActivityForResult para que detecte cuando se cierra la actividad y cambie el estado del navegador
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) { // Compruebo que es la actividad 1 por el id
            menu_navegador.setSelectedItemId(R.id.mnu_home); // Selecciono el Inicio
        }
    }

    private void cargarFragment(Fragment fragment) {


        // Comprobar si el fragmento ya está cargado
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flGeneral);

        if (currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())) {
            // Si el fragmento actual no es el que intentamos cargar, reemplazamos el fragmento
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flGeneral, fragment) // Reemplazamos el fragmento
                    //.addToBackStack(null) // Añadimos a la pila de retroceso (opcional)
                    .commit();
        }


    }
    public void traducirBottomNavigationView(BottomNavigationView bottomNavigationView, String idiomaDestino) {

        Menu menu = bottomNavigationView.getMenu();

        for (int i = 0; i < menu.size(); i++) {

            MenuItem menuItem = menu.getItem(i);

           String textoOriginal = menuItem.getTitle().toString();

            Traductor.traducirTexto(textoOriginal, "es", idiomaDestino, new Traductor.TraduccionCallback() {
                @Override
                public void onTextoTraducido(String textoTraducido) {

                    menuItem.setTitle(textoTraducido);
                }
            });
        }
    }

}