package com.ktim1435.wordnet;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.print.Doc;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ktim1435.language.Gramatics;
import com.ktim1435.language.Word;

public class WordNet {
	private File inputFile;
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private ArrayList<Word> words = new ArrayList<Word>();
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
		NodeList literalList = doc.getElementsByTagName("LITERAL");
		
		parseWords(literalList);
		
		
	}
	
	public void createSentenceFile() {
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
								type = getType(contents[j]);
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

	private void parseWords(NodeList list) throws Exception {
		for (int i = 0; i < list.getLength(); i++) {
			String type = list.item(i).getParentNode().getParentNode().getChildNodes().item(0).getTextContent().split("-")[2];
			String content = list.item(i).getTextContent().split("[0-9]")[0];
			if (content.indexOf('(') == -1 && content.indexOf('_') == -1) {
				Word word = new Word();
				word.setText(content);
				if (type.equals("n")) {
					nouns.add(content);
					word.setType("noun");
				} else if (type.equals("v")) {
					verbs.add(content);
					word.setType("verb");
				} else if (type.equals("a")) {
					adjectives.add(content);
					word.setType("adjective");
				} else if (type.equals("b")) {
					adverbs.add(content);
					word.setType("adverb");
				} else {
					throw new Exception("Non Existent Type");
				}
				words.add(word);
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
	 * Returns the supposed type of an input word, if said word is a modified version of an existing word, it will try to guess the type.
	 * @param word
	 * @return The type of the word parameter.
	 * @throws Exception
	 */
	public String getType(String word) throws Exception {
		Word result = new Word();
		Word resultSpecial = new Word();
		Word resultNonSpecial = new Word();
		
//		try {
//			String type = getOneDeepType(word);
//			return type;
//		} catch (Exception e) {
			word = word.toLowerCase();
			resultSpecial = checkAllSubwords(word);
			
			word = replaceSpecialChars(word);
			resultNonSpecial = checkAllSubwords(word);

			result = resultSpecial.getText().length() > resultNonSpecial.getText().length() ? resultSpecial : resultNonSpecial;
			
			if(noResultFound(result))
				throw new Exception("Not Mapped Word");
			else {
//				if (result.getType().equals("Noun")) {
//					nouns.add(result.getText());
//				} else if (result.getType().equals("Verb")) {
//					verbs.add(result.getText());
//				} else if (result.getType().equals("Adjective")) {
//					adjectives.add(result.getText());
//				} else if (result.getType().equals("Adverb")) {
//					adverbs.add(result.getText());
//				}
//				words.add(result);
				return result.getType();
			}
				
//		}
	}
	
	/**
	 * Gets the actual word type, not looking for subword types.
	 * @param word
	 * @return
	 * @throws Exception
	 */
	private String getOneDeepType(String word) throws Exception {
		word = word.toLowerCase();
		if (word.matches("[0-9]*"))
			return "Number";
		if (word.equals("én") || word.equals("te") || word.equals("ő") || word.equals("mi") || word.equals("ti") || word.equals("ők")
				|| word.equals("engem") || word.equals("téged") || word.equals("őt")
				|| word.equals("minket") || word.equals("titeket") || word.equals("őket"))
			return "Pronoun";
		if (word.equals("a") || word.equals("az") || word.equals("egy"))
			return "Article";
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
	

	private boolean noResultFound(Word result) {
		return result.getType().equals("");
	}

	/**
	 * Checks all subwords of a word to estimate the words type.
	 * @param word
	 * @return
	 */
	private Word checkAllSubwords(String word) {
		char[] chars = word.toCharArray();
		Word longestSubword = new Word();
		for (int k = 0; k < chars.length; k++) {
			for (int i = chars.length; i > k; i--) {
				String newWord = "";
				for (int j = k; j < i; j++) {
					newWord += chars[j];
				}

				try {
					String type = getOneDeepType(newWord);
					if ((chars.length == newWord.length() || (!type.equals("Article") && !type.equals("Pronoun")))
							&& newWord.length() > longestSubword.getText().length()) {
						if (type.equals("Noun")) {
								boolean isVerb = false;
								try {
									isVerb = chars[i] == 'z';
								} catch (ArrayIndexOutOfBoundsException e) {
									//do nothing, we are at the end of the string
									//e.printStackTrace();
								}
								if (isVerb) {
									longestSubword.setText(newWord + 'z');
									longestSubword.setType("Verb");
								} else {
									longestSubword.setText(newWord);
									longestSubword.setType(type);
								}
						} else {
							longestSubword.setText(newWord);
							longestSubword.setType(type);
						}
					}
				} catch (Exception e) {
					//do nothing, the newly created string doasn't exist
				}
			}	
		}
		//System.out.println(longestSubword.getType());
		return longestSubword;
	}

	/**
	 * Replaces all special hungarian characters in a word with standard characters
	 * @param word
	 * @return
	 */
	private String replaceSpecialChars(String word) {
		word = word.replace("á", "a");
		word = word.replace("é", "e");
		word = word.replace("ó", "o");
		word = word.replace("ö", "o");
		word = word.replace("ő", "o");
		word = word.replace("ú", "u");
		word = word.replace("ü", "u");
		word = word.replace("ű", "u");
		word = word.replace("í", "i"); 
		return word;
	}
	

	
	
	

}
