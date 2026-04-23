# ADR-004: Baseline de accesibilidad WCAG 2.2 AA + EAA 2025

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

La [European Accessibility Act (EAA)](https://ec.europa.eu/social/main.jsp?catId=1202) entró en vigor el **28 de junio de 2025** y obliga a que software comercial dirigido a consumidores en la UE cumpla criterios de accesibilidad equivalentes a **WCAG 2.2 AA**. Aplica a aplicaciones móviles distribuidas en el mercado europeo.

Fuera de la obligación legal, la accesibilidad es una señal de seniority muy fuerte en portfolio: la mayoría de apps del ecosistema se limitan al mínimo que el linter detecta (`contentDescription` en imágenes) y no tratan la accesibilidad como entregable verificado.

El proyecto se vende como "vitrina Android moderno bien hecho", así que la accesibilidad entra como **first-class concern**, no como checklist al final.

## Decisión

Definir un baseline de accesibilidad **medible y verificado en CI**, con los siguientes compromisos:

### 1. Semantics completa en Compose

- `contentDescription` obligatoria en toda `Image` / `Icon` (o explícitamente `null` con justificación)
- `Modifier.semantics { heading() }` en títulos de pantalla
- `Modifier.semantics { role = Role.Button }` cuando un `Box`/`Row` actúe como botón
- `Modifier.semantics { stateDescription = ... }` en toggles y estados custom
- `mergeDescendants = true` en cards completas que forman una unidad lógica
- `LiveRegion` en el resultado de la edad calculada (TalkBack anuncia el cambio automáticamente)

### 2. Dynamic type hasta fontScale 2.0

- Todos los layouts deben soportar `fontScale` hasta **2.0** sin truncamiento ni solapamiento
- Verificación en Paparazzi con matrix `fontScale = [1.0, 1.3, 1.7, 2.0]`
- CI falla si un snapshot `fontScale=2.0` rompe respecto al baseline

### 3. Contraste WCAG 2.2 AA

- Ratio mínimo **4.5:1** en texto normal, **3:1** en texto grande (≥18pt regular / ≥14pt bold)
- Verificado con `AccessibilityChecks.enable()` en tests instrumentados
- Tema alterno activado automáticamente cuando `isHighTextContrastEnabled == true`

### 4. Touch targets 48dp

- Todo elemento interactivo (`clickable`, `toggleable`) mínimo **48dp × 48dp**
- Regla custom Konsist (cuando se introduzca) que falla si un `IconButton` sin `size` modifier se usa como hit area única

### 5. Reduce motion

- Toda animación spring / expressive se degrada a crossfade inmediato cuando `AccessibilityManager.isReducedAnimationsEnabled() == true`
- El MotionScheme del tema expone una variante `reduced` que se inyecta vía `CompositionLocal`

### 6. TalkBack como acceptance test

- Antes de cerrar cada PR de UI: recorrido completo con TalkBack activo
- Documentado con video corto adjunto al PR cuando el flujo cambia

## Opciones consideradas

### "Cumplimos con el linter de Android"
- **Pro**: cero trabajo extra
- **Contra**: el linter detecta ~20% de los problemas reales (el resto es semantics, contraste dinámico, orden de foco, fontScale)
- **Contra**: no es WCAG 2.2 AA ni de lejos

### Solo WCAG 2.1 AA
- **Pro**: más ampliamente conocido
- **Contra**: WCAG 2.2 AA es el estándar referenciado por EAA 2025 — escoger 2.1 es quedarse un paso atrás

## Consecuencias

### Positivas

- **Badge "WCAG 2.2 AA" en README** respaldado por tests reproducibles
- **EAA 2025 compliant** sin trabajo adicional si la app se distribuye en Europa
- **Seniority signal fuerte**: documentación de accesibilidad con tests es rarísima en portfolio
- **Calidad real de producto**: los usuarios con discapacidad visual / motora pueden usar la app

### Negativas

- **+1.5 días de esfuerzo por cada pantalla nueva** (auditoría semantics + Paparazzi fontScale + manual TalkBack)
- **Paparazzi es precondición**: hay que adelantar su instalación antes de cerrar este baseline (ver plan de fases)
- **Tests instrumentados con `AccessibilityChecks` son más lentos**: suman ~30s al CI en cada run
- **Disciplina continua**: cualquier PR futuro debe pasar el baseline o se considera regresión

## Referencias

- [WCAG 2.2 — W3C](https://www.w3.org/TR/WCAG22/)
- [European Accessibility Act](https://ec.europa.eu/social/main.jsp?catId=1202)
- [Accessibility in Jetpack Compose](https://developer.android.com/jetpack/compose/accessibility)
- [AccessibilityChecks — Espresso](https://developer.android.com/reference/androidx/test/espresso/accessibility/AccessibilityChecks)
