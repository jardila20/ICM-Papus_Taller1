package com.example.tll1_icm_papus

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class DetailsActivity : AppCompatActivity() {

    private val API_KEY = "7c03fc9a6c59f3845dc95f706ce244b6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val destino = intent.getParcelableExtra<Destino>("destino")

        val tvNombre: TextView = findViewById(R.id.tvNombre)
        val tvPais: TextView = findViewById(R.id.tvPais)
        val tvCategoria: TextView = findViewById(R.id.tvCategoria)
        val tvActividad: TextView = findViewById(R.id.tvActividad)
        val tvPrecio: TextView = findViewById(R.id.tvPrecio)
        val btnFavorito: Button = findViewById(R.id.btnFavorito)
        val tvClima: TextView = findViewById(R.id.tvClima)

        destino?.let {
            tvNombre.text = it.nombre
            tvPais.text = "País: ${it.pais}"
            tvCategoria.text = "Categoría: ${it.categoria}"
            tvActividad.text = "Actividad: ${it.actividad}"
            tvPrecio.text = "Precio: $${it.precio_usd} USD"

            obtenerCoordenadasYClima(it.nombre, tvClima)
        }

        btnFavorito.setOnClickListener {
            if (destino != null && !MainActivity.favoritos.contains(destino)) {
                MainActivity.favoritos.add(destino)
                Toast.makeText(this, "Añadido a favoritos", Toast.LENGTH_SHORT).show()
                btnFavorito.isEnabled = false
            } else if (destino != null) {
                Toast.makeText(this, "Ya está en favoritos", Toast.LENGTH_SHORT).show()
                btnFavorito.isEnabled = false
            }
        }
    }

    private fun obtenerCoordenadasYClima(ciudad: String, tvClima: TextView) {
        val cliente = OkHttpClient()
        val ciudadCodificada = URLEncoder.encode(ciudad, StandardCharsets.UTF_8.toString())

        // **CORRECCIÓN:** Se cambió 'http' por 'https'
        val urlGeocoding = "https://api.openweathermap.org/geo/1.0/direct?q=$ciudadCodificada&limit=1&appid=$API_KEY"

        val solicitudGeocoding = Request.Builder()
            .url(urlGeocoding)
            .build()

        cliente.newCall(solicitudGeocoding).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread { tvClima.text = "Error de red: ${e.message}" }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val respuestaJson = response.body?.string()

                    if (respuestaJson.isNullOrEmpty() || respuestaJson == "[]") {
                        runOnUiThread { tvClima.text = "Coordenadas no encontradas para $ciudad" }
                        return
                    }

                    try {
                        val jsonArray = JSONArray(respuestaJson)
                        if (jsonArray.length() > 0) {
                            val jsonObject = jsonArray.getJSONObject(0)
                            val lat = jsonObject.getDouble("lat")
                            val lon = jsonObject.getDouble("lon")

                            obtenerClima(lat, lon, tvClima)
                        } else {
                            runOnUiThread { tvClima.text = "Coordenadas no encontradas para $ciudad" }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread { tvClima.text = "Error al procesar la respuesta de coordenadas" }
                    }
                } else {
                    runOnUiThread { tvClima.text = "Error en la respuesta del servidor: ${response.code}" }
                }
            }
        })
    }

    private fun obtenerClima(lat: Double, lon: Double, tvClima: TextView) {
        val cliente = OkHttpClient()
        // **CORRECCIÓN:** Se cambió 'http' por 'https'
        val urlClima = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$API_KEY&units=metric&lang=es"

        val solicitudClima = Request.Builder()
            .url(urlClima)
            .build()

        cliente.newCall(solicitudClima).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread { tvClima.text = "Error de red al obtener el clima" }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val respuestaJson = response.body?.string()
                    if (respuestaJson != null) {
                        try {
                            val jsonObject = JSONObject(respuestaJson)
                            val main = jsonObject.getJSONObject("main")
                            val temp = main.getDouble("temp").toInt()
                            val weatherArray = jsonObject.getJSONArray("weather")
                            val weather = weatherArray.getJSONObject(0)
                            val description = weather.getString("description")

                            val textoClima = "Clima: $temp°C, $description"

                            runOnUiThread { tvClima.text = textoClima }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread { tvClima.text = "Datos de clima no disponibles" }
                        }
                    }
                } else {
                    runOnUiThread { tvClima.text = "Clima no encontrado" }
                }
            }
        })
    }
}