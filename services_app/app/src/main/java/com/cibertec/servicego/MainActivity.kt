package com.cibertec.servicego

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cibertec.servicego.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding permite conectar el activity_main.xml completo con Kotlin
    // sin usar findViewById. En clase se programará la lógica del Checkpoint 01.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Punto de inicio de la clase:
        // aquí se agregarán las llamadas a las funciones del Checkpoint 01.
    }
}
