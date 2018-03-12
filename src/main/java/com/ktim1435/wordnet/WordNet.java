package com.ktim1435.wordnet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.ktim1435.language.Word;

public class WordNet {
	private Document doc;
	private ArrayList<String> domains = new ArrayList<String>();
	private ArrayList<Word> words = new ArrayList<Word>();
	private Map<String, ArrayList<Word>> separatedWords = new HashMap<String, ArrayList<Word>>();
	private Map<String, String> typesMap = new HashMap<String, String>();

	/**
	 * Create an instance of the WordNet class, in order to access hungarian
	 * word generating algorithms.
	 */
	public WordNet() {
		try {
			prepareTypesMap();
			doc = openDoc();
			parseDoc();
			praseDocForDomains();
			addSpecificWords();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void praseDocForDomains() {
		NodeList parsedDomains = doc.getElementsByTagName("DOMAIN");
		for (int i = 0; i < parsedDomains.getLength(); i++) {
			String name = parsedDomains.item(i).getTextContent();
			if (!domains.contains(name))
				domains.add(name);
		}
	}
	
	public ArrayList<String> getDomains() {
		return domains;
	}

	private void prepareTypesMap() {
		typesMap.put("n", "Noun");
		typesMap.put("noun", "Noun");
		typesMap.put("Noun", "Noun");
		typesMap.put("v", "Verb");
		typesMap.put("verb", "Verb");
		typesMap.put("verb", "Verb");
		typesMap.put("a", "Adjective");
		typesMap.put("adjective", "Adjective");
		typesMap.put("Adjective", "Adjective");
		typesMap.put("b", "Adverb");
		typesMap.put("adverb", "Adverb");
		typesMap.put("Adverb", "Adverb");
		typesMap.put("pronoun", "Pronoun");
		typesMap.put("Pronoun", "Pronoun");
		typesMap.put("article", "Article");
		typesMap.put("Article", "Article");
		typesMap.put("number", "Number");
		typesMap.put("Number", "Number");
	}

	private void addSpecificWords() {
		words.add(new Word("én", "én", "Pronoun"));
		words.add(new Word("te", "te", "Pronoun"));
		words.add(new Word("ő", "ő", "Pronoun"));
		words.add(new Word("mi", "mi", "Pronoun"));
		words.add(new Word("ti", "ti", "Pronoun"));
		words.add(new Word("ők", "ők", "Pronoun"));
		words.add(new Word("engem", "engem", "Pronoun"));
		words.add(new Word("téged", "téged", "Pronoun"));
		words.add(new Word("őt", "őt", "Pronoun"));
		words.add(new Word("minket", "minket", "Pronoun"));
		words.add(new Word("titeket", "titeket", "Pronoun"));
		words.add(new Word("őket", "őket", "Pronoun"));
		words.add(new Word("ez", "ez", "Pronoun"));
		words.add(new Word("egy", "egy", "Article"));
		words.add(new Word("a", "a", "Article"));
		words.add(new Word("az", "az", "Article"));
		words.add(new Word("mind", "mind", "Number"));
	}

	/**
	 * Opens the huwn.xml document for later parsing.
	 * 
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
	 * 
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
		String domain = getDomainOfWordNode(list.item(i));
		String content = getSingleContentOfWordNode(list.item(i));
		
		if (isValidContent(content))
			addWordToWordNet(type, content, domain);
	}

	private String getDomainOfWordNode(Node item) {
		NodeList cn = item.getParentNode().getParentNode().getChildNodes();
		for (int i = 0; i<cn.getLength(); i++) 
			if(cn.item(i).getNodeName().equals("DOMAIN"))
				return cn.item(i).getTextContent();
		return "NODOM";
	}

	private void addWordToWordNet(String type, String content, String domain) {
		words.add(new Word(content, content, typesMap.get(type), domain));
	}

	private boolean isValidContent(String content) {
		return content.indexOf('(') == -1 && content.indexOf('_') == -1 && content.indexOf(" ") == -1;
	}

	private String getSingleContentOfWordNode(Node i) {
		return i.getTextContent().split("[0-9]")[0];
	}

	private String getTypeOfWordNode(Node i) {
		return i.getParentNode().getParentNode().getChildNodes().item(0).getTextContent().split("-")[2];
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
			// IF WANTED
			// addWordToWordNet(result.getType(), result.getText());
			return result.getType();
		}
	}

	/**
	 * Returns the root word that is already in the WordNet
	 * 
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
			// IF WANTED
			// addWordToWordNet(result.getType(), result.getText());
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
		Word w = getWord(word);

		if (word.matches("[0-9]*"))
			return "Number";

		if (w != null)
			return w.getType();

		throw new Exception("Not Mapped Word");
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
	public Word getWordByType(String type) {
		Random r = new Random();
		ArrayList<Word> localWords;
		int i = 0;
		if (separatedWords.containsKey(type))
			localWords = separatedWords.get(type);
		else {
			localWords = new ArrayList<Word>();
			for (Word word : words)
				if (word.getType().equals(type))
					localWords.add(word);
			separatedWords.put(type, localWords);
		}
		i = r.nextInt(localWords.size());

		return localWords.get(i);
	}

	/**
	 * Returns a random word.
	 * 
	 * @return
	 */
	public Word getRandomWord() {
		Random r = new Random();
		return words.get(r.nextInt(words.size()));
	}

	/**
	 * Get the full word object if it already exists in wordnet, only by string.
	 * 
	 * @param word
	 * @return
	 */
	private Word getWord(String word) {
		for (Word w : words)
			if (w.getText().equals(word))
				return w;
		return null;
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

	public String getDomainOfWordByRoot(String word) {
		return getWord(word).getDomain();
	}

}
