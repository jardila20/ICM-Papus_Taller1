// Destino.kt

package com.example.tll1_icm_papus
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destino(
    val nombre: String,
    val pais: String,
    val categoria: String,
    val actividad: String,
    val precio_usd: Int
) : Parcelable {
    override fun toString(): String {
        return nombre
    }
}