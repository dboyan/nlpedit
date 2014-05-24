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

import javax.swing.Icon;
import javax.swing.JTree;
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

public class NLPTree extends JTree {
	public NLPTree(Tree root) {
		super(new DefaultTreeModel(new NLPTreeNode(root)));
		getSelectionModel().setSelectionMode(
			TreeSelectionModel.SINGLE_TREE_SELECTION);
		setEditable(true);
	}

	public void setTree(Tree root) {
		setModel(new DefaultTreeModel(new NLPTreeNode(root)));
	}

	public String getTreeString() {
		NLPTreeNode p = (NLPTreeNode)treeModel.getRoot();
		return p.getTreeString();
	}

	public Tree getTree() {
		Tree tree = new LabeledScoredTreeNode();
		try {
		tree = (new PennTreeReader(new StringReader(getTreeString()), new LabeledScoredTreeFactory(new StringLabelFactory()))).readTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tree;
	}
}

