/*
    Tinyapps https://github.com/provirus/tinyapps
    Copyright (C) 2014-2016

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package ca.pgon.saviorgui.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ProfilesManager {

	/**
	 * A comparator that does not care about the case.
	 */
	private static Comparator<String> ignoreCaseComparator = new Comparator<String>() {

		@Override
		public int compare(String o1, String o2) {
			return o1.compareToIgnoreCase(o2);
		}
	};

	static public String getBaseProfilePath() {
		String basePath = System.getProperty("user.home") + File.separatorChar;

		// Check if there is an Application Data directory (on Windows)
		if ((new File(basePath + "Application Data")).exists()) {
			basePath += "Application Data" + File.separatorChar;
		}

		// Choose our directory
		basePath += "Savior2" + File.separatorChar;

		// Create the directory if it doesn't exists
		File tmp = new File(basePath);
		if (!(tmp).exists()) {
			tmp.mkdirs();
		}

		return tmp.getAbsolutePath() + File.separatorChar;
	}

	static public boolean deleteProfile(String profileName) {
		return (new File(getBaseProfilePath() + profileName)).delete();
	}

	static public String[] listProfiles() {
		String[] files = new File(getBaseProfilePath()).list();

		List<String> result = Arrays.asList(files);
		Collections.sort(result, ignoreCaseComparator);

		return result.toArray(new String[0]);
	}

	static public Profile loadProfile(String profileName) {
		return load(getBaseProfilePath() + profileName);
	}

	static public boolean saveProfile(String profileName, Profile profile) {
		return save(getBaseProfilePath() + profileName, profile);
	}

	static public Profile load(String filename) {
		try {
			// Init
			Profile profile = new Profile();
			profile.sourceParams = new HashMap<String, String>();
			profile.destinationParams = new HashMap<String, String>();
			profile.ignoreList = new ArrayList<String>();

			// Get the document and the elements
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new FileInputStream(filename));
			Element rootEl = doc.getRootElement();
			Element sourceEl = rootEl.getChild("source");
			Element destinationEl = rootEl.getChild("destination");
			Element ignoreListEl = rootEl.getChild("ignoreList");

			// Fill the source
			profile.sourceFileSystemType = sourceEl.getChildText("fileSystem");
			profile.sourceBasePath = sourceEl.getChildText("basePath");
			for (Object obj : sourceEl.getChild("params").getChildren()) {
				Element e = (Element) obj;
				profile.sourceParams.put(e.getName(), e.getText());
			}

			// Fill the destination
			profile.destinationFileSystemType = destinationEl.getChildText("fileSystem");
			profile.destinationBasePath = destinationEl.getChildText("basePath");
			for (Object obj : destinationEl.getChild("params").getChildren()) {
				Element e = (Element) obj;
				profile.destinationParams.put(e.getName(), e.getText());
			}

			// The engine
			profile.engineType = rootEl.getChildText("engineType");

			// The modification
			profile.modDate = "1".equals(rootEl.getChildText("modDate"));
			profile.modSize = "1".equals(rootEl.getChildText("modSize"));
			profile.modMD5 = "1".equals(rootEl.getChildText("modMD5"));

			// Ignore list
			if (ignoreListEl != null) {
				for (Object o : ignoreListEl.getChildren("ignore")) {
					Element el = (Element) o;
					profile.ignoreList.add(el.getText());
				}
			}

			return profile;
		} catch (JDOMException ex) {
		} catch (IOException ex) {
		}

		return null;
	}

	static public boolean save(String filename, Profile profile) {
		Element rootEl = new Element("savior");
		Element sourceEl = new Element("source");
		Element destinationEl = new Element("destination");
		Element ignoreListEl = new Element("ignoreList");

		rootEl.addContent(sourceEl);
		rootEl.addContent(destinationEl);
		rootEl.addContent(ignoreListEl);

		targetFill(sourceEl, profile.sourceFileSystemType, profile.sourceBasePath, profile.sourceParams);
		targetFill(destinationEl, profile.destinationFileSystemType, profile.destinationBasePath, profile.destinationParams);

		rootEl.addContent(new Element("engineType").setText(profile.engineType));

		rootEl.addContent(new Element("modDate").setText(profile.modDate ? "1" : "0"));
		rootEl.addContent(new Element("modSize").setText(profile.modSize ? "1" : "0"));
		rootEl.addContent(new Element("modMD5").setText(profile.modMD5 ? "1" : "0"));

		// Ignore list
		if (profile.ignoreList != null) {
			for (String s : profile.ignoreList) {
				ignoreListEl.addContent(new Element("ignore").setText(s));
			}
		}

		Document doc = new Document(rootEl);
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			OutputStream outStream = new FileOutputStream(filename);
			xmlOutputter.output(doc, outStream);
			outStream.close();
		} catch (FileNotFoundException ex) {
			return false;
		} catch (IOException ex) {
			return false;
		}

		return true;
	}

	static private void targetFill(Element targetEl, String fileSystem, String basePath, Map<String, String> params) {
		targetEl.addContent(new Element("fileSystem").addContent(fileSystem));
		targetEl.addContent(new Element("basePath").addContent(basePath));

		Element paramsEl = new Element("params");
		targetEl.addContent(paramsEl);
		if (params != null) {
			for (Entry<String, String> next : params.entrySet()) {
				paramsEl.addContent(new Element(next.getKey()).addContent(next.getValue()));
			}
		}
	}
}
