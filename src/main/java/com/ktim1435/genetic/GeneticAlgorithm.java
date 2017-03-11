package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Random;

import com.ktim1435.poem.*;
import com.ktim1435.wordnet.WordNet;

public class GeneticAlgorithm {
	public static int ROWCOUNT = 4;
	public static int SPECIMEN_COUNT = 10;
	public static WordNet wn = new WordNet();
	
	public static Line getOneLine() {
		
		Random r = new Random();
		String lineText = "";
		try {
			int wordCount = r.nextInt(4) + 2;
			while (wordCount > 0) {
				String word = wn.getWord("random");
				wordCount -= word.split(" ").length;
				lineText += word + " ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Line line = new Line(lineText);
		return line;
	}
	
	public static Verse getOneVerse() {
		Verse verse = new Verse("");
		for (int i = 0; i < ROWCOUNT; i++) {
			Line line = getOneLine();
			verse.addLine(line);
		}
		return verse;
	}
	
	public static ArrayList<Verse> getOneGeneration() {
		ArrayList<Verse> verses = new ArrayList<Verse>();
		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			verses.add(getOneVerse());
		}
		return verses;
		
	}

}
