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

// DataType
public class TerminalEmptyDefault extends TerminalLiteralDefault {

	public TerminalEmptyDefault() {
		super("");
		this.nodeType = new LeafNodeTypeDefault();
	}

	LeafNodeTypeDefault nodeType;

	public NodeType getNodeType() {
		return this.nodeType;
	}

	// --- Object ---
	@Override
	public String toString() {
		return "<empty>";
	}

	@Override
	public int hashCode() {
		return this.getValue().hashCode();
	}

	@Override
	public boolean equals(Object arg) {
		if (arg instanceof TerminalEmptyDefault) {
			TerminalEmptyDefault other = (TerminalEmptyDefault) arg;
			return this.getValue().equals(other.getValue());
		} else {
			return false;
		}
	}

}