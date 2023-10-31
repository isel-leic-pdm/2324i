@file:Suppress("unused")
package isel.pdm.jokes

import androidx.compose.ui.test.*
import isel.pdm.jokes.ui.IsReadOnly

private fun isReadOnly(): SemanticsMatcher =
    SemanticsMatcher.keyIsDefined(IsReadOnly)

fun SemanticsNodeInteraction.assertIsReadOnly(): SemanticsNodeInteraction =
    assert(isReadOnly())

fun SemanticsNodeInteraction.assertIsNotReadOnly(): SemanticsNodeInteraction =
    assert(!isReadOnly())