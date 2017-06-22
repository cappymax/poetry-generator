package com.ktim1435.wordnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ktim1435.language.Gramatics;
import com.ktim1435.language.Word;

public class SentenceAnalyzer {
	private Document doc;
	private WordNet wn;
	private Random r = new Random(System.currentTimeMillis());
	// public ArrayList<Sentence> sentences = new ArrayList<Sentence>();
	

	public Map<String, TreeMap<String, Integer>> beforeType = new TreeMap<String, TreeMap<String,Integer>>();
	public  Map<String,ArrayList<Entry<String, Integer>>> sortedBeforeType = new TreeMap();
	
	public  Map<String, TreeMap<String, Integer>> afterType = new TreeMap<String, TreeMap<String,Integer>>();
	public  Map<String,ArrayList<Entry<String, Integer>>> sortedAfterType = new TreeMap();;
	
	
	
	public Map<String, String> typeMap = new TreeMap<String, String>();
	public Map<String, String> domainMap = new TreeMap<String, String>();
	public Map<String, Integer> wordStats = new TreeMap<String, Integer>();
	public Map<String, Integer> rootStats = new TreeMap<String, Integer>();
	public Map<String, Integer> typeStats = new TreeMap<String, Integer>();
	public Map<String,Map<Word,Integer>> wordListByType= new HashMap<String, Map<Word,Integer>>();
	
	public SortedSet<Entry<String, Integer>> sortedWordStats;
	public SortedSet<Entry<String, Integer>> sortedRootStats;
	public SortedSet<Entry<String, Integer>> sortedTypeStats;

	ArrayList<Word> words = new ArrayList<Word>();

	//sorting on a set with custom compare, special thanks to stack overflow for this one
	static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e1.getValue().compareTo(e2.getValue());
				return res != 0 ? res : 1; // Special fix to preserve items with
											// equal values
			}
		});
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
		BufferedReader br  = new BufferedReader(
			    new InputStreamReader(new FileInputStream(new File("sentences.txt")),"UTF-8"));
		try {
			System.out.println("Reading sentence file");
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String thisLine;
			String[] splittedLine, splittedText, splittedRoots, splittedTypes;
			
			int q = 0;

			while (line != null) {
				sb.append(line);
				thisLine = sb.toString();
				splittedLine = thisLine.split("[|]");

				splittedText = splittedLine[0].split(" ");
				splittedRoots = splittedLine[1].split(" ");
				splittedTypes = splittedLine[2].split(" ");

				for (int i = 0; i < splittedText.length; i++) {
					String domain;
					try {
						domain = wn.getDomainOfWordByRoot(splittedRoots[i]);
					} catch(Exception e) {
						domain = "NODOM";
					}
					Word w = new Word(splittedText[i], splittedRoots[i], splittedTypes[i], domain);
						
					if (typeStats.containsKey(w.getType()))
						typeStats.put(w.getType(), typeStats.get(w.getType()) + 1);
					else
						typeStats.put(w.getType(), 1);

					if (rootStats.containsKey(w.getRoot()))
						rootStats.put(w.getRoot(), rootStats.get(w.getRoot()) + 1);
					else
						rootStats.put(w.getRoot(), 1);

					if (wordStats.containsKey(w.toString())) {

						TreeMap<String, Integer> before = beforeType.get(w.getText());
						TreeMap<String, Integer> after = afterType.get(w.getText());		
			
						String beforeText = i == 0 ? "nothing" : splittedTypes[i-1];
						String afterText = i == splittedText.length - 1 ? "nothing" : splittedTypes[i+1];
					
						
						if (!before.containsKey(beforeText)) 
							before.put(beforeText, 1);
						else 
							before.put(beforeText, before.get(beforeText) + 1);	
						
						if (!after.containsKey(afterText)) 
							after.put(afterText, 1);
						else 
							after.put(afterText, after.get(afterText) + 1);
						
						beforeType.put(w.getText(), before);
						afterType.put(w.getText(), after);
						
						wordStats.put(w.toString(), wordStats.get(w.toString()) + 1);
					} else {
						TreeMap<String, Integer> before = new TreeMap<String, Integer>();
						TreeMap<String, Integer> after = new TreeMap<String, Integer>();	
						if (i == 0) { 
							before.put("nothing", 1);
							beforeType.put(w.getText(), before);
						} else {
							before.put(splittedTypes[i-1], 1);
							beforeType.put(w.getText(), before);
						}
						if (i == splittedText.length -1) {
							after.put("nothing", 1);
							afterType.put(w.getText(), after);
						}
						else {
							after.put(splittedTypes[i+1], 1);
							afterType.put(w.getText(), after);
						}
						
						typeMap.put(w.getText(), w.getType());
						domainMap.put(w.getText(), w.getDomain());
						
						words.add(w);
						wordStats.put(w.toString(), 1);
					}
				}
				sb = new StringBuilder();
				line = br.readLine();
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			sortedWordStats = entriesSortedByValues(wordStats);
			sortedRootStats = entriesSortedByValues(rootStats);
			sortedTypeStats = entriesSortedByValues(typeStats);
				
			for (String s : afterType.keySet()) {
				TreeMap<String, Integer> after = afterType.get(s);
				SortedSet<Entry<String, Integer>> sortedAfter = entriesSortedByValues(after);
				ArrayList<Entry<String,Integer>> sortedAfter2 = new ArrayList<Map.Entry<String,Integer>>();
				int i = 0;
				for (Entry e : sortedAfter.toArray(new Entry[1])) {
					SimpleEntry e2 = new SimpleEntry(e.getKey(),e.getValue());
					if (i == sortedAfter.size() - 6) {
						e2.setValue(1);
					}  else if (i == sortedAfter.size() - 5) {
						e2.setValue(2);
					} else if (i == sortedAfter.size() - 4) {
						e2.setValue(3);
					} else if (i == sortedAfter.size() - 3) {
						e2.setValue(4);
					} else if (i == sortedAfter.size() - 2) {
						e2.setValue(5);
					} else if (i == sortedAfter.size() - 1 ) {
						e2.setValue(6);
					}else e2.setValue(0);
					
					
					i++;
					sortedAfter2.add(e2);

				}
				

				sortedAfterType.put(s, sortedAfter2);
			}
			
			
			
			
			for (String s : beforeType.keySet()) {
				TreeMap<String, Integer> before = beforeType.get(s);
				SortedSet<Entry<String, Integer>> sortedBefore = entriesSortedByValues(before);
				ArrayList<Entry<String,Integer>> sortedBefore2 = new ArrayList<Map.Entry<String,Integer>>();
				int i = 0;
				for (Entry<String, Integer> e : sortedBefore) {
					if (i == sortedBefore.size() - 6) {
						e.setValue(2);
					} else if (i == sortedBefore.size() - 7) {
						e.setValue(1);
					} else if (i == sortedBefore.size() - 5) {
						e.setValue(3);
					} else if (i == sortedBefore.size() - 4) {
						e.setValue(4);
					} else if (i == sortedBefore.size() - 2) {
						e.setValue(5);
					} else if (i == sortedBefore.size() - 1) {
						e.setValue(6);
					} else e.setValue(0);
					
					i++;
					sortedBefore2.add(e);
				}
				
				sortedBeforeType.put(s, sortedBefore2);
			}
			
//			System.out.println("Cumsum words");
//			wordStats = cumSum(wordStats);
//			System.out.println("CumSum roots");
//			rootStats = cumSum(rootStats);
			System.out.println("CumSum types");
			typeStats = cumSum(typeStats);
			


			// w.write(rootStats.toString().replace(' ', '\n'));
			System.out.println("Done reading");
			br.close();
		}
	}

	

	private <T> Map<T,Integer> cumSum(Map<T, Integer> stats) {
		Map<T, Integer> newMap = new TreeMap<T, Integer>();
		Object[] array = stats.keySet().toArray();
		newMap.put((T) array[0], stats.get(array[0]));
		int current = stats.get(array[0]);
		
		for (int i = 1; i < stats.keySet().toArray().length; i++) {
			current += stats.get(array[i]);
			newMap.put((T) array[i], current);
		}
			
		return newMap;
		
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

	public ArrayList<Word> getWordsByRoot(String root) {
		ArrayList<Word> result = new ArrayList<Word>();
		for (Word w : words) {
			if (w.getRoot().equals(root))
				result.add(w);
			if (w.getText().contains("root") && !result.contains(w))
				result.add(w);
		}
		return result;
	}

	/**
	 * Returns a word, that corresponds to the statistics gotten from senteces.txt
	 * @return
	 */
	public Word getWord() {
		double a = r.nextDouble();
		if (a < 0.1) {
			return wn.getRandomWord();
		}
		int b = r.nextInt(typeStats.get("Verb"));
		for (int i = 0; i < typeStats.size(); i++) 
			if (b < getIthTypeStat(i)) 
				return getOneWordWithType(typeStats.keySet().toArray()[i].toString()); 
			
		return null;
	}
	
	
	private Integer getIthTypeStat(int i) {
		return typeStats.get(typeStats.keySet().toArray()[i]);
	}
	
	
	private Word getOneWordWithType(String type) {
		if (!wordListByType.containsKey(type)) {
			Map<Word,Integer> result = new TreeMap<Word, Integer>();
			for (Word w : words) {
				if (w.getType().equals(type))
					result.put(w, wordStats.get(w.getText()));
			}
			System.out.println("CumSum result " + type);
			result = cumSum(result);
			wordListByType.put(type,result);
		}
		
		Map<Word,Integer> result = wordListByType.get(type);
		Object[] resultSet = result.keySet().toArray();
		
		int a = r.nextInt(result.get(resultSet[resultSet.length-1]));

		for (int i = 0; i < result.size(); i++) 
			if (a <= getIthResultStat(i, result, resultSet)) 
				return (Word) resultSet[i];
			
		return null;
	}
	
	private Integer getIthResultStat(int i,Map<Word,Integer> result, Object[] resultSet) {
		return result.get(resultSet[i]);
	}

	public ArrayList<Entry<String, Integer>> getBeforeTypes(String w) {
		return sortedBeforeType.get(w);
	}

	public ArrayList<Entry<String, Integer>> getAfterTypes(String w) {
	
		return sortedAfterType.get(w);
	}
	
	public String getWordType(String a) {
		return typeMap.get(a);
	}
	
	public String getWordDomain(String a) {
		return domainMap.get(a);
	}
}
