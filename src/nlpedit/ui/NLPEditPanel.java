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
import java.io.File;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class NLPEditPanel extends JPanel {
	private NLPProject project;
	private JTextPane textPane;

	public NLPEditPanel() {
		buildGUI();

		project = null;
	}

	private void buildGUI() {
		textPane = new JTextPane();

		textPane.setEditable(false);
		textScrollPane = new JScrollPane(textPane);
		textScrollPane.setPreferredSize(new Dimension(250, 250));
		add(textScrollPane);
	}

	public void importText(File fileName) {
		project = new NLPProject(fileName);
		textPane.setText(project.getDocument());
	}

	private JScrollPane textScrollPane;
}
