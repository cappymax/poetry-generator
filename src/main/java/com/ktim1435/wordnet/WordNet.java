package com.ktim1435.wordnet;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.print.Doc;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WordNet {
	private File inputFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private ArrayList<String> nouns = new ArrayList<String>();
	private ArrayList<String> verbs = new ArrayList<String>();
	private ArrayList<String> adjectives = new ArrayList<String>();
	private ArrayList<String> adverbs = new ArrayList<String>();
	
	/**
	 *	Create an instance of the WordNet class, in order to access hungarian word generating algorithms. 
	 */
	public WordNet() {
		try {
			inputFile = new File("huwn.xml");
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			parseDoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void parseDoc() throws Exception {
		NodeList list = doc.getElementsByTagName("LITERAL");
		
		for (int i = 0; i < list.getLength(); i++) {
			String type = list.item(i).getParentNode().getParentNode().getChildNodes().item(0).getTextContent().split("-")[2];
			String content = list.item(i).getTextContent().split("[0-9]")[0];
			if (content.indexOf('(') == -1) {
				if (type.equals("n")) {
					nouns.add(content);
				} else if (type.equals("v")) {
					verbs.add(content);
				} else if (type.equals("a")) {
					adjectives.add(content);
				} else if (type.equals("b")) {
					adverbs.add(content);
				} else {
					throw new Exception("Non Existent Type");
				}
			}
		}
		
	}
	
	/**
	 * Generate a random word with a set type. 
	 * Types include:<br>
	 * 		noun<br>
	 * 		verb<br>
	 * 		adverb<br>
	 * 		adjective<br>
	 * 		random - any type of word<br>
	 * @param type - type of the expected word.
	 * @return A new word of set type.
	 * @throws Exception - non existent type
	 */
	public String getWord(String type) throws Exception {
		Random r = new Random();
		
		if (type.equals("noun"))
			return nouns.get(r.nextInt(nouns.size()));
		if (type.equals("n")) 
			return nouns.get(r.nextInt(nouns.size()));
		if (type.equals("verb")) 
			return verbs.get(r.nextInt(verbs.size()));
		if (type.equals("v")) 
			return verbs.get(r.nextInt(verbs.size()));
		if (type.equals("adjective")) 
			return adjectives.get(r.nextInt(adjectives.size()));
		if (type.equals("a")) 
			return adjectives.get(r.nextInt(adjectives.size()));
		if (type.equals("adverb")) 
			return adverbs.get(r.nextInt(adverbs.size()));
		if (type.equals("b")) 
			return adverbs.get(r.nextInt(adverbs.size()));
		if (type.equals("random")) {
			int rn = r.nextInt(4);
			if (rn == 0)
				return getWord("noun");
			if (rn == 1)
				return getWord("verb");
			if (rn == 2) 
				return getWord("adjective");
			if (rn == 3)
				return getWord("adverb");
		}
		
		throw new Exception("Non Existent Type");
	}
	
	/**
	 * Returns the type of an input word.
	 * @param word
	 * @return The type of the word parameter.
	 * @throws Exception
	 */
	public String getType(String word) throws Exception {
		if (nouns.contains(word))
			return "Noun";
		if (verbs.contains(word))
			return "Verb";
		if (adjectives.contains(word))
			return "Adjective";
		if (adverbs.contains(word))
			return "Adverb";
		throw new Exception("Not Mapped Word");
	}

}
