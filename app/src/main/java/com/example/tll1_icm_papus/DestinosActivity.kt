package com.example.tll1_icm_papus

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader
import android.content.Intent


class DestinosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destinos)

        val listView: ListView = findViewById(R.id.listViewDestinos)

        val categoria = intent.getStringExtra("categoria") ?: "Todos"

        val inputStream = assets.open("destinos.json")
        val reader = InputStreamReader(inputStream)
        val destinoType = object : TypeToken<List<Destino>>() {}.type
        val destinos: List<Destino> = Gson().fromJson(reader, destinoType)

        // The list is already of type List<Destino>, so we don't need a map
        val destinosFiltrados = if (categoria == "Todos") destinos else destinos.filter { it.categoria == categoria }

        // Use the original list of Destino objects
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            destinosFiltrados
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val destinoSeleccionado = adapter.getItem(position)
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("destino", destinoSeleccionado)
            startActivity(intent)
        }
    }
}