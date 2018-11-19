package net.akehurst.language.processor.vistraq

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

import net.akehurst.language.api.processor.LanguageProcessor
import net.akehurst.language.api.sppt.SharedPackedParseTree
import net.akehurst.language.processor.processor

@RunWith(Parameterized::class)
class test_QueryParserValid(val data:Data) {

    companion object {
        var processor: LanguageProcessor = tgqlprocessor()
        var sourceFiles = arrayOf("vistraq/sampleValidQueries.txt")

        fun tgqlprocessor() : LanguageProcessor {
            val grammarStr = ClassLoader.getSystemClassLoader().getResource("vistraq/Query.ogl").readText()
            return processor(grammarStr)
         }

        @JvmStatic
        @Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            val col = ArrayList<Array<Any>>()
            for (sourceFile in test_QueryParserValid.sourceFiles) {
                val `is` = ClassLoader.getSystemClassLoader().getResourceAsStream(sourceFile)
                val br = BufferedReader(InputStreamReader(`is`))
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
                        col.add(arrayOf(Data(sourceFile, line)))
                        line = br.readLine()
                    }
                }
            }
            return col
        }
    }

    class Data(val sourceFile: String, val queryStr: String) {

        // --- Object ---
        override fun toString(): String {
            return this.sourceFile + ": " + this.queryStr
        }
    }

    @Test
    fun test() {
        val queryStr = this.data!!.queryStr
        val result = processor.parse("query", queryStr)
        Assert.assertNotNull(result)
        val resultStr = result.asString
        Assert.assertEquals(queryStr, resultStr)
    }



}
