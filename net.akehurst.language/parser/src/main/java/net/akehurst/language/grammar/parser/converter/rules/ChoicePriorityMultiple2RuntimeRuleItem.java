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
package net.akehurst.language.grammar.parser.converter.rules;

import java.util.ArrayList;
import java.util.List;

import net.akehurst.language.grammar.parser.converter.Converter;
import net.akehurst.language.grammar.parser.runtime.RuntimeRule;
import net.akehurst.language.grammar.parser.runtime.RuntimeRuleItem;
import net.akehurst.language.grammar.parser.runtime.RuntimeRuleItemKind;
import net.akehurst.language.ogl.semanticStructure.ChoicePriority;
import net.akehurst.language.ogl.semanticStructure.Concatenation;
import net.akehurst.language.ogl.semanticStructure.ConcatenationItem;
import net.akehurst.transform.binary.api.BinaryRule;
import net.akehurst.transform.binary.api.BinaryTransformer;

public class ChoicePriorityMultiple2RuntimeRuleItem extends AbstractChoice2RuntimeRuleItem<ChoicePriority> {

    @Override
    public boolean isValidForLeft2Right(final ChoicePriority left) {
        return 1 < left.getAlternative().size();
    }

    @Override
    public boolean isAMatch(final ChoicePriority left, final RuntimeRuleItem right, final BinaryTransformer transformer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public RuntimeRuleItem constructLeft2Right(final ChoicePriority left, final BinaryTransformer transformer) {
        final Converter converter = (Converter) transformer;
        final int maxRuleRumber = converter.getFactory().getRuntimeRuleSet().getTotalRuleNumber();
        final RuntimeRuleItem right = converter.getFactory().createRuntimeRuleItem(RuntimeRuleItemKind.PRIORITY_CHOICE);
        return right;
    }

    @Override
    public void updateLeft2Right(final ChoicePriority left, final RuntimeRuleItem right, final BinaryTransformer transformer) {

        final List<RuntimeRule> rrAlternatives = new ArrayList<>();

        for (final Concatenation concat : left.getAlternative()) {

            if (concat.getItem().size() > 1) {
                // create a new virtual rule to contain the concatenation
                // items
                final RuntimeRule rr = transformer
                        .transformLeft2Right((Class<? extends BinaryRule<Concatenation, RuntimeRule>>) (Class<?>) Concatenation2RuntimeRule.class, concat);
                rrAlternatives.add(rr);
            } else {
                final RuntimeRule rr = transformer.transformLeft2Right(
                        (Class<? extends BinaryRule<ConcatenationItem, RuntimeRule>>) (Class<?>) AbstractConcatinationItem2RuntimeRule.class,
                        concat.getItem().get(0));
                rrAlternatives.add(rr);
            }
        }

        final RuntimeRule[] items = rrAlternatives.toArray(new RuntimeRule[rrAlternatives.size()]);
        right.setItems(items);

    }

    @Override
    public void updateRight2Left(final ChoicePriority arg0, final RuntimeRuleItem arg1, final BinaryTransformer transformer) {
        // TODO Auto-generated method stub

    }

    @Override
    public ChoicePriority constructRight2Left(final RuntimeRuleItem arg0, final BinaryTransformer transformer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValidForRight2Left(final RuntimeRuleItem arg0) {
        // TODO Auto-generated method stub
        return false;
    }

}
