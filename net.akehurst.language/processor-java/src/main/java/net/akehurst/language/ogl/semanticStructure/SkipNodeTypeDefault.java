/**
 * Copyright (C) 2015 Dr. David H. Akehurst (http://dr.david.h.akehurst.net)
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
package net.akehurst.language.ogl.semanticStructure;

import net.akehurst.language.api.grammar.NodeType;
import net.akehurst.language.api.grammar.NodeTypeIdentity;

public class SkipNodeTypeDefault implements NodeType {

    private final NodeTypeIdentity identity;

    public SkipNodeTypeDefault(final SkipRuleDefault rule) {
        this.identity = new NodeIdentityDefault(rule.getName());
    }

    @Override
    public NodeTypeIdentity getIdentity() {
        return this.identity;
    }

    // --- Object ---
    @Override
    public String toString() {
        return this.getIdentity().asPrimitive();
    }

    @Override
    public int hashCode() {
        return this.getIdentity().hashCode();
    }

    @Override
    public boolean equals(final Object arg) {
        if (arg instanceof SkipNodeTypeDefault) {
            final SkipNodeTypeDefault other = (SkipNodeTypeDefault) arg;
            return this.getIdentity().equals(other.getIdentity());
        } else {
            return false;
        }
    }
}