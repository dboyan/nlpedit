// NLP Editor
// Copyright (c) 2014	Boyan Ding
// All rights reserved.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of GNU General Public Licence
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRENTY; without even the implied warrenty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General public license for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
//

package nlpedit.ui;

import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.Component;
import java.io.StringReader;
import java.io.IOException;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.ling.StringLabelFactory;

public class NLPTree extends JPanel {
	protected NLPTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;

	public NLPTree(Tree root) {
		super(new GridLayout(1, 0));
		init(root);
	}

	public NLPTree(){
		super(new GridLayout(1, 0));
		init(null);
	}

	public void init(Tree root) {
		if (root == null) {
			rootNode = new NLPTreeNode("ROOT");
		} else {
			rootNode = new NLPTreeNode(root);
		}
		treeModel = new DefaultTreeModel(rootNode);
		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}

	public void setTree(Tree root) {
		treeModel = new DefaultTreeModel(new NLPTreeNode(root));
		tree.setModel(treeModel);
	}

	public String getTreeString() {
		NLPTreeNode p = (NLPTreeNode)treeModel.getRoot();
		return p.getTreeString();
	}

	public Tree getTree() {
		Tree ntree = new LabeledScoredTreeNode();
		try {
		ntree = (new PennTreeReader(new StringReader(getTreeString()), new LabeledScoredTreeFactory(new StringLabelFactory()))).readTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ntree;
	}

	public void removeCurrentNode() {
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null) {
			NLPTreeNode currentNode = (NLPTreeNode)(currentSelection.getLastPathComponent());
			NLPTreeNode parent = (NLPTreeNode)(currentNode.getParent());

			if (parent != null) {
				treeModel.removeNodeFromParent(currentNode);
			}
		}
	}

	public void addObject(String child) {
		NLPTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null) {
			parentNode = rootNode;
		} else {
			parentNode = (NLPTreeNode)(parentPath.getLastPathComponent());
		}
		addObject(parentNode, child, true);
	}

	public void addObject(NLPTreeNode parent, String child, boolean shouldBeVisible) {
		NLPTreeNode childNode = new NLPTreeNode(child);

		if (parent == null) {
			parent = rootNode;
		}

		treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

		if (shouldBeVisible) {
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
	}
}

