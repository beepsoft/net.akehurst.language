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

package net.akehurst.language.agl.runtime.structure

import net.akehurst.language.agl.runtime.structure.RuntimeRuleChoiceKind
import net.akehurst.language.agl.runtime.structure.runtimeRuleSet
import net.akehurst.language.parser.scanondemand.test_ScanOnDemandParserAbstract
import kotlin.test.Test
import kotlin.test.assertEquals

class test_aABCc{

    companion object {
        /*
            S = b | a S c ;

            S = b | S1
            S1 = a S c
         */
        val rrs = runtimeRuleSet {
            choice("S",RuntimeRuleChoiceKind.LONGEST_PRIORITY) {
                literal("b")
                ref("S1")
            }
            concatenation("S1") { literal("a"); ref("S"); literal("c")}
        }

        val S = rrs.findRuntimeRule("S")
        val S1 = rrs.findRuntimeRule("S1")
        val a = rrs.findRuntimeRule("'a'")
        val b = rrs.findRuntimeRule("'b'")
        val c = rrs.findRuntimeRule("'c'")
        val G = rrs.startingState(S, emptySet()).runtimeRule

        val s0 = rrs.startingState(S, emptySet())
    }


    @Test
    fun calcFirstAt() {

        var actual = s0.stateSet.calcFirstAt(RulePosition(G,0,0),BooleanArray(rrs.runtimeRules.size))
        var expected = setOf(b, a)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(G,0,1),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(rrs.END_OF_TEXT)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(G,0,RulePosition.END_OF_RULE),BooleanArray(rrs.runtimeRules.size))
        expected = setOf()
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S,0,0),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(b)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S,0,RulePosition.END_OF_RULE),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(c, rrs.END_OF_TEXT)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S,1,0),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(a)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S,1,0),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(a)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S,1,RulePosition.END_OF_RULE),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(c, rrs.END_OF_TEXT)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S1,0,0),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(a)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S1,0,1),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(b, a)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S1,0,2),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(c)
        assertEquals(expected,actual)

        actual = s0.stateSet.calcFirstAt(RulePosition(S1,0,RulePosition.END_OF_RULE),BooleanArray(rrs.runtimeRules.size))
        expected = setOf(c, rrs.END_OF_TEXT)
        assertEquals(expected,actual)
    }

}