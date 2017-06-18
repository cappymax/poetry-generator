package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Random;

import com.ktim1435.poem.Line;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;

public class GeneticAlgorithm {
	private String DOMAIN = "ANYTHING";
	private int MUTATION_PERCENT = 5;
	private int ROWCOUNT = 4;
	private int SPECIMEN_COUNT = 20;
	private int MAX_WORD_COUNT = 5;
	private int MIN_WORD_COUNT = 2;
	public WordNet wn;
	public SentenceAnalyzer sa;
	private Generation currentGeneration = null;
	private Generation newGeneration = null;
	private Random r = new Random();

	public GeneticAlgorithm(WordNet wn, SentenceAnalyzer sa, int mutPerc, String domain) {
		this.wn = wn;
		this.sa = sa;
		this.MUTATION_PERCENT = mutPerc;
		this.DOMAIN = domain;
	}

	public Line getOneLine() {

		Random r = new Random();
		String lineText = "";
		try {
			int wordCount = r.nextInt(MAX_WORD_COUNT - MIN_WORD_COUNT + 1) + MIN_WORD_COUNT;
			while (wordCount > 0) {
				String word = sa.getWord().getText();
				try {
					while (wordCount == 1 && wn.getType(word) == "Article") {
						word = sa.getWord().getText();
					}
				} catch (Exception e) {
					// do nothing, good outcome
				}

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

	public Generation getFirstGeneration() {
		ArrayList<Verse> verses = new ArrayList<Verse>();
		for (int i = 0; i < SPECIMEN_COUNT; i++) {
			verses.add(getOneVerse());
		}
		currentGeneration = new Generation(verses, sa, DOMAIN);

		return currentGeneration;
	}

	public Generation getOneGeneration() {
		ArrayList<Integer> used = new ArrayList<Integer>();
		
		for (int i = 0; i < SPECIMEN_COUNT / 2; i++)
			used.add(0);
		
		if (currentGeneration == null)
			return getFirstGeneration();

		ArrayList<Verse> newSpecimens = currentGeneration.maintain(SPECIMEN_COUNT / 2);

		ArrayList<Verse> finalSpecimens = new ArrayList<Verse>();

		for (int i = 0; i < SPECIMEN_COUNT / 2; i++) {
			used.remove(i);
			used.add(i, 1);
			finalSpecimens.add(mutate(newSpecimens.get(i))); // always keep the best 50% intact, maybe mutate
		}
		
		
		for (int i = 0; i < SPECIMEN_COUNT / 4; i++) { //pair the remaining count
			int first = r.nextInt(SPECIMEN_COUNT / 2);
			while (used.get(first) == 2)
				first = r.nextInt(SPECIMEN_COUNT / 2);
			int firstcnt = used.get(first);
			used.remove(first);
			used.add(first, firstcnt + 1);
			
			int second = r.nextInt(SPECIMEN_COUNT / 2);
			while (used.get(second) == 2 || second == first) 
				second = r.nextInt(SPECIMEN_COUNT / 2);
			int secondcnt = used.get(second);
			used.remove(second);
			used.add(second, secondcnt + 1);
			
			ArrayList<Verse> specimens = cross(newSpecimens.get(first).toString(),newSpecimens.get(second).toString());
			finalSpecimens.add(specimens.get(0));
			finalSpecimens.add(specimens.get(1));
			

		}
		
		
		System.out.println("Finished new generation of specimens\n");
		
		currentGeneration = new Generation(finalSpecimens, sa, DOMAIN);
	
		
		return currentGeneration;

	}

	private ArrayList<Verse> cross(String firsts, String seconds) {
		Verse firstv = new Verse(firsts);
		Verse secondv = new Verse(seconds);
		int val = r.nextInt(100) + 1;
		if (val < MUTATION_PERCENT) {
			firstv.switchup(); // change two words between one another
		} else if (val < 2 * MUTATION_PERCENT) {
			firstv.addWord(sa, MAX_WORD_COUNT); // add a new word to a random
												// place
		} else if (val < 3 * MUTATION_PERCENT) {
			firstv.deleteWord(MIN_WORD_COUNT); // remove a word from a random
		} else if (val < 4 * MUTATION_PERCENT) {
			firstv.changeWord(sa); // change a word to a new word at a random
									// position
		}
		
		val = r.nextInt(100) + 1;
		if (val < MUTATION_PERCENT) {
			secondv.switchup(); // change two words between one another
		} else if (val < 2 * MUTATION_PERCENT) {
			secondv.addWord(sa, MAX_WORD_COUNT); // add a new word to a random
		} else if (val < 3 * MUTATION_PERCENT) {
			secondv.deleteWord(MIN_WORD_COUNT); // remove a word from a random
		} else if (val < 4 * MUTATION_PERCENT) {
			secondv.changeWord(sa); // change a word to a new word at a random
		}
		
		int row11 = r.nextInt(ROWCOUNT);
		int row12 = r.nextInt(ROWCOUNT);
		while (row12 == row11)
			row12 = r.nextInt(ROWCOUNT);
		
		int row21 = r.nextInt(ROWCOUNT);
		int row22 = r.nextInt(ROWCOUNT);
		while (row22 == row21)
			row22 = r.nextInt(ROWCOUNT);
		

		
		ArrayList<Line> firstLines = firstv.getLines();	
		ArrayList<Line> secondLines = secondv.getLines();
		
		Line line11 = firstLines.get(row11);
		Line line12 = firstLines.get(row12);
		Line line21 = secondLines.get(row21);
		Line line22 = secondLines.get(row22);
		
		
		
		firstLines.remove(row11);
		firstLines.add(row11, line21);
		
		firstLines.remove(row12);
		firstLines.add(row12, line22);
		
		secondLines.remove(row21);
		secondLines.add(row21, line11);
		
		secondLines.remove(row22);
		secondLines.add(row22, line12);
		
		Verse firstv2 = new Verse(firstLines);
		Verse secondv2 = new Verse(secondLines);
		
		
		ArrayList<Verse> specimens = new ArrayList<Verse>();
		
		specimens.add(firstv2);
		specimens.add(secondv2);
		
		
		return specimens;
	}

	private Verse mutate(Verse verse) {
		Verse newVerse = new Verse("");
		int val = r.nextInt(100) + 1;
		if (val < MUTATION_PERCENT * 100) {
			newVerse = verse.switchup(); // change two words between one another
		} else if (val < 2 * MUTATION_PERCENT) {
			newVerse = verse.addWord(sa, MAX_WORD_COUNT); // add a new word to a random
												// place
		} else if (val < 3 * MUTATION_PERCENT) {
			newVerse = verse.deleteWord(MIN_WORD_COUNT); // remove a word from a random
												// place
		} else if (val < 4 * MUTATION_PERCENT) {
			newVerse = verse.changeWord(sa); // change a word to a new word at a random
									// position
		} else 
			newVerse = verse;

		
		return newVerse;
	}

}
