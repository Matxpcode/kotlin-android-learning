package com.example.personalapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalapp.databinding.ActivityDetalleBinding
import com.example.personalapp.databinding.ActivityMainBinding

class DetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = intent.extras
        if (extras!=null){
            val nombre = extras.getString("nombre")
            val distrito = extras.getString("distrito")
            val estadoCivil = extras.getString("estado_civil")

            binding.lblDetalle.text="""
                Nombre Completo: $nombre
                Distrito: $distrito
                Estado Civil: $estadoCivil
            """.trimIndent()
        }
    }
}