package nlpedit.ui;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import javax.swing.JOptionPane;

import edu.stanford.nlp.trees.Tree;

public class NLPTreeNode extends DefaultMutableTreeNode implements Serializable
{
	public int x, y, height, width;
	
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

	public String value()
	{
		return (String)userObject;
	}

	@Override
	public NLPTreeNode getChildAt(int i)
	{
		return (NLPTreeNode)super.getChildAt(i);
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

