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
import net.akehurst.language.core.sppf.ISPPFBranch;
import net.akehurst.language.core.sppf.IParseTree;
import net.akehurst.language.grammar.parser.forrest.ParseTreeBuilder;
import net.akehurst.language.ogl.semanticStructure.Grammar;
import net.akehurst.language.ogl.semanticStructure.GrammarBuilder;
import net.akehurst.language.ogl.semanticStructure.Namespace;
import net.akehurst.language.ogl.semanticStructure.NonTerminal;
import net.akehurst.language.ogl.semanticStructure.TerminalLiteral;
import net.akehurst.language.ogl.semanticStructure.TerminalPattern;

public class Parser_PriorityChoice_Test extends AbstractParser_Test {

	Grammar abc() {
		final GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("abc").priorityChoice(new NonTerminal("a"), new NonTerminal("b"), new NonTerminal("c"));
		b.rule("a").concatenation(new TerminalLiteral("a"));
		b.rule("b").concatenation(new TerminalLiteral("b"));
		b.rule("c").concatenation(new TerminalLiteral("c"));
		return b.get();
	}

	Grammar aempty() {
		final GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("a").priorityChoice();
		return b.get();
	}

	@Test
	public void aempty_a_empty() {
		// grammar, goal, input
		try {
			final Grammar g = this.aempty();
			final String goal = "a";
			final String text = "";

			final IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);

			final ParseTreeBuilder b = this.builder(g, text, goal);
			final ISPPFBranch expected = b.branch("a", b.emptyLeaf("a"));
			Assert.assertEquals(expected, tree.getRoot());

		} catch (final ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void abc_abc_a() {
		// grammar, goal, input
		try {
			final Grammar g = this.abc();
			final String goal = "abc";
			final String text = "a";

			final IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);

			final ParseTreeBuilder b = this.builder(g, text, goal);
			final ISPPFBranch expected = b.branch("abc", b.branch("a", b.leaf("a", "a")));
			Assert.assertEquals(expected, tree.getRoot());

		} catch (final ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void abc_abc_b() {
		// grammar, goal, input
		try {
			final Grammar g = this.abc();
			final String goal = "abc";
			final String text = "b";

			final IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);

			final ParseTreeBuilder b = this.builder(g, text, goal);
			final ISPPFBranch expected = b.branch("abc", b.branch("b", b.leaf("b", "b")));

			Assert.assertEquals(expected, tree.getRoot());

		} catch (final ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void abc_abc_c() {
		// grammar, goal, input
		try {
			final Grammar g = this.abc();
			final String goal = "abc";
			final String text = "c";

			final IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);

			final ParseTreeBuilder b = this.builder(g, text, goal);
			final ISPPFBranch expected = b.branch("abc", b.branch("c", b.leaf("c", "c")));

			Assert.assertEquals(expected, tree.getRoot());

		} catch (final ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}

	Grammar kwOrId1() {
		final GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("S").priorityChoice(new NonTerminal("type"));
		b.rule("type").priorityChoice(new NonTerminal("id"), new NonTerminal("kw"));
		b.rule("kw").concatenation(new TerminalLiteral("int"));
		b.rule("id").concatenation(new TerminalPattern("[a-z]+"));
		return b.get();
	}

	@Test
	public void kwOrId_S_int() throws ParseFailedException {
		// grammar, goal, input

		final Grammar g = this.kwOrId1();
		final String goal = "S";
		final String text = "int";

		final IParseTree tree = this.process(g, text, goal);
		Assert.assertNotNull(tree);

		final ParseTreeBuilder b = this.builder(g, text, goal);
		final ISPPFBranch expected = b.branch("S", b.branch("type", b.branch("id", b.leaf("[a-z]+", "int"))));

		Assert.assertEquals(expected, tree.getRoot());

	}

	Grammar kwOrId2() {
		final GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("S").priorityChoice(new NonTerminal("type"));
		b.rule("type").priorityChoice(new NonTerminal("kw"), new NonTerminal("id"));
		b.rule("kw").concatenation(new TerminalLiteral("int"));
		b.rule("id").concatenation(new TerminalPattern("[a-z]+"));
		return b.get();
	}

	@Test
	public void kwOrId_S_id() throws ParseFailedException {
		// grammar, goal, input

		final Grammar g = this.kwOrId2();
		final String goal = "S";
		final String text = "int";

		final IParseTree tree = this.process(g, text, goal);
		Assert.assertNotNull(tree);

		final ParseTreeBuilder b = this.builder(g, text, goal);
		b.define("S {");
		b.define("  type {");
		b.define("    kw { 'int' }");
		b.define("  }");
		b.define("}");
		final IParseTree expected = b.build();

		Assert.assertEquals(expected, tree);

	}
	// more tests needed!!!!
}
