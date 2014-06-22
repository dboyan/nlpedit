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
import nlpedit.core.NLPSentenceInfo;
import nlpedit.event.*;
import nlpedit.util.FileContentGetter;

import java.io.File;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JOptionPane;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;

public class NLPEditPanel extends JPanel
			  implements ParseStatusListener, 
	   			     ParseFinishListener {
	private NLPProject project;
	
	private JTextPane textPane;
	private JScrollPane textScrollPane;
	
	private TreeJPanel editPanel;
	private JScrollPane editScrollPane;
	
	private JSplitPane splitPane;
	private JPanel topPanel;
	private JPanel buttonsPanel;
	
	private JButton prevButton;
	private JButton nextButton;
	
	private JPanel statusPanel;

	private JLabel statusLabel;
	private JProgressBar progressBar;
	private SimpleAttributeSet normalStyle, highlightStyle;

	private LexicalizedParser lp;
	private LexicalizedParserQuery lpq;
	private TokenizerFactory<CoreLabel> tokenizerFactory;

	private int currentPos, currentSentence;

	public NLPEditPanel() {
		buildGUI();

		highlightStyle = new SimpleAttributeSet();
		normalStyle = new SimpleAttributeSet();
		StyleConstants.setBackground(highlightStyle, Color.yellow);
		StyleConstants.setBackground(normalStyle, textPane.getBackground());

		lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
		lpq = lp.parserQuery();
		tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");

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
		editPanel = new TreeJPanel();
		editScrollPane = new JScrollPane();
		statusPanel = new JPanel();
		statusLabel = new JLabel();

		setLayout(new BorderLayout());

		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
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
		textPane.setPreferredSize(new Dimension(350, 250));
		textPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				textPaneMouseClicked(e);
			}
		});

		//editPanel

		textScrollPane.setViewportView(textPane);
		topPanel.add(textScrollPane, BorderLayout.CENTER);

		editScrollPane.setViewportView(editPanel);
		
		splitPane.setLeftComponent(topPanel);
		splitPane.setRightComponent(editScrollPane);
		add(splitPane, BorderLayout.CENTER);

		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusLabel.setText("Ready");
		statusPanel.add(statusLabel);
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setVisible(false);
		statusPanel.add(progressBar);

		add(statusPanel, BorderLayout.SOUTH);
	}

	public void importText(File fileName) {
		String doc = FileContentGetter.getFileContent(fileName);

		project = new NLPProject(doc);
		project.setupParser(lpq, tokenizerFactory);
		project.addStatusListener(this);
		project.addFinishListener(this);
		textPane.setText(project.getDocument());
		parseAll();
	}

	private void parseAll() {
		progressBar.setMaximum(project.getSentenceCount());
		progressBar.setValue(0);
		statusLabel.setText("Parsing (0 of " + project.getSentenceCount() + ")");
		progressBar.setVisible(true);

		project.parseAll();
		if (project.getSentenceCount() > 0) {
			scrollToSentence(0);
		}
	}

	public void openProject(File file) {
		project = new NLPProject(file);
		project.setupParser(lpq, tokenizerFactory);
		project.addStatusListener(this);
		project.addFinishListener(this);
		textPane.setText(project.getDocument());

		if (project.getSentenceCount() > 0) {
			scrollToSentence(0);
		}
	}

	public void saveProject(File file) {
		project.saveToFile(file);
	}

	private void highlightSentencePos(int pos) {
		NLPSentenceInfo info = project.getInfoFromPos(pos);
		highlightSentence(info.getBeginPos(), info.getEndPos());
	}

	private void highlightSentenceID(int id) {
		NLPSentenceInfo info = project.getInfoFromID(id);
		highlightSentence(info.getBeginPos(), info.getEndPos());
	}

	private void highlightSentence(int begin, int end) {
		highlightText(0, textPane.getText().length(), normalStyle);
		highlightText(begin, end, highlightStyle);
	}

	private void highlightText(int start, int end, SimpleAttributeSet style) {
		if (start < end) {
			textPane.getStyledDocument().setCharacterAttributes(
				start, end - start + 1, style, false);
		}
	}

	private void refreshNavigator() {
		prevButton.setEnabled(currentSentence > 0);
		nextButton.setEnabled(currentSentence < project.getSentenceCount() - 1);
	}

	private void scrollToSentence(int id) {
		NLPSentenceInfo info = project.getInfoFromID(id);
		currentSentence = id;
		currentPos = info.getBeginPos();
		textPane.setCaretPosition(currentPos);
		highlightSentenceID(id);
		refreshNavigator();
		String str = project.getDocument().substring(info.getBeginPos(), info.getEndPos() + 1);
		JOptionPane.showMessageDialog(null, str, "hehe", JOptionPane.INFORMATION_MESSAGE);
		editPanel.displaySentence(str);
	}

	public void reparseAll() {
		if (project != null) {
			parseAll();
		}
	}

	public void reparseCurrent() {
		if (project != null) {
			statusLabel.setText("Parsing...");
			project.parseOneSentence(currentSentence);
		}
	}

	public void scrollToPreviousSentence() {
		if (currentSentence > 0) {
			scrollToSentence(currentSentence - 1);
		}
	}

	public void scrollToNextSentence() {
		if (currentSentence < project.getSentenceCount() - 1) {
			scrollToSentence(currentSentence + 1);
		}
	}

	private void prevActionPerformed(ActionEvent e) {
		scrollToPreviousSentence();
	}

	private void nextActionPerformed(ActionEvent e) {
		scrollToNextSentence();
	}

	private void textPaneMouseClicked(MouseEvent e) {
		if (project == null || project.getSentenceCount() == 0)
			return ;

		int pos = textPane.getCaretPosition();
		NLPSentenceInfo info = project.getInfoFromPos(pos);

		scrollToSentence(info.getSentenceID());
	}

	public void parseStatusEmitted(ParseStatusEvent e) {
		int count = e.getCount();

		progressBar.setValue(count);
		statusLabel.setText("Parsing (" + count + " of " +
			project.getSentenceCount() + ")");
	}

	public void parseFinished(ParseFinishEvent e) {
		progressBar.setVisible(false);
		statusLabel.setText("Ready");
	}
}
