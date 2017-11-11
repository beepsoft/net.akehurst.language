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
package net.akehurst.language.parser;

import org.junit.Assert;
import org.junit.Test;

import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.core.sppf.IParseTree;
import net.akehurst.language.core.sppf.ISPPFBranch;
import net.akehurst.language.core.sppf.ISharedPackedParseTree;
import net.akehurst.language.grammar.parser.forrest.ParseTreeBuilder;
import net.akehurst.language.ogl.semanticStructure.Grammar;
import net.akehurst.language.ogl.semanticStructure.GrammarBuilder;
import net.akehurst.language.ogl.semanticStructure.Namespace;
import net.akehurst.language.ogl.semanticStructure.NonTerminal;
import net.akehurst.language.ogl.semanticStructure.TerminalLiteral;
import net.akehurst.language.ogl.semanticStructure.TerminalPattern;

public class Parser_PascalRange_Test extends AbstractParser_Test {
	/*
	 * expr : range | real ; range: integer '..' integer ; integer : "[0-9]+" ; real : "([0-9]+[.][0-9]*)|([.][0-9]+)" ;
	 *
	 */
	Grammar pascal() {
		final GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("expr").choice(new NonTerminal("range"), new NonTerminal("real"));
		b.rule("range").concatenation(new NonTerminal("integer"), new TerminalLiteral(".."), new NonTerminal("integer"));
		b.rule("integer").concatenation(new TerminalPattern("[0-9]+"));
		b.rule("real").concatenation(new TerminalPattern("([0-9]+[.][0-9]*)|([.][0-9]+)"));

		return b.get();
	}

	/**
	 * @param
	 * @throws ParseFailedException
	 */
	@Test
	public void pascal_expr_p5() throws ParseFailedException {
		// grammar, goal, input

		final Grammar g = this.pascal();
		final String goal = "expr";
		final String text = ".5";

		final ISharedPackedParseTree forest = this.process(g, text, goal);

		final ParseTreeBuilder b = this.builder(g, text, goal);
		b.define("expr {");
		b.define("  real { '([0-9]+[.][0-9]*)|([.][0-9]+)' : '.5' }");
		b.define("}");
		final IParseTree expected = b.build();
		// final ISPPFBranch expected = b.branch("expr", b.branch("real", b.leaf("([0-9]+[.][0-9]*)|([.][0-9]+)", ".5")));

		Assert.assertNotNull(forest);
		Assert.assertEquals(expected, forest);

	}

	@Test
	public void pascal_expr_1p() throws ParseFailedException {
		// grammar, goal, input

		final Grammar g = this.pascal();
		final String goal = "expr";
		final String text = "1.";

		final ISharedPackedParseTree tree = this.process(g, text, goal);
		Assert.assertNotNull(tree);

		final ParseTreeBuilder b = this.builder(g, text, goal);
		;
		final ISPPFBranch expected = b.branch("expr", b.branch("real", b.leaf("([0-9]+[.][0-9]*)|([.][0-9]+)", "1.")));
		Assert.assertEquals(expected, tree.getRoot());

	}

	@Test
	public void pascal_expr_1to5() throws ParseFailedException {
		// grammar, goal, input

		final Grammar g = this.pascal();
		final String goal = "expr";
		final String text = "1..5";

		final ISharedPackedParseTree tree = this.process(g, text, goal);
		Assert.assertNotNull(tree);

		final ParseTreeBuilder b = this.builder(g, text, goal);
		;
		final ISPPFBranch expected = b.branch("expr",
				b.branch("range", b.branch("integer", b.leaf("[0-9]+", "1")), b.leaf("..", ".."), b.branch("integer", b.leaf("[0-9]+", "5"))));
		Assert.assertEquals(expected, tree.getRoot());

	}
}
