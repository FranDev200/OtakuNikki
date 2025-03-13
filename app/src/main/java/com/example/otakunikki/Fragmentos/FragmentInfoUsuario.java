package com.example.otakunikki.Fragmentos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import android.widget.Toast;

import com.example.otakunikki.Actividades.InicioSesion;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.example.otakunikki.Actividades.SeleccionPerfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentInfoUsuario extends Fragment {
    private Button btnEliminarPerfil, btnDesconexion, btnCambioPerfil;
    private EditText etNombreUsuario, etContraseniaUsuario;
    TextView tvRegion, tvNomPerfil, tvCorreoUsuario;
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
        tvCorreoUsuario = vista.findViewById(R.id.tvCorreoUsuario);
        etContraseniaUsuario = vista.findViewById(R.id.etContraseniaUsuario);
        spRegion = vista.findViewById(R.id.spRegion);
        imgPerfil = vista.findViewById(R.id.imgPerfil);
        tvRegion = vista.findViewById(R.id.tvRegionSeleccionada);
        tvNomPerfil = vista.findViewById(R.id.tvNomPerfil);

        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("NombrePerfil", Context.MODE_PRIVATE);
        String nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");

        Log.i("DEBUG", "Perfil recibido en FragmentInfoUsuario: " + nombrePerfil);
        tvNomPerfil.setText(nombrePerfil);


        btnCambioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambioPerfil();
            }
        });


        btnDesconexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                builder.setTitle("¿Estás seguro de desconectarte?\n")
                        .setIcon(R.drawable.logout);


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(requireContext(), "Has elegido no salir", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext(), "Has cerrado sesión en: " + nombrePerfil, Toast.LENGTH_LONG).show();

                        FirebaseAuth.getInstance().signOut();

                        // Eliminar la preferencia de recordar sesión
                        SharedPreferences.Editor editor = requireContext().getSharedPreferences("PreferenciaSesion", Context.MODE_PRIVATE).edit();
                        editor.putBoolean("rememberMe", false);
                        editor.remove("userId"); // Eliminar UID
                        editor.apply();

                        // Redirigir al usuario al LoginActivity
                        Intent intent = new Intent(requireActivity(), InicioSesion.class);
                        startActivity(intent);
                        requireActivity().finish();

                    }
                });

                builder.create();
                builder.show();

            }
        });

        btnEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

                builder.setTitle("¿Estás seguro de borrar el perfil " + nombrePerfil + "?\n")
                        .setIcon(R.drawable.eliminar);


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(requireContext(), "Has elegido no borrar", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(requireContext(), "Perfil: " + nombrePerfil + " eliminado.", Toast.LENGTH_LONG).show();
                        EliminarPerfil(nombrePerfil);
                    }
                });

                builder.create();
                builder.show();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, regiones);
        spRegion.setAdapter(adapter);
        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (regiones[position].contentEquals("--Seleccion una región--")) {
                    tvRegion.setText(regiones[position]);
                }
                if (regiones[position].contentEquals("España")) {
                    tvRegion.setText((regiones[position]));
                }
                if (regiones[position].contentEquals("Estados Unidos")) {
                    tvRegion.setText((regiones[position]));
                }
                if (regiones[position].contentEquals("Japón")) {
                    tvRegion.setText((regiones[position]));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return vista;
    }

    private void EliminarPerfil(String nombrePerfil){
        /**LOGICA PARA LA ELIMINACION DE UN PERFIL DE USUARIO**/
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            Toast.makeText(requireContext(), "No hay usuario autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();



        // Obtener la lista de perfiles del usuario en Firestore
        db.collection("Usuarios").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                if (usuario != null) {
                    // Filtrar la lista para eliminar el perfil seleccionado
                    usuario.getListaPerfiles().removeIf(perfil -> perfil.getNombrePerfil().equals(nombrePerfil));

                    // Actualizar en Firestore
                    db.collection("Usuarios").document(userId)
                            .update("listaPerfiles", usuario.getListaPerfiles())
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(requireContext(), "Perfil eliminado correctamente", Toast.LENGTH_SHORT).show();

                                // Redirigir a SeleccionPerfil
                                CambioPerfil();


                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Error al eliminar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error al obtener usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

    private void CambioPerfil(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            return;  // No hacer nada si no hay usuario autenticado
        }

        String userId = auth.getCurrentUser().getUid();

        // Mostrar un mensaje de carga para indicar que se está procesando la información
        Toast.makeText(requireContext(), "Cargando perfiles...", Toast.LENGTH_SHORT).show();

        // Obtener la información del usuario desde Firestore
        db.collection("Usuarios").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                if (usuario != null) {
                    Intent intent = new Intent(requireActivity(), SeleccionPerfil.class);
                    intent.putExtra("Usuario", usuario);
                    startActivity(intent);

                    // Cerrar el fragmento después de cambiar de actividad
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(FragmentInfoUsuario.this).commit();
                }
            } else {
                Toast.makeText(requireContext(), "No se encontró el usuario", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error al obtener usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

}