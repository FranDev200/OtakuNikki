package com.example.otakunikki.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.otakunikki.Actividades.ActividadInicial;
import com.example.otakunikki.R;
import com.example.otakunikki.Actividades.SeleccionPerfil;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentInfoUsuario extends Fragment {
    private Button btnEliminarPerfil, btnDesconexion, btnCambioPerfil;
    private EditText etNombreUsuario, etCorreoUsuario, etContraseniaUsuario;
    TextView tvRegion;
    private Spinner spRegion;
    private String[] regiones = {"España", "Estados Unidos", "Japón"};
    private ImageButton imgPerfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_info_usuario, container, false);


        btnEliminarPerfil = vista.findViewById(R.id.btnEliminarPerfil);
        btnDesconexion = vista.findViewById(R.id.btnDesconexion);
        btnCambioPerfil = vista.findViewById(R.id.btnCambioPerfil);
        etNombreUsuario = vista.findViewById(R.id.etNombreUsuario);
        etCorreoUsuario = vista.findViewById(R.id.etCorreoUsuario);
        etContraseniaUsuario = vista.findViewById(R.id.etContraseniaUsuario);
        spRegion = vista.findViewById(R.id.spRegion);
        imgPerfil = vista.findViewById(R.id.imgPerfil);
        tvRegion = vista.findViewById(R.id.tvRegionSeleccionada);

        btnCambioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SeleccionPerfil.class);
                startActivity(intent);
            }
        });

        btnDesconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                // Eliminar la preferencia de recordar sesión
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("PreferenciaSesion", Context.MODE_PRIVATE).edit();
                editor.putBoolean("rememberMe", false);
                editor.apply();

                // Redirigir al usuario al LoginActivity
                Intent intent = new Intent(requireActivity(), ActividadInicial.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        btnEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SeleccionPerfil.class);
                startActivity(intent);

                // Aquí puedes implementar la lógica para eliminar el perfil del usuario

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, regiones);
        spRegion.setAdapter(adapter);
        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(regiones[position].contentEquals("--Seleccion una región--")){
                    tvRegion.setText(regiones[position]);
                }
                if(regiones[position].contentEquals("España")){
                    tvRegion.setText((regiones[position]));
                }
                if(regiones[position].contentEquals("Estados Unidos")){
                    tvRegion.setText((regiones[position]));
                }
                if(regiones[position].contentEquals("Japón")){
                    tvRegion.setText((regiones[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return vista;
    }
}