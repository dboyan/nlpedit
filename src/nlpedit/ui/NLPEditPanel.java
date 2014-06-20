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

import nlpedit.core.NLPProject;
import java.io.File;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JButton;

public class NLPEditPanel extends JPanel {
	private NLPProject project;
	private JTextPane textPane;
	private JPanel editPanel; // TODO
	private JLabel statusLabel;

	public NLPEditPanel() {
		buildGUI();

		project = null;
	}

	private void buildGUI() {
		splitPane = new JSplitPane();
		topPanel = new JPanel();
		buttonsPanel = new JPanel();
		prevButton = new JButton();
		nextButton = new JButton();
		textPane = new JTextPane();
		textScrollPane = new JScrollPane();
		editPanel = new JPanel(); // TODO
		statusPanel = new JPanel();
		statusLabel = new JLabel();

		setLayout(new BorderLayout());

		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		topPanel.setLayout(new BorderLayout());
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		prevButton.setText("< Prev");
		prevButton.setEnabled(false);
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevActionPerformed(e);
			}
		});
		buttonsPanel.add(prevButton);

		nextButton.setText("Next >");
		nextButton.setEnabled(false);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextActionPerformed(e);
			}
		});
		buttonsPanel.add(nextButton);
		topPanel.add(buttonsPanel, BorderLayout.NORTH);

		textPane.setEditable(false);
		textPane.setPreferredSize(new Dimension(250, 250));
		textScrollPane.setViewportView(textPane);
		topPanel.add(textScrollPane, BorderLayout.CENTER);

		splitPane.setLeftComponent(topPanel);

		splitPane.setRightComponent(editPanel);
		add(splitPane, BorderLayout.CENTER);

		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusLabel.setText("Ready");
		statusPanel.add(statusLabel);

		add(statusPanel, BorderLayout.SOUTH);
	}

	public void importText(File fileName) {
		project = new NLPProject(fileName);
		textPane.setText(project.getDocument());
	}

	private void prevActionPerformed(ActionEvent e) {
	}

	private void nextActionPerformed(ActionEvent e) {
	}

	private JScrollPane textScrollPane;
	private JSplitPane splitPane;
	private JPanel topPanel;
	private JPanel buttonsPanel;
	private JButton prevButton;
	private JButton nextButton;
	private JPanel statusPanel;
}
