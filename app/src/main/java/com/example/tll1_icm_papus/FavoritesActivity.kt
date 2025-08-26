package com.example.tll1_icm_papus

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        val listView: ListView = findViewById(R.id.listViewFavoritos)

        // Obtener la lista de favoritos del companion object de MainActivity
        val favoritos = MainActivity.favoritos

        // Crear una lista de nombres para mostrar en el ListView
        val nombresFavoritos = favoritos.map { it.nombre }

        // Configurar el adaptador para el ListView
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            nombresFavoritos
        )

        listView.adapter = adapter
    }
}