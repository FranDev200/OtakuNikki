package com.example.otakunikki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class Explorar extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar);


        viewPager2 = findViewById(R.id.vpPaginador);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(), getLifecycle()));
        viewPager2.setOrientation(viewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            //Ahora, además para que la marca de tab seleccionado (linea horizontal) funcione al cambiar de Fragment activo
            //Es importante hacerlo porque si no, no irá sincronizado con el tabLayout.addOnTabSelectedListener() desarrollado debajo
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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
            switch (position) {
                case 0:
                    return new FragmentoTodoElAnime();
                case 1:
                    return new FragmentoGeneros();
                default:
                    return new FragmentoUltTendencias();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}