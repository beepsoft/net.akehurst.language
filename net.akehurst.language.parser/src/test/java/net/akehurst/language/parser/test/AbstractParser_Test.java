package net.akehurst.language.parser.test;

import net.akehurst.language.core.parser.INodeType;
import net.akehurst.language.core.parser.IParseTree;
import net.akehurst.language.core.parser.IParser;
import net.akehurst.language.core.parser.ParseFailedException;
import net.akehurst.language.core.parser.ParseTreeException;
import net.akehurst.language.ogl.semanticModel.Grammar;
import net.akehurst.language.ogl.semanticModel.RuleNotFoundException;
import net.akehurst.language.parser.ScannerLessParser;
import net.akehurst.language.parser.forrest.ForrestFactory;
import net.akehurst.language.parser.forrest.ParseTreeBuilder;
import net.akehurst.language.parser.runtime.Factory;

import org.junit.Assert;
import org.junit.Before;

abstract
public class AbstractParser_Test {
	
	protected Factory parseTreeFactory;
	
	@Before
	public void before() {
		this.parseTreeFactory = new Factory();
	}
	
	ParseTreeBuilder builder(Grammar grammar, String text, String goal) {
		ForrestFactory ff = new ForrestFactory(this.parseTreeFactory, text);
		return new ParseTreeBuilder(ff, grammar, goal, text);
	}
	
	protected IParseTree process(Grammar grammar, String text, String goalName) throws ParseFailedException {
		try {
			INodeType goal = grammar.findAllRule(goalName).getNodeType();
			IParser parser = new ScannerLessParser(this.parseTreeFactory, grammar);
			IParseTree tree = parser.parse(goal, text);
			return tree;
		} catch (RuleNotFoundException e) {
			Assert.fail(e.getMessage());
			return null;
		} catch (ParseTreeException e) {
			Assert.fail(e.getMessage());
			return null;
		}
	}

}