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

package net.akehurst.language.parser.scannerless.listSeparated

import net.akehurst.language.agl.runtime.structure.runtimeRuleSet
import net.akehurst.language.parser.scannerless.test_ScannerlessParserAbstract
import kotlin.test.Test

class test_literal_a0n_optSep : test_ScannerlessParserAbstract() {

    // S = [a / sep]*
    // sep = ','?
    // a = 'a'
    private val S = runtimeRuleSet {
        sList("S", 0, -1, "'a'", "sep")
        literal("'a'", "a")
        multi("sep",0,1,"','")
        literal("','",",")
    }

    @Test
    fun empty() {
        val rrs = this.S
        val goal = "S"
        val sentence = ""

        val expected = "S { §empty }"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun a() {
        val rrs = this.S
        val goal = "S"
        val sentence = "a"

        val expected = "S { 'a' }"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun aca() {
        val rrs = this.S
        val goal = "S"
        val sentence = "a,a"

        val expected = "S {'a' sep{','} 'a'}"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun aa() {
        val rrs = this.S
        val goal = "S"
        val sentence = "aa"

        val expected = "S {'a' sep { §empty } 'a'}"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun acaa() {
        val rrs = this.S
        val goal = "S"
        val sentence = "a,aa"

        val expected = "S {'a' sep { ',' } 'a' sep { §empty } 'a'}"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun acaca() {
        val rrs = this.S
        val goal = "S"
        val sentence = "a,a,a"

        val expected = "S {'a' sep{','} 'a' sep{','} 'a'}"

        super.test(rrs, goal, sentence, expected)
    }

    @Test
    fun acax100() {
        val rrs = this.S
        val goal = "S"
        val sentence = "a"+",a".repeat(99)

        val expected = "S {'a'"+" sep{','} 'a'".repeat(99)+"}"

        super.test(rrs, goal, sentence, expected)
    }

}