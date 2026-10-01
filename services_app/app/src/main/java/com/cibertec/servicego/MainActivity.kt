package com.cibertec.servicego

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.cibertec.servicego.databinding.ActivityMainBinding
import org.w3c.dom.Document

class MainActivity : AppCompatActivity() {

    // ViewBinding permite conectar el activity_main.xml completo con Kotlin
    // sin usar findViewById.
    /*
    Info personal:
     binding -> accede a las vistas xml, almacena referencias a objetos vivos.
     > genera una clase por cada layout xml, esta clase contiene referencias directas tipadas y null-safe a todas las vistas q tienen un id en ese layout
     > resumen: es un objeto, q tras 'inflar un layout' da acceso directo a cada vistas de ese layout, sin buscarla por id manualmente
    */

    //1.declaramos la instancia del binding para acceder a los objetos obtenidos del layout xml
    //> lateinit: 'inicializalo despues', para que no marque error por la falta de inicializar
    private lateinit var binding: ActivityMainBinding

    private var nextCode = 1

    //2.metodo principal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)  //gestor de estado de la app

        //3.configuracion del binding
        binding = ActivityMainBinding.inflate(layoutInflater)   //transformamos el xml a objetos en memoria
        setContentView(binding.root)    //mostramos estos objetos en pantalla

        //4.metodos a implementar dentro de onCreate
        mostrarEstadoInicial()
        configurarEventosDeTexto()

        //5.designamos el evento 'click' a la vista(objeto) 'buttonRegistrarServicio'
        binding.buttonRegistrarServicio.setOnClickListener {
            registrarServicio()
        }

    }

    //6.metodo para mostrar todas las vistas en estado inicial
    private fun mostrarEstadoInicial(){
        //getString(id_generado_al_buscar_en_xml) -> el metodo buscara el id(valor numerico por defecto) en string.xml y traera su valor en formato text
        binding.textViewResumenTitulo.text=getString(R.string.resumen_inicial_titulo)
        binding.textViewResumenCliente.text=getString(R.string.resumen_cliente_placeholder)
        binding.textViewResumenDescripcion.text=getString(R.string.resumen_descripcion_placeholder)
        binding.textViewResumenDireccion.text=getString(R.string.resumen_direccion_placeholder)
        binding.textViewEstadoRegistro.text=getString(R.string.estado_pendiente)
        binding.textViewMensajeVisible.text=getString(R.string.mensaje_inicial)
        binding.textViewCodigoPreliminar.text=getString(R.string.codigo_preliminar_formato,generarCodigoCorrelativo())
        binding.textViewCostoEstimado.text=getString(R.string.costo_estimado_formato,getString(R.string.costo_base_inicial))
        binding.textViewIndicadoresTecnicos.text=getString(R.string.indicadores_iniciales)
        binding.buttonRegistrarServicio.isEnabled=false
    }

    //7.metodo para configurar eventos del texto
    private fun configurarEventosDeTexto(){
        binding.editTextCliente.doAfterTextChanged {
            binding.editTextCliente.error=null
            actualizarIndicadoresBasicos()
        }

        binding.editTextDescripcion.doAfterTextChanged {
            binding.editTextDescripcion.error=null
            actualizarIndicadoresBasicos()
        }

        binding.editTextDireccion.doAfterTextChanged {
            binding.editTextDireccion.error=null
            actualizarIndicadoresBasicos()
        }
    }

    //8.metodo para actualizar indicadores basicos
    private fun actualizarIndicadoresBasicos(){
        //recopilamos la data de los inputs
        val cliente = binding.editTextCliente.text.toString().trim()
        val descripcion = binding.editTextDescripcion.text.toString().trim()
        val direccion = binding.editTextDireccion.text.toString().trim()

        //validamos los inputs
        val clienteValido = cliente.isNotBlank()
        val descripcionValida = descripcion.length >= 0
        val direccionValida = direccion.isNotBlank()

        //validacion total de los inputs
        val listoParaRegistrar = clienteValido && descripcionValida && direccionValida

        binding.textViewCodigoPreliminar.text=getString(R.string.codigo_preliminar_formato,generarCodigoCorrelativo())

        if (descripcion.isBlank()||direccion.isBlank()){
            binding.textViewCostoEstimado.text=getString(R.string.costo_estimado_formato,getString(R.string.costo_base_inicial))
            binding.textViewIndicadoresTecnicos.text=getString(R.string.indicadores_iniciales)
            binding.textViewMensajeVisible.text = getString(R.string.mensaje_inicial)
        }else{
            //la descripcion se clasifica primero, y luego se lo usa para los demas
            val tipoServicio = obtenerTipoServicio(descripcion)
            val modalidad = obtenerModalidad(descripcion)
            val costoEstimado = calcularCostoEstimado(tipoServicio,modalidad)
            val tiempoEstimado = calcularTiempoEstimado(tipoServicio,modalidad)

            binding.textViewCostoEstimado.text=getString(R.string.costo_estimado_formato,"S/ ${"%.2f".format(costoEstimado)}")
            binding.textViewIndicadoresTecnicos.text=
                getString(
                    R.string.indicadores_operativos_formato,
                    "%.1f".format(tiempoEstimado),
                    tipoServicio,
                    modalidad
                )
            binding.textViewMensajeVisible.text=getString(R.string.mensaje_estimacion_previa,tipoServicio.lowercase())
        }

        binding.buttonRegistrarServicio.isEnabled=listoParaRegistrar

        if (listoParaRegistrar){
            binding.textViewEstadoRegistro.text=getString(R.string.estado_listo)
        }else{
            binding.textViewEstadoRegistro.text=getString(R.string.estado_pendiente)
        }
    }

    private fun generarCodigoCorrelativo(): String{
        return "SG-%04d".format(nextCode)
    }

    private fun contieneAlgunTermino(texto: String, terminos: List<String>): Boolean{
        return terminos.any{termino->termino in texto}
    }

    private fun obtenerTipoServicio(descripcion:String): String{
        //la descripcion se normaliza a minusculas para comparar palabras clave sin depender de may/min
        val descripcionNormalizada = descripcion.lowercase()
        val atiendeRed = contieneAlgunTermino(
            descripcionNormalizada,
            listOf("red","wifi","router","internet")
        )

        val atiendeImpresora = contieneAlgunTermino(
            descripcionNormalizada,
            listOf("impresora","toner","tinta","escaner")
        )

        val requiereInstalacion = contieneAlgunTermino(
            descripcionNormalizada,
            listOf("instalacion","instalar","configurar","montaje")
        )

        return when{
            atiendeRed && atiendeImpresora -> getString(R.string.tipo_servicio_mixto)
            atiendeRed -> getString(R.string.tipo_servicio_red)
            atiendeImpresora -> getString(R.string.tipo_servicio_impresion)
            requiereInstalacion -> getString(R.string.tipo_servicio_instalacion)
            else -> getString(R.string.tipo_servicio_diagnostico)
        }
    }

    private fun obtenerModalidad(descripcion: String): String{
        //la modalidad pasa a prioritaria cuando la descripcion contiene terminos de urgencia
        val descripcionNormalizada = descripcion.lowercase()
        val servicioUrgente = contieneAlgunTermino(
            descripcionNormalizada,
            listOf("urgente","caido","sin servicio","no enciende")
        )

        return if (servicioUrgente){
            getString(R.string.modalidad_prioritaria)
        }else{
            getString(R.string.modalidad_programada)
        }
    }

    private fun calcularCostoEstimado(tipoServicio:String,modalidad:String):Double{
        //El costo cambia segun el tipo de trabajo identificado.
        //La modalidad prioritaria agrega un recargo fijo
        val costoBase = when(tipoServicio){
            getString(R.string.tipo_servicio_red) ->70.0
            getString(R.string.tipo_servicio_impresion) ->60.0
            getString(R.string.tipo_servicio_instalacion) ->90.0
            getString(R.string.tipo_servicio_mixto) ->110.0
            else -> 45.0
        }
        val recargoPrioridad = if (modalidad==getString(R.string.modalidad_prioritaria))25.0 else 0.0

        return costoBase+recargoPrioridad
    }

    private fun calcularTiempoEstimado(tipoServicio: String,modalidad: String): Double{
        //El tiempo base sigue la misma clasificacion del costo para mantener coherencia entre esfuerzo tecnico y estimacion mostrada
        val tiempoBase = when(tipoServicio){
            getString(R.string.tipo_servicio_red) -> 2.0
            getString(R.string.tipo_servicio_impresion) ->1.5
            getString(R.string.tipo_servicio_instalacion) ->3.0
            getString(R.string.tipo_servicio_mixto) ->3.5
            else -> 1.0
        }
        val recargoPrioridad = if(modalidad==getString(R.string.modalidad_prioritaria))0.5 else 0.0
        return tiempoBase+recargoPrioridad
    }
}
