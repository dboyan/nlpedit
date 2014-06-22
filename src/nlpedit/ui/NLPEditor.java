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
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;

public class NLPEditor extends JFrame {
	private NLPEditPanel mainPanel;
	private JFileChooser jfc;

	public NLPEditor() {
		buildGUI();

		jfc = new JFileChooser();
		mainPanel = new NLPEditPanel();
		getContentPane().add("Center", mainPanel);
		pack();

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	protected void buildGUI() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		importTextItem = new JMenuItem();
		openProjectItem = new JMenuItem();
		saveProjectItem = new JMenuItem();
		separatorItem1 = new JSeparator();
		exitItem = new JMenuItem();
		editMenu = new JMenu();
		reparseAllItem = new JMenuItem();
		reparseOneItem = new JMenuItem();
		separatorItem2 = new JSeparator();
		prevSentenceItem = new JMenuItem();
		nextSentenceItem = new JMenuItem();
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
		openProjectItem.setText("Open Project");
		openProjectItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openProjectActionPerformed(e);
			}
		});
		fileMenu.add(openProjectItem);
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
		editMenu.setText("Edit");
		reparseAllItem.setText("Reparse All");
		reparseAllItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reparseAllActionPerformed(e);
			}
		});
		editMenu.add(reparseAllItem);
		reparseOneItem.setText("Reparse Sentence");
		reparseOneItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reparseOneActionPerformed(e);
			}
		});
		editMenu.add(reparseOneItem);
		editMenu.add(separatorItem2);
		prevSentenceItem.setText("Previous Sentence");
		prevSentenceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevSentenceActionPerformed(e);
			}
		});
		editMenu.add(prevSentenceItem);
		nextSentenceItem.setText("Next Sentence");
		nextSentenceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextSentenceActionPerformed(e);
			}
		});
		editMenu.add(nextSentenceItem);
		menuBar.add(editMenu);
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
		int retval = jfc.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			mainPanel.importText(jfc.getSelectedFile());
		}
	}

	protected void saveProjectActionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Project File", "npj");
		chooser.setFileFilter(filter);
		int retval = chooser.showSaveDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			String fPath = chooser.getSelectedFile().getAbsolutePath();
			String fName = chooser.getSelectedFile().getName();
			Pattern p = Pattern.compile("(.*)+\\.(npj)$");
			Matcher m = p.matcher(fName);
			if (!m.matches()) {
				fPath = fPath + ".npj";
			}
			mainPanel.saveProject(new File(fPath));
		}
	}

	protected void openProjectActionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
			"Project File", "npj");
		chooser.setFileFilter(filter);
		int retval = chooser.showOpenDialog(this);
		if (retval == JFileChooser.APPROVE_OPTION) {
			mainPanel.openProject(chooser.getSelectedFile());
		}
	}

	protected void exitActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	protected void reparseAllActionPerformed(ActionEvent e) {
		mainPanel.reparseAll();
	}

	protected void reparseOneActionPerformed(ActionEvent e) {
		mainPanel.reparseCurrent();
	}

	protected void prevSentenceActionPerformed(ActionEvent e) {
		mainPanel.scrollToPreviousSentence();
	}

	protected void nextSentenceActionPerformed(ActionEvent e) {
		mainPanel.scrollToNextSentence();
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
	private JMenuItem openProjectItem;
	private JMenuItem saveProjectItem;
	private JSeparator separatorItem1;
	private JMenuItem exitItem;
	private JMenu editMenu;
	private JMenuItem reparseAllItem;
	private JMenuItem reparseOneItem;
	private JSeparator separatorItem2;
	private JMenuItem prevSentenceItem;
	private JMenuItem nextSentenceItem;
	private JMenu helpMenu;
	private JMenuItem aboutItem;

	public static void main(String args[]) {
		new NLPEditor();
	}
}

