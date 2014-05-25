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

public class NLPTreeEx extends JFrame {
	private NLPTree tree;
	private JButton parseButton;
	private JButton updateTextButton;
	private JTextArea sentArea;
	private JTextArea treeArea;
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
		tree.setTree(parse);
	}

	private void updateText() {
		treeArea.setText(tree.getTreeString());
	}

	private void initComponents() {
		JPanel buttonPanel = new JPanel();
		parseButton = new JButton("parse");
		updateTextButton = new JButton("update text");
		tree = new NLPTree();
		sentArea = new JTextArea(5, 40);
		treeArea = new JTextArea(5, 40);

		sentArea.setLineWrap(true);
		treeArea.setLineWrap(true);

		parseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parseSentence();
				updateText();
			}
		});

		updateTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateText();
			}
		});

		Container contentPane = getContentPane();

		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		buttonPanel.add(parseButton);
		buttonPanel.add(updateTextButton);
		contentPane.add(buttonPanel);

		contentPane.add(sentArea);

		JScrollPane treeScroll = new JScrollPane(tree);
		contentPane.add(treeScroll);

		contentPane.add(treeArea);
		pack();
	}

	public static void main(String[] args) {
		new NLPTreeEx();
	}
}
