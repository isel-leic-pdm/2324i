package isel.pdm.demos.tictactoe.utils

import org.junit.Assert
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T> xAssertNotNull(value: T?, lazyMessage: () -> String) {
    contract {
        returns() implies (value != null)
    }

    Assert.assertNotNull(lazyMessage(), value)
}

@OptIn(ExperimentalContracts::class)
fun <T> xAssertNotNull(value: T?) {
    contract {
        returns() implies (value != null)
    }

    Assert.assertNotNull(value)
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T : Any> xAssertIs(value: Any?, lazyMessage: () -> String = { "" }): T {
    contract {
        returns() implies (value is T)
    }

    val castedValue = value as? T
    xAssertNotNull(castedValue, lazyMessage)
    return castedValue
}
