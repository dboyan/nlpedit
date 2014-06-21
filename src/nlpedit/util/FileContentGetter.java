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

package nlpedit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileContentGetter {
	public static String getFileContent(File fileName) {
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

