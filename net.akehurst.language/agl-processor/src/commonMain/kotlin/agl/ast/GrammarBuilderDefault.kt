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

package net.akehurst.language.agl.ast

import net.akehurst.language.api.grammar.*
import net.akehurst.language.collections.lazyMapNonNull

class GrammarBuilderDefault(val namespace: Namespace, val name: String) {

    private val _literals = lazyMapNonNull<String, Terminal>() {
        TerminalDefault(it, false)
    }

    val grammar: GrammarDefault


    init {
        this.grammar = GrammarDefault(namespace, name, mutableListOf<Rule>());
    }

    fun rule(name: String): RuleBuilder {
        return RuleBuilder(RuleDefault(grammar, name, false, false))
    }

    fun skip(name: String, isLeaf: Boolean = false): RuleBuilder {
        return RuleBuilder(RuleDefault(this.grammar, name, true, isLeaf))
    }

    fun leaf(name: String): RuleBuilder {
        return RuleBuilder(RuleDefault(this.grammar, name, false, true))
    }

    fun terminalLiteral(value: String): Terminal {
        return _literals[value]
    }

    fun terminalPattern(value: String): Terminal {
        return TerminalDefault(value, true)
    }

    fun nonTerminal(name: String): NonTerminal {
        if (name.contains(".")) {
            TODO()
        } else {
            return NonTerminalDefault(name, this.grammar)
        }
    }

    fun concatenation(vararg sequence: ConcatenationItem): Concatenation {
        return ConcatenationDefault(sequence.toList())
    }

    class RuleBuilder(val rule: Rule) {

        fun empty() {
            this.rule.rhs = EmptyRuleDefault()
        }

        fun concatenation(vararg sequence: ConcatenationItem) {
            this.rule.rhs = ChoiceEqualDefault(listOf(ConcatenationDefault(sequence.toList())));
        }

        fun choiceEqual(vararg alternative: Concatenation) {
            //val alternativeConcats = alternative.map { ChoiceEqualDefault(listOf(it)) }
            this.rule.rhs = ChoiceEqualDefault(alternative.asList());
        }

        fun choiceEqual(vararg alternative: ConcatenationItem) {
            val alternativeConcats = alternative.map { ConcatenationDefault(listOf(it)) }
            this.rule.rhs = ChoiceEqualDefault(alternativeConcats);
        }

        fun choicePriority(vararg alternative: Concatenation) {
            //val alternativeConcats = alternative.map { ChoicePriorityDefault(listOf(it)) }
            this.rule.rhs = ChoicePriorityDefault(alternative.asList());
        }

        fun multi(min: Int, max: Int, item: TangibleItem) {
            this.rule.rhs = ChoiceEqualDefault(listOf(ConcatenationDefault(listOf(MultiDefault(min, max, item)))));
        }

        //TODO: original only allows separator to be a TerminalLiteral here,  I think any Terminal is ok though!
        fun separatedList(min: Int, max: Int, separator: Terminal, item: TangibleItem) {
            this.rule.rhs = ChoiceEqualDefault(listOf(ConcatenationDefault(listOf(SeparatedListDefault(min, max, separator, item)))));
        }
    }
}
