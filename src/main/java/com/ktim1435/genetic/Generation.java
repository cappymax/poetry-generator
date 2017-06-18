package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;

public class Generation {
	private String domain;
	private ArrayList<Verse> specimens = new ArrayList<Verse>();
	
	private int getScore(Verse v, SentenceAnalyzer sa) {
		return v.getRithm().calculateRithmValue() + v.getRhyme().calculateRhymeValue() + v.calculateSemantics(sa) + v.calculateDomainScore(sa,domain);
	}
	
	public Generation(ArrayList<Verse> verses, final SentenceAnalyzer sa, String domain) {
		this.specimens = verses;
		this.domain = domain;

		Collections.sort(verses, new Comparator<Verse>() {
	        public int compare(Verse v1, Verse v2)
	        {
	            return  ((Integer)getScore(v2, sa)).compareTo((Integer)getScore(v1, sa));
	        }
	    });
	}
	
	public ArrayList<Verse> getSpecimens() {
		return specimens;
	}
	
	public Verse getSpecimen(int i) {
		return specimens.get(i);
	}

	public ArrayList<Verse> maintain(int n) {
		ArrayList<Verse> newSpecimens = new ArrayList<Verse>();
		for (int i = 0; i < n; i++) {
			newSpecimens.add(specimens.get(i));
		}
		
		return newSpecimens;
		
	}
}
