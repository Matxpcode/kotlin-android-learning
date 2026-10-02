package com.example.recyclerapp.adaptador

import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerapp.databinding.ItemContactoBinding
import com.example.recyclerapp.entidad.Contacto

class ContactoVH(private val binding: ItemContactoBinding): RecyclerView.ViewHolder(binding.root){
    fun completarInformacion(contacto: Contacto){
        binding.lblNombre.text=contacto.nombre
        binding.lblCorreo.text=contacto.correo
    }

}