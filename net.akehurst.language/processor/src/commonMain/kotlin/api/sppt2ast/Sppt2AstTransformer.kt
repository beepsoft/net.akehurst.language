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

package net.akehurst.language.api.sppt2ast

import net.akehurst.language.api.sppt.SharedPackedParseTree

/**
 *
 * A Semantic Analyser converts a Parse Tree (in this case a SharedPackedParseTree) into a "Semantic Model". i.e. it will map the parse tree to some other data
 * structure that contains carries more semantic meaning than the parse tree, maybe mapping certain string values into actual object references that the string
 * value implies.
 *
 */
interface Sppt2AstTransformer {

	/**
	 * reset the sppt2ast, clearing any cached values
	 */
	fun clear()

	/**
	 * map the tree into an instance of the targetType
	 *
	 */
	fun <T> transform(sppt: SharedPackedParseTree): T
}