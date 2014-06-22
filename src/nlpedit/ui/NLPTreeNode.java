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
		if (userObject != null)
			return (String)userObject;
		else
			return "fuck you";
	}

	public void setValue(String str)
	{
		userObject = str;
	}

	boolean isInside(int a, int b)
	{
		return a >= x && a < x + width && b >= y && b < y + height;
	}

	public NLPTreeNode find(int x, int y)
	{
		NLPTreeNode ret;
		if (isInside(x, y))
			return this;
		else
			for (int i = 0; i < getChildCount(); ++i)
				if ((ret = getChildAt(i).find(x, y)) != null)
					return ret;
		return null;
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

