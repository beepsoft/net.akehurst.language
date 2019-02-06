package net.akehurst.language.processor.vistraq

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import net.akehurst.language.api.processor.LanguageProcessor
import net.akehurst.language.processor.Agl


@RunWith(Parameterized::class)
class test_NaturalLanguage(val data:Data) {

    companion object {

        private val grammarStr = this::class.java.getResource("/natural/English.agl").readText()
        var processor: LanguageProcessor = tgqlprocessor()

        var sourceFiles = arrayOf("/natural/english-sentences-valid.txt")

        fun tgqlprocessor() : LanguageProcessor {
            //val grammarStr = ClassLoader.getSystemClassLoader().getResource("vistraq/Query.ogl").readText()
            return Agl.processor(grammarStr)
         }

        @JvmStatic
        @Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val col = ArrayList<Array<Any>>()
            for (sourceFile in sourceFiles) {
               // val inps = ClassLoader.getSystemClassLoader().getResourceAsStream(sourceFile)
                val inps = this::class.java.getResourceAsStream(sourceFile)

                val br = BufferedReader(InputStreamReader(inps))
                var line: String? = br.readLine()
                while (null != line) {
                    line = line.trim { it <= ' ' }
                    if (line.isEmpty()) {
                        // blank line
                        line = br.readLine()
                    } else if (line.startsWith("//")) {
                        // comment
                        line = br.readLine()
                    } else {
                        val goal = line.substringBefore("|").trim()
                        val sentence = line.substringAfter("|").trim()
                        col.add(arrayOf(Data(sourceFile, goal, sentence)))
                        line = br.readLine()
                    }
                }
            }
            return col
        }
    }

    class Data(val sourceFile: String, val goal:String, val sentence: String) {

        // --- Object ---
        override fun toString(): String {
            return "$sourceFile : $goal : $sentence"
        }
    }

    @Test
    fun test() {
        val result = processor.parse("query", this.data.sentence)
        Assert.assertNotNull(result)
        val resultStr = result.asString
        Assert.assertEquals(this.data.sentence, resultStr)
    }

}
