package net.akehurst.language.parse.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import net.akehurst.language.grammar.parser.runtime.RuntimeRule;

public class GraphNodeBranch extends AbstractGraphNode implements IGraphNode {

	public GraphNodeBranch(ParseGraph graph,  RuntimeRule rr, int priority, int startPosition, int textLength, int nextItemIndex, int height) {
		super(graph, rr, startPosition, textLength);
		this.priority = priority;
		this.nextItemIndex = nextItemIndex;
		this.children = new ArrayList<>();
		this.height = height;
	}

	int priority;
	List<IGraphNode> children;
	int nextItemIndex;
	int height;

	@Override
	public IGraphNode duplicateWithNextChild(IGraphNode nextChild) {
		int newLength = this.getMatchedTextLength() + nextChild.getMatchedTextLength();
		int newNextItemIndex = this.getNextItemIndex() +1;
		
		// if duplicate will be complete && if its id already exists
		//   if (parents are the same) return already existing
		IGraphNode gn = this.graph.findCompleteNode(this.runtimeRule.getRuleNumber(), startPosition, newLength);

		
//		GraphNodeBranch duplicate = (GraphNodeBranch)this.graph.createBranch(this.runtimeRule, this.priority, this.startPosition, newLength, newNextItemIndex, this.height);
		GraphNodeBranch duplicate = new GraphNodeBranch(graph, this.runtimeRule, this.priority, this.startPosition, newLength, newNextItemIndex, this.height);
		duplicate.children = new ArrayList<>(this.children);
		duplicate.children.add(nextChild);
		duplicate.getPrevious().addAll(this.getPrevious());
		
		if (duplicate.getCanGrow()) {
			this.graph.addGrowable(duplicate);
		}
		if (duplicate.getIsComplete()) {
			this.graph.registerCompleteNode(duplicate);
		}
		
		return duplicate;
	}

	@Override
	public IGraphNode duplicateWithNextSkipChild(IGraphNode nextChild) {
		int newLength = this.getMatchedTextLength() + nextChild.getMatchedTextLength();
		int newNextItemIndex = this.getNextItemIndex();
		GraphNodeBranch duplicate = (GraphNodeBranch)this.graph.createBranch(this.runtimeRule, this.priority, this.startPosition, newLength, newNextItemIndex, this.height);
		duplicate.children = new ArrayList<>(this.children);
		duplicate.children.add(nextChild);
		duplicate.getPrevious().addAll(this.getPrevious());
		
		if (duplicate.getCanGrow()) {
			this.graph.addGrowable(duplicate);
		}
		if (duplicate.getIsComplete()) {
			this.graph.registerCompleteNode(duplicate);
		}
		
		return duplicate;
	}
	
	@Override
	public int getNextItemIndex() {
		return this.nextItemIndex;
	}
	
	@Override
	public boolean getIsLeaf() {
		return false;
	}

	@Override
	public boolean getIsEmpty() {
		return 0==this.getMatchedTextLength();
	}

	@Override
	public int getPriority() {
		return this.priority;
	}


	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public boolean getCanGrow() {
		if (this.getIsStacked()) {
			return true;
		} else {
			return this.getCanGrowWidth();
		}
	}

	@Override
	public boolean getIsSkip() {
		return this.getRuntimeRule().getIsSkipRule();
	}

	@Override
	public boolean getIsComplete() {
		switch (this.getRuntimeRule().getRhs().getKind()) {
			case EMPTY:
			break;
			case CHOICE:
				return true;
			case PRIORITY_CHOICE:
				return true;
			case CONCATENATION: {
				return this.getRuntimeRule().getRhs().getItems().length <= this.nextItemIndex || this.nextItemIndex == -1; // the -1 is used when creating dummy
																															// // test here!
			}
			case MULTI: {
				int size = this.nextItemIndex;
				return size >= this.getRuntimeRule().getRhs().getMultiMin() || this.nextItemIndex == -1; // the -1 is used when creating dummy branch...should
																											// really need the test here!
			}
			case SEPARATED_LIST: {
				int size = this.nextItemIndex;
				return (size % 2) == 1 || this.nextItemIndex == -1; // the -1 is used when creating dummy branch...should really need the test here!
			}
			default:
			break;
		}
		throw new RuntimeException("Internal Error: rule kind not recognised");

	}

	@Override
	public boolean getCanGraftBack() {
		if (getPrevious().isEmpty()) {
			return this.getIsComplete();
		}
		PreviousInfo info = this.getPrevious().get(0);
		return this.getIsComplete() && this.getIsStacked() && info.node.getExpectedItemAt(info.atPosition).getRuleNumber() == this.getRuntimeRule().getRuleNumber();
	}

	@Override
	public boolean getCanGrowWidth() {
		// boolean reachedEnd = this.getMatchedTextLength() >= inputLength;
		// if (reachedEnd)
		// return false;
		if (this.getIsComplete() && this.getIsEmpty()) {
			return false;
		}
		switch (this.getRuntimeRule().getRhs().getKind()) {
			case EMPTY: {
				return false;
			}
			case CHOICE: {
				return false;
			}
			case PRIORITY_CHOICE: {
				return false;
			}
			case CONCATENATION: {
				if (this.nextItemIndex < this.getRuntimeRule().getRhs().getItems().length) {
					return true;
				} else {
					return false; // !reachedEnd;
				}
			}
			case MULTI: {
				int size = this.nextItemIndex;
				int max = this.getRuntimeRule().getRhs().getMultiMax();
				return -1 == max || size < max;
			}
			case SEPARATED_LIST: {
				return true;
			}
			default:
			break;
		}
		throw new RuntimeException("Internal Error: rule kind not recognised");

	}

	@Override
	public boolean getIsStacked() {
		return !this.getPrevious().isEmpty();
	}

	@Override
	public boolean hasNextExpectedItem() {
		switch (this.getRuntimeRule().getRhs().getKind()) {
			case EMPTY:
				return false;
			case CHOICE:
				return false;
			case PRIORITY_CHOICE:
				return false;
			case CONCATENATION: {
				if (this.nextItemIndex >= this.getRuntimeRule().getRhs().getItems().length) {
					return false;
				} else {
					return true;
				}
			}
			case MULTI: {
				return true;
			}
			case SEPARATED_LIST:
				return true;
			default:
			break;
		}
		throw new RuntimeException("Internal Error: rule kind not recognised");
	}

	@Override
	public RuntimeRule getNextExpectedItem() {
		switch (this.getRuntimeRule().getRhs().getKind()) {
			case EMPTY:
			break;
			case CHOICE: {
				throw new RuntimeException("Internal Error: item is choice");
			}
			case PRIORITY_CHOICE: {
				throw new RuntimeException("Internal Error: item is priority choice");
			}
			case CONCATENATION: {
				if (this.nextItemIndex >= this.getRuntimeRule().getRhs().getItems().length) {
					throw new RuntimeException("Internal Error: No NextExpectedItem");
				} else {
					return this.getRuntimeRule().getRhsItem(this.nextItemIndex);
				}
			}
			case MULTI: {
				return this.getRuntimeRule().getRhsItem(0);
			}
			case SEPARATED_LIST: {
				if ((this.nextItemIndex % 2) == 1) {
					return this.getRuntimeRule().getSeparator();
				} else {
					return this.getRuntimeRule().getRhsItem(0);
				}
			}
			default:
			break;
		}
		throw new RuntimeException("Internal Error: rule kind not recognised");
	}

	@Override
	public RuntimeRule getExpectedItemAt(int atPosition) {
		switch (this.getRuntimeRule().getRhs().getKind()) {
			case EMPTY:
			break;
			case CHOICE: {
				throw new RuntimeException("Internal Error: item is choice");
			}
			case PRIORITY_CHOICE: {
				throw new RuntimeException("Internal Error: item is priority choice");
			}
			case CONCATENATION: {
				if (atPosition >= this.getRuntimeRule().getRhs().getItems().length) {
					throw new RuntimeException("Internal Error: No Item at position "+atPosition);
				} else {
					return this.getRuntimeRule().getRhsItem(atPosition);
				}
			}
			case MULTI: {
				return this.getRuntimeRule().getRhsItem(0);
			}
			case SEPARATED_LIST: {
				if ((this.nextItemIndex % 2) == 1) {
					return this.getRuntimeRule().getSeparator();
				} else {
					return this.getRuntimeRule().getRhsItem(0);
				}
			}
			default:
			break;
		}
		throw new RuntimeException("Internal Error: rule kind not recognised");
	}
	
	@Override
	public List<IGraphNode> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		return this.getRuntimeRule().getNodeTypeName()
				+ "(" +this.getRuntimeRule().getRuleNumber()+","+this.getStartPosition()+","+this.getMatchedTextLength()+","+this.getNextItemIndex()+")"
				+ (this.getPrevious().isEmpty() ? "" : " -> " + this.getPrevious().get(0));
	}
}
