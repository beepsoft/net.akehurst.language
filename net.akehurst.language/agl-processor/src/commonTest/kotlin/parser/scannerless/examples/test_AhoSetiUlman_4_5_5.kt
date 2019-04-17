package parser.scannerless.examples

import net.akehurst.language.agl.runtime.structure.RuntimeRuleItem
import net.akehurst.language.agl.runtime.structure.RuntimeRuleItemKind
import net.akehurst.language.agl.runtime.structure.RuntimeRuleSetBuilder
import net.akehurst.language.api.parser.ParseFailedException
import net.akehurst.language.parser.scannerless.test_ScannerlessParserAbstract
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class test_AhoSetiUlman_4_5_5 : test_ScannerlessParserAbstract() {

    // S = CC ;
    // C = cC | d ;
    private fun S(): RuntimeRuleSetBuilder {
        val b = RuntimeRuleSetBuilder()
        val r_c = b.literal("c")
        val r_d = b.literal("d")
        val r_C1 = b.rule("C1").build()
        val r_C = b.rule("C").choiceEqual(r_C1, r_d)
        b.rule(r_C1).concatenation(r_c, r_C)
        val r_S = b.rule("S").concatenation(r_C, r_C)
        return b
    }

    @Test
    fun c() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "c"

        val ex = assertFailsWith(ParseFailedException::class) {
            super.test(rrb, goal, sentence)
        }
        assertEquals(1, ex.location.line, "line is wrong")
        assertEquals(1, ex.location.column, "column is wrong")
    }

    @Test
    fun d() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "d"

        val ex = assertFailsWith(ParseFailedException::class) {
            super.test(rrb, goal, sentence)
        }
        assertEquals(1, ex.location.line, "line is wrong")
        assertEquals(1, ex.location.column, "column is wrong")
    }


    @Test
    fun dd() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "dd"

        val expected = """
            S { C { 'd' } C { 'd' } }
        """.trimIndent()

        super.testStringResult(rrb, goal, sentence, expected)
    }


    @Test
    fun dcd() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "dcd"

        val expected = """
            S { C { 'd' } C{ C1 { 'c' C { 'd' } } } }
        """.trimIndent()

        super.test(rrb, goal, sentence, expected)
    }

}