package com.ktim1435.poem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ktim1435.language.Gramatics;
import com.ktim1435.poetry.Rhyme;

public class Verse implements PoeticElement {
	private List<Line> lines = new ArrayList<Line>();
	private Rhyme rhyme = new Rhyme();

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

	public String getLines() {
		String combinedLines = "";
		for (Line l : lines) {
			combinedLines += l.getText() + Gramatics.END_OF_LINE;
		}
		return combinedLines;
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
		return this.getLines();
	}
	
	public Rhyme getRhyme() {
		rhyme.calculateRhyme(lines);
		return rhyme;
	}
	
	
}
