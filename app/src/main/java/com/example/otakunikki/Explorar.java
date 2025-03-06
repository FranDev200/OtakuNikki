package com.example.otakunikki;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class Explorar extends AppCompatActivity {

    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorar);

        viewPager2 = findViewById(R.id.vpPaginador);
        viewPager2.setAdapter(new PagerAdapter(getSupportFragmentManager(), getLifecycle()));
        viewPager2.setOrientation(viewPager2.ORIENTATION_HORIZONTAL);

    }

    class PagerAdapter extends FragmentStateAdapter {


        public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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
            return 1;
        }
    }
}