package com.alvaroquintana.edadperruna.wearsync

import android.content.Context
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Publica la raza favorita del usuario al canal DataClient de Wear OS.
 *
 * Cuando el reloj (`:wear`) esta presente y emparejado, recibe el DataItem via
 * `DataClient.OnDataChangedListener`. Si no hay reloj, la publicacion es un no-op
 * silencioso: Google Play Services la persiste y el reloj la recoge al emparejarse.
 *
 * Path del protocolo: `/favorite_breed`. Payload: nombre, URL de imagen, esperanza
 * de vida media en años, timestamp.
 *
 * En este PR (#9) se publica la infraestructura pero **no se invoca desde UI**:
 * un PR siguiente anadira un boton "estrella" en BreedDescriptionScreen que llame
 * `publish(...)`. Dejarlo inyectable desde ahora permite iterar sin tocar el
 * protocolo mas adelante.
 */
@Singleton
class WearSyncPublisher @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun publishFavoriteBreed(breed: FavoriteBreedPayload) {
        val request = PutDataMapRequest.create(WearSyncPaths.FAVORITE_BREED).apply {
            dataMap.putString(WearSyncKeys.NAME, breed.name)
            dataMap.putString(WearSyncKeys.IMAGE_URL, breed.imageUrl)
            dataMap.putInt(WearSyncKeys.AVG_LIFE_YEARS, breed.avgLifeYears)
            dataMap.putLong(WearSyncKeys.UPDATED_AT, System.currentTimeMillis())
        }
        Wearable.getDataClient(context)
            .putDataItem(request.asPutDataRequest().setUrgent())
            .await()
    }
}

data class FavoriteBreedPayload(
    val name: String,
    val imageUrl: String,
    val avgLifeYears: Int,
)

object WearSyncPaths {
    const val FAVORITE_BREED = "/favorite_breed"
}

object WearSyncKeys {
    const val NAME = "name"
    const val IMAGE_URL = "image_url"
    const val AVG_LIFE_YEARS = "avg_life_years"
    const val UPDATED_AT = "updated_at"
}
