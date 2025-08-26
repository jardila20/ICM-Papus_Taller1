package com.example.tll1_icm_papus

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.tll1_icm_papus.databinding.ActivityMainBinding
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//ignorar: prueba de sync con git
    //si sirviooo jajaja

    companion object {
        val favoritos = mutableListOf<Destino>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inflar el layout con viewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar spinner
        val categorias = listOf(
            "Todos",
            "Playas",
            "Montañas",
            "Ciudades Históricas",
            "Maravillas del Mundo",
            "Selvas"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategorias.adapter = adapter

        // listeners de los botones
        binding.btnExplorar.setOnClickListener {
            binding.btnExplorar.setOnClickListener {
                val categoriaSeleccionada = binding.spCategorias.selectedItem.toString()
                val intent = Intent(this, DestinosActivity::class.java)
                intent.putExtra("categoria", categoriaSeleccionada)
                startActivity(intent)
            }

        }
        binding.btnFavoritos.setOnClickListener {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }

        binding.btnRecomendaciones.setOnClickListener {
            val intent = Intent(this, RecommendationsActivity::class.java)
            startActivity(intent)
        }
    }
}

