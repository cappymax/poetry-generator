package com.ktim1435.wordnet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ktim1435.language.Gramatics;

public class SentenceAnalyzer {
	private Document doc;
	private WordNet wn = new WordNet();
	
	public SentenceAnalyzer() {
		try {
			doc = wn.openDoc();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateSentenceFile() {
		System.out.println("Writing sentence types to file.");
		try{
		    PrintWriter writer = new PrintWriter("sentences.txt", "UTF-8");
		    System.out.println("Parsing usages");
		    parseUsages(writer);
		    System.out.println("Parsing defines");
		    parseDefs(writer);
		    writer.close();
		} catch (IOException e) {
		   System.out.println("Failed writeing");
		}
	}
	
	private void parseUsages(PrintWriter writer) {
		NodeList list = doc.getElementsByTagName("USAGE");
		parseListOfSentences(writer, list);
		
	}

	private void parseDefs(PrintWriter writer) {
		NodeList list = doc.getElementsByTagName("DEF");
		parseListOfSentences(writer, list);
	}

	private void parseListOfSentences(PrintWriter writer, NodeList list) {
		WordNet wn = new WordNet();
		for (int i = 0 ; i < list.getLength(); i++) {
			if (i % 10 == 0) System.out.println(i + " th step");
			String content = list.item(i).getTextContent();
			String[] contents = content.toLowerCase().split("[ ,.!?:]");
			if (contents.length <= 6) { //check for max length structures
				String types = "";
				for (int j = 0; j < contents.length; j++) {
					String type = "";
					if (!contents[j].equals("")) {//in cases of ", "
						try {
								type = wn.getType(contents[j]);
						} catch (Exception e) {
							type = "N\\A";
						}
						types += type + " ";
					}
				}
				writer.write(types + Gramatics.END_OF_LINE);
			}
		}
	}
	
}
