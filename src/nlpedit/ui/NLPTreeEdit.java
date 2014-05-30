package nlpedit.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;

import edu.stanford.nlp.trees.Tree;

public class NLPTreeEdit extends JPanel {
	private NLPTree treePanel;

	public NLPTreeEdit() {
		super(new BorderLayout());
		treePanel = new NLPTree();

		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.addObject("NEW_NODE");
			}
		});

		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				treePanel.removeCurrentNode();
			}
		});

		add(treePanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(addButton);
		panel.add(removeButton);
		add(panel, BorderLayout.SOUTH);
	}

	public void setTree(Tree root) {
		treePanel.setTree(root);
	}

	public String getTreeString() {
		return treePanel.getTreeString();
	}

	public Tree getTree() {
		return treePanel.getTree();
	}
}

