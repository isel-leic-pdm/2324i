package isel.pdm.demos.tictactoe.demo

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SequencesAndFlows {

    @Test
    fun `list of ints`() {
        val list = buildList {
            repeat(10) {
                add(it)
            }
        }

        val result = list
            .filter { it % 2 == 0 }
            .map { it * 2 }

        result
            .forEach {
                println(it)
            }
    }

   @Test
   fun `sequence of ints`() {

       val sequence = sequence {
           var i = 0
           while (true) {
               yield(i++)
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
    fun `flow of ints`() {
        val flow = flow {
            var i = 0
            while (i < 10) {
                delay(1000)
                emit(i++)
            }
        }

        val result = flow
            .filter { it % 2 == 0 }
            .map { it * 2 }

        runBlocking {
            println(result.last())
        }
    }
}