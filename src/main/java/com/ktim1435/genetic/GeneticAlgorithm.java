package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Random;

import com.ktim1435.poem.Line;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;

public class GeneticAlgorithm {
	public int ROWCOUNT = 4;
	public int SPECIMEN_COUNT = 10;
	public WordNet wn;
	public SentenceAnalyzer sa;
	private Generation currentGeneration;
	
	public GeneticAlgorithm(WordNet wn, SentenceAnalyzer sa) {
		this.wn = wn;
		this.sa = sa;
	}
	
	public Line getOneLine() {
		
		Random r = new Random();
		String lineText = "";
		try {
			int wordCount = r.nextInt(4) + 2;
			while (wordCount > 0) {
				String word = sa.getWord().getText();
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
	
	public Generation getOneGeneration() {
		ArrayList<Verse> verses = new ArrayList<Verse>();
		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			verses.add(getOneVerse());
		}
		currentGeneration = new Generation(verses);
		
		return currentGeneration;
		
	}

}
