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

package net.akehurst.language.agl.regex

import net.akehurst.language.collections.Stack

class RegexMatcherBuilder {

    companion object {

    }

    // List[StateNumber] -> Map<Unicode-Int, List<StateNumber>>
    val nfa = mutableListOf<State>()
    var nextStateNumber = 0
    var stack = Stack<Fragment>()
    var startState: State = RegexMatcher.ERROR_STATE

    // return state number of new state
    private fun createState(isSplit: Boolean): State {
        val state = State(this.nextStateNumber, isSplit)
        this.nextStateNumber++
        this.nfa.add(state)
        return state
    }

    private fun cloneState(orig:State?, nullTransitions:MutableList<Transition>, map:MutableMap<State,State>): State? {
        return if (null==orig) {
            null
        } else {
            val clone = map[orig]
            if (null == clone) {
                val c = this.createState(orig.isSplit)
                map[orig]=c
                val ctrans = orig.outgoing.map { this.cloneTransition(it, nullTransitions, map) }
                c.outgoing.addAll(ctrans)
               c
            } else {
                clone
            }
        }
    }

    fun cloneTransition(orig:Transition, nullTransitions:MutableList<Transition>, map:MutableMap<State,State>): Transition {
        val clone = when(orig) {
            is TransitionEmpty -> TransitionEmpty()
            is TransitionAny -> TransitionAny()
            is TransitionLiteral -> TransitionLiteral(orig.value)
            is TransitionMatcher -> TransitionMatcher(orig.value)
        }
        clone.to = this.cloneState(orig.to, nullTransitions, map)
        if (null==clone.to) {
            nullTransitions.add(clone)
        }
        return clone
    }

    fun clone(orig:Fragment): Fragment {
        val coutgoing = mutableListOf<Transition>()
        val cstart = this.cloneState(orig.start, coutgoing, mutableMapOf())!!
        return Fragment(cstart, coutgoing)
    }

    fun start() {
        val state = this.createState(true)
        state.outgoing.add(TransitionEmpty())
        this.stack.push(Fragment(state, state.outgoing))
        this.startState = state
    }

    fun matchAny() {
        val state = this.createState(false)
        val trans = TransitionAny()
        state.outgoing.add(trans)
        val frag = Fragment(state, state.outgoing)
        this.stack.push(frag)
    }

    fun character(input: Char) {
        val state = this.createState(false)
        val trans = TransitionLiteral(input)
        state.outgoing.add(trans)
        val frag = Fragment(state, state.outgoing)
        this.stack.push(frag)
    }

    fun characterClass(matcher: CharacterMatcher) {
        val state = this.createState(false)
        val trans = TransitionMatcher(matcher)
        state.outgoing.add(trans)
        val frag = Fragment(state, state.outgoing)
        this.stack.push(frag)
    }

    fun concatenate() {
        when (this.stack.size) {
            0 -> {
            }
            1 -> {
                //this.start = this.stack.peek().start
            }
            else -> {
                val f2 = this.stack.pop()
                val f1 = this.stack.pop()
                f1.outgoing.forEach { it.to = f2.start }
                val frag = Fragment(f1.start, f2.outgoing)
                this.stack.push(frag)
                //this.start = f1.start
            }
        }
    }

    fun choice() {
        val f2 = this.stack.pop()
        val f1 = this.stack.pop()
        val split = this.createState(true)
        val t1 = TransitionEmpty()
        t1.to = f1.start
        val t2 = TransitionEmpty()
        t2.to = f2.start
        split.outgoing.add(t1)
        split.outgoing.add(t2)
        val frag = Fragment(split, f1.outgoing + f2.outgoing)
        this.stack.push(frag)
        //this.start = split
    }

    fun multi01() {
        val f1 = this.stack.pop()
        val split = this.createState(true)
        val t1 = TransitionEmpty()
        t1.to = f1.start
        val t2 = TransitionEmpty()
        split.outgoing.add(t1)
        split.outgoing.add(t2)
        val frag = Fragment(split, f1.outgoing + t2)
        this.stack.push(frag)
        //this.start = split
    }

    fun multi1n() {
        val f1 = this.stack.pop()
        val split = this.createState(true)
        f1.outgoing.forEach { it.to = split }
        val t1 = TransitionEmpty()
        t1.to = f1.start
        val t2 = TransitionEmpty()
        split.outgoing.add(t1)
        split.outgoing.add(t2)
        val frag = Fragment(f1.start, listOf(t2))
        this.stack.push(frag)
        //this.start = f1.start
    }

    fun multi0n() {
        val f1 = this.stack.pop()
        val split = this.createState(true)
        val t1 = TransitionEmpty()
        t1.to = f1.start
        val t2 = TransitionEmpty()
        split.outgoing.add(t1)
        split.outgoing.add(t2)
        f1.outgoing.forEach { it.to = split }
        val frag = Fragment(split, listOf(t2))
        this.stack.push(frag)
        //this.start = split
    }

    fun repetition(n:Int, m:Int) {
        val repFrag = this.stack.pop()
        val nRepetitions = mutableListOf<Fragment>()
        for(r in 0 until n) {
            val nextFrag = this.clone(repFrag)
            nRepetitions.add(nextFrag)
        }
        val mRepetitions = mutableListOf<Fragment>()
        if (-1==m) {
            mRepetitions.add(this.clone(repFrag))
        } else {
            for (r in n until m) {
                val nextFrag = this.clone(repFrag)
                mRepetitions.add(nextFrag)
            }
        }
        var needConcat = false //first one doesn't need a concat
        for(r in 0 until n) {
            val nextFrag = nRepetitions[r]
            this.stack.push(nextFrag)
            if (needConcat) {
                this.concatenate()
            } else {
                needConcat = true
            }
        }
        if (-1==m) {
            this.stack.push(mRepetitions[0])
            this.multi0n()
            if (needConcat) {
                this.concatenate()
            } else {
                needConcat = true
            }
        }else {
            for(r in n until m) {
                val nextFrag = mRepetitions[r-n]
                this.stack.push(nextFrag)
                this.multi01()
                if (needConcat) {
                    this.concatenate()
                } else {
                    needConcat = true
                }
            }
        }
    }

    fun startGroup() {
    }

    fun finishGroup() {
    }

    fun build(): RegexMatcher {
        return RegexMatcher(this.startState, this.nfa)
    }

    fun concatenateGoal() {
        if (this.stack.isEmpty) {
            //don't add goal
        } else {
            val f1 = this.stack.pop()
            f1.outgoing.forEach { it.to = RegexMatcher.MATCH_STATE }
            this.startState = f1.start
        }
    }
}
