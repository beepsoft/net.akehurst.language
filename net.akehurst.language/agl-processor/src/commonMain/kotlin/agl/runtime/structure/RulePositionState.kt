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

package net.akehurst.language.agl.runtime.structure

class RulePositionState(
    val stateNumber: Int,
    val rulePosition: RulePosition,
    val heightLookahead: Set<RuntimeRule>,
    val graftLookahead: Set<RuntimeRule>
) {

    companion object {
        val END_OF_RULE = -1
    }

    val items:Set<RuntimeRule> get() { return this.rulePosition.items }
    val runtimeRule:RuntimeRule get() { return this.rulePosition.runtimeRule }
    val choice:Int get() { return this.rulePosition.choice }
    val position:Int get() { return this.rulePosition.position }

    val isAtEnd: Boolean get() { return this.rulePosition.isAtEnd }

    override fun hashCode(): Int {
        return stateNumber
    }

    override fun equals(other: Any?): Boolean {
        return if (other is RulePositionState) {
            other.stateNumber == this.stateNumber
        } else {
            false
        }
    }

    override fun toString(): String {
        return "RP($stateNumber,${rulePosition.runtimeRule.name},${rulePosition.choice},${rulePosition.position},$heightLookahead, $graftLookahead)"
    }

    fun deepEquals(rps2:RulePositionState) :Boolean {
        return this.stateNumber == rps2.stateNumber
            && this.rulePosition == rps2.rulePosition
            && this.heightLookahead == rps2.heightLookahead
            && this.graftLookahead == rps2.graftLookahead
    }

}