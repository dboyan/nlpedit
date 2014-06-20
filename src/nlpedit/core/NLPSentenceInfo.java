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

public class NLPSentenceInfo {
	private int beginPos;
	private int endPos;
	private int sentenceID;

	public NLPSentenceInfo(int begin, int end, int id) {
		beginPos = begin;
		endPos = end;
		sentenceID = id;
	}

	public int getBeginPos() {
		return beginPos;
	}

	public int getEndPos() {
		return endPos;
	}

	public int getSentenceID() {
		return sentenceID;
	}
}
