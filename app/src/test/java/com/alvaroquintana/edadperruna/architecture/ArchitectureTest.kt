package com.alvaroquintana.edadperruna.architecture

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Reglas arquitectonicas enforced por [Konsist](https://docs.konsist.lemonappdev.com/).
 *
 * Esto es un pass inicial — 3 reglas que capturan invariantes reales del codebase:
 *  1. Las clases `*ViewModel` tienen que extender `androidx.lifecycle.ViewModel`.
 *  2. Los archivos del package `core.domain` no pueden importar nada de `android.*`
 *     ni de frameworks Hilt/Firebase/Room (domain puro, testeable sin Android).
 *  3. Los `*UseCase` viven en un package que contenga `usecase` o `domain` — caza
 *     los use cases colocados por error en `data/` o `ui/`.
 *
 * Cuando una regla falle, el test explica que la rompio y que fichero. Ampliar con
 * mas reglas a medida que el codebase crezca (layer boundaries por modulo, etc.).
 */
class ArchitectureTest {

    @Test
    fun `viewmodel classes must extend androidx ViewModel`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withNameEndingWith("ViewModel")
            .filter { !it.name.startsWith("Hilt_") } // skip Hilt-generated stubs
            .assertTrue { vm ->
                vm.hasParent { parent -> parent.name == "ViewModel" }
            }
    }

    @Test
    fun `core domain must not depend on Android framework nor Hilt`() {
        Konsist
            .scopeFromProduction()
            .files
            .filter { it.packagee?.name?.contains(".core.domain") == true }
            .forEach { file ->
                val forbidden = file.imports.filter { imp ->
                    imp.name.startsWith("android.") ||
                        imp.name.startsWith("androidx.") && !imp.name.startsWith("androidx.annotation") ||
                        imp.name.startsWith("dagger.") ||
                        imp.name.startsWith("com.google.firebase")
                }
                check(forbidden.isEmpty()) {
                    "core.domain must remain pure. ${file.name} imports forbidden: ${forbidden.map { it.name }}"
                }
            }
    }

    @Test
    fun `use case classes live in a domain or usecase package`() {
        Konsist
            .scopeFromProduction()
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { uc ->
                val pkg = uc.packagee?.name.orEmpty()
                pkg.contains("domain") || pkg.contains("usecase")
            }
    }
}
