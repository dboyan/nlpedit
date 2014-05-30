package nlpedit.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.stanford.nlp.trees.Tree;

public class NLPTreeNode extends DefaultMutableTreeNode
{
	public NLPTreeNode(Tree root) {
		super(root.value());

		int childs = root.numChildren();
		for (int i = 0; i < childs; i++) {
			add(new NLPTreeNode(root.getChild(i)));
		}
	}

	public NLPTreeNode(String name) {
		super(name);
	}

	public String getTreeString() {
		return getTreeStringImpl(new StringBuffer()).toString();
	}

	private StringBuffer getTreeStringImpl(StringBuffer buf) {
		if (!isLeaf()) {
			buf.append("(");
		}
		buf.append((String)userObject);
		if (!isLeaf()) {
			for (Object p : children) {
				NLPTreeNode child = (NLPTreeNode)p;
				buf.append(" ");
				child.getTreeStringImpl(buf);
			}
			buf.append(")");
		}
		return buf;
	}
}

