package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Random;

import com.ktim1435.poem.*;
import com.ktim1435.wordnet.WordNet;

public class GeneticAlgorithm {
	public int ROWCOUNT = 4;
	public int SPECIMEN_COUNT = 10;
	public WordNet wn;
	
	public GeneticAlgorithm(WordNet wn) {
		this.wn = wn;
	}
	
	public Line getOneLine() {
		
		Random r = new Random();
		String lineText = "";
		try {
			int wordCount = r.nextInt(4) + 2;
			while (wordCount > 0) {
				String word = wn.getRandomWord().getText();
				wordCount -= word.split(" ").length;
				lineText += word + " ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Line line = new Line(lineText);
		return line;
	}
	
	public Verse getOneVerse() {
		Verse verse = new Verse("");
		for (int i = 0; i < ROWCOUNT; i++) {
			Line line = getOneLine();
			verse.addLine(line);
		}
		return verse;
	}
	
	public ArrayList<Verse> getOneGeneration() {
		ArrayList<Verse> verses = new ArrayList<Verse>();
		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			verses.add(getOneVerse());
		}
		return verses;
		
	}

}
