package com.example.recyclerapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recyclerapp.adaptador.ContactoAdapter
import com.example.recyclerapp.databinding.ActivityMainBinding
import com.example.recyclerapp.entidad.Contacto

class MainActivity : AppCompatActivity() {
   //PASO 1: declaramos el binding
    private lateinit var binding: ActivityMainBinding

    //metodo principal de inicializacion para preparar recursos, inflar vistas, y manejo de logica inicial
    //su parametro guarda informacion del estado anterior: savedInstanceState
    override fun onCreate(savedInstanceState: Bundle?) {
        //primer comando antes de cualquier otro
        //da aviso al S.O para la construccion de la actividad(gestiona sus recursos)
        //y maneja los estados (ejem. en q forma estuvo anteriormente vertical/horiz, evitar perdida de estos datos)
        super.onCreate(savedInstanceState)

        //llamamos metodo de configuracion
        setupBindingAndWindow()


    }

    //metodo de configuracion de binding y ventana
    private fun setupBindingAndWindow(){
        //PASO 2: cargamos el binding
        //proceso de transformacion de texto xml a una estructura de objetos controlables en memoria
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)    //renderiza esa estructura de objetos en la pantalla del usuario

        binding.rvContacto.adapter = ContactoAdapter(listaContactos()){
            contacto ->
            Log.i("ACT", "CLICK EN CONTACTO: ${contacto.nombre}")
        }


        //comando de configuracion de diseño por default para pantallas modernas
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun listaContactos(): List<Contacto>{
        return listOf(
            Contacto("Luis Ventura","plventur@cibertec.edu.pe"),
            Contacto("Rick Sanchez","ricksanchez@cibertec.edu.pe"),
            Contacto("Morty Smith","mortysmith@cibertec.edu.pe"),
            Contacto("Summer Smith","summersmith@cibertec.edu.pe"),
            Contacto("Pepito Estrada","pepitoestrada@cibertec.edu.pe"),
            Contacto("Rick Sanchez","ricksanchez@cibertec.edu.pe"),
            Contacto("Morty Smith","mortysmith@cibertec.edu.pe"),
            Contacto("Summer Smith","summersmith@cibertec.edu.pe"),
            Contacto("Pepito Estrada","pepitoestrada@cibertec.edu.pe"),
            Contacto("Luis Ventura","plventur@cibertec.edu.pe"),
            Contacto("Luis Ventura","plventur@cibertec.edu.pe"),
            Contacto("Luis Ventura","plventur@cibertec.edu.pe"),
            Contacto("Luis Ventura","plventur@cibertec.edu.pe"),
            Contacto("Luis Ventura","plventur@cibertec.edu.pe")
        )
    }

}