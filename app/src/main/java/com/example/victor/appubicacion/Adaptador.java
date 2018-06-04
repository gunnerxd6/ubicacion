package com.example.victor.appubicacion;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.UsuariosViewHolder>{
    List<UserInformation> usuarios;
    Context ctx ;

    public Adaptador(List<UserInformation> usuarios, Context ctx) {
        this.usuarios = usuarios;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recycler,parent,false);
        UsuariosViewHolder holder = new UsuariosViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        final UserInformation user = usuarios.get(position);
        holder.usuario.setText(user.getName());
        holder.ubicacion.setText(user.getUbicacion());
        holder.comparte.setText(String.valueOf(user.isCompartir_ubicacion()));
        holder.items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String coordinates = "http://maps.google.com/maps?daddr=" + user.latitud + "," + user.getLongitud();

                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(coordinates) );
                ctx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class UsuariosViewHolder extends RecyclerView.ViewHolder{

        TextView usuario,ubicacion,comparte;
        ConstraintLayout items;
        Context ctx;

        public UsuariosViewHolder(View itemView) {
            super(itemView);
            usuario = itemView.findViewById(R.id.tv_rv_usuario);
            ubicacion = itemView.findViewById(R.id.tv_rv_ubicacion);
            comparte = itemView.findViewById(R.id.tv_rv_comparte);
            items = itemView.findViewById(R.id.items_lista);


        }
    }
}
