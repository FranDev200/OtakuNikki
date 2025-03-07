package com.example.otakunikki.Clases;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.otakunikki.Fragmentos.FragmentoGeneros;
import com.example.otakunikki.Fragmentos.FragmentoTodoElAnime;
import com.example.otakunikki.Fragmentos.FragmentoUltTendencias;
import com.example.otakunikki.R;
import com.google.android.material.tabs.TabLayout;

public class Explorar extends AppCompatActivity {

    private ImageButton imgRetroceso;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private boolean flag = false; //Lo hacemos para no cargar varios fragment si se pulsa varias veces

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Explorar", "onCreate llamado");

        setContentView(R.layout.activity_explorar);

        imgRetroceso = findViewById(R.id.imgRetroceso);
        viewPager2 = findViewById(R.id.vpPaginador);
        tabLayout = findViewById(R.id.tabLayout);

        imgRetroceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(), getLifecycle()));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            //Ahora, además para que la marca de tab seleccionado (linea horizontal) funcione al cambiar de Fragment activo
            //Es importante hacerlo porque si no, no irá sincronizado con el tabLayout.addOnTabSelectedListener() desarrollado debajo
            @Override
            public void onPageSelected(int position) {
                Log.d("ViewPager2", "Página seleccionada: " + position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("TabLayout", "Tab seleccionada en posición: " + tab.getPosition());
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    class AdaptadorFragment extends FragmentStateAdapter {
        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d("Viewpager2", "Creando gragmento en la pos: " + position);
            switch (position) {
                case 0:
                    return new FragmentoTodoElAnime();
                case 1:
                    return new FragmentoGeneros();
                default:
                    return  new FragmentoUltTendencias();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}