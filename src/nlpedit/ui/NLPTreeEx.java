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

import java.io.StringReader;
import javax.swing.*;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.parser.ui.TreeJPanel;

public class NLPTreeEx extends JFrame {
	private NLPTreeEdit treeEdit;
	private JButton parseButton;
	private JButton updateTextButton;
	private JButton updateGraphButton;
	private JTextArea sentArea;
	private JTextArea treeArea;
	private TreeJPanel treeGraph;
	private LexicalizedParser lp;
	private LexicalizedParserQuery lpq;
	private TokenizerFactory<CoreLabel> tokenizerFactory;

	public NLPTreeEx() {
		initComponents();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
		lpq = lp.parserQuery();
		tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		setVisible(true);
	}

	public void parseSentence() {
		String sent = sentArea.getText();
		if (sent.equals("")){
			return ;
		}
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize();
		lpq.parse(rawWords);
		Tree parse = lpq.getBestParse();
		treeEdit.setTree(parse);
	}

	private void updateText() {
		treeArea.setText(treeEdit.getTreeString());
	}

	private void updateGraph() {
		treeGraph.setTree(treeEdit.getTree());
	}

	private void initComponents() {
		JPanel buttonPanel = new JPanel();
		parseButton = new JButton("parse");
		updateTextButton = new JButton("update text");
		updateGraphButton = new JButton("update graph");
		treeEdit = new NLPTreeEdit();
		sentArea = new JTextArea(5, 40);
		treeArea = new JTextArea(5, 40);
		treeGraph = new TreeJPanel();

		sentArea.setLineWrap(true);
		treeArea.setLineWrap(true);

		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parseSentence();
				updateText();
				updateGraph();
			}
		});

		updateTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateText();
			}
		});

		updateGraphButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGraph();
			}
		});

		JPanel leftPanel = new JPanel();

		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

		buttonPanel.add(parseButton);
		buttonPanel.add(updateTextButton);
		buttonPanel.add(updateGraphButton);
		leftPanel.add(buttonPanel);

		leftPanel.add(sentArea);

		leftPanel.add(treeEdit);

		leftPanel.add(treeArea);

		JScrollPane graphScroll = new JScrollPane(treeGraph);

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, graphScroll);

		add(splitPane);

		pack();
	}

	public static void main(String[] args) {
		new NLPTreeEx();
	}
}
