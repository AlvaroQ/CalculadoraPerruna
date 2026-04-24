import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.baselineprofile) apply false
    alias(libs.plugins.kover)
}

dependencies {
    // TODO(kover): re-add :app once Kover 0.9.x picks up its debug variant under
    // AGP 9 multi-variant (debug + release + nonMinifiedRelease + benchmarkRelease).
    // Today the aggregator silently drops :app, so its ViewModel coverage doesn't
    // contribute. :core and :core-domain-pure carry the JVM business logic anyway.
    kover(project(":core"))
    kover(project(":core-domain-pure"))
}

// Coverage policy: Option B (per-layer enforcement on testable JVM code only).
// - Filters strip generated graph (Hilt, Compose singletons, KSP), Android-only
//   surfaces (Activities, Screens, Navigation, Theme, Tokens), and packages that
//   require Robolectric/instrumented tests (Room DAO/DB, DataStore proto, Activity
//   utils, NetworkObserver).
// - Two rules: per-package floor (50%) blocks any included package from rotting,
//   plus an application-wide floor (70%) blocks the aggregate from drifting.
kover {
    reports {
        filters {
            excludes {
                classes(
                    "*ComposableSingletons*",
                    "*BuildConfig",
                    // Hilt-generated graph (FQN match — leading * required)
                    "*Hilt_*",
                    "*_HiltModules*",
                    "*_HiltComponents*",
                    "*_GeneratedInjector",
                    "*_Factory",
                    "*_Factory\$*",
                    "*_MembersInjector",
                    "*_Impl",
                    "*_Impl\$*",
                    "*.R",
                    "*.R\$*",
                    "*\$\$serializer",
                    // Android-only entry points / declarative surfaces (incl. inner classes)
                    "*Activity",
                    "*Activity\$*",
                    "*Screen*",
                    "*Application",
                    "*Application\$*",
                    // SDK wrappers — call third-party static APIs, integration-tested
                    "com.alvaroquintana.edadperruna.managers.AnalyticsManager",
                    "com.alvaroquintana.edadperruna.managers.AnalyticsManager\$*",
                    "com.alvaroquintana.edadperruna.managers.AdMobAdsClient",
                    "com.alvaroquintana.edadperruna.managers.AdMobAdsClient\$*",
                )
                packages(
                    // Generated DI graph
                    "hilt_aggregated_deps",
                    "dagger.hilt.internal",
                    // Hilt modules (wiring only — not testable JVM logic)
                    "com.alvaroquintana.edadperruna.di",
                    "com.alvaroquintana.edadperruna.core.di",
                    // Room: DAO/DB require Robolectric or instrumented tests
                    "com.alvaroquintana.edadperruna.core.data.local.dao",
                    "com.alvaroquintana.edadperruna.core.data.local.db",
                    // DataStore Proto: generated code
                    "com.alvaroquintana.edadperruna.core.data.local.datastore",
                    // Android Context-bound observers
                    "com.alvaroquintana.edadperruna.core.data.network",
                    // Designsystem: tokens / themes / colors are declarations, no logic
                    "com.alvaroquintana.edadperruna.core.designsystem.theme",
                    "com.alvaroquintana.edadperruna.core.designsystem.tokens",
                    "com.alvaroquintana.edadperruna.core.designsystem.color",
                    "com.alvaroquintana.edadperruna.core.designsystem.shape",
                    "com.alvaroquintana.edadperruna.core.designsystem.type",
                    "com.alvaroquintana.edadperruna.core.designsystem.a11y",
                    // App layer: navigation graph + Activity helpers (Android Context)
                    "com.alvaroquintana.edadperruna.application",
                    "com.alvaroquintana.edadperruna.ui",
                    "com.alvaroquintana.edadperruna.ui.navigation",
                    "com.alvaroquintana.edadperruna.utils",
                    // Compose components — no business logic, validated via screenshot tests
                    "com.alvaroquintana.edadperruna.ui.components",
                    "com.alvaroquintana.edadperruna.core.designsystem.components",
                    // Wear sync publisher: thin DataClient wrapper, integration-tested via reloj
                    "com.alvaroquintana.edadperruna.wearsync",
                )
                annotatedBy("*Generated*")
            }
        }

        verify {
            rule("Per-package line coverage >= 50%") {
                groupBy = GroupingEntityType.PACKAGE
                bound {
                    minValue = 50
                    coverageUnits = CoverageUnit.LINE
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }
            rule("Project line coverage >= 70%") {
                groupBy = GroupingEntityType.APPLICATION
                bound {
                    minValue = 70
                    coverageUnits = CoverageUnit.LINE
                    aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                }
            }
        }
    }
}
