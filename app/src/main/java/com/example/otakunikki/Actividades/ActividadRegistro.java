package com.example.otakunikki.Actividades;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.Traductor;
import com.example.otakunikki.GestionImagenes.AdaptadorFilasImagenes;
import com.example.otakunikki.Clases.Perfil;
import com.example.otakunikki.Clases.Usuario;
import com.example.otakunikki.Fragmentos.FragmentInfoUsuario;
import com.example.otakunikki.R;

//FIREBASE
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ActividadRegistro extends AppCompatActivity {
    private String[] paises = {"Español (España)", "Inglés", "Japonés", "Francés", "Italiano", "Alemán"};
    private Spinner spnRegion;
    private TextView tvPaisSeleccionado;
    private Button btnConfirmar, btnCancelar;
    private ImageButton imgIconoUser;
    private EditText etNombreCompleto, etNombreUsuario, etEmail, etPwd, etPwdConfirmacion;
    private CheckBox chkTerminos;
    private String idioma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_registro);

        SharedPreferences infoIdioma = getSharedPreferences("Idiomas", MODE_PRIVATE);
        idioma = infoIdioma.getString("idioma", "es");

        /**ATRIBUTOS PARA EL REGISTRO DE USUARIO**/
        imgIconoUser = findViewById(R.id.imgIconoUser);

        etNombreCompleto = findViewById(R.id.etNombreCompleto);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etEmail = findViewById(R.id.etEmail);
        etPwd = findViewById(R.id.etPwd);
        etPwdConfirmacion = findViewById(R.id.etPwdConfirmacion);
        chkTerminos = findViewById(R.id.chkTerminos);


        imgIconoUser = findViewById(R.id.imgIconoUser);

        spnRegion = findViewById(R.id.spnRegion);
        btnConfirmar = findViewById(R.id.btnConfirmar);
        btnCancelar = findViewById(R.id.btnCancelar);
        tvPaisSeleccionado = findViewById(R.id.tvPaisSeleccionado);

        Traductor.traducirTexto(tvPaisSeleccionado.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                tvPaisSeleccionado.setText(textoTraducido);
            }
        });

        TraduccionControles();
        TraducirSpinner();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getApplicationContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, paises);
        spnRegion.setAdapter(adapter);

        spnRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String idioma = "es"; // utilizamos este por defecto
                if (position == 0) {
                    idioma = "es";
                    tvPaisSeleccionado.setText(paises[0]);
                }
                if (position == 1) {
                    idioma = "en";
                    tvPaisSeleccionado.setText(paises[1]);
                }
                if (position == 2) {
                    idioma = "ja";
                    tvPaisSeleccionado.setText(paises[2]);
                }
                if (position == 3) {
                    idioma = "fr";
                    tvPaisSeleccionado.setText(paises[3]);
                }
                if (position == 4) {
                    idioma = "it";
                    tvPaisSeleccionado.setText(paises[4]);
                }
                if (position == 5) {
                    idioma = "de";
                    tvPaisSeleccionado.setText(paises[5]);
                }


                SharedPreferences preferences = getApplicationContext().getSharedPreferences("Idiomas", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("idioma", idioma);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /**OBJETOS PARA EL USO DE FIREBASE**/
        FirebaseAuth authRegistro = FirebaseAuth.getInstance();
        FirebaseFirestore baseDatos = FirebaseFirestore.getInstance();
        /***************************************/

        /**LOGICA PARA SELECCIONAR LA FOTO**/
        imgIconoUser.setOnClickListener(v -> {
            View popupView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_seleccion_imagenes, null);
            PopupWindow popupWindow = new PopupWindow(popupView, RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT, true);

            RecyclerView rvSeleccionImagenes = popupView.findViewById(R.id.rvSeleccionImagenes);
            rvSeleccionImagenes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

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

            AdaptadorFilasImagenes seccionAdapter = new AdaptadorFilasImagenes(getApplicationContext(), secciones, imagenResId -> {
                imgIconoUser.setImageResource(imagenResId);
                imgIconoUser.setTag(imagenResId);
                popupWindow.dismiss();
            });

            rvSeleccionImagenes.setAdapter(seccionAdapter);

            popupWindow.setElevation(10f);
            popupWindow.showAtLocation(imgIconoUser, Gravity.CENTER, 0, 0);
        });


        /***************************************/
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Antes de guardar el usuario almaceno el idioma que guardé con shared preferences*/

                String idioma = getApplicationContext().getSharedPreferences("Idiomas", MODE_PRIVATE).getString("idioma", "es");

                /***************************************************************/
                String email = etEmail.getText().toString();
                String nombreCompleto = etNombreCompleto.getText().toString();
                String nombreUsuario = etNombreUsuario.getText().toString();
                String pwd = etPwd.getText().toString();
                String pwdConfirmada = etPwdConfirmacion.getText().toString();

                // Verificar que todos los campos estén completos
                if (nombreCompleto.isEmpty() || nombreUsuario.isEmpty() || email.isEmpty() || pwd.isEmpty() || pwdConfirmada.isEmpty() || !chkTerminos.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_LONG).show();
                    return; // Detener la ejecución si algún campo está vacío
                }

                // Verificar que el correo electrónico tenga un formato válido
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Formato email no valido", Toast.LENGTH_LONG).show();
                    return; // Detener la ejecución si el formato del correo electrónico es incorrecto
                }

                // Verificar que las contraseñas coincidan
                if (!pwd.equals(pwdConfirmada)) {
                    Toast.makeText(getApplicationContext(), "Contraseñas no coinciden", Toast.LENGTH_LONG).show();
                    return;
                }

                // Verificar que la contraseña tenga al menos 6 caracteres
                if (pwd.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Minimo 6 caracteres en la contraseña", Toast.LENGTH_LONG).show();
                    return;
                }

                // Crear el usuario en Firebase
                authRegistro.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser firebaseUser = authRegistro.getCurrentUser();
                                if (firebaseUser != null) {
                                    /**ID QUE GENERA FIREBASE**/
                                    String userId = firebaseUser.getUid();
                                    List<Perfil> userProfiles = new ArrayList<>();

                                    if (imgIconoUser.getTag()==null) {
                                        userProfiles.add(new Perfil(nombreUsuario + "_Perfil1", R.drawable.luffychibi));
                                    } else {
                                        int imagen = (int) imgIconoUser.getTag();
                                        userProfiles.add(new Perfil(nombreUsuario + "_Perfil1", imagen));
                                    }

                                    // Crear el objeto de usuario
                                    Usuario newUser = new Usuario(userId, nombreCompleto, nombreUsuario, email, idioma, userProfiles);

                                    // Guardar el usuario en Firestore
                                    baseDatos.collection("Usuarios").document(userId)
                                            .set(newUser)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("Firebase", "Usuario creado correctamente");
                                                Log.d("Firebase", "Usuario creado con idioma: " + idioma);
                                                abrirSeleccion(newUser); // Abrir la siguiente actividad después de guardar el usuario
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("Firebase", "Error guardando al usuario: " + e.getMessage());
                                                Toast.makeText(getApplicationContext(), "Error al guardar intentelo de nuevo", Toast.LENGTH_LONG).show();
                                            });
                                }
                            } else {
                                Exception exception = task.getException();
                                if (exception instanceof FirebaseAuthWeakPasswordException) {
                                    Toast.makeText(getApplicationContext(), "Contraseña debil", Toast.LENGTH_LONG).show();
                                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(getApplicationContext(), "Formato mail no valido", Toast.LENGTH_LONG).show();
                                } else if (exception instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "Email ya en uso", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Fallo de autentificacion: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void abrirSeleccion(Usuario user) {
        Intent intent = new Intent(getApplicationContext(), SeleccionPerfil.class);
        intent.putExtra("Usuario", user);
        startActivity(intent);
    }
    public void TraducirSpinner(){
        Traductor.traducirTexto(paises[0], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[0] = textoTraducido;

            }
        });
        Traductor.traducirTexto(paises[1], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[1] = textoTraducido;

            }
        });
        Traductor.traducirTexto(paises[2], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[2] = textoTraducido;

            }
        });
        Traductor.traducirTexto(paises[3], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[3] = textoTraducido;

            }
        });
        Traductor.traducirTexto(paises[4], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[4] = textoTraducido;

            }
        });
        Traductor.traducirTexto(paises[5], "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                paises[5] = textoTraducido;

            }
        });
    }

    private void TraduccionControles() {
        Traductor.traducirTexto(etNombreCompleto.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etNombreCompleto.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(etNombreUsuario.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etNombreUsuario.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(etEmail.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etEmail.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(etPwd.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etPwd.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(etPwdConfirmacion.getHint().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                etPwdConfirmacion.setHint(textoTraducido);
            }
        });
        Traductor.traducirTexto(chkTerminos.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                chkTerminos.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(btnConfirmar.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnConfirmar.setText(textoTraducido);
            }
        });
        Traductor.traducirTexto(btnCancelar.getText().toString(), "es", idioma, new Traductor.TraduccionCallback() {
            @Override
            public void onTextoTraducido(String textoTraducido) {
                btnCancelar.setText(textoTraducido);
            }
        });

    }

}
