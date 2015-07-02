package net.akehurst.language.ogl.semanticModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Concatenation extends RuleItem {

	public Concatenation(TangibleItem... item) {
		this.item = Arrays.asList(item);
	}

	public void setOwningRule(Rule value) {
		this.owningRule = value;
		for(RuleItem c: this.getItem()) {
			c.setOwningRule(value);
		}
	}
	
	List<TangibleItem> item;
	public List<TangibleItem> getItem() {
		return this.item;
	}
	
//	@Override
//	public INodeType getNodeType() {
//		return new RuleNodeType(this.getOwningRule());
//	}
	
	@Override
	public <T, E extends Throwable> T accept(Visitor<T,E> visitor, Object... arg) throws E {
		return visitor.visit(this, arg);
	}
	
//	public Set<TangibleItem> findFirstTangibleItem() {
//		Set<TangibleItem> result = new HashSet<>();
//		result.addAll( this.getItem().get(0).findFirstTangibleItem() );
//		return result;
//	}
//	
	public Set<Terminal> findAllTerminal() {
		Set<Terminal> result = new HashSet<>();
		for(TangibleItem ti: this.getItem()) {
			result.addAll( ti.findAllTerminal() );
		}
		return result;
	}
	
	@Override
	public Set<NonTerminal> findAllNonTerminal() {
		Set<NonTerminal> result = new HashSet<>();
		for(TangibleItem ti: this.getItem()) {
			result.addAll( ti.findAllNonTerminal() );
		}
		return result;
	}
	
//	@Override
//	public boolean isMatchedBy(INode node) throws RuleNotFoundException {
//		if (node instanceof IBranch) {
//			IBranch branch = (IBranch)node;
//			boolean isMatched = branch.getChildren().size() == this.getItem().size();
//			if (isMatched) {
//				for(int i=0; i < branch.getChildren().size(); ++i) {
//					INode cn = branch.getChildren().get(i);
//					ConcatinationItem item = this.getItem().get(i);
//					if ( ! item.isMatchedBy(cn) ) {
//						return false;
//					}
//				}
//			}
//			return isMatched;
//		}
//		return false;
//	}
	
	//--- Object ---
	@Override
	public String toString() {
		String r = "";
		for(RuleItem i : this.getItem()) {
			r += i.toString() + " ";
		}
		return r;
	}
	
	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object arg) {
		if (arg instanceof Concatenation) {
			Concatenation other = (Concatenation)arg;
			return this.toString().equals(other.toString());
		} else {
			return false;
		}
	}
}