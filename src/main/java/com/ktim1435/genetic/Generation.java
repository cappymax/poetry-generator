package com.ktim1435.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.ktim1435.poem.Verse;

public class Generation {
	private ArrayList<Verse> specimens = new ArrayList<Verse>();
	
	private int getScore(Verse v) {
		return v.getRithm().calculateRithmValue() + v.getRhyme().calculateRhymeValue();
	}
	
	public Generation(ArrayList<Verse> verses) {
		this.specimens = verses;

		Collections.sort(verses, new Comparator<Verse>() {
	        public int compare(Verse v1, Verse v2)
	        {
	            return  ((Integer)getScore(v2)).compareTo((Integer)getScore(v1));
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
