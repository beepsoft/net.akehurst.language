/**
 * Copyright (C) 2020 Dr. David H. Akehurst (http://dr.david.h.akehurst.net)
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

package net.akehurst.language.processor

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class test_TutorialExamples {


    @Test
    fun example1() {

    }

    @Test
    fun example3_1() {
        val grammarStr = """
            namespace test
            grammar Test {
                typeReference =  userDefinedType | builtInType;
                builtInType = 'int' | 'boolean' | 'real' ;
                userDefinedType = NAME ;
                leaf NAME = "[a-zA-Z][a-zA-Z0-9]*" ;
            }
        """.trimIndent()
        val processor = Agl.processor(grammarStr)

        val sppt = processor.parse("int")
        val actual = sppt.toStringAll.trim()
        assertNotNull(sppt)

        val expected = """
            typeReference { builtInType { 'int' } }
        """.trimIndent()

        assertEquals(expected, actual)
    }

    @Test
    fun example3_3() {
        val grammarStr = """
            namespace test
            grammar Test {
                typeReference =  builtInType | userDefinedType;
                userDefinedType = NAME ;
                builtInType = 'int' | 'boolean' | 'real' ;
                leaf NAME = "[a-zA-Z][a-zA-Z0-9]*" ;
            }
        """.trimIndent()
        val processor = Agl.processor(grammarStr)

        val sppt = processor.parse("int")
        val actual = sppt.toStringAll.trim()
        assertNotNull(sppt)

        val expected = "typeReference { builtInType { 'int' } }"

        assertEquals(expected, actual)
    }

    @Test
    fun example3_4() {
        val grammarStr = """
            namespace test
            grammar Test {
                typeReference = builtInType | userDefinedType ;
                builtInType = 'int' | 'boolean' | 'real' ;
                userDefinedType = NAME ;
                NAME = "[a-zA-Z][a-zA-Z0-9]*" ;
            }
        """.trimIndent()
        val processor = Agl.processor(grammarStr)

        val sppt = processor.parse("prop : int")
        val actual = sppt.toStringAll
        assertNotNull(sppt)

        val expected = """
            propertyDeclaration {
                typeReference { userDefinedType { 'NAME' : 'int' } }
            }
        """.trimIndent()

        assertEquals(expected, actual)
    }

}

