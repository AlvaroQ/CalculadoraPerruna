package com.alvaroquintana.edadperruna.wear.sync

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Cold Flow que emite la raza favorita sincronizada desde el fone via DataClient.
 *
 * - `null` significa "el fone aun no publico una raza" (estado inicial en reloj vacio).
 * - Cada cambio en el DataItem `/favorite_breed` produce una emision nueva.
 * - Cancelacion del flow retira el listener — no hay leak.
 *
 * Matices a tener en cuenta (estado del arte 2025-2026):
 *  - `DataClient` reintenta sync automaticamente si el emparejamiento del reloj se
 *    pierde. No hace falta manejarlo aqui.
 *  - Si se instala solo el APK del reloj sin el fone, el listener simplemente no
 *    emite — lo cual es aceptable para el MVP (UI del reloj muestra solo el picker).
 *  - El path debe coincidir exactamente con el publicado por `:app`: `/favorite_breed`.
 */
data class FavoriteBreed(
    val name: String,
    val imageUrl: String,
    val avgLifeYears: Int,
)

internal const val FAVORITE_BREED_PATH = "/favorite_breed"
private const val KEY_NAME = "name"
private const val KEY_IMAGE_URL = "image_url"
private const val KEY_AVG_LIFE_YEARS = "avg_life_years"

fun Context.favoriteBreedFlow(): Flow<FavoriteBreed?> = callbackFlow {
    val listener = DataClient.OnDataChangedListener { events ->
        events.forEach { event ->
            when (event.type) {
                DataEvent.TYPE_CHANGED -> {
                    if (event.dataItem.uri.path == FAVORITE_BREED_PATH) {
                        val map = DataMapItem.fromDataItem(event.dataItem).dataMap
                        trySend(
                            FavoriteBreed(
                                name = map.getString(KEY_NAME).orEmpty(),
                                imageUrl = map.getString(KEY_IMAGE_URL).orEmpty(),
                                avgLifeYears = map.getInt(KEY_AVG_LIFE_YEARS, 12),
                            )
                        )
                    }
                }
                DataEvent.TYPE_DELETED -> {
                    if (event.dataItem.uri.path == FAVORITE_BREED_PATH) {
                        trySend(null)
                    }
                }
            }
        }
    }
    val dataClient = Wearable.getDataClient(this@favoriteBreedFlow)
    dataClient.addListener(listener)
    awaitClose { dataClient.removeListener(listener) }
}
