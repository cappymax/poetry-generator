package com.ktim1435.language;

import java.util.ArrayList;
import java.util.Collection;

import com.ktim1435.wordnet.WordNet;

public class Sentence {
	private ArrayList<Word> text;
	private WordNet wn;
	
	public Sentence(String text, WordNet wn) {
		this.wn = wn;
		this.text = createWordList(text);

	}
	
	private ArrayList<Word> createWordList(String text) {
		ArrayList<Word> words = new ArrayList<Word>();
		
		text = text.toLowerCase();
		String[] splittedText = text.split("[ ,.]");
		for (String aWord : splittedText) 
			try {
				words.add(new Word(aWord, wn.getType(aWord)));
			} catch (Exception e) {
				//do nothing
				e.printStackTrace();
			}
		
		
		
		return words;
	}
	
	public Sentence(Collection<Word> words, WordNet wn) {
		this.wn = wn;
		this.text = new ArrayList<Word>(words);
	}
	
	public ArrayList<Word> getText() {
		return text;
	}

	public void setText(ArrayList<Word> text) {
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = createWordList(text);
	}
	
	
	public String toString() {
		return toReadbleString(text);
	}
	
	private String toReadbleString(ArrayList<Word> words) {
		String result = "";
		
		for (Word word : words) 
			result += word.getText() + " ";
		
		result = Gramatics.removeExcessSpaces(result);
		//result = Gramatics.uppercaseFirstLetter(result);
		
		return result;
	}
	
	public String getTypesString() {
		return toTypeString(text);
	}
	
	private String toTypeString(ArrayList<Word> words) {
		String result = "";
		
		for (Word word : words) 
			result += word.getType() + " ";
		
		result = Gramatics.removeExcessSpaces(result);
		//result = Gramatics.uppercaseFirstLetter(result);
		
		return result;
	}
	
}
