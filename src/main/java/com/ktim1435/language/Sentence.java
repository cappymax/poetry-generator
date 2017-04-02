package com.ktim1435.language;

import java.util.ArrayList;
import java.util.Collection;

import com.ktim1435.wordnet.WordNet;

public class Sentence {
	private ArrayList<Word> words;
	private WordNet wn;
	
	public Sentence(String text, WordNet wn) {
		this.wn = wn;
		this.words = createWordList(text);

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
		this.words = new ArrayList<Word>(words);
	}
	
	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}
	
	public void setWords(String text) {
		this.words = createWordList(text);
	}
	
	
	public String toString() {
		return toReadbleString(words);
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
		return toTypeString(words);
	}
	
	private String toTypeString(ArrayList<Word> words) {
		String result = "";
		
		for (Word word : words) 
			result += word.getType() + " ";
		
		result = Gramatics.removeExcessSpaces(result);
		//result = Gramatics.uppercaseFirstLetter(result);
		
		return result;
	}
	
	public String getRootsString() {
		return toTypeString(words);
	}
	
	private String toRootsString(ArrayList<Word> words) {
		String result = "";
		
		for (Word word : words) 
			result += word.getRoot() + " ";
		
		result = Gramatics.removeExcessSpaces(result);
		//result = Gramatics.uppercaseFirstLetter(result);
		
		return result;
	}
	
}
