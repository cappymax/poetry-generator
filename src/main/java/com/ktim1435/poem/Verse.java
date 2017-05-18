package com.ktim1435.poem;

import java.util.ArrayList;
import java.util.Collection;
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
	
	
}
