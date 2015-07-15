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
package net.akehurst.language.grammar.parser.forrest;

public class BranchIdentifier {

	public BranchIdentifier(int ruleNumber, int start, int end) {
		this.ruleNumber = ruleNumber;
		this.start = start;
		this.end = end;
		this.hashCode_cache = ruleNumber ^ start ^ end;
	}
	int ruleNumber;
	int start;
	int end;
	
	@Override
	public boolean equals(Object arg) {
		if (!(arg instanceof BranchIdentifier)) {
			return false;
		}
		BranchIdentifier other = (BranchIdentifier)arg;
		
		return this.start == other.start
			&& this.end == other.end
			&& this.ruleNumber==other.ruleNumber;
	}
	
	int hashCode_cache;
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "("+this.ruleNumber+","+this.start+","+this.end+")";
	}
	
}