package nlpedit.ui;

import java.io.*;
import java.awt.Container;
import java.util.List;

import javax.swing.*;

import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.trees.Tree;

public class SentenceParser
{
	private LexicalizedParser lp;
	private LexicalizedParserQuery lpq;
	private TokenizerFactory<CoreLabel> tokenizerFactory;

	public SentenceParser()
	{
		lp = LexicalizedParser.loadModel("englishPCFG.ser.gz");
		lpq = lp.parserQuery();
		tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");	
	}

	public NLPTreeNode parse(JPanel p, String sent) {
		/*File f = new File("G:/git/my2/nlpedit/build/jar/ca.txt");
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		osw.write("fuck");
		osw.close();*/
		//JOptionPane.showMessageDialog(null, sent, "haha", JOptionPane.INFORMATION_MESSAGE);
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize();
		lpq.parse(rawWords);
		Tree t = lpq.getBestParse();
		NLPTreeNode nlpt = new NLPTreeNode(t);
		//JOptionPane.showMessageDialog(null, t.value(), "haha", JOptionPane.INFORMATION_MESSAGE);
		return nlpt;
	}
}