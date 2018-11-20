/**
 * Copyright (C) 2018 Dr. David H. Akehurst (http://dr.david.h.akehurst.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.akehurst.language.ogl.grammar

import net.akehurst.language.processor.processor
import kotlin.test.Test
import kotlin.test.assertNotNull

class test_OglGrammar {

    @Test
    fun empty() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun pattern() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = "[a-c]" ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_concatenation() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a' 'b' 'c' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_choice_equal() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a' | 'b' | 'c' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_choice_priority() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a' < 'b' < 'c' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_multi_0_1() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a'? ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_multi_0_n() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a'* ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun literal_multi_1_n() {

        val grammarStr = """
            namespace test
            grammar Test {
                a = 'a'+ ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun nonTerminal_concatenation() {

        val grammarStr = """
            namespace test
            grammar Test {
                r = a b c ;
                a = 'a' ;
                b = 'b' ;
                c = 'c' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun nonTerminal_multi_0_1() {

        val grammarStr = """
            namespace test
            grammar Test {
                r = a ? ;
                a = 'a' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun nonTerminal_multi_0_n() {

        val grammarStr = """
            namespace test
            grammar Test {
                r = a* ;
                a = 'a' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }

    @Test
    fun nonTerminal_multi_1_n() {

        val grammarStr = """
            namespace test
            grammar Test {
                r = a+ ;
                a = 'a' ;
            }
        """.trimIndent()

        val p = processor(grammarStr)


        assertNotNull(p)
    }
}
