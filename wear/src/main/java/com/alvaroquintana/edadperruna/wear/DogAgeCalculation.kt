package com.alvaroquintana.edadperruna.wear

import kotlin.math.ln
import kotlin.math.roundToInt

/**
 * Wear-local copy of the dog-age formula used in `:core:domain`.
 *
 * Rationale: :core arrastra Hilt, Firebase y Room — innecesarios en un reloj y perjudiciales
 * para cold-start en Wear OS. Como la formula es trivial (16*ln(years)+31, origen NBC News
 * 2019) se duplica aqui. El PR #9 (Wear sync) vera si merece la pena extraer un modulo
 * ligerito `:core:domain-pure` compartido.
 */
internal fun humanYearsFromDogYears(dogYears: Int): Int {
    if (dogYears <= 0) return 0
    return (16.0 * ln(dogYears.toDouble()) + 31.0).roundToInt().coerceAtLeast(0)
}
