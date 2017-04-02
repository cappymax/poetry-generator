package com.ktim1435.wordnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ktim1435.language.Gramatics;
import com.ktim1435.language.Sentence;
import com.ktim1435.language.Word;

public class SentenceAnalyzer {
	private Document doc;
	private WordNet wn;
	public ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	
	public SentenceAnalyzer(WordNet wn) {
		this.wn = wn;
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
	
	
	public void readSentenceFile() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("sentences.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    String thisLine;
		    String[] splittedLine, splittedText, splittedRoots, splittedTypes;
		    ArrayList<Word> words = new ArrayList<Word>();
		    
		    while (line != null) {
		        sb.append(line);
		        thisLine = sb.toString();
		        splittedLine = thisLine.split("[|]");
		        
		        splittedText = splittedLine[0].split(" ");
		        splittedRoots = splittedLine[1].split(" ");
		        splittedTypes = splittedLine[2].split(" ");

		        for (int i = 0; i < splittedText.length; i++) 
		        	words.add(new Word(splittedText[i],splittedRoots[i],splittedTypes[i]));
		        
		        sentences.add(new Sentence(words, wn));
		        line = br.readLine();
		    }
		    //everything = sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		    br.close();
		}
	}
	
	/**
	 * Generates the sentences.txt file containing a well written format of sentence | root words | types
	 */
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
		Word word = new Word();
		Word calculated = new Word();
		String type,root,text,types,roots,texts;
		for (int i = 0 ; i < list.getLength(); i++) {
			if (i % 10 == 0) System.out.println(i + " th step");
			String content = list.item(i).getTextContent();
			String[] contents = content.toLowerCase().split("[ ,.!?:]");
			if (contents.length <= 6) { //check for max length structures
				types = "";
				roots = "";
				texts = "";
				for (int j = 0; j < contents.length; j++) {
					if (!contents[j].equals("")) {//in cases of ", "
						try {
							calculated = wn.getRoot(contents[j]);
							type = calculated.getType();
							root = calculated.getText();
							text = contents[j];
						} catch (Exception e) {
							text = contents[j];
							root = "N\\A";
							type = "N\\A";
						}
						types += type + " ";
						roots += root + " ";
						texts += text + " ";
						
					}
				}
				writer.write(texts + "|" + roots + "|" + types + Gramatics.END_OF_LINE);
			}
		}
	}
	
}
