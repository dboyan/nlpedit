package org.dby.nlpdep.ui;

import java.io.StringReader;
import javax.swing.JFrame;
import java.awt.Container;
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

	public NLPTreeEx() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		LexicalizedParser lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
		LexicalizedParserQuery lpq = lp.parserQuery();
		String sent = "The quick brown fox jumps over the lazy dog";
		TokenizerFactory<CoreLabel> tokenizerFactory = 
			PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize();
		lpq.parse(rawWords);
		Tree parse = lpq.getBestParse();

		tree = new NLPTree(parse);
		add(tree);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new NLPTreeEx();
	}
}
