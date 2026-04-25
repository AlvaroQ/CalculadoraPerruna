package com.alvaroquintana.edadperruna.wear.tile

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.lastCalcDataStore by preferencesDataStore(name = "last_calc")

private val DOG_YEARS_KEY = intPreferencesKey("dog_years")
private val DOG_MONTHS_KEY = intPreferencesKey("dog_months")
private val HUMAN_YEARS_KEY = intPreferencesKey("human_years")

data class LastCalculation(
    val dogYears: Int,
    val dogMonths: Int,
    val humanYears: Int,
)

object LastCalculationStore {

    suspend fun save(context: Context, dogYears: Int, dogMonths: Int, humanYears: Int) {
        context.lastCalcDataStore.edit { prefs ->
            prefs[DOG_YEARS_KEY] = dogYears
            prefs[DOG_MONTHS_KEY] = dogMonths
            prefs[HUMAN_YEARS_KEY] = humanYears
        }
    }

    // TileService.onTileRequest is invoked on a background binder thread, so a
    // short blocking read here is acceptable — DataStore Preferences resolves
    // in <10ms and the alternative (kotlinx-coroutines-guava `future { }`)
    // would pull a transitive dependency and complicate the simple read path.
    fun readBlocking(context: Context): LastCalculation? = runBlocking {
        val prefs = context.lastCalcDataStore.data.first()
        val years = prefs[DOG_YEARS_KEY] ?: return@runBlocking null
        val human = prefs[HUMAN_YEARS_KEY] ?: return@runBlocking null
        LastCalculation(
            dogYears = years,
            dogMonths = prefs[DOG_MONTHS_KEY] ?: 0,
            humanYears = human,
        )
    }
}
