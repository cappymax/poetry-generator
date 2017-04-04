package com.ktim1435.wordnet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

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
	// public ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	public Map<String, Integer> wordStats = new TreeMap<String, Integer>();
	public Map<String, Integer> rootStats = new TreeMap<String, Integer>();
	ArrayList<Word> words = new ArrayList<Word>();

	static <K,V extends Comparable<? super V>> SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
            new Comparator<Map.Entry<K,V>>() {
                public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1; // Special fix to preserve items with equal values
                }
            }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

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

			int q = 0;

			while (line != null) {
				q++;
				if (q % 10 == 0) {
					System.out.println(q);
				}
				sb.append(line);
				thisLine = sb.toString();
				splittedLine = thisLine.split("[|]");
				//System.out.println(splittedLine[0] + " " + splittedLine[1]);

				splittedText = splittedLine[0].split(" ");
				splittedRoots = splittedLine[1].split(" ");
				splittedTypes = splittedLine[2].split(" ");

				for (int i = 0; i < splittedText.length; i++) {
					Word w = new Word(splittedText[i], splittedRoots[i], splittedTypes[i]);

					if (rootStats.containsKey(w.getRoot())) 
						rootStats.put(w.getRoot(), rootStats.get(w.getRoot())+1);
					else 
						rootStats.put(w.getRoot(), 1);
					
					
					if (wordStats.containsKey(w.toString())) {
						words.add(w);
						wordStats.put(w.toString(), wordStats.get(w.toString()) + 1);
					} else
						wordStats.put(w.toString(), 1);
				}

				// sentences.add(new Sentence(words, wn));
				sb = new StringBuilder();
				line = br.readLine();
			}

			// everything = sb.toString();
		} catch (Exception e) {
		
			//e.printStackTrace();
		} finally {
//			PrintWriter w = new PrintWriter("alma.txt");
			
			//System.out.println(entriesSortedByValues(wordStats).size());
//			w.write(entriesSortedByValues(rootStats).toString() + "\n" + entriesSortedByValues(wordStats).toString());
//			for (Entry<String, Integer> s : entriesSortedByValues(wordStats)) {
//				w.write(s.toString() + "\n");
//			}

//			w.close();
			


			//w.write(rootStats.toString().replace(' ', '\n'));
			br.close();
		}
	}

	/**
	 * Generates the sentences.txt file containing a well written format of
	 * sentence | root words | types
	 */
	public void generateSentenceFile() {
		System.out.println("Writing sentence types to file.");
		try {
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
		String type, root, text, types, roots, texts;
		for (int i = 0; i < list.getLength(); i++) {
			if (i % 10 == 0)
				System.out.println(i + " th step");
			String content = list.item(i).getTextContent();
			String[] contents = content.toLowerCase().split("[ ,.!?:]");
			if (contents.length <= 6) { // check for max length structures
				types = "";
				roots = "";
				texts = "";
				for (int j = 0; j < contents.length; j++) {
					if (!contents[j].equals("")) {// in cases of ", "
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
