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

package org.dby.nlpdep.ui;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Vector;

import edu.stanford.nlp.trees.Tree;

public class NLPTreeModel implements TreeModel {
	private Vector<TreeModelListener> treeModelListeners = 
		new Vector<TreeModelListener>();
	private Tree root;

	public NLPTreeModel(Tree nRoot) {
		root = nRoot;
	}

	public void addTreeModelListener(TreeModelListener l) {
		treeModelListeners.addElement(l);
	}

	public Object getChild(Object parent, int index) {
		Tree p = (Tree)parent;
		return p.getChild(index);
	}

	public int getChildCount(Object parent) {
		Tree p = (Tree)parent;
		return p.numChildren();
	}

	public int getIndexOfChild(Object parent, Object child) {
		Tree p = (Tree)parent;
		Tree c = (Tree)child;
		return p.indexOf(c);
	}

	public Object getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		Tree p = (Tree)node;
		return p.isLeaf();
	}

	public void removeTreeModelListener(TreeModelListener l) {
		treeModelListeners.removeElement(l);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}
}

