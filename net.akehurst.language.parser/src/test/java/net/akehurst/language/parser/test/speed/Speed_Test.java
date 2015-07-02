package net.akehurst.language.parser.test.speed;

import net.akehurst.language.core.parser.IParseTree;
import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.ogl.semanticModel.Grammar;
import net.akehurst.language.ogl.semanticModel.GrammarBuilder;
import net.akehurst.language.ogl.semanticModel.Namespace;
import net.akehurst.language.ogl.semanticModel.NonTerminal;
import net.akehurst.language.ogl.semanticModel.TerminalLiteral;
import net.akehurst.language.parser.runtime.Factory;
import net.akehurst.language.parser.test.AbstractParser_Test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Speed_Test extends AbstractParser_Test {

	@Before
	public void before() {
		this.parseTreeFactory = new Factory();
	}
	
	Grammar abcds() {
		GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("abcds").choice(new NonTerminal("abcd"), new NonTerminal("abcds.group1"));
		b.rule("abcds.group1").concatenation(new NonTerminal("abcd"), new NonTerminal("abcds"));
		b.rule("abcd").concatenation(new NonTerminal("abs"), new NonTerminal("cds"));
		b.rule("abs").choice(new NonTerminal("ab"),new NonTerminal("abs.group1"));
		b.rule("abs.group1").concatenation(new NonTerminal("ab"), new NonTerminal("abs"));
		b.rule("cds").choice(new NonTerminal("cd"),new NonTerminal("cds.group1"));
		b.rule("cds.group1").concatenation(new NonTerminal("cd"), new NonTerminal("cds"));
		b.rule("ab").concatenation(new NonTerminal("a"), new NonTerminal("b"));
		b.rule("cd").concatenation(new NonTerminal("c"), new NonTerminal("d"));
		b.rule("a").choice(new TerminalLiteral("a"));
		b.rule("b").choice(new TerminalLiteral("b"));
		b.rule("c").choice(new TerminalLiteral("c"));
		b.rule("d").choice(new TerminalLiteral("d"));
		return b.get();
	}

	Grammar abcds_2() {
		GrammarBuilder b = new GrammarBuilder(new Namespace("test"), "Test");
		b.rule("abcds").choice(new NonTerminal("abcds.group1"), new NonTerminal("abcd"));
		b.rule("abcds.group1").concatenation(new NonTerminal("abcd"), new NonTerminal("abcds"));
		b.rule("abcd").concatenation(new NonTerminal("abs"), new NonTerminal("cds"));
		b.rule("abs").choice(new NonTerminal("abs.group1"), new NonTerminal("ab"));
		b.rule("abs.group1").concatenation(new NonTerminal("ab"), new NonTerminal("abs"));
		b.rule("cds").choice(new NonTerminal("cds.group1"), new NonTerminal("cd"));
		b.rule("cds.group1").concatenation(new NonTerminal("cd"), new NonTerminal("cds"));
		b.rule("ab").concatenation(new NonTerminal("a"), new NonTerminal("b"));
		b.rule("cd").concatenation(new NonTerminal("c"), new NonTerminal("d"));
		b.rule("a").choice(new TerminalLiteral("a"));
		b.rule("b").choice(new TerminalLiteral("b"));
		b.rule("c").choice(new TerminalLiteral("c"));
		b.rule("d").choice(new TerminalLiteral("d"));
		return b.get();
	}

	
	@Test
	public void abcds_abcds_abcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds();
			String goal = "abcds";
			String text = "abcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void abcds_abcds_abcdabcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds();
			String goal = "abcds";
			String text = "abcdabcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void abcds_abcds_ababcdcdababcdcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds();
			String goal = "abcds";
			String text = "ababcdcdababcdcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void abcds_abcds_abababcdcdcdabababcdcdcdabababcdcdcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds();
			String goal = "abcds";
			String text = "abababcdcdcdabababcdcdcdabababcdcdcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void abcds_abcds_abababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds();
			String goal = "abcds";
			String text = "abababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	@Test
	public void abcds_abcds_abababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcd() {
		// grammar, goal, input
		try {
			Grammar g = abcds_2();
			String goal = "abcds";
			String text = "abababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcdabababcdcdcd";
			
			
			IParseTree tree = this.process(g, text, goal);
			Assert.assertNotNull(tree);
		
		} catch (ParseFailedException e) {
			Assert.fail(e.getMessage());
		}
	}
}