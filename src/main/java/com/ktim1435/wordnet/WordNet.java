package com.ktim1435.wordnet;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import javax.print.Doc;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ktim1435.language.Gramatics;
import com.ktim1435.language.Word;

public class WordNet {
	private Document doc;
	private ArrayList<Word> words = new ArrayList<Word>();
	private ArrayList<String> nouns = new ArrayList<String>();
	private ArrayList<String> verbs = new ArrayList<String>();
	private ArrayList<String> adjectives = new ArrayList<String>();
	private ArrayList<String> adverbs = new ArrayList<String>();

	/**
	 * Create an instance of the WordNet class, in order to access hungarian
	 * word generating algorithms.
	 */
	public WordNet() {
		try {
			doc = openDoc();
			parseDoc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the huwn.xml document for later parsing.
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document openDoc() throws ParserConfigurationException, SAXException, IOException {
		File inputFile;
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		Document opennedDoc;

		inputFile = new File("huwn.xml");
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		opennedDoc = dBuilder.parse(inputFile);
		opennedDoc.getDocumentElement().normalize();

		return opennedDoc;
	}

	/**
	 * Calls for word parsing of words in LITERAL tags.
	 * @throws Exception
	 */
	private void parseDoc() throws Exception {
		NodeList literalList = doc.getElementsByTagName("LITERAL");
		parseWords(literalList);
	}

	
	private void parseWords(NodeList list) throws Exception {
		for (int i = 0; i < list.getLength(); i++)
			processIthWord(list, i);
	}

	private void processIthWord(NodeList list, int i) throws Exception {
		String type = getTypeOfWordNode(list.item(i));
		String content = getSingleContentOfWordNode(list.item(i));
		
		if (isValidContent(content))
			addWordToWordNet(type, content);
	}

	private void addWordToWordNet(String type, String content) throws Exception {
		if (isNoun(type)) 
			addWordToNouns(content);
		else if (isVerb(type)) 
			addWordToVerbs(content);
		else if (isAdjective(type))
			addWordToAdjectives(content);
		else if (isAdverb(type))
			addWordToAdverbs(content);
		else 
			throw new Exception("Non Existent Type");
	}

	private boolean isAdverb(String type) {
		return type.equals("b") || type.equals("adverb");
	}

	private boolean isAdjective(String type) {
		return type.equals("a") || type.equals("adjective");
	}

	private boolean isVerb(String type) {
		return type.equals("v") || type.equals("verb");
	}

	private boolean isNoun(String type) {
		return type.equals("n") || type.equals("noun");
	}

	private void addWordToAdverbs(String content) {
		Word word = new Word();
		word.setText(content);
		adverbs.add(content);
		word.setType("adverb");
		words.add(word);
	}

	private void addWordToAdjectives(String content) {
		Word word = new Word();
		word.setText(content);
		adjectives.add(content);
		word.setType("adjective");
		words.add(word);
	}

	private void addWordToVerbs(String content) {
		Word word = new Word();
		word.setText(content);
		verbs.add(content);
		word.setType("verb");
		words.add(word);
	}

	private void addWordToNouns(String content) {
		Word word = new Word();
		word.setText(content);
		nouns.add(content);
		word.setType("noun");
		words.add(word);
	}

	private boolean isValidContent(String content) {
		return content.indexOf('(') == -1 && content.indexOf('_') == -1;
	}

	private String getSingleContentOfWordNode(Node i) {
		return i.getTextContent().split("[0-9]")[0];
	}

	private String getTypeOfWordNode(Node i) {
		return i.getParentNode().getParentNode().getChildNodes().item(0).getTextContent()
				.split("-")[2];
	}

	/**
	 * Generate a random word with a set type. Types include:<br>
	 * noun<br>
	 * verb<br>
	 * adverb<br>
	 * adjective<br>
	 * random - any type of word<br>
	 * 
	 * @param type
	 *            - type of the expected word.
	 * @return A new word of set type.
	 * @throws Exception
	 *             - non existent type
	 */
	public String getWord(String type) throws Exception {
		Random r = new Random();

		if (isNoun(type))
			return nouns.get(r.nextInt(nouns.size()));
		if (isVerb(type))
			return verbs.get(r.nextInt(verbs.size()));
		if (isAdjective(type))
			return adjectives.get(r.nextInt(adjectives.size()));
		if (isAdverb(type))
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
	 * Returns the supposed type of an input word, if said word is a modified
	 * version of an existing word, it will try to guess the type.
	 * 
	 * @param word
	 * @return The type of the word parameter.
	 * @throws Exception
	 */
	public String getType(String word) throws Exception {
		Word result = new Word();
		Word resultSpecial = new Word();
		Word resultNonSpecial = new Word();

		word = word.toLowerCase();
		resultSpecial = checkAllSubwords(word);

		word = replaceSpecialChars(word);
		resultNonSpecial = checkAllSubwords(word);

		result = getBetterResult(resultSpecial, resultNonSpecial);

		if (noResultFound(result))
			throw new Exception("Not Mapped Word");
		else {
			//IF WANTED
			//addWordToWordNet(result.getType(), result.getText());
			return result.getType();
		}
	}
	
	/**
	 * Returns the basic word that is already in the WordNet
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public Word getRoot(String word) throws Exception {
		Word result = new Word();
		Word resultSpecial = new Word();
		Word resultNonSpecial = new Word();

		word = word.toLowerCase();
		resultSpecial = checkAllSubwords(word);

		word = replaceSpecialChars(word);
		resultNonSpecial = checkAllSubwords(word);

		result = getBetterResult(resultSpecial, resultNonSpecial);

		if (noResultFound(result))
			throw new Exception("Not Mapped Word");
		else {
			//IF WANTED
			//addWordToWordNet(result.getType(), result.getText());
			return result;
		}
	}

	private Word getBetterResult(Word resultSpecial, Word resultNonSpecial) {
		return resultSpecial.getText().length() > resultNonSpecial.getText().length() ? resultSpecial
				: resultNonSpecial;
	}

	/**
	 * Gets the actual word type, not looking for subword types.
	 * 
	 * @param word
	 * @return
	 * @throws Exception
	 */
	private String getOneDeepType(String word) throws Exception {
		word = word.toLowerCase();
		if (word.matches("[0-9]*"))
			return "Number";
		if (word.equals("én") || word.equals("te") || word.equals("ő") || word.equals("mi") || word.equals("ti")
				|| word.equals("ők") || word.equals("engem") || word.equals("téged") || word.equals("őt")
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
	 * 
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
								// do nothing, we are at the end of the string
								// e.printStackTrace();
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
					// do nothing, the newly created string doasn't exist
				}
			}
		}
		// System.out.println(longestSubword.getType());
		return longestSubword;
	}

	/**
	 * Replaces all special hungarian characters in a word with standard
	 * characters
	 * 
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
