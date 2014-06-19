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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class NLPEditor extends JFrame {
	private JPanel mainPanel;

	public NLPEditor() {
		buildGUI();

		mainPanel = new JPanel();
		getContentPane().add("Center", mainPanel);
		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	protected void buildGUI() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		importTextItem = new JMenuItem();
		saveProjectItem = new JMenuItem();
		separatorItem1 = new JSeparator();
		exitItem = new JMenuItem();
		helpMenu = new JMenu();
		aboutItem = new JMenuItem();

		setTitle("NLP Dependency Editor");

		fileMenu.setText("File");
		importTextItem.setText("Import text");
		importTextItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				importTextActionPerformed(e);
			}
		});
		fileMenu.add(importTextItem);
		saveProjectItem.setText("Save Project");
		saveProjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveProjectActionPerformed(e);
			}
		});
		fileMenu.add(saveProjectItem);
		fileMenu.add(separatorItem1);
		exitItem.setText("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitActionPerformed(e);
			}
		});
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		helpMenu.setText("Help");
		aboutItem.setText("About");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutActionPerformed(e);
			}
		});
		helpMenu.add(aboutItem);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);
		pack();
	}

	protected void importTextActionPerformed(ActionEvent e) {
	}

	protected void saveProjectActionPerformed(ActionEvent e) {
	}

	protected void exitActionPerformed(ActionEvent e) {
	}

	protected void aboutActionPerformed(ActionEvent e) {
		JOptionPane.showMessageDialog(this,
			"NLP Editor\n" +
			"Author: Yeji Shen and Boyan Ding\n" +
			"Published under GPLv2",
			"About",
			JOptionPane.INFORMATION_MESSAGE);
	}

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem importTextItem;
	private JMenuItem saveProjectItem;
	private JSeparator separatorItem1;
	private JMenuItem exitItem;
	private JMenu helpMenu;
	private JMenuItem aboutItem;

	public static void main(String args[]) {
		new NLPEditor();
	}
}

