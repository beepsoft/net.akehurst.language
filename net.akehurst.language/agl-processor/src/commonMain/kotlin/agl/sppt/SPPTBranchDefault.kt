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

package net.akehurst.language.agl.sppt

import net.akehurst.language.api.sppt.*
import net.akehurst.language.agl.runtime.structure.RuntimeRule
import net.akehurst.language.api.parser.InputLocation

class SPPTBranchDefault(
        runtimeRule: RuntimeRule,
        location: InputLocation,                     // can't use children.first.startPosition, there may not be any children
        nextInputPosition: Int,                 // dont't use children.sumBy { it.matchedTextLength }, it requires unwanted iteration
        priority: Int
) : SPPTNodeAbstract(
        runtimeRule,
        location,
        nextInputPosition,
        priority
), SPPTBranch
{

    // --- SPPTBranch ---

    override val childrenAlternatives: MutableSet<List<SPPTNode>> = mutableSetOf() //TODO: need to be sure a List is ok here !

    override val children : List<SPPTNode> get() {
        return this.childrenAlternatives.first()
    }

    override val nonSkipChildren: List<SPPTNode> by lazy {
        this.children.filter { !it.isSkip }
    }

    override val branchNonSkipChildren: List<SPPTBranch> by lazy {
        this.children.filter { it.isBranch && !it.isSkip }.filterIsInstance<SPPTBranch>()
    }

    override fun nonSkipChild(index: Int): SPPTNode {
        return this.nonSkipChildren[index]
    }


    override fun branchChild(index: Int): SPPTBranch {
        return this.branchNonSkipChildren[index]
    }


    // --- SPPTNode ---

    override val matchedText: String by lazy {
        this.children.joinToString(separator = "") { it.matchedText }
    }

    override val nonSkipMatchedText: String get() {
        return this.nonSkipChildren.map { it.nonSkipMatchedText }.joinToString("")
    }

    override fun contains(other: SPPTNode): Boolean {
        if (other is SPPTBranch) {
            if (this.identity == other.identity) {
                // for each alternative list of other children, check there is a matching list
                // of children in this alternative children
                var allOthersAreContained = true // if no other children alternatives contain is a match
                for (otherChildren in other.childrenAlternatives) {
                    // for each of this alternative children, find one that 'contains' otherChildren
                    var foundContainMatch = false
                    for (thisChildren in this.childrenAlternatives) {
                        if (thisChildren.size == otherChildren.size) {
                            // for each pair of nodes, one from each of otherChildren thisChildren
                            // check thisChildrenNode contains otherChildrenNode
                            var thisMatch = true
                            for (i in 0 until thisChildren.size) {
                                val thisChildrenNode = thisChildren.get(i)
                                val otherChildrenNode = otherChildren.get(i)
                                thisMatch = thisMatch and thisChildrenNode.contains(otherChildrenNode)
                            }
                            if (thisMatch) {
                                foundContainMatch = true
                                break
                            } else {
                                // if thisChildren alternative doesn't contain, try the next one
                                continue
                            }
                        } else {
                            // if sizes don't match check next in set of this alternative children
                            continue
                        }
                    }
                    allOthersAreContained = allOthersAreContained and foundContainMatch
                }
                return allOthersAreContained
            } else {
                // if identities don't match
                return false
            }

        } else {
            // if other is not a branch
            return false
        }
    }

    override val isEmptyLeaf: Boolean = false

    override val isLeaf: Boolean = false

    override val isBranch: Boolean = true

    override val asLeaf: SPPTLeaf
        get() {
            throw SPPTException("Not a Leaf", null)
        }

    override val asBranch: SPPTBranch = this

    override val lastLocation get() = if (children.isEmpty()) this.location else children.last().lastLocation


    override fun <T, A> accept(visitor: SharedPackedParseTreeVisitor<T, A>, arg: A): T {
        return visitor.visit(this, arg)
    }

    // --- Object ---
    override fun toString(): String {
        val tag = if (null==this.embeddedIn) this.runtimeRule.tag else "${embeddedIn}.${runtimeRule.tag}"
        var r = ""
        r += this.startPosition.toString() + ","
        r += this.nextInputPosition
        r += ":" + tag + "(" + this.runtimeRule.number + ")"
        return r
    }

    override fun hashCode(): Int {
        return this.identity.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SPPTBranch) {
            return false
        } else {
             return if (this.identity != other.identity) {
                false
            } else {
                 this.contains(other) && other.contains(this)
             }
        }
    }
}