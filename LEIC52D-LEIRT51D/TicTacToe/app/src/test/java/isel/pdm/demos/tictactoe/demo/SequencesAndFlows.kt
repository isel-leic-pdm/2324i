package isel.pdm.demos.tictactoe.demo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Test

class SequencesAndFlows {

    @Test
    fun `list demo`() {
        val list = buildList {
            repeat(10) {
                add(it)
            }
        }
        val result = list
            .filter { it % 2 == 0 }
            .map { it * 2 }

        result.forEach(::println)
    }

    @Test
    fun `sequence demo`() {
        val sequence = sequence {
            var it = 0
            while (true) {
                yield(it++)
            }
        }
        val result = sequence
            .filter { it % 2 == 0 }
            .map { it * 2 }

        result.forEach {
            println(it)
        }
    }

    @Test
    fun `flow demo`() {
        val flow = flow {
            var it = 0
            while (true) {
                delay(1000)
                emit(it++)
            }
        }
        val result = flow
            .filter { it % 2 == 0 }
            .map { it * 2 }

        runBlocking {
            result.collect {
                println(it)
            }
        }
    }
}