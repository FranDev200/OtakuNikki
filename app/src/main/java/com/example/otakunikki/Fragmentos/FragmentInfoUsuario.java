package com.example.otakunikki.Fragmentos;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.otakunikki.Actividades.InicioSesion;
import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.GestionImagenes.AdaptadorFilasImagenes;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.R;
import com.example.otakunikki.Actividades.SeleccionPerfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.view.Gravity;

public class FragmentInfoUsuario extends Fragment {
    private Button btnEliminarPerfil, btnDesconexion, btnCambioPerfil, btnGuardarCambios;
    private EditText etNombreUsuario;
    TextView tvTitUser, tvTitCorreo, tvTitPerfil, tvRegion, tvNomPerfil, tvCorreoUsuario, tvTitIdioma;
    private String[] regiones = {"Español (España)", "Inglés", "Japonés", "Francés", "Italiano", "Alemán"};
    private String[] idiomas = {"es", "en", "ja", "fr", "it","de" };
    private ImageButton imgPerfil;
    private String TAG = "InfoUsuario";
    private String idioma;
    private String tituloCierreSesion = "¿Estás seguro de desconectarte?\n";
    private String btnCancelarCierreSesion = "Cancelar";
    private String btnAceptarCierreSesion = "Aceptar";
    private String cargandoPerfil = "Cargando perfiles...";
    private String tituloBorrar = "¿Estás seguro de borrar el perfil?\n";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        SharedPreferences preferencesIdi = getActivity().getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = preferencesIdi.getString("idioma", "es");
        setLocale(idioma); // Aplica el idioma al inicio

        View vista = inflater.inflate(R.layout.fragment_info_usuario, container, false);
        TraduccionVariablesAlert();
        btnEliminarPerfil = vista.findViewById(R.id.btnEliminarPerfil);
        btnDesconexion = vista.findViewById(R.id.btnDesconexion);
        btnCambioPerfil = vista.findViewById(R.id.btnCambioPerfil);
        btnGuardarCambios = vista.findViewById(R.id.btnGuardarCambios);
        etNombreUsuario = vista.findViewById(R.id.etNombreUsuario);
        tvTitUser = vista.findViewById(R.id.tvTitUser);
        tvTitIdioma = vista.findViewById(R.id.tvTitIdioma);
        tvTitCorreo = vista.findViewById(R.id.tvTitCorreo);
        tvTitPerfil = vista.findViewById(R.id.tvTitPerfil);
        tvCorreoUsuario = vista.findViewById(R.id.tvCorreoUsuario);

        imgPerfil = vista.findViewById(R.id.imgPerfil);
        tvRegion = vista.findViewById(R.id.tvRegionSeleccionada);
        tvNomPerfil = vista.findViewById(R.id.tvNomPerfil);

        TraducirPantalla();
        TraducirSpinner();

        SharedPreferences pref = requireActivity().getSharedPreferences("DatosUsuario", MODE_PRIVATE);
        String email = pref.getString("email", "Sin email"); // Valor por defecto
        String region = pref.getString("region", "No disponible");
        tvCorreoUsuario.setText(email);


        // Recuperar el nombre del perfil desde SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("NombrePerfil", MODE_PRIVATE);
        String nombrePerfil = preferences.getString("PerfilSeleccionado", "Perfil no encontrado");
        int imagenPerfil = preferences.getInt("ImagenPerfil", R.drawable.fernchibi);

        tvNomPerfil.setText(nombrePerfil);
        etNombreUsuario.setText(nombrePerfil);
        imgPerfil.setImageResource(imagenPerfil);
        imgPerfil.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_seleccion_imagenes, null);
            PopupWindow popupWindow = new PopupWindow(popupView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);

            RecyclerView rvSeleccionImagenes = popupView.findViewById(R.id.rvSeleccionImagenes);
            rvSeleccionImagenes.setLayoutManager(new LinearLayoutManager(requireContext()));

            // Creo la lista de imágenes para los diferentes animes
            Map<String, List<Integer>> secciones = new LinkedHashMap<>(); //Ordeno por orden de inserccion en el mapa para que sea más fácil de ver
            secciones.put("One piece", Arrays.asList(R.drawable.luffychibi, R.drawable.zorochibi, R.drawable.namichibi, R.drawable.chopper, R.drawable.robbinchibi, R.drawable.lawchibi, R.drawable.doflamingochibi, R.drawable.corazonchibi));
            secciones.put("Jujutsu Kaisen", Arrays.asList(R.drawable.itadorichibi, R.drawable.nobarachibi, R.drawable.megumichibi, R.drawable.satoruchibi, R.drawable.tojichibi, R.drawable.kaisenchibi, R.drawable.sukunachibi, R.drawable.nanamichibi));
            secciones.put("Frieren", Arrays.asList(R.drawable.frierenchibi, R.drawable.fernchibi, R.drawable.starckchibi, R.drawable.himmelchibi, R.drawable.heiterchibi, R.drawable.eisenchibi));
            secciones.put("Haikyū", Arrays.asList(R.drawable.hinatachibi , R.drawable.tobiochibii, R.drawable.daichichibi, R.drawable.sugawarachibi, R.drawable.azumanechibi, R.drawable.nishinoyachibi, R.drawable.tanakachibi, R.drawable.tsukishimachibi, R.drawable.yamaguchichibi));
            secciones.put("Black Clover", Arrays.asList(R.drawable.astachibi, R.drawable.yunochibi, R.drawable.noellechibi, R.drawable.yamichibi, R.drawable.finralchibi, R.drawable.magnachibi, R.drawable.luckchibi, R.drawable.gauchechibi, R.drawable.vanessachibi, R.drawable.charmychibi, R.drawable.gordonchibi, R.drawable.greychibi));
            secciones.put("Kimetsu No Yaiba", Arrays.asList(R.drawable.tanjirochibi, R.drawable.nezukochibi, R.drawable.zenitsuchibi, R.drawable.inosukechibi, R.drawable.tomiokachibi, R.drawable.shionobuchibi, R.drawable.rengokuchibi, R.drawable.uzuichibi, R.drawable.tokitochibi, R.drawable.himejimachibi, R.drawable.igurochibi, R.drawable.sanemichibi));
            secciones.put("Naruto", Arrays.asList(R.drawable.narutochibi, R.drawable.sakurachibi, R.drawable.sasukechibi, R.drawable.shikamaruchibi, R.drawable.inochibi, R.drawable.akimichichibi, R.drawable.hyugachibi, R.drawable.aburamechibi, R.drawable.kibachibi, R.drawable.nejichibi, R.drawable.rockleechibi, R.drawable.tentenchibi));
            secciones.put("Dragon Ball Z", Arrays.asList(R.drawable.gokuchibi, R.drawable.gohanchibi, R.drawable.trunkschibi, R.drawable.vegetachibi, R.drawable.yamchachibi, R.drawable.tenchibi, R.drawable.kamichibi, R.drawable.androidechibi, R.drawable.cellchibi, R.drawable.gerochibi ));

            AdaptadorFilasImagenes seccionAdapter = new AdaptadorFilasImagenes(requireContext(), secciones, imagenResId -> {
                imgPerfil.setImageResource(imagenResId);
                imgPerfil.setTag(imagenResId);
                popupWindow.dismiss();
            });

            rvSeleccionImagenes.setAdapter(seccionAdapter);

            popupWindow.setElevation(10f);
            popupWindow.showAtLocation(imgPerfil, Gravity.CENTER, 0, 0);
        });


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

                builder.setTitle(tituloCierreSesion)
                        .setIcon(R.drawable.logout);

                builder.setNegativeButton(btnCancelarCierreSesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

                builder.setPositiveButton(btnAceptarCierreSesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        FirebaseAuth.getInstance().signOut();

                        // Eliminar la preferencia de recordar sesión
                        SharedPreferences.Editor editor = requireContext().getSharedPreferences("PreferenciaSesion", MODE_PRIVATE).edit();
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

                builder.setTitle(tituloBorrar)
                        .setIcon(R.drawable.eliminar);


                builder.setNegativeButton(btnCancelarCierreSesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton(btnAceptarCierreSesion, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(requireContext(), "Perfil: " + nombrePerfil + " eliminado.", Toast.LENGTH_LONG).show();
                        EliminarPerfil(nombrePerfil);
                    }
                });

                builder.create();
                builder.show();
            }
        });

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarPerfil(nombrePerfil);
            }
        });

        int posicion = 0;
        for (int i = 0; i < idiomas.length; i++) {
            if (idiomas[i].equals(idioma)) {
                posicion = i;
                break;
            }
        }



        if (posicion >= 0 && posicion < regiones.length) {

            tvRegion.setText(regiones[posicion]);
            Traductor.traducirTexto(tvRegion.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
                @Override
                public void onTextoTraducido(String textoTraducido) {
                    tvRegion.setText(textoTraducido);
                }
            });

        }




        return vista;
    }

    private void setLocale(String idioma) {

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

    }

    private void EliminarPerfil(String nombrePerfil) {
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
                                //Toast.makeText(requireContext(), "Perfil eliminado correctamente", Toast.LENGTH_SHORT).show();
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

    private void CambioPerfil() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() == null) {
            return;  // No hacer nada si no hay usuario autenticado
        }

        String userId = auth.getCurrentUser().getUid();

        // Mostrar un mensaje de carga para indicar que se está procesando la información
        Toast.makeText(requireContext(), cargandoPerfil, Toast.LENGTH_SHORT).show();

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

    private void ActualizarPerfil(String nombrePerfil) {
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
                    // Filtrar la lista para actualizar el perfil seleccionado
                    for (Perfil aux : usuario.getListaPerfiles()) {
                        if (aux.getNombrePerfil().equals(nombrePerfil)) {
                            aux.setNombrePerfil(etNombreUsuario.getText().toString());
                            int imagen = aux.getImagenPerfil(); // Imagen por defecto
                            if (imgPerfil.getTag() != null && (int) imgPerfil.getTag() != aux.getImagenPerfil()) {
                                imagen = (int) imgPerfil.getTag();
                            }
                            aux.setImagenPerfil(imagen);
                        }

                    }

                    // Actualizar en Firestore
                    db.collection("Usuarios").document(userId)
                            .update("listaPerfiles", usuario.getListaPerfiles())
                            .addOnSuccessListener(aVoid -> {
                                // Redirigir a SeleccionPerfil
                                CambioPerfil();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Error al actualizar perfil: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(requireContext(), "Error al obtener usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void TraducirPantalla() {
        /**Traducimos los controles**/

        Traductor.traducirTexto(btnCambioPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnCambioPerfil.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvTitUser.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTitUser.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvTitIdioma.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTitIdioma.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvTitCorreo.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTitCorreo.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(tvTitPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvTitPerfil.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(btnGuardarCambios.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnGuardarCambios.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(btnDesconexion.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnDesconexion.setText(textoTraducido);
            }
        });

        Traductor.traducirTexto(btnEliminarPerfil.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnEliminarPerfil.setText(textoTraducido);
            }
        });
    }

    public void TraducirSpinner() {

        Traductor.traducirTexto(regiones[0], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                regiones[0] = textoTraducido;
            }
        });

        Traductor.traducirTexto(regiones[1], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                regiones[1] = textoTraducido;
            }
        });

        Traductor.traducirTexto(regiones[2], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                regiones[2] = textoTraducido;
            }
        });

    }

    public void TraduccionVariablesAlert() {
        Traductor.traducirTexto(tituloCierreSesion, "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tituloCierreSesion = textoTraducido;
            }
        });
        Traductor.traducirTexto(btnCancelarCierreSesion, "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnCancelarCierreSesion = textoTraducido;
            }
        });
        Traductor.traducirTexto(btnAceptarCierreSesion, "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnAceptarCierreSesion = textoTraducido;
            }
        });
        Traductor.traducirTexto(cargandoPerfil, "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                cargandoPerfil = textoTraducido;
            }
        });
        Traductor.traducirTexto(tituloBorrar, "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tituloBorrar = textoTraducido;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}