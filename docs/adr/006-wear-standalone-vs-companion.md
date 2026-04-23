# ADR-006: Wear OS standalone primero, companion como iteración

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

El proyecto añade un módulo `:wear` para demostrar **form-factor awareness** — una señal moderna que casi ningún portfolio Android exhibe. Dos modelos posibles:

1. **Standalone** — la app del reloj es autosuficiente: picker de edad en el reloj, cálculo local con `DogAgeCalculator` compartido desde `:core:domain`, resultado mostrado sin depender del fone
2. **Companion** — la app del reloj sincroniza con la del fone mediante `Wearable.DataClient` / `MessageClient`: raza favorita sincronizada, cálculos disparados desde el reloj y consumidos en el fone, etc.

El companion es más ambicioso y "vende" más en entrevista (comunicación cross-device), pero tiene edge cases reales que convierten el esfuerzo nominal de 4 días en 5-7 si no se controla:

- Cold start del reloj mientras el fone está offline
- Capability announcements que no llegan hasta que ambos apps se abren al menos una vez
- Gestión de batería (`WorkManager` en el reloj tiene reglas propias)
- Sincronización de `DataItem` frente a usuarios que desinstalan y reinstalan el fone

## Decisión

**Dos PRs secuenciales**:

1. **PR #8 — `:wear` standalone** (2.5 días): reloj autosuficiente, `ScalingLazyColumn` con picker 1-20 años, resultado con animación de arco expresiva, rotary input habilitado. Dependencia de `:core:domain` (para `DogAgeCalculator`) y `:core:designsystem` (para coherencia visual).

2. **PR #9 — Wear ↔ phone sync** (1.5 días): encima del standalone ya estable, añadir `Wearable.DataClient` para sincronizar raza favorita entre fone y reloj, y `MessageClient` para disparar cálculos.

Esta secuencia se aprueba antes de arrancar la implementación del módulo `:wear`.

## Opciones consideradas

### Companion desde el primer PR
- **Pro**: entrega la feature completa en un solo merge
- **Contra**: el PR se infla de 2.5d a 4d nominales y hasta 7d reales por edge cases
- **Contra**: si el sync se atranca, el PR queda abierto y el módulo `:wear` entero queda fuera del README
- **Contra**: testing manual del sync requiere dos dispositivos emparejados — hit en productividad

### Solo standalone, sin companion nunca
- **Pro**: esfuerzo mínimo, riesgo mínimo
- **Contra**: pierde la diferenciación real de "apps que hablan entre dispositivos"
- **Contra**: el flujo "toco el widget, el reloj lo muestra" no existe

## Consecuencias

### Positivas

- **PR #8 entrega un artefacto cerrado y demostrable**: GIF del reloj funcionando solo, badge "Wear OS ready" en README, merge rápido
- **PR #9 construye encima de algo estable**: sin el riesgo de mezclar el diseño inicial del módulo con protocolo de comunicación
- **Fallback natural**: si PR #9 se alarga, PR #8 ya está merged y el proyecto no pierde la señal Wear
- **Testabilidad**: PR #8 se puede testear con Paparazzi + preview Wear; PR #9 requiere emulador pareado pero solo para una feature concreta

### Negativas

- **Dos PRs en vez de uno**: más overhead de review, aunque cada uno es más reviewable por separado
- **En el tiempo entre PR #8 y PR #9 el reloj NO sincroniza con el fone**: si el proyecto se publica entre ambos, hay una versión "intermedia" sin sync. Asumido, dado que el proyecto no tiene ciclo de release comprometido con usuarios reales.
- **El sync en PR #9 debe ser honestamente útil**: no sincronizar por sincronizar. El caso de uso concreto es "raza favorita marcada en el fone aparece automáticamente en el reloj" — documentado en el PR.

## Referencias

- [Wear OS — standalone vs phone-dependent apps](https://developer.android.com/training/wearables/apps/standalone-apps)
- [Wearable Data Layer API](https://developer.android.com/training/wearables/data/data-layer)
- [Compose for Wear OS](https://developer.android.com/training/wearables/compose)
