package com.example.otakunikki.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.otakunikki.Clases.Explorar;
import com.example.otakunikki.Fragmentos.FragmentInfoUsuario;
import com.example.otakunikki.Fragmentos.FragmentInicio;
import com.example.otakunikki.Fragmentos.FragmentoListas;
import com.example.otakunikki.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MenuPrincipal extends AppCompatActivity {
    private BottomNavigationView menu_navegador;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        menu_navegador = findViewById(R.id.menu_navegador);
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
        startActivity(intent);
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

}