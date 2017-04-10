package com.ktim1435.poem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ktim1435.language.Gramatics;

public class Poem implements PoeticElement {
	private List<Verse> verses = new ArrayList<Verse>();

	public Poem(String newPoem) {
		verses.add(new Verse(newPoem));
	}
	
	public String getVerses() {
		String combinedVerses = "";
		for (Verse v : verses) {
			combinedVerses += v.getLines() + Gramatics.FREE_LINE;
		}
		return combinedVerses;
	}

	public void addVerse(Verse newVerse) {
		verses.add(newVerse);
	}

	public void addVerses(Collection<Verse> newVerses) {
		verses.addAll(newVerses);
	}

	public String toString() {
		return this.getVerses();
	}
	
	

}
