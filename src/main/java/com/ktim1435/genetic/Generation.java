package com.ktim1435.genetic;

import java.util.ArrayList;

import com.ktim1435.poem.Verse;

public class Generation {
	private ArrayList<Verse> specimens = new ArrayList<Verse>();
	
	public Generation(ArrayList<Verse> verses) {
		this.specimens = verses;
	}
	
	public ArrayList<Verse> getSpecimens() {
		return specimens;
	}
}
