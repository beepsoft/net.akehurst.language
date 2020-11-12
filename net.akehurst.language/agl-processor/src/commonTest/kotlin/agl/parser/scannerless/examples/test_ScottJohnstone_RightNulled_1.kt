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

package net.akehurst.language.parser.scanondemand.examples

import net.akehurst.language.agl.parser.ScanOnDemandParser
import net.akehurst.language.agl.runtime.structure.*
import net.akehurst.language.api.parser.ParseFailedException
import net.akehurst.language.parser.scanondemand.test_ScanOnDemandParserAbstract
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.*

class test_ScottJohnstone_RightNulled_1 : test_ScanOnDemandParserAbstract() {
    /**
     * S = abAa | aBAa | aba
     * A = a | aA
     * B = b
     */
    /**
     * S = S1 | S2 | 'a' ;
     * S1 = S S S ;
     * S2 = S S ;
     */
    private val rrs = runtimeRuleSet {
        TODO()
    }

    @Test
    fun empty() {
        TODO()
    }

    @Test
    fun a() {
        TODO()
    }

}