package com.ktim1435.poem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.ktim1435.language.Gramatics;
import com.ktim1435.poetry.Rhyme;
import com.ktim1435.poetry.Rithm;
import com.ktim1435.wordnet.SentenceAnalyzer;

public class Verse implements PoeticElement {
	private List<Line> lines = new ArrayList<Line>();
	private Rhyme rhyme = new Rhyme();
	private Rithm rithm = new Rithm();
	private Random r = new Random();
	
	
	public Verse(ArrayList<Line> lines) {
		this.lines = lines;
	}
	
	public Verse(String newVerse) {
		int index = 0;
		String oneLine = "";

		newVerse += Gramatics.END_OF_LINE;
		while (!newVerse.isEmpty()) {
			index = newVerse.indexOf(Gramatics.END_OF_LINE);
			oneLine = newVerse.substring(0, index);
			lines.add(new Line(oneLine));

			newVerse = newVerse.substring(index + 1, newVerse.length());
		}
		lines = Gramatics.correctLines(lines);
	}
	
	public Verse(Verse verse) {
		int index = 0;
		String oneLine = "";
		String newVerse = verse.toString();
		
		newVerse += Gramatics.END_OF_LINE;
		while (!newVerse.isEmpty()) {
			index = newVerse.indexOf(Gramatics.END_OF_LINE);
			oneLine = newVerse.substring(0, index);
			lines.add(new Line(oneLine));

			newVerse = newVerse.substring(index + 1, newVerse.length());
		}
		lines = Gramatics.correctLines(lines);
	}

	public ArrayList<Line> getLines() {
		return (ArrayList<Line>) lines;
	}

	public void addLine(Line newLine) {
		lines.add(newLine);
		lines = Gramatics.correctLines(lines);
	}

	public void addLines(Collection<Line> newLines) {
		lines.addAll(newLines);
		lines = Gramatics.correctLines(lines);
	}

	public String toString() {
		String combinedLines = "";
		for (Line l : lines) {
			combinedLines += l.getText() + Gramatics.END_OF_LINE;
		}
		return combinedLines;
	}
	
	public Rhyme getRhyme() {
		rhyme.calculateRhyme(lines);
		return rhyme;
	}
	
	public Rithm getRithm() {
		rithm.calculateRithm(lines);
		return rithm;
	}

	
	public Verse changeWord(SentenceAnalyzer sa) {
		int lineNr = r.nextInt(lines.size());
		List<Line> localLines = lines;
		
		localLines.get(lineNr).changeWord(sa);

		Verse v = new Verse((ArrayList<Line>)localLines);
		
		
		return v;
	}

	public Verse deleteWord(int MIN_WORD_COUNT) {
		int lineNr = r.nextInt(lines.size());
		List<Line> localLines = lines;
		
		localLines.get(lineNr).deleteWord(MIN_WORD_COUNT);

		Verse v = new Verse((ArrayList<Line>)localLines);
		
		
		return v;
	}

	public Verse addWord(SentenceAnalyzer sa, int MAX_WORD_COUNT) {
		int lineNr = r.nextInt(lines.size());
		List<Line> localLines = lines;
		
		localLines.get(lineNr).addWord(sa,MAX_WORD_COUNT);

		Verse v = new Verse((ArrayList<Line>)localLines);
		
		
		return v;
	}

	public Verse switchup() {
		int lineNr = r.nextInt(lines.size());
		List<Line> localLines = lines;
		localLines.get(lineNr).switchup();
		
		Verse v = new Verse((ArrayList<Line>)localLines);
		
		
		return v;
	}
	
	public int calculateSemantics(SentenceAnalyzer sa) {
		int s = 0;
		for (int i = 0; i < lines.size(); i++) {
			s += lines.get(i).calculateSemantics(sa);
		}
//		if repeating is in need of fixing, uncomment
//		int repeating = 0;
//		for (int i = 0; i < lines.size() - 1; i++) {
//			for (int j = i + 1; j < lines.size(); j++) {
//				if (lines.get(i).getText().equals(lines.get(j).getText())) repeating++;
//			}
//		}
//		HashMap<String, Integer> wordsMap = new HashMap<String, Integer>();
//		for (int i = 0; i < lines.size(); i++) {
//			String[] words = lines.get(0).toString().split(" ");
//			for (int j = 0; j < words.length; j++) {
//				words[j] = words[j].toLowerCase();
//				if (wordsMap.containsKey(words[j])) {
//					wordsMap.put(words[j], wordsMap.get(words[j])+1);
//				} else {
//					wordsMap.put(words[j], 1);
//				}
//			}
//		}
//		int s2 = 0;
//		for (String k : wordsMap.keySet()) {
//			s2 += wordsMap.get(k);
//		}
		
		return (s / lines.size());// - repeating - (s2 / wordsMap.size());
	}

	public int calculateDomainScore(SentenceAnalyzer sa, String string) {
		int s = 0;
		for (int i = 0; i < lines.size(); i++) {
			s += lines.get(i).calculateDomainScore(sa, string);
		}
		return s / lines.size();
	}
	
	
}
