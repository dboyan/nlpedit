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

package nlpedit.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map;

import nlpedit.ui.NLPTreeNode;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParserQuery;

public class NLPProject {
	private String document;
	private TreeMap<Integer, Integer> boundaryMap;
	private Vector<Integer> boundaryArray;
	private Vector<NLPTreeNode> treeArray;
	private int numSentence;

	private LexicalizedParserQuery lpq;
	private TokenizerFactory<CoreLabel> tokenizerFactory;

	public NLPProject(String str) {
		document = str;
		initialize();
	}

	public NLPProject(File file) {
		FileInputStream fis;
		ObjectInputStream ois;
		NLPProjectSerializedForm sForm;

		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			sForm = (NLPProjectSerializedForm)
				ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
			sForm = null;
		}

		document = sForm.document;
		boundaryArray = sForm.boundaryArray;
		boundaryMap = sForm.boundaryMap;
		treeArray = sForm.treeArray;
		numSentence = treeArray.size() - 1;
	}

	private void initialize() {
		setupBoundaries();
	}

	public void setupParser(LexicalizedParserQuery l, TokenizerFactory<CoreLabel> tf) {
		lpq = l;
		tokenizerFactory = tf;
	}

	public void parseAll() {
		NLPParseAllWorker worker = new NLPParseAllWorker();
		worker.run();
	}

	public String getDocument() {
		return document;
	}

	public int getSentenceCount() {
		return numSentence;
	}

	public NLPTreeNode getTree(int id) {
		return treeArray.elementAt(id);
	}

	public void saveToFile(File file) {
		try {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		NLPProjectSerializedForm sForm = new NLPProjectSerializedForm(
				document, boundaryMap, boundaryArray,
				treeArray);
		oos.writeObject(sForm);
		oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NLPSentenceInfo getInfoFromPos(int pos) {
		int sentID, begin, end;
		Map.Entry<Integer, Integer> entry;

		entry = boundaryMap.lowerEntry(pos);
		if (entry.getValue() == numSentence) {
			sentID = entry.getValue() - 1;
		} else {
			sentID = entry.getValue();
		}

		begin = boundaryArray.elementAt(sentID);
		end = boundaryArray.elementAt(sentID + 1) - 1;
		return new NLPSentenceInfo(begin, end, sentID);
	}

	public NLPSentenceInfo getInfoFromID(int ID) {
		return new NLPSentenceInfo(boundaryArray.elementAt(ID),
					   boundaryArray.elementAt(ID + 1) - 1,
					   ID);
	}

	private void setupBoundaries() {
		StringReader reader = new StringReader(document);
		DocumentPreprocessor processor = new DocumentPreprocessor(reader);
		int sentCount = 0;

		boundaryMap = new TreeMap<Integer, Integer>();
		boundaryArray = new Vector<Integer>();

		for (List<HasWord> sentence : processor) {
			if (sentence.size() == 0)
				continue;
			if (sentCount == 0) {
				boundaryMap.put(0, 0);
				boundaryArray.add(0);
			} else {
				HasOffset first = (HasOffset) sentence.get(0);
				boundaryMap.put(first.beginPosition(), sentCount);
				boundaryArray.add(first.beginPosition());
			}
			sentCount++;
		}
		boundaryMap.put(document.length(), sentCount);
		boundaryArray.add(document.length());
		numSentence = sentCount;
	}

	private NLPTreeNode parseSentence(String s) {
		if (s.trim().equals("")) {
			return new NLPTreeNode("ROOT");
		}
		List<CoreLabel> rawWords = tokenizerFactory.getTokenizer(new StringReader(s)).tokenize();
		lpq.parse(rawWords);
		return new NLPTreeNode(lpq.getBestParse());
	}

	private NLPTreeNode parseSentenceID(int num) {
		int start = boundaryArray.elementAt(num);
		int end = boundaryArray.elementAt(num + 1);
		return parseSentence(document.substring(start, end));
	}

	class NLPProjectSerializedForm implements Serializable {
		String document;
		TreeMap<Integer, Integer> boundaryMap;
		Vector<Integer> boundaryArray;
		Vector<NLPTreeNode> treeArray;

		public NLPProjectSerializedForm(String d,
				TreeMap<Integer, Integer> bm,
				Vector<Integer> ba,
				Vector<NLPTreeNode> ta) {
			document = d;
			boundaryMap = bm;
			boundaryArray = ba;
			treeArray = ta;
		}
	}

	class NLPParseAllWorker implements Runnable {
		public void run() {
			treeArray.clear();
			for (int i = 0; i < numSentence; i++) {
				treeArray.add(parseSentenceID(i));
			}
		}
	}
}

