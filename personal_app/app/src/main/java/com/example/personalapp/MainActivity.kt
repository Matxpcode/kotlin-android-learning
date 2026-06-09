package com.example.personalapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.personalapp.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    //paso1: declaramos "binding" global para interactuar con views component
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //se deja primero super.onCreate para carga completa de propiedades de layout
        super.onCreate(savedInstanceState)

        //paso2: inicializamos el binding para convertir de xml -> view object, e indicamos la ruta principal
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //usamos el metodo para q inicie la app con los datos
        cargarDistrito()

        binding.btnImprimir.setOnClickListener {
            //declaramos las variables error para que se eliminen cuando no este vacio
            binding.txtNombreCompleto.error = null
            binding.txtDistrito.error=null

            //recopilamos la data de los inputs nombre, distrito y estadoCivil
            val nombre = binding.txtNombreCompleto.editText?.text.toString()
            //Validamos nombre
            if (nombre.isEmpty()){
                binding.txtNombreCompleto.error="Valor requerido"
                return@setOnClickListener
            }

            val distrito = binding.txtDistrito.editText?.text.toString()
            //Validamos distrito
            if (distrito.isEmpty()){
                binding.txtDistrito.error="Seleccione valor"
                return@setOnClickListener
            }

            //verificamos cual de los check se selecciono
            val estadoCivil = when(binding.rgEstadoCivil.checkedRadioButtonId){
                binding.rbSoltero.id ->"Soltero"
                binding.rbCasado.id ->"Casado"
                binding.rbDivorciado.id ->"Divorciado"
                binding.rbViudo.id -> "Viudo"
                else -> ""
            }
            //Validamos estadoCivil
            if (estadoCivil.isEmpty()){
                //aqui no se puede mostrar mensaje de texto rojo como en los input, usaremos "dialog"
                MaterialAlertDialogBuilder(this)
                    .setTitle("Advertencia")
                    .setMessage("Debe seleccionar un estado civil para continuar")
                    .show()

                return@setOnClickListener
            }

            //Antes de enviar los datos al otro Activity, añadimos un mensaje de confirmacion
            MaterialAlertDialogBuilder(this)
                .setTitle("Confirmacion")
                .setMessage("¿Desea enviar los datos?")
                .setPositiveButton("Aceptar"){_,_ ->
                    //Pasamos los datos a una nueva "Activity" llamada "DetalleActiviy" usando "Intent"
                    val next = Intent(this, DetalleActivity::class.java)
                    next.putExtra("nombre",nombre)
                    next.putExtra("distrito",distrito)
                    next.putExtra("estado_civil",estadoCivil)
                    startActivity(next)
                }
                .setNeutralButton("Cancelar"){dialog,_ ->
                    //Cerramos manualmente
                    dialog.dismiss()

                    //Reseteamos los campos
                    binding.txtNombreCompleto.editText?.text?.clear()
                    binding.txtDistrito.editText?.text?.clear()

                    //Quitamos mensajes de error por si quedan visibles
                    binding.txtNombreCompleto.error=null
                    binding.txtDistrito.error=null

                    //Limpiamos estadoCivil
                    binding.rgEstadoCivil.clearCheck()
                }
                .show()
        }
    }

    fun cargarDistrito(){
        val items = listOf("Surco", "La Molina", "Miraflores", "San Luis")
        val adapter = ArrayAdapter(this, R.layout.item_combo, items)
        (binding.txtDistrito.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}