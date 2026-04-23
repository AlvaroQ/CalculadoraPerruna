# ADR-003: Estrategia de adopción de Predictive Back

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

**Predictive Back** es el gesto de Android que muestra un preview animado del destino al hacer swipe-back, introducido como opt-in en Android 13 (API 33) y convertido en obligatorio en Android 16 (API 36, el `targetSdk` actual del proyecto).

Sin adopción explícita:
- En Android 13-15, el gesto sigue funcionando al modo "clásico" (sin preview)
- En Android 16+ (targetSdk 36), `enableOnBackInvokedCallback` pasa a ser el comportamiento por defecto y las `OnBackPressedCallback` que no se hayan migrado pueden comportarse incorrectamente

El proyecto usa hoy Navigation Compose y `BackHandler` en varias pantallas. El migrado no es automático: `BackHandler` convive con `PredictiveBackHandler` pero solo este último recibe el evento `progress` del gesto.

## Decisión

Migración completa a Predictive Back con las siguientes reglas:

1. **`android:enableOnBackInvokedCallback="true"`** en el elemento `<application>` de `AndroidManifest.xml`
2. **`BackHandler` → `PredictiveBackHandler`** en cada Composable que intercepte el back
3. **Animaciones coherentes** durante el gesto usando `PredictiveBackEvent.progress` y `swipeEdge` para interpolar transiciones personalizadas
4. **`SharedTransitionLayout` + `AnimatedContent`** en Navigation Compose para que las transiciones shared-element respeten el progress del gesto
5. **Fallback gracioso en Android 13-14**: cuando no hay preview, el handler clásico sigue funcionando

## Opciones consideradas

### Mantener `BackHandler` clásico
- **Pro**: cero trabajo
- **Contra**: en Android 16+ (nuestro targetSdk) el comportamiento es inconsistente — la app se ve "legacy"
- **Contra**: pierde el punto de "vitrina de APIs 2025-2026"

### Migrar solo Android 14+ y dejar 13 con BackHandler
- **Pro**: menos código condicional
- **Contra**: innecesario — `PredictiveBackHandler` ya gestiona el fallback internamente

## Consecuencias

### Positivas

- **Coherencia con el sistema operativo**: el gesto muestra el destino real, no una animación genérica
- **Animaciones personalizadas durante el gesto**: el shared element de la imagen de la raza acompaña el swipe
- **Ready para Android 16+**: cuando `enableOnBackInvokedCallback` pase a ser forzoso, la app ya está migrada
- **Diferenciación en capturas**: los GIFs del README muestran el gesto predictivo — muy pocas apps lo exhiben

### Negativas

- **Testing manual imprescindible**: el comportamiento correcto del gesto solo se valida en dispositivo real o emulador con gesture navigation activado
- **Riesgo de regresión sutil**: si un `PredictiveBackHandler` no consume el evento (`isEnabled = false`), puede dejar pantallas sin salida — cubierto con test instrumentado
- **`SharedTransitionLayout` añade complejidad**: requiere `AnimatedContentScope` y cuidado con `Modifier.sharedElement` — documentado en el PR de migración

## Referencias

- [Predictive back gestures — Android Developers](https://developer.android.com/guide/navigation/custom-back/predictive-back-gesture)
- [PredictiveBackHandler in Compose](https://developer.android.com/reference/kotlin/androidx/activity/compose/package-summary#PredictiveBackHandler)
