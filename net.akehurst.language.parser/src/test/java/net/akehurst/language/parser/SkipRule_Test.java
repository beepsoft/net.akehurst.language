package net.akehurst.language.parser;

import net.akehurst.language.core.parser.IBranch;
import net.akehurst.language.core.parser.IParseTree;
import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.ogl.semanticModel.Grammar;
import net.akehurst.language.ogl.semanticModel.GrammarBuilder;
import net.akehurst.language.ogl.semanticModel.Namespace;
import net.akehurst.language.ogl.semanticModel.NonTerminal;
import net.akehurst.language.ogl.semanticModel.TerminalLiteral;
import net.akehurst.language.ogl.semanticModel.TerminalPattern;
import net.akehurst.language.parser.forrest.ParseTreeBuilder;

import org.junit.Assert;
import org.junit.Test;

public class SkipRule_Test extends AbstractParser_Test {
	
	Grammar as() {
		GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.skip("WS").concatination( new TerminalPattern("\\s+") );

		b.rule("as").multi(1,-1,new NonTerminal("a"));
		b.rule("a").concatination(new TerminalLiteral("a"));
		return b.get();
	}
	
	@Test
	public void as_as_a() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = "a";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 2}",st);
			
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("a",
						b.leaf("a", "a")
					)
				);
			
			Assert.assertEquals(expected, tree.getRoot());
			
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_WSa() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = " a";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 3}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"]]",nt);
			
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());
			
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_aaa() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = "aaa";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 4}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [a : ['a' : \"a\"], a : ['a' : \"a\"], a : ['a' : \"a\"]]",nt);
			
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("a",
						b.leaf("a", "a")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());
			
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_aWSaWSa() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = "a a a";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 6}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"]]",nt);
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());
			
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_WSaWSaWSa() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = " a a a";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 7}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"]]",nt);
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_aWSaWSaWS() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = "a a a ";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 7}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"]]",nt);
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void as_as_WSaWSaWSaWS() {
		// grammar, goal, input
		try {
			Grammar g = as();
			String goal = "as";
			String text = " a a a ";
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
			
			ToStringVisitor v = new ToStringVisitor("", "");
			String st = tree.accept(v, "");
			
			Assert.assertEquals("Tree {*as 1, 8}",st);
			
			String nt = tree.getRoot().accept(v, "");
			//Assert.assertEquals("as : [a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"], WS : [\"\\s+\" : \" \"], a : ['a' : \"a\"]]",nt);
			ParseTreeBuilder b = new ParseTreeBuilder(this.as(), goal, text);
			IBranch expected = 
				b.branch("as",
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					),
					b.branch("a",
						b.leaf("a", "a")
					),
					b.branch("WS",
						b.leaf("\\s+", " ")
					)
				);
			Assert.assertEquals(expected, tree.getRoot());

		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
}