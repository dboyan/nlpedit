package nlpedit.ui;

import edu.stanford.nlp.ling.StringLabelFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.nlp.trees.PennTreeReader;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.objectbank.TokenizerFactory;


import javax.swing.*;
import java.io.StringReader;
import java.awt.Container;
import java.util.List;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.StringReader;

/**
* Class for displaying a Tree.
*
* @author Dan Klein
*/

@SuppressWarnings("serial")
public class TreeJPanel extends JPanel {

	protected int VERTICAL_ALIGN = SwingConstants.CENTER;
	protected int HORIZONTAL_ALIGN = SwingConstants.CENTER;

	private int maxFontSize = 128;
	private int minFontSize = 2;

	protected static final double sisterSkip = 2.5;
	protected static final double parentSkip = 1.35;
	protected static final double belowLineSkip = 0.075;
	protected static final double aboveLineSkip = 0.075;

	public static class WidthResult {
		public final double width; // = 0.0;
		public final double nodeTab; // = 0.0;
		public final double nodeCenter; // = 0.0;
		public final double childTab; // = 0.0;

		public WidthResult(double width, double nodeTab, double nodeCenter, double childTab) {
			this.width = width;
			this.nodeTab = nodeTab;
			this.nodeCenter = nodeCenter;
			this.childTab = childTab;
		}
	}


	protected NLPTreeNode tree;
	public SentenceParser sentParser = new SentenceParser();
	
	public NLPTreeNode getTree() {
		return tree;
	}

	public void setTree(NLPTreeNode tree) {
		//JOptionPane.showMessageDialog(null, tree.value(), "hoho", JOptionPane.INFORMATION_MESSAGE);
		this.tree = tree;
		repaint();
	}

	public void displaySentence(String sent)
	{
		if (sent == "")
			return ; 
		//JOptionPane.showMessageDialog(null, sent, "hehe", JOptionPane.INFORMATION_MESSAGE);
		//JOptionPane.showMessageDialog(null, sent, "hehe2", JOptionPane.INFORMATION_MESSAGE);
		setTree(sentParser.parse(this, sent));
	}

	protected static String nodeToString(NLPTreeNode t) {  
		return (t == null || t.value() == null) ? " " : t.value();
	}

	protected static double width(NLPTreeNode tree, FontMetrics fM) {
		return widthResult(tree, fM).width;
	}

	protected static WidthResult widthResult(NLPTreeNode tree, FontMetrics fM) {
		if (tree == null) {
			return new WidthResult(0.0, 0.0, 0.0, 0.0);
		}
		double local = fM.stringWidth(nodeToString(tree));
		if (tree.isLeaf()) {
			return new WidthResult(local, 0.0, local / 2.0, 0.0);
		}
		double sub = 0.0;
		double nodeCenter = 0.0;
		//double childTab = 0.0;
		for (int i = 0, numKids = tree.getChildCount(); i < numKids; i++) {
			WidthResult subWR = widthResult(tree.getChildAt(i), fM);
			if (i == 0) {
				nodeCenter += (sub + subWR.nodeCenter) / 2.0;
			}
			if (i == numKids - 1) {
				nodeCenter += (sub + subWR.nodeCenter) / 2.0;
			}
			sub += subWR.width;
			if (i < numKids - 1) {
				sub += sisterSkip * fM.stringWidth(" ");
			}
		}
		double localLeft = local / 2.0;
		double subLeft = nodeCenter;
		double totalLeft = Math.max(localLeft, subLeft);
		double localRight = local / 2.0;
		double subRight = sub - nodeCenter;
		double totalRight = Math.max(localRight, subRight);
		return new WidthResult(totalLeft + totalRight, totalLeft - localLeft,
			nodeCenter + totalLeft - subLeft, totalLeft - subLeft);
	}

	protected static double height(NLPTreeNode tree, FontMetrics fM) {
		if (tree == null) {
			return 0.0;
		}
		double depth = tree.getLevel();
		return fM.getHeight() * (1.0 + depth * (1.0 + parentSkip + aboveLineSkip + belowLineSkip));
	}

	protected FontMetrics pickFont(Graphics2D g2, NLPTreeNode tree, Dimension space) {
		Font font = g2.getFont();
		String fontName = font.getName();
		int style = font.getStyle();

		for (int size = maxFontSize; size > minFontSize; size--) {
			font = new Font(fontName, style, size);
			g2.setFont(font);
			FontMetrics fontMetrics = g2.getFontMetrics();
			if (height(tree, fontMetrics) > space.getHeight()) {
				continue;
			}
			if (width(tree, fontMetrics) > space.getWidth()) {
				continue;
			}
			//System.out.println("Chose: "+size+" for space: "+space.getWidth());
			return fontMetrics;
		}
		font = new Font(fontName, style, minFontSize);
		g2.setFont(font);
		return g2.getFontMetrics();
	}


	private static double paintTree(NLPTreeNode t, Point2D start, Graphics2D g2, FontMetrics fM) {
		
		if (t == null) {
			return 0.0;
		}
		String nodeStr = nodeToString(t);
		double nodeWidth = fM.stringWidth(nodeStr);
		double nodeHeight = fM.getHeight();
		double nodeAscent = fM.getAscent();
		WidthResult wr = widthResult(t, fM);
		double treeWidth = wr.width;
		double nodeTab = wr.nodeTab;
		double childTab = wr.childTab;
		double nodeCenter = wr.nodeCenter;
		//double treeHeight = height(t, fM);
		// draw root
		g2.drawString(nodeStr, (float) (nodeTab + start.getX()), (float) (start.getY() + nodeAscent));
		
		JOptionPane.showMessageDialog(null, nodeStr, "haha", JOptionPane.INFORMATION_MESSAGE);
	
		if (t.isLeaf()) {
			return nodeWidth;
		}
		double layerMultiplier = (1.0 + belowLineSkip + aboveLineSkip + parentSkip);
		double layerHeight = nodeHeight * layerMultiplier;
		double childStartX = start.getX() + childTab;
		double childStartY = start.getY() + layerHeight;
		double lineStartX = start.getX() + nodeCenter;
		double lineStartY = start.getY() + nodeHeight * (1.0 + belowLineSkip);
		double lineEndY = lineStartY + nodeHeight * parentSkip;
		// recursively draw children
		for (int i = 0; i < t.getChildCount(); i++) {
			NLPTreeNode child = t.getChildAt(i);
			double cWidth = paintTree(child, new Point2D.Double(childStartX, childStartY), g2, fM);
			// draw connectors
			wr = widthResult(child, fM);
			double lineEndX = childStartX + wr.nodeCenter;
			g2.draw(new Line2D.Double(lineStartX, lineStartY, lineEndX, lineEndY));
			childStartX += cWidth;
			if (i < t.getChildCount() - 1) {
				childStartX += sisterSkip * fM.stringWidth(" ");
			}
		}
		return treeWidth;
	}


	protected void superPaint(Graphics g) {
		super.paintComponent(g);
	}


	@Override
	public void paintComponent(Graphics g) {
		superPaint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Dimension space = getSize();
		FontMetrics fM = pickFont(g2, tree, space);
		double width = width(tree, fM);
		double height = height(tree, fM);
		double startX = 0.0;
		double startY = 0.0;
		if (HORIZONTAL_ALIGN == SwingConstants.CENTER) {
			startX = (space.getWidth() - width) / 2.0;
		}
		if (HORIZONTAL_ALIGN == SwingConstants.RIGHT) {
			startX = space.getWidth() - width;
		}
		if (VERTICAL_ALIGN == SwingConstants.CENTER) {
			startY = (space.getHeight() - height) / 2.0;
		}
		if (VERTICAL_ALIGN == SwingConstants.BOTTOM) {
			startY = space.getHeight() - height;
		}
		paintTree(tree, new Point2D.Double(startX, startY), g2, fM);
	}

	public TreeJPanel() {
		this(SwingConstants.CENTER, SwingConstants.CENTER);
	}

	public TreeJPanel(int hAlign, int vAlign) {
		HORIZONTAL_ALIGN = hAlign;
		VERTICAL_ALIGN = vAlign;
		setPreferredSize(new Dimension(400, 300));
	}

	public void setMinFontSize(int size) {
		minFontSize = size;
	}

	public void setMaxFontSize(int size) {
		maxFontSize = size;
	}


	public Font pickFont() {
		Font font = getFont();
		String fontName = font.getName();
		int style = font.getStyle();
		int size = ( maxFontSize + minFontSize ) / 2;
		return new Font(fontName, style, size);
	}

	public Dimension getTreeDimension(NLPTreeNode tree, Font font) {
		FontMetrics fM = getFontMetrics(font);
		return new Dimension((int) width(tree, fM), (int) height(tree, fM));
	}

	/*
	public void parseSentence(String sent) {
		if (sent.equals("")){
			return ;
		}
		TokenizerFactory<CoreLabel> tokenizerFactory;
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(sent)).tokenize();
		lpq.parse(rawWords);
		Tree parse = lpq.getBestParse();
		treeEdit.setTree(parse);
	}
	*/

	public static void main(String[] args) throws IOException {
		/*
		TreeJPanel tjp = new TreeJPanel();
		// String ptbTreeString1 = "(ROOT (S (NP (DT This)) (VP (VBZ is) (NP (DT a) (NN test))) (. .)))";
		String ptbTreeString = "(ROOT (S (NP (NNP Interactive_Tregex)) (VP (VBZ works)) (PP (IN for) (PRP me)) (. !))))";
		if (args.length > 0) {
			ptbTreeString = args[0];
		}
		NLPTreeNode tree = (new PennTreeReader(new StringReader(ptbTreeString), new LabeledScoredTreeFactory(new StringLabelFactory()))).readTree();
		tjp.setTree(tree);
		tjp.setBackground(Color.white);
		JFrame frame = new JFrame();
		frame.getContentPane().add(tjp, BorderLayout.CENTER);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		frame.setVisible(true);
		frame.setVisible(true);*/
	}

}
