package net.akehurst.language.parser.scannerless.choicePriority

import net.akehurst.language.agl.runtime.structure.RuntimeRuleChoiceKind
import net.akehurst.language.agl.runtime.structure.RuntimeRuleItem
import net.akehurst.language.agl.runtime.structure.RuntimeRuleItemKind
import net.akehurst.language.agl.runtime.structure.RuntimeRuleSetBuilder
import net.akehurst.language.api.parser.ParseFailedException
import net.akehurst.language.parser.scannerless.test_ScannerlessParserAbstract
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class test_ifThenElse_Simple : test_ScannerlessParserAbstract() {

    // invert the dangling else

    // S =  expr ;
    // ifthenelse = 'if' expr 'then' expr 'else' expr ;
    // ifthen = 'if' expr 'then' expr ;
    // expr = var < conditional ;
    // conditional = ifthen < ifthenelse ;
    // var = 'V' ;
    private fun S(): RuntimeRuleSetBuilder {
        val b = RuntimeRuleSetBuilder()
        val r_expr = b.rule("expr").build()
        val r_if = b.literal("if")
        val r_then = b.literal("then")
        val r_else = b.literal("else")
        val r_var = b.rule("var").choice(RuntimeRuleChoiceKind.LONGEST_PRIORITY, b.literal("W"),b.literal("X"),b.literal("Y"),b.literal("Z"))
        val r_ifthen = b.rule("ifthen").concatenation(r_if,r_expr,r_then,r_expr)
        val r_ifthenelse = b.rule("ifthenelse").concatenation(r_if,r_expr,r_then,r_expr,r_else,r_expr)
        val r_conditional = b.rule("conditional").choice(RuntimeRuleChoiceKind.PRIORITY_LONGEST, r_ifthen, r_ifthenelse)
        b.rule(r_expr).choice(RuntimeRuleChoiceKind.LONGEST_PRIORITY, r_var, r_conditional)
        b.rule("S").concatenation(r_expr)
        return b
    }

    @Test
    fun empty_fails() {
        val rrb = this.S()
        val goal = "S"
        val sentence = ""

        val ex = assertFailsWith(ParseFailedException::class) {
            super.test(rrb, goal, sentence)
        }
        assertEquals(1, ex.location.line)
        assertEquals(1, ex.location.column)
    }

    @Test
    fun ifthenelse() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "ifWthenXelseY"

        val expected = """
            S {
              expr {
                conditional {
                    ifthenelse {
                      'if'
                      expr { var { 'W' } }
                      'then'
                      expr { var { 'X' } }
                      'else'
                      expr { var { 'Y' } }
                    }
                }
              }
            }
        """.trimIndent()

        //NOTE: season 35, long expression is dropped in favour of the shorter one!

        super.test(rrb, goal, sentence, expected)
    }

    @Test
    fun ifthen() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "ifWthenX"

        val expected = """
            S {
              expr {
                conditional {
                    ifthen {
                      'if'
                      expr { var { 'W' } }
                      'then'
                      expr { var { 'X' } }
                    }
                }
              }
            }
        """.trimIndent()

        super.test(rrb, goal, sentence, expected)
    }

    @Test
    fun ifthenelseifthen() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "ifWthenXelseifYthenZ"

        val expected = """
            S {
              expr {
                conditional {
                    ifthenelse {
                      'if'
                      expr { var { 'W' } }
                      'then'
                      expr { var { 'X' } }
                      'else'
                      expr {
                        conditional {
                            ifthen {
                              'if'
                              expr { var { 'Y'} }
                              'then'
                              expr { var { 'Z' } }
                            }
                        }
                      }
                    }
                }
              }
            }
        """.trimIndent()

        super.test(rrb, goal, sentence, expected)
    }

    @Test
    fun ifthenifthenelse() {
        val rrb = this.S()
        val goal = "S"
        val sentence = "ifWthenifXthenYelseZ"

        val expected1 = """
         S { expr { conditional { ifthenelse {
                'if'
                expr { var { 'W' } }
                'then'
                expr { conditional { ifthen {
                      'if'
                      expr { var { 'X' } }
                      'then'
                      expr { var { 'Y' } }
                    } } }
                'else'
                expr { var { 'Z' } }
              } } } }
        """.trimIndent()


        super.testStringResult(rrb, goal, sentence, expected1)
        //super.testStringResult(rrb, goal, sentence, expected1, expected2)
    }


}
