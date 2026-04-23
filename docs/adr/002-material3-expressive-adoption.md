# ADR-002: Adopción de Material 3 Expressive

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

Google presentó **Material 3 Expressive** en 2025 como la evolución emocional de Material 3. Introduce:

- **Motion physics**: animaciones basadas en `SpringSpec` en lugar de curvas tween rígidas
- **Shape morphing**: formas que se transforman fluidamente entre estados (corners asimétricos, morphing orgánico)
- **`MotionScheme`**: sistema consciente de elegir entre motion `standard` (productividad) o `expressive` (emocional)
- **Componentes expresivos**: `Button`, `FAB`, `NavigationBar` con personalidad visual definida
- **`NavigationSuiteScaffold`**: abstracción sobre `NavigationBar` / `NavigationRail` / `NavigationDrawer` que se adapta al window size class

La tesis del proyecto (vitrina Android moderno bien hecho) exige aprovechar APIs 2025-2026 que la mayoría de apps del ecosistema aún no implementan.

## Decisión

Adoptar Material 3 Expressive en todo `:app` como **default** y mantener paridad en `:widget` y `:wear` cuando la API lo permita.

Acciones concretas:

1. Bump de `compose-material3` a versión con soporte estable para `material3-adaptive`, `material3-adaptive-navigation-suite` y `material3-adaptive-layout`
2. Declaración explícita de `MotionScheme.expressive()` en `EdadPerrunaTheme` (el tema raíz del design system)
3. Migración de `NavigationBar` / `Scaffold` clásico a `NavigationSuiteScaffold` para ganar responsive automático
4. Extensión del `Shape` system con corners asimétricos en la pantalla Result (el hero de la app)
5. Animación `spring`-based en la aparición del número de años humanos calculados
6. Todo el motion respeta `AccessibilityManager.isReducedAnimationsEnabled()` — ver ADR-004

## Opciones consideradas

### Quedarse en Material 3 "standard"
- **Pro**: cero trabajo, cero riesgo de regresiones visuales
- **Contra**: pierde el punto diferencial principal del proyecto
- **Contra**: la app se ve "como las demás" cuando los recruiters revisan capturas

### Construir design language propio
- **Pro**: identidad visual única
- **Contra**: choca con la tesis ("best practices oficiales Google, sin frameworks alternativos")
- **Contra**: enorme inversión de tiempo para ROI dudoso en portfolio

## Consecuencias

### Positivas

- **Diferenciación visual real**: apps con motion physics correcto son minoría en el ecosistema
- **Responsive por defecto**: `NavigationSuiteScaffold` resuelve fone / fablet / tablet / foldable sin lógica manual
- **Coherencia con tokens del design system**: `:core:designsystem` expone `EdadPerrunaMotionScheme`, `EdadPerrunaShapes`, `EdadPerrunaTypography` consumidos desde `:app`, `:widget` y `:wear`
- **Documentable con GIFs y Paparazzi**: el README puede mostrar before/after

### Negativas

- **Animaciones spring aumentan la complejidad de testing**: hay que mockear el scheduler o usar `TestCoroutineScheduler` en tests Compose
- **Glance (`:widget`) NO soporta todavía todos los componentes Expressive**: habrá brecha visual entre fone y widget — se mitiga usando mismos colores/shapes y motion reducido en widget
- **Compose compiler metrics reports pueden cambiar**: el PR que introduzca Expressive debe reportar recomposición antes/después para evitar regresiones de performance

## Referencias

- [Material 3 Expressive — Material Design](https://m3.material.io/blog/building-with-m3-expressive)
- [NavigationSuiteScaffold — Android Developers](https://developer.android.com/reference/kotlin/androidx/compose/material3/adaptive/navigationsuite/package-summary)
