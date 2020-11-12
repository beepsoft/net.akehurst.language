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

import kotlin.test.Test
import kotlin.test.assertEquals

class test_concatenation_abc : test_Abstract() {
    // S =  'a' 'b' 'c' ;
    companion object {

        val rrs = runtimeRuleSet {
            concatenation("S") { literal("a");literal("b");literal("c") }
        }

        val S = rrs.findRuntimeRule("S")
        val SM = rrs.fetchStateSetFor(S)
        val G = SM.startState.runtimeRules.first()

        val a = rrs.findRuntimeRule("'a'")
        val b = rrs.findRuntimeRule("'b'")
        val c = rrs.findRuntimeRule("'c'")

        val s0 = SM.startState
        val s1 = SM.states[listOf(RP(a, 0, RulePosition.END_OF_RULE))]
        val s2 = SM.states[listOf(RP(S, 0, 1))]
        val s3 = SM.states[listOf(RP(b, 0, RulePosition.END_OF_RULE))]
        val s4 = SM.states[listOf(RP(S, 0, 2))]

        val lhs_a = SM.runtimeRuleSet.createLookaheadSet(setOf(a))
        val lhs_b = SM.runtimeRuleSet.createLookaheadSet(setOf(b))
        val lhs_c = SM.runtimeRuleSet.createLookaheadSet(setOf(c))
        val lhs_aU = SM.runtimeRuleSet.createLookaheadSet(setOf(a, UP))
    }

    @Test
    fun firstOf() {
        val rulePositions = listOf(
                Triple(RP(G, 0, RulePosition.START_OF_RULE), lhs_U, setOf(a)), // G = . S
                Triple(RP(G, 0, RulePosition.END_OF_RULE), lhs_U, setOf(UP)), // G = S .
                Triple(RP(S, 0, RulePosition.START_OF_RULE), lhs_U, setOf(a)), // S = . a b c
                Triple(RP(S, 0, 1), lhs_U, setOf(b)), // S = a . b c
                Triple(RP(S, 0, 2), lhs_U, setOf(c)), // S = a b . c
                Triple(RP(S, 0, RulePosition.END_OF_RULE), lhs_U, setOf(UP))   // S = a b c .
        )

        for (t in rulePositions) {
            val rp = t.first
            val lhs = t.second
            val expected = t.third

            val actual = SM.firstOf(rp, lhs.content)

            assertEquals(expected, actual, "failed $rp")
        }
    }

    @Test
    fun calcClosure_G_0_0() {
        val cl_G = ClosureItem(null, RP(G, 0, 0), RP(G, 0, 0), lhs_U)
        val cl_G_S0 = ClosureItem(cl_G, RP(S, 0, 0), RP(G, 0, 0), lhs_b)

        val actual = SM.calcClosure(ClosureItem(null, RP(G, 0, 0), RP(G, 0, 0), lhs_U))
        val expected = setOf(
                cl_G, cl_G_S0
        )
        assertEquals(expected, actual)
    }

    @Test
    fun s0_widthInto() {

        val actual = s0.widthInto(null).toList()

        val expected = listOf(
                Pair(RulePosition(a, 0, RulePosition.END_OF_RULE), lhs_b)
        )
        assertEquals(expected, actual)

    }

    @Test
    fun s0_transitions() {
        val actual = s0.transitions(null)

        val expected = listOf(
                Transition(s0, s1, Transition.ParseAction.WIDTH, lhs_b, LookaheadSet.EMPTY, null) { _, _ -> true }
        ).toList()
        assertEquals(expected.size, actual.size)
        for (i in actual.indices) {
            assertEquals(expected[i], actual[i])
        }
    }

    @Test
    fun s1_heightOrGraftInto_s0() {

        val actual = s1.heightOrGraftInto(s0.rulePositions).toList()

        val expected = listOf(
                HeightGraft(
                        null,
                        listOf(RulePosition(S, 0, 0)),
                        listOf(RulePosition(S, 0, 1)),
                        lhs_b, lhs_U)
        )
        assertEquals(expected, actual)

    }

    @Test
    fun s1_transitions_s0() {
        val actual = s1.transitions(s0)

        val expected = listOf(
                Transition(s1, s2, Transition.ParseAction.HEIGHT, lhs_b, lhs_U, listOf(RP(S, 0, 0))) { _, _ -> true }
        ).toList()
        assertEquals(expected.size, actual.size)
        for (i in actual.indices) {
            assertEquals(expected[i], actual[i])
        }
    }

    @Test
    fun s2_transitions_s0() {
        val actual = s2.transitions(s0)

        val expected = listOf(
                // upLookahead and prevGuard are unused for WIDTH
                Transition(s2, s3, Transition.ParseAction.WIDTH, lhs_c, lhs_E, null) { _, _ -> true }
        ).toList()
        assertEquals(expected.size, actual.size)
        for (i in actual.indices) {
            assertEquals(expected[i], actual[i])
        }
    }

    @Test
    fun s3_transitions_s2() {
        val actual = s3.transitions(s2)

        val expected = listOf(
                Transition(s3, s4, Transition.ParseAction.GRAFT, lhs_c, LookaheadSet.UP, listOf(RP(S,0,1))) { _, _ -> true }
        ).toList()
        assertEquals(expected.size, actual.size)
        for (i in actual.indices) {
            assertEquals(expected[i], actual[i])
        }
    }
}