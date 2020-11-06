package net.akehurst.language.agl.runtime.runtimeRuleSet

import net.akehurst.language.agl.parser.ScanOnDemandParser
import net.akehurst.language.agl.runtime.structure.RuntimeRuleChoiceKind
import net.akehurst.language.agl.runtime.structure.RuntimeRuleSet
import net.akehurst.language.agl.runtime.structure.runtimeRuleSet
import kotlin.test.Test
import kotlin.test.assertEquals

class test_buildFor {

    @Test
    fun concatenation() {
        val rrs = runtimeRuleSet {
            concatenation("S") { literal("a"); literal("c"); literal("c") }
        }

        val actual = rrs.buildFor("S")

        assertEquals(6, actual.states.values.size)
        //TODO: expected Transitions
    }


    @Test
    fun leftRecursion() {
        val rrs = runtimeRuleSet {
            choice("S", RuntimeRuleChoiceKind.LONGEST_PRIORITY) {
                literal("a")
                ref("S1")
            }
            concatenation("S1") { ref("S"); literal("a") }
        }

        val actual = rrs.buildFor("S")

        assertEquals(9, actual.states.values.size)
        assertEquals(12, actual.allBuiltTransitions.size)
        //TODO: expected Transitions
    }

    @Test
    fun nested() {
        val rrs = runtimeRuleSet {
            concatenation("S") { ref("S1") }
            concatenation("S1") { ref("M"); ref("S2") }
            multi("M", 0, -1, "'b'")
            concatenation("S2") { ref("S3") }
            concatenation("S3") { literal("a") }
            literal("'b'", "b")
        }

        val actual = rrs.buildFor("S")

        val parser = ScanOnDemandParser(rrs)
        parser.parse("S", "ba")
        parser.parse("S", "a")

        assertEquals(9, actual.states.values.size)
        assertEquals(12, actual.allBuiltTransitions.size)
        //TODO: expected Transitions
    }

    @Test
    fun xx() {
        val rrs = runtimeRuleSet {
            choice("S",RuntimeRuleChoiceKind.LONGEST_PRIORITY) {
                ref("ABCX");
                ref("S2")
            }
            concatenation("S2") {  ref("S"); literal("y");  ref("S");}
            concatenation("ABCX") {  ref("ABCopt"); literal("x") }
            multi("ABCopt",0,1,"ABC")
            concatenation("ABC") { literal("a"); literal("b"); literal("c") }

        }

        val parser = ScanOnDemandParser(rrs)
        val gr = rrs.findRuntimeRule("S")
        val s0 = rrs.startingState(gr)
        s0.stateSet.build()
        parser.parse("S", "abcx")
        parser.parse("S", "x")
    }

}