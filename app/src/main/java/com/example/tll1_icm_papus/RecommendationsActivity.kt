package com.example.tll1_icm_papus

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class RecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usamos el mismo layout que la actividad de detalles
        setContentView(R.layout.activity_details)

        // Establece el título de la barra de acción
        supportActionBar?.title = "Destino Recomendado"

        // Referencias a los TextViews del layout de detalles
        val tvNombre: TextView = findViewById(R.id.tvNombre)
        val tvPais: TextView = findViewById(R.id.tvPais)
        val tvCategoria: TextView = findViewById(R.id.tvCategoria)
        val tvActividad: TextView = findViewById(R.id.tvActividad)
        val tvPrecio: TextView = findViewById(R.id.tvPrecio)

        // Ocultamos el botón de favoritos, ya que no es relevante en esta pantalla
        val btnFavorito: Button = findViewById(R.id.btnFavorito)
        btnFavorito.visibility = Button.GONE

        // Obtener la lista de favoritos
        val favoritos = MainActivity.favoritos

        // Si no hay favoritos, mostrar "NA"
        if (favoritos.isEmpty()) {
            tvNombre.text = "NA"
            tvPais.text = ""
            tvCategoria.text = ""
            tvActividad.text = ""
            tvPrecio.text = ""
            return
        }

        // Encontrar la categoría más frecuente
        val categoriaMasFrecuente = favoritos
            .groupBy { it.categoria }
            .maxByOrNull { it.value.size }
            ?.key

        // Si se encuentra una categoría, filtrar y seleccionar un destino aleatorio
        if (categoriaMasFrecuente != null) {
            val destinosDeCategoriaMasFrecuente = favoritos.filter { it.categoria == categoriaMasFrecuente }

            // Elegir un destino aleatorio de esa categoría
            val destinoRecomendado = destinosDeCategoriaMasFrecuente[Random.nextInt(destinosDeCategoriaMasFrecuente.size)]

            // Mostrar la información completa
            tvNombre.text = destinoRecomendado.nombre
            tvPais.text = "País: ${destinoRecomendado.pais}"
            tvCategoria.text = "Categoría: ${destinoRecomendado.categoria}"
            tvActividad.text = "Actividad: ${destinoRecomendado.actividad}"
            tvPrecio.text = "Precio: $${destinoRecomendado.precio_usd} USD"
        } else {
            // Caso de respaldo
            tvNombre.text = "NA"
            tvPais.text = ""
            tvCategoria.text = ""
            tvActividad.text = ""
            tvPrecio.text = ""
        }
    }
}