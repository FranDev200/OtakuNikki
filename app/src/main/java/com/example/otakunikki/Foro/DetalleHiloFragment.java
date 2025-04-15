package com.example.otakunikki.Foro;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otakunikki.Clases.HiloForo;
import com.example.otakunikki.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DetalleHiloFragment extends Fragment {

    private TextView tvTitulo, tvUsuario, tvFecha, tvComentario;
    private RecyclerView rvRespuestas;
    private Button btnResponder;

    private HiloForo hiloPrincipal;
    private FirebaseFirestore db;
    private List<HiloForo> listaRespuestas;
    private AdaptadorRespuestas adaptador;

    public static DetalleHiloFragment nuevaInstancia(HiloForo hilo) {

        DetalleHiloFragment fragment = new DetalleHiloFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("hiloPrincipal", hilo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalle_hilo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View vista, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(vista, savedInstanceState);

        ImageButton imgRetroceso = vista.findViewById(R.id.imgRetroceso);
        imgRetroceso.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        tvTitulo = vista.findViewById(R.id.tvTituloHiloDetalle);
        tvUsuario = vista.findViewById(R.id.tvUsuarioHiloDetalle);
        tvFecha = vista.findViewById(R.id.tvFechaHiloDetalle);
        tvComentario = vista.findViewById(R.id.tvComentarioHiloDetalle);
        rvRespuestas = vista.findViewById(R.id.rvRespuestas);
        btnResponder = vista.findViewById(R.id.btnResponder);

        db = FirebaseFirestore.getInstance();
        listaRespuestas = new ArrayList<>();
        adaptador = new AdaptadorRespuestas(listaRespuestas);
        rvRespuestas.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRespuestas.setAdapter(adaptador);

        // Obtener hilo principal desde argumentos
        if (getArguments() != null) {
            hiloPrincipal = getArguments().getParcelable("hiloPrincipal");
            if (hiloPrincipal != null) {
                mostrarHiloPrincipal();
                cargarRespuestas();
            }
        }

        btnResponder.setOnClickListener(v -> mostrarDialogoRespuesta());
    }

    private void mostrarHiloPrincipal() {
        tvTitulo.setText(hiloPrincipal.getTitulo());
        tvUsuario.setText(hiloPrincipal.getUsuario());
        tvFecha.setText(convertirTimestampAFecha(hiloPrincipal.getFecha()));
        tvComentario.setText(hiloPrincipal.getComentario());
    }

    private void cargarRespuestas() {
        // Asumimos que cada hilo está guardado en una colección "hilos" en este caso son las respuesras de los usuaris
        // y las respuestas están en una subcolección "respuestas"
        db.collection("hilos")
                .whereEqualTo("titulo", hiloPrincipal.getTitulo()) // o buscar por ID si se guarda
                .get()
                .addOnSuccessListener(docs -> {
                    if (!docs.isEmpty()) {
                        String hiloId = docs.getDocuments().get(0).getId();
                        db.collection("hilos").document(hiloId)
                                .collection("respuestas")
                                .orderBy("fecha", Query.Direction.ASCENDING)
                                .get()
                                .addOnSuccessListener(resps -> {
                                    listaRespuestas.clear();
                                    for (DocumentSnapshot doc : resps) {
                                        HiloForo respuesta = doc.toObject(HiloForo.class);
                                        listaRespuestas.add(respuesta);
                                    }
                                    adaptador.notifyDataSetChanged();
                                });
                    }
                });
    }

    private void mostrarDialogoRespuesta() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.item_ad_agregar_hilo, null);
        TextView etTitulo = dialogView.findViewById(R.id.etTituloHilo);
        TextView etMensaje = dialogView.findViewById(R.id.etMensajeForo);
        Button btnAceptar = dialogView.findViewById(R.id.btnPublicarHilo);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarPublicacion);

        etTitulo.setVisibility(View.GONE); // no es necesario para respuestas

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(dialogView);
        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        btnAceptar.setOnClickListener(v -> {
            String comentario = etMensaje.getText().toString();
            if (!comentario.isEmpty()) {
                agregarRespuesta(comentario);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Comentario vacío", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
    }

    private void agregarRespuesta(String comentario) {
        HiloForo nuevaRespuesta = new HiloForo("Usuario", "", com.google.firebase.Timestamp.now(), comentario);
        db.collection("hilos")
                .whereEqualTo("titulo", hiloPrincipal.getTitulo()) // igual, mejor buscar por ID
                .get()
                .addOnSuccessListener(docs -> {
                    if (!docs.isEmpty()) {
                        String hiloId = docs.getDocuments().get(0).getId();
                        db.collection("hilos")
                                .document(hiloId)
                                .collection("respuestas")
                                .add(nuevaRespuesta)
                                .addOnSuccessListener(docRef -> {
                                    listaRespuestas.add(nuevaRespuesta);
                                    adaptador.notifyItemInserted(listaRespuestas.size() - 1);
                                });
                    }
                });
    }

    private String convertirTimestampAFecha(com.google.firebase.Timestamp timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp.toDate());
    }
}
