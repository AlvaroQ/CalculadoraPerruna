# ADR-005: Glance sobre RemoteViews para widget de raza favorita

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

Una de las señales modernas del proyecto es un **widget de home screen** que muestra la raza favorita del usuario con su edad equivalente en años humanos, y permite refrescar el cálculo sin abrir la app.

Hay dos caminos técnicos para widgets en Android:

1. **RemoteViews** — el modelo clásico: XML layouts servidos desde un `AppWidgetProvider`, limitados a un subset de Views (`TextView`, `ImageView`, `Button`, `LinearLayout`, `FrameLayout`…)
2. **Glance** (`androidx.glance:glance-appwidget`) — modelo declarativo Compose-style introducido en 2022 y estabilizado en 2024. Los `@Composable` de Glance se compilan a RemoteViews por debajo.

## Decisión

Implementar el widget con **Glance**, en un módulo `:widget` nuevo que depende de `:core:designsystem` y `:core:domain`.

Estructura:

- `FavoriteBreedWidget : GlanceAppWidget` con 3 tamaños declarados en `SizeMode.Responsive`:
  - **1×1** — icono de la raza + edad humana
  - **2×2** — icono + nombre de la raza + edad humana
  - **4×2** — todo lo anterior + botón "Recalcular"
- `FavoriteBreedWidgetReceiver : GlanceAppWidgetReceiver` como entry point del sistema
- `actionStartActivity` para abrir `:app` en la pantalla de la raza al tocar el widget
- `actionRunCallback<UpdateFavoriteBreedAction>()` para recalcular sin abrir la app
- Los tokens de color, tipografía y shape vienen de `:core:designsystem` — misma identidad visual que `:app`

## Opciones consideradas

### RemoteViews XML tradicional
- **Pro**: API estable desde Android 1.x, documentación amplísima, cero riesgo de cambios breaking
- **Pro**: soporte de APIs de widget que Glance aún no cubre (p. ej. `RemoteViews.setCompoundButtonChecked`)
- **Contra**: layouts XML duplicados que no reutilizan nada del design system Compose
- **Contra**: el punto "Compose moderno" de la tesis queda cojo — tendríamos Compose en `:app` y XML en `:widget`
- **Contra**: cualquier cambio de diseño obliga a tocar dos worlds

### No hacer widget
- **Pro**: cero trabajo
- **Contra**: se pierde una señal diferencial fuerte (Glance es minoritario en apps del ecosistema)

## Consecuencias

### Positivas

- **Reutilización real del design system**: los tokens de `:core:designsystem` alimentan `:app`, `:widget` y `:wear` — coherencia visual 100%
- **Declarativo, testable con Paparazzi**: los `@Composable` Glance se snapshotean igual que los de `:app`
- **`actionRunCallback` es trivial**: recalcular sin abrir la app se resuelve con una Action Compose-nativa, sin `PendingIntent` a mano
- **Point visible en README**: "Glance widget con paridad visual al design system" es un bullet concreto y verificable

### Negativas

- **Glance no soporta todas las APIs de RemoteViews**: algunos componentes (como transiciones shared-element desde widget a app) no son posibles — asumido, no son parte del scope
- **Restricciones de widget heredadas**: igual que RemoteViews, el widget no puede ejecutar código arbitrario en su proceso — datos deben venir de DataStore / Room vía `GlanceStateDefinition` o `StateFlow` colectado en el `GlanceAppWidget`
- **Versión de Glance con Material 3 Expressive parcial**: hay componentes Expressive que NO existen aún en Glance — el widget tendrá una versión "reducida" del design system (motion simple, shapes básicos). Esta asimetría se acepta y se documenta como limitación de la plataforma, no del proyecto.
- **minSdk 23 cumplido**: Glance requiere API 23+ y el proyecto ya está en `minSdk=23`, sin impacto

## Referencias

- [Glance — official documentation](https://developer.android.com/develop/ui/compose/glance)
- [Glance vs RemoteViews decision guide](https://developer.android.com/develop/ui/compose/glance#appwidgets)
