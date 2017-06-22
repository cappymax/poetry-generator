package com.ktim1435.poem;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Random;

import com.ktim1435.wordnet.SentenceAnalyzer;

public class Line implements PoeticElement {
	private String text = "";

	public Line(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}

	public String getLastVowell() {
		String[] texts = text.toLowerCase().split("[^aeiouáéőúóüöű]");
		return texts[texts.length - 1];
	}

	public int getVowellCount() {
		return text.toLowerCase().split("[aeiouáéőúóüöű]").length - 1;
	}

	public void changeWord(SentenceAnalyzer sa) {
		String newWord = sa.getWord().getText();
		String[] words = text.split(" ");
		text = "";
		Random r = new Random();
		int wordNr = r.nextInt(words.length);

		for (int i = 0; i < words.length; i++) {
			if (i == wordNr)
				text += newWord + " ";
			else
				text += words[i] + " ";
			if (i == 0)
				text = text.substring(0, 1).toUpperCase() + text.substring(1);
		}
	}

	public void deleteWord(int MIN_WORD_COUNT) {
		String[] words = text.split(" ");

		if (words.length > MIN_WORD_COUNT) {
			text = "";
			Random r = new Random();
			int wordNr = r.nextInt(words.length);

			for (int i = 0; i < words.length; i++) {
				if (i != wordNr)
					text += words[i] + " ";

			}
			text = text.substring(0, 1).toUpperCase() + text.substring(1);
		}
	}

	public void addWord(SentenceAnalyzer sa, int MAX_WORD_COUNT) {
		String[] words = text.split(" ");

		if (words.length < MAX_WORD_COUNT) {
			String newWord = sa.getWord().getText();
			text = "";
			Random r = new Random();
			int wordNr = r.nextInt(words.length);

			for (int i = 0; i < words.length; i++) {
				if (i == wordNr) {
					text += newWord + " ";
					i--;
					wordNr = -1;
				} else
					text += words[i] + " ";
				if (i == 0)
					text = text.substring(0, 1).toUpperCase() + text.substring(1);
			}
		}
	}

	public void switchup() {
		String[] words = text.split(" ");
		text = "";
		Random r = new Random();
		int wordNr1 = r.nextInt(words.length);
		int wordNr2 = r.nextInt(words.length);

		while (wordNr1 == wordNr2)
			wordNr2 = r.nextInt(words.length);

		String word1 = words[wordNr1].toLowerCase();
		String word2 = words[wordNr2].toLowerCase();

		for (int i = 0; i < words.length; i++) {
			if (i == wordNr1)
				text += word2 + " ";
			else if (i == wordNr2)
				text += word1 + " ";
			else
				text += words[i] + " ";
			if (i == 0)
				text = text.substring(0, 1).toUpperCase() + text.substring(1);
		}

	}

	public int calculateSemantics(SentenceAnalyzer sa) {
		String[] words = text.split(" ");
		String before = "";
		String after = "";
		int s = 0;
		for (int i = 0; i < words.length; i++) {
			try {
				if (i == 0)
					before = "nothing";
				else
					before = sa.getWordType(words[i - 1].toLowerCase());
				if (i >= words.length - 1)
					after = "nothing";
				else
					after = sa.getWordType(words[i + 1]);

				for (Entry<String, Integer> e : sa.getAfterTypes(words[i].toLowerCase()))
					if (e.getKey().equals(after))
						s += e.getValue();

				for (Entry<String, Integer> e : sa.getBeforeTypes(words[i].toLowerCase()))
					if (e.getKey().equals(before))
						s += e.getValue();
			}

			catch (Exception e) {
				// System.out.println(sa.getWordType(words[i-1].toLowerCase()));
			}
		}

		return s / (2 * words.length);
	}

	public int calculateDomainScore(SentenceAnalyzer sa, String string) {
		ArrayList<String> acceptedDomains = new ArrayList<String>();
		if (string.equals("LOVE")) {
			acceptedDomains.add("quality");
			acceptedDomains.add("factotum");
			acceptedDomains.add("sexuality");
			acceptedDomains.add("art");
			acceptedDomains.add("free_time");
			acceptedDomains.add("free time");
			acceptedDomains.add("money");
			acceptedDomains.add("dance");
			acceptedDomains.add("theatre");
			acceptedDomains.add("cinema");
			acceptedDomains.add("artisanship");
			acceptedDomains.add("literature");
			acceptedDomains.add("color");
			acceptedDomains.add("jewellery");
			acceptedDomains.add("sculpture");
			acceptedDomains.add("painting");
			acceptedDomains.add("NODOM");
		}
		String[] words = text.split(" ");
		int s = 0;
		for (int i = 0; i < words.length; i++) {
			if (string.equals("ANYTHING") || acceptedDomains.contains(sa.getWordDomain(words[i]))) {
				s += 4;
			}
		}

		return s / words.length;
	}
}
