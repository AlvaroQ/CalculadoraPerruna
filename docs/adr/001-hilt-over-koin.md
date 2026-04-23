# ADR-001: Hilt como motor de inyección de dependencias

- **Status**: Accepted
- **Date**: 2026-04-23
- **Deciders**: Alvaro Quintana

## Contexto

CalculadoraPerruna forma parte de un portfolio Kotlin de 4 proyectos con tesis diferenciadas, donde cada uno responde a una pregunta distinta de entrevista senior Android 2026. La decisión de DI por proyecto es intencionalmente heterogénea:

| Proyecto | DI |
|----------|----|
| CalculadoraPerruna | Hilt |
| AdivinaBandera | Dagger puro (sin Hilt) |
| AdivinaRaza | Koin KMP |
| PrideQuiz | Koin |

La pregunta concreta para este proyecto es: **¿cómo se ve una app Android producción-grade alineada al 100% con el stack recomendado por Google?**

## Decisión

Usar **Hilt 2.59.2** como único mecanismo de inyección de dependencias en CalculadoraPerruna.

- `@HiltAndroidApp` en `EdadPerrunaApp`
- `@AndroidEntryPoint` en Activities
- `@HiltViewModel` + `@Inject` en ViewModels
- Módulos `@InstallIn(SingletonComponent::class)` para bindings de singletones
- `@Module` + `@Binds` para interfaces (Analytics, DogAgeCalculator, repositories)
- KSP en lugar de KAPT para el procesamiento de anotaciones

## Opciones consideradas

### Koin
- **Pro**: setup más simple, sin procesamiento de anotaciones, tiempos de build menores
- **Contra**: resolución en runtime — errores de DI se manifiestan en ejecución, no en compilación
- **Contra**: en un proyecto que se vende como "production-grade Google-aligned", Koin es la apuesta del ecosistema alternativo, no la de Google

### Dagger puro (sin Hilt)
- **Pro**: máximo control, sin generación mágica, componentes explícitos
- **Contra**: ceremonia excesiva para una app pequeña (componentes por Activity, scopes manuales, subcomponents)
- **Contra**: duplicaría la apuesta de AdivinaBandera en el portfolio

### Metro
- **Pro**: nuevo compile-time DI, promete simplicidad sobre Dagger
- **Contra**: demasiado joven, riesgo de abandono, sin adopción empresarial verificable

## Consecuencias

### Positivas

- **Compile-time safety**: errores de grafo DI fallan en compilación, no en runtime
- **Scoping Android-aware**: `SingletonComponent`, `ActivityRetainedComponent`, `ViewModelComponent` sin que el autor los diseñe
- **KSP ready**: procesamiento ~2x más rápido que KAPT, sin stubs Java
- **Integración oficial con Jetpack Compose**: `hiltViewModel()` vía `androidx.hilt:hilt-navigation-compose`
- **Portabilidad a features futuras**: cualquier módulo nuevo (`:widget`, `:wear`) se integra con `@InstallIn` sin rediseño

### Negativas

- **Lock-in al ecosistema Google**: Hilt no es multiplataforma. Si este proyecto quisiera migrar a KMP, habría que reemplazarlo (no es objetivo).
- **Build time**: el procesamiento KSP añade tiempo al build incremental (~1-3s por módulo con cambios en el grafo)
- **Hilt genera código en `build/generated/`**: la primera vez que se abre el proyecto IDE puede mostrar errores rojos hasta que se ejecute un build completo

## Referencias

- [Hilt — official documentation](https://dagger.dev/hilt/)
- [Why Hilt? — Android Developers](https://developer.android.com/training/dependency-injection/hilt-android)
