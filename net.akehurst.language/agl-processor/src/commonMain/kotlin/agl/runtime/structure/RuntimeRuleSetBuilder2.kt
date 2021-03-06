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

import net.akehurst.language.api.parser.ParserException

fun runtimeRuleSet(init: RuntimeRuleSetBuilder2.() -> Unit): RuntimeRuleSet {
    val b = RuntimeRuleSetBuilder2()
    b.init()
    return b.ruleSet()
}

class RuntimeRuleSetBuilder2() {

    private var runtimeRuleSet: RuntimeRuleSet? = null

    val ruleBuilders: MutableList<RuntimeRuleBuilder> = mutableListOf()

    fun findRuleBuilderByTag(tag: String): RuntimeRuleBuilder? {
        return this.ruleBuilders.firstOrNull {
            it.tag == tag
        }
    }

    fun ruleSet(): RuntimeRuleSet {
        if (null == this.runtimeRuleSet) {
            //build and validate
            val rules = this.ruleBuilders.mapIndexed { index, rb ->
                rb.buildRule(index)
            }
            val ruleMap = mutableMapOf<String, RuntimeRule>()
            rules.forEach { ruleMap[it.tag] = it }
            val rbs = this.ruleBuilders.toList() //to stop concuttent modification
            rbs.forEach { rb ->
                when (rb.kind) {
                    RuntimeRuleKind.GOAL -> {
                        TODO()
                    }
                    RuntimeRuleKind.TERMINAL -> {
                        val rule = rb.rule!!
                        if (null != rule.rhsOpt) {
                            throw ParserException("Invalid Rule ${rule.tag}")
                        }
                    }
                    RuntimeRuleKind.NON_TERMINAL -> {
                        val rule = rb.buildRhs(ruleMap)
                        if (null == rule.rhsOpt) {
                            throw ParserException("Invalid Rule ${rule.tag}")
                        }
                    }
                    RuntimeRuleKind.EMBEDDED -> {
                        val rule = rb.rule!!
                        if (null == rule.embeddedRuntimeRuleSet) {
                            throw ParserException("Invalid Rule ${rule.tag}")
                        }
                        if (null == rule.embeddedStartRule) {
                            throw ParserException("Invalid Rule ${rule.tag}")
                        }
                    }
                }
            }
            this.runtimeRuleSet = RuntimeRuleSet(ruleMap.values.toList())
        }
        return this.runtimeRuleSet ?: error("Should never happen")
    }

    fun skip(tag: String, init: RuntimeRuleItemsBuilder.() -> Unit) {
        val rhsB = RuntimeRuleItemsBuilder(this, RuntimeRuleItemKind.CONCATENATION, RuntimeRuleChoiceKind.NONE, -1, 0, isSkip = true)
        rhsB.init()
        val rb = RuntimeRuleBuilder(this, tag, "", RuntimeRuleKind.NON_TERMINAL, false, true, rhsB)
        this.ruleBuilders.add(rb)
    }

    fun empty(ruleThatIsEmpty: RuntimeRule): RuntimeRuleBuilder {
        val name = "§empty." + ruleThatIsEmpty.tag
        val rhsB = RuntimeRuleItemsBuilder(this, RuntimeRuleItemKind.EMPTY, RuntimeRuleChoiceKind.NONE, -1, 0, false, false)
        rhsB.ref(ruleThatIsEmpty.tag)
        val rb = RuntimeRuleBuilder(this, name, name, RuntimeRuleKind.TERMINAL, false, false, rhsB)
        this.ruleBuilders.add(rb)
        return rb
    }

    fun empty(name: String) {
        val rhsB = RuntimeRuleItemsBuilder(this, RuntimeRuleItemKind.EMPTY, RuntimeRuleChoiceKind.NONE, -1, 0, false, true)
        val rb = RuntimeRuleBuilder(this, name, name, RuntimeRuleKind.TERMINAL, false, false, rhsB)
        this.ruleBuilders.add(rb)
    }

    fun literal(tag: String, value: String, isSkip: Boolean = false) {
        val existing = this.findRuleBuilderByTag(tag)
        if (null == existing) {
            val rb = RuntimeRuleBuilder(this, tag, value, RuntimeRuleKind.TERMINAL, false, isSkip)
            this.ruleBuilders.add(rb)
        } else {
            //do nothing // throw RuntimeException("Already got a rule with tag = $name")
        }
    }

    fun pattern(tag: String, pattern: String, isSkip: Boolean = false) {
        val existing = this.findRuleBuilderByTag(tag)
        if (null == existing) {
            val rb = RuntimeRuleBuilder(this, tag, pattern, RuntimeRuleKind.TERMINAL, true, isSkip)
            this.ruleBuilders.add(rb)
        } else {
            //do nothing //throw RuntimeException("Already got a rule with tag = $name")
        }
    }

    private fun _rule(tag: String, kind: RuntimeRuleItemKind, choiceKind: RuntimeRuleChoiceKind, min: Int = -1, max: Int = 0, init: RuntimeRuleItemsBuilder.() -> Unit): RuntimeRuleBuilder {
        val rhsB = RuntimeRuleItemsBuilder(this, kind, choiceKind, min, max)
        when (kind) {
            RuntimeRuleItemKind.MULTI -> if (min == 0) rhsB.addEmptyRule = true
        }
        rhsB.init()
        val rb = RuntimeRuleBuilder(this, tag, "", RuntimeRuleKind.NON_TERMINAL, false, false, rhsB)
        this.ruleBuilders.add(rb)
        return rb
    }

    fun concatenation(name: String, init: RuntimeRuleItemsBuilder.() -> Unit): RuntimeRuleBuilder = _rule(name, RuntimeRuleItemKind.CONCATENATION, RuntimeRuleChoiceKind.NONE, -1, 0, init)
    fun choice(name: String, choiceKind: RuntimeRuleChoiceKind, init: RuntimeRuleItemsBuilder.() -> Unit): RuntimeRuleBuilder = _rule(name, RuntimeRuleItemKind.CHOICE, choiceKind, -1, 0, init)
    fun multi(name: String, min: Int, max: Int, itemRef: String): RuntimeRuleBuilder {
        return _rule(name, RuntimeRuleItemKind.MULTI, RuntimeRuleChoiceKind.NONE, min, max) {
            ref(itemRef)
            empty()
        }
    }

    fun sList(name: String, min: Int, max: Int, itemRef: String, sepRef: String): RuntimeRuleBuilder {
        return _rule(name, RuntimeRuleItemKind.SEPARATED_LIST, RuntimeRuleChoiceKind.NONE, min, max) {
            ref(itemRef)
            ref(sepRef)
            empty()
        }
    }

    fun embedded(tag: String, embeddedRuleSet: RuntimeRuleSet, startRule: RuntimeRule): RuntimeRuleBuilder {
        val rb = RuntimeRuleBuilder(this, tag, "", RuntimeRuleKind.EMBEDDED, false, false, null, embeddedRuleSet, startRule)
        this.ruleBuilders.add(rb)
        return rb
    }
}


class RuntimeRuleBuilder(
        val rrsb: RuntimeRuleSetBuilder2,
        val tag: String,
        val value: String,
        val kind: RuntimeRuleKind,
        val isPattern: Boolean,
        val isSkip: Boolean,
        val rhsBuilder: RuntimeRuleItemsBuilder? = null,
        val embeddedRuleSet: RuntimeRuleSet? = null,
        val startRule: RuntimeRule? = null
) {
    var rule: RuntimeRule? = null

    fun buildRule(number: Int): RuntimeRule {
        if (null == this.rule) {
            this.rule = RuntimeRule(number, tag, value, kind, isPattern, isSkip, embeddedRuleSet, startRule)
        }
        return this.rule!!
    }

    fun buildRhs(ruleMap: MutableMap<String, RuntimeRule>): RuntimeRule {
        val rhs = rhsBuilder!!.build(ruleMap, this.rule!!)
        this.rule!!.rhsOpt = rhs
        return this.rule!!
    }
}

class RuntimeRuleItemsBuilder(
        val rrsb: RuntimeRuleSetBuilder2,
        val kind: RuntimeRuleItemKind,
        val choiceKind: RuntimeRuleChoiceKind,
        val min: Int,
        val max: Int,
        val isSkip: Boolean = false,
        var addEmptyRule: Boolean = false
) {

    private val items = mutableListOf<RuntimeRuleRef>()

    fun empty() {
        addEmptyRule = true
    }

    fun literal(value: String): RuntimeRuleRef {
        val tag = "'$value'"
        this.rrsb.literal(tag, value)
        return ref(tag)
    }

    fun pattern(pattern: String): RuntimeRuleRef {
        val tag = "\"$pattern\""
        this.rrsb.pattern(tag, pattern)
        return ref(tag)
    }

    fun ref(name: String): RuntimeRuleRef {
        val ref = RuntimeRuleRef(name)
        items.add(ref)
        return ref
    }

    fun build(ruleMap: MutableMap<String, RuntimeRule>, rr: RuntimeRule): RuntimeRuleItem {
        val items2 = if (addEmptyRule) {
            val er = this.rrsb.empty(rr)
            val nextRuleNumber = ruleMap.size
            val r = er.buildRule(nextRuleNumber)
            r.rhsOpt = RuntimeRuleItem(RuntimeRuleItemKind.EMPTY, RuntimeRuleChoiceKind.NONE, -1, 0, arrayOf(rr))
            ruleMap[r.tag] = r
            items + RuntimeRuleRef(er.tag)
        } else {
            items
        }
        val rItems = items2.map {
            ruleMap[it.tag]
                    ?: error("Rule ${it.tag} not found")
        }
        val rhs = RuntimeRuleItem(this.kind, this.choiceKind, this.min, this.max, rItems.toTypedArray())
        return rhs
    }
}

data class RuntimeRuleRef(
        val tag: String
) {

}