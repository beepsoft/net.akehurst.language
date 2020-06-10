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
import kotlin.test.assertNotNull

class test_LR1_States {

    companion object {
        // This grammar is LR(1) but not LALR(1)

        // S = A a | b A c | B c | b B a ;
        // A = d ;
        // B = d ;
        //
        // S = S1 | S2 | S3 | S4
        // S1 = A a ;
        // S2 = b A c ;
        // S3 = B c ;
        // S4 = b B a ;
        // A = d ;
        // B = d ;

        val rrs = runtimeRuleSet {
            choice("S", RuntimeRuleChoiceKind.LONGEST_PRIORITY) {
                ref("S1")
                ref("S2")
                ref("S3")
                ref("S4")
            }
            concatenation("S1") { ref("A"); literal("a") }
            concatenation("S2") { literal("b"); ref("A"); literal("c") }
            concatenation("S3") { ref("B"); literal("c") }
            concatenation("S4") { literal("b"); ref("B"); literal("a") }
            concatenation("A") { literal("d") }
            concatenation("B") { literal("d") }
        }
        val S = rrs.findRuntimeRule("S")
        val S1 = rrs.findRuntimeRule("S1")
        val S2 = rrs.findRuntimeRule("S2")
        val S3 = rrs.findRuntimeRule("S3")
        val S4 = rrs.findRuntimeRule("S4")
        val rA = rrs.findRuntimeRule("A")
        val rB = rrs.findRuntimeRule("B")
        val a = rrs.findRuntimeRule("'a'")
        val b = rrs.findRuntimeRule("'b'")
        val c = rrs.findRuntimeRule("'c'")
        val d = rrs.findRuntimeRule("'d'")
    }

    @Test
    fun s0_closureLR0() {
        val s0 = rrs.startingState(S)
        val actual = s0.closureLR0

        val i0 = LR0ClosureItem(null, RulePosition(s0.runtimeRule, 0, 0))
        val i01 = LR0ClosureItem(i0, RulePosition(S, 0, 0))
        val i02 = LR0ClosureItem(i0, RulePosition(S, 1, 0))
        val i03 = LR0ClosureItem(i0, RulePosition(S, 2, 0))
        val i04 = LR0ClosureItem(i0, RulePosition(S, 3, 0))
        val i011 = LR0ClosureItem(i01, RulePosition(S1, 0, 0))
        val i021 = LR0ClosureItem(i02, RulePosition(S2, 0, 0))
        val i031 = LR0ClosureItem(i03, RulePosition(S3, 0, 0))
        val i041 = LR0ClosureItem(i04, RulePosition(S4, 0, 0))
        val i0111 = LR0ClosureItem(i011, RulePosition(rA, 0, 0))
        val i0311 = LR0ClosureItem(i031, RulePosition(rB, 0, 0))
        val i0211 = LR0ClosureItem(i021, RulePosition(b, 0, 0))
        val i0411 = LR0ClosureItem(i041, RulePosition(b, 0, 0))
        val i01111 = LR0ClosureItem(i0111, RulePosition(d, 0, 0))
        val i03111 = LR0ClosureItem(i0311, RulePosition(d, 0, 0))

        val expected = setOf<LR0ClosureItem>(
                i0, i01, i011, i0111, i01111,
                i02, i021, i0211,
                i03, i031, i0311, i03111,
                i04, i041, i0411
        )

        assertEquals(expected, actual)
    }

    @Test
    fun s0_lookaheadSet() {
        val s0 = rrs.startingState(S)

        val iAd = s0.closureLR0.first { it.rulePosition == RulePosition(d, 0, 0) && it.parentItem?.rulePosition?.runtimeRule == rA }
        val iBd = s0.closureLR0.first { it.rulePosition == RulePosition(d, 0, 0) && it.parentItem?.rulePosition?.runtimeRule == rB }
        val iS2b = s0.closureLR0.first { it.rulePosition == RulePosition(b, 0, 0) && it.parentItem?.rulePosition?.runtimeRule == S2 }
        val iS4b = s0.closureLR0.first { it.rulePosition == RulePosition(b, 0, 0) && it.parentItem?.rulePosition?.runtimeRule == S4 }

        val actual_iAd = s0.lookaheadSet(iAd, LookaheadSet.EMPTY)
        val expected_iAd = LookaheadSet(0, s0, arrayOf(a))
        assertEquals(expected_iAd, actual_iAd)
        assertEquals(expected_iAd.content.toList(), actual_iAd.content.toList())

        val actual_iBd = s0.lookaheadSet(iBd, LookaheadSet.EMPTY)
        val expected_iBd = LookaheadSet(1, s0, arrayOf(c))
        assertEquals(expected_iBd, actual_iBd)
        assertEquals(expected_iBd.content.toList(), actual_iBd.content.toList())

        val actual_iS2b = s0.lookaheadSet(iS2b, LookaheadSet.EMPTY)
        val expected_iS2b = LookaheadSet(2, s0, arrayOf(d))
        assertEquals(expected_iS2b, actual_iS2b)
        assertEquals(expected_iS2b.content.toList(), actual_iS2b.content.toList())

        val actual_iS4b = s0.lookaheadSet(iS4b, LookaheadSet.EMPTY)
        val expected_iS4b = LookaheadSet(3, s0, arrayOf(d))
        assertEquals(expected_iS4b, actual_iS4b)
        assertEquals(expected_iS4b.content.toList(), actual_iS4b.content.toList())
    }

    @Test
    fun s0_transitions() {
        val s0 = rrs.startingState(S)

        val actual_s0 = s0.transitions(null, LookaheadSet.EMPTY)

        val s2 = s0.stateSet.fetch(RulePosition(b, 0, RulePosition.END_OF_RULE))
        val s1 = s0.stateSet.fetch(RulePosition(d, 0, RulePosition.END_OF_RULE))
        val expected_s0 = listOf<Transition>(
                Transition(s0, s1, Transition.ParseAction.WIDTH, LookaheadSet(0, s0, arrayOf(a)), null, { _, _ -> true }),
                Transition(s0, s2, Transition.ParseAction.WIDTH, LookaheadSet(4, s0, arrayOf(d)), null, { _, _ -> true }),
                Transition(s0, s1, Transition.ParseAction.WIDTH, LookaheadSet(2, s0, arrayOf(c)), null, { _, _ -> true })
        )
        assertEquals(expected_s0, actual_s0)
    }


    @Test
    fun s0_createClosure() {
        val s0 = rrs.startingState(S)
        val actual = s0.createClosure(emptySet())

        val expected = setOf<RulePositionWithLookahead>(
                RulePositionWithLookahead(RulePosition(s0.runtimeRule, 0, 0), setOf()),
                RulePositionWithLookahead(RulePosition(S, 0, 0), setOf(RuntimeRuleSet.END_OF_TEXT)),
                RulePositionWithLookahead(RulePosition(S, 1, 0), setOf(RuntimeRuleSet.END_OF_TEXT)),
                RulePositionWithLookahead(RulePosition(S, 2, 0), setOf(RuntimeRuleSet.END_OF_TEXT)),
                RulePositionWithLookahead(RulePosition(S, 3, 0), setOf(RuntimeRuleSet.END_OF_TEXT)),
                RulePositionWithLookahead(RulePosition(S1, 0, 0), setOf(a)),
                RulePositionWithLookahead(RulePosition(S2, 0, 0), setOf(d)),
                RulePositionWithLookahead(RulePosition(S3, 0, 0), setOf(c)),
                RulePositionWithLookahead(RulePosition(S4, 0, 0), setOf(d)),
                RulePositionWithLookahead(RulePosition(rA, 0, 0), setOf(a)), //NOT c, because the A in S2 is not at pos 0
                RulePositionWithLookahead(RulePosition(rB, 0, 0), setOf(c)), //NOT a, because the B in S4 is not at pos 0
                RulePositionWithLookahead(RulePosition(b, 0, 0), setOf(d)),
                RulePositionWithLookahead(RulePosition(d, 0, 0), setOf(a)),
                RulePositionWithLookahead(RulePosition(d, 0, 0), setOf(c))
        )

        assertEquals(expected, actual)
    }

    @Test
    fun s1_growsInto() {
        // G
        val s0 = rrs.startingState(S)
        val trans_s0 = s0.transitions(null, LookaheadSet.EMPTY)
        val tr_a_d = trans_s0.first { it.lookaheadGuard.content.contains(a) }
        val tr_d_b = trans_s0.first { it.lookaheadGuard.content.contains(d) }
        val tr_c_d = trans_s0.first { it.lookaheadGuard.content.contains(c) }
        // - WIDTH -> d
        val s1 = s0.stateSet.fetch(RulePosition(d, 0, RulePosition.END_OF_RULE))
        val actual = s1.growsInto(s0, tr_a_d.lookaheadGuard)
        assertNotNull(actual)
        val expected = listOf<Pair<RulePosition, LookaheadSet>>(
                Pair(RulePosition(rA, 0, 0), LookaheadSet(5, s0, arrayOf(a))),
                Pair(RulePosition(rB, 0, 0), LookaheadSet(6, s0, arrayOf(c)))
        )

        assertEquals(expected, actual)
    }

    @Test
    fun s1_transitions() {
        val s0 = rrs.startingState(S)
        val s0_trans = s0.transitions(null, LookaheadSet.EMPTY)
        val s1 = s0.stateSet.fetch(RulePosition(d, 0, RulePosition.END_OF_RULE))
        val tr_a_d = s0_trans.first { it.lookaheadGuard.content.contains(a) }

        val actual = s1.transitions(s0, tr_a_d.lookaheadGuard)
        val s3 = s0.stateSet.fetch(RulePosition(S2, 0, 1))
        val s4 = s0.stateSet.fetch(RulePosition(S4, 0, 1))
        val expected = listOf<Transition>(
                Transition(s0, s1, Transition.ParseAction.WIDTH, LookaheadSet(0, s0, arrayOf(a)), null, { _, _ -> true }),
                Transition(s0, s1, Transition.ParseAction.WIDTH, LookaheadSet(4, s0, arrayOf(d)), null, { _, _ -> true }),
                Transition(s0, s1, Transition.ParseAction.WIDTH, LookaheadSet(2, s0, arrayOf(c)), null, { _, _ -> true })
        )
    }
/*
    @Test
    fun transitions() {
        // G(s0) -*-> ?
        val s0 = rrs.startingState(S)
        val actual_s0 = s0.transitions(null, LookaheadSet.EMPTY)

        val s1 = s0.stateSet.fetch(RulePosition(b, 0, RulePosition.END_OF_RULE))
        val s2 = s0.stateSet.fetch(RulePosition(d, 0, RulePosition.END_OF_RULE))
        val expected_s0 = listOf<Transition>(
                Transition(s0, s1, Transition.ParseAction.WIDTH, setOf(d), null, { _, _ -> true }),
                Transition(s0, s2, Transition.ParseAction.WIDTH, setOf(a,c), null, { _, _ -> true })
        )
        assertEquals(expected_s0, actual_s0)

        // G(s0) -WIDTH-> b(s1) -*-> ?
        val actual_s0_s1 = s1.transitions(s0)
        val s3 = s0.stateSet.fetch(RulePosition(S2, 0, 1))
        val s4 = s0.stateSet.fetch(RulePosition(S4, 0, 1))
        val expected_s0_s1 = listOf<Transition>(
                Transition(s1, s3, Transition.ParseAction.HEIGHT, setOf(d), null, { _, _ -> true }),
                Transition(s1, s4, Transition.ParseAction.HEIGHT, setOf(d), null, { _, _ -> true })
        )
        assertEquals(expected_s0_s1, actual_s0_s1)

        // G(s0) -WIDTH-> b(s1) -HEIGHT-> S2(s3) -*-> ?
        val actual_s0_s3 = s3.transitions(s0)
        val expected_s0_s3 = listOf<Transition>(
                Transition(s3, s2, Transition.ParseAction.WIDTH, setOf(c), null, { _, _ -> true })
        )
        assertEquals(expected_s0_s3, actual_s0_s3)

        // G(s0) -WIDTH-> b(s1) -HEIGHT-> S4(s4) -*-> ?
        val actual_s4 = s4.transitions(s0)
        val expected_s4 = listOf<Transition>(
                Transition(s4, s2, Transition.ParseAction.WIDTH, setOf(a), null, { _, _ -> true })
        )
        assertEquals(expected_s4, actual_s4)

        // G(s0) -WIDTH-> d(s2) -*-> ?
        val actual_s0_s2 = s2.transitions(s0)
        val s5 = s0.stateSet.fetch(RulePosition(rA, 0, RulePosition.END_OF_RULE))
        val s6 = s0.stateSet.fetch(RulePosition(rB, 0, RulePosition.END_OF_RULE))
        val expected_s0_s2 = listOf<Transition>(
                Transition(s2, s5, Transition.ParseAction.HEIGHT, setOf(a,c), null, { _, _ -> true }),
                Transition(s2, s6, Transition.ParseAction.HEIGHT, setOf(a,c), null, { _, _ -> true })
        )
        assertEquals(expected_s0_s2, actual_s0_s2)

        // G(s0) -WIDTH-> b(s1) -HEIGHT-> S2(s4) -WIDTH-> d(s2) -*-> ?
        val actual_s4_s2 = s2.transitions(s4)
        val expected_s4_s2 = listOf<Transition>(
                Transition(s2, s6, Transition.ParseAction.HEIGHT, setOf(a,c), null, { _, _ -> true })
        )
        assertEquals(expected_s4_s2, actual_s4_s2)

    }
*/

}