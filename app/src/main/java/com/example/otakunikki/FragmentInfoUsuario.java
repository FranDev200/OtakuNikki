package com.example.otakunikki;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class FragmentInfoUsuario extends Fragment {
    private Button btnEliminarPerfil, btnDesconexion, btnCambioPerfil;
    private EditText etNombreUsuario, etCorreoUsuario, etContraseniaUsuario;
    private Spinner spRegion;
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
                Intent intent = new Intent(getActivity().getApplicationContext(), ActividadInicial.class);
                startActivity(intent);

                // Aquí puedes implementar la lógica para desconectarse del usuario

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


        return vista;
    }
}