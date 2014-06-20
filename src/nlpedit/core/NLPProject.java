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
import java.io.FileReader;
import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.process.DocumentPreprocessor;

public class NLPProject implements Serializable {
	private String document;
	private TreeMap<Integer, Integer> boundaryMap;
	private Vector<Integer> boundaryArray;
	private int numSentence;

	public NLPProject(String str) {
		document = str;
		initialize();
	}

	public NLPProject(File fileName) {
		document = getFileContent(fileName);
		initialize();
	}

	private void initialize() {
		setupBoundaries();
	}

	public String getDocument() {
		return document;
	}

	public int getSentenceCount() {
		return numSentence;
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

	private static String getFileContent(File fileName) {
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(fileName));
			String line = reader.readLine();
			while (line != null) {
				buf.append(line);
				buf.append("\n");
				line = reader.readLine();
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}
}

