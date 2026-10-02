package com.example.recyclerapp.adaptador

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerapp.databinding.ItemContactoBinding
import com.example.recyclerapp.entidad.Contacto

class ContactoAdapter(val lista: List<Contacto>, val click: (Contacto)-> Unit): RecyclerView.Adapter<ContactoVH>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactoVH {
        //es para crear el objeto que hace referencia al layout (view holder)
        val binding = ItemContactoBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ContactoVH(binding)
    }

    override fun onBindViewHolder(
        holder: ContactoVH,
        position: Int
    ) {
        val unContacto = lista[position]
        holder.completarInformacion(unContacto)

        //añadimos evento click en el contacto
        holder.itemView.setOnClickListener {
            Log.i("ADA", "CLICK EN: ${unContacto.nombre}")
            click(unContacto)
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}