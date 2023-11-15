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

        val listOfInts = buildList {
            repeat(10) {
                add(it)
            }
        }

        listOfInts
            .filter { it % 2 == 0 }
            .map { it * 2 }
            .forEach { println(it) }
    }

    @Test
    fun `sequence demo`() {
        val sequenceOfInts = sequence {
            var i = 0
            while(true) {
                yield(i++)
            }
        }

        sequenceOfInts
            .filter { it % 2 == 0 }
            .map { it * 2 }
            .forEach {
                println(it)
            }
    }

    @Test
    fun `flow demo`() {
        val flowOfInts = flow {
            var i = 0
            while(true) {
                delay(1000)
                emit(i++)
            }
        }

        runBlocking {
            flowOfInts
                .filter { it % 2 == 0 }
                .map { it * 2 }
                .collect(::println)
        }
    }
}