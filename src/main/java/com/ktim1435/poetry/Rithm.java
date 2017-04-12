package com.ktim1435.poetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ktim1435.poem.Line;

public class Rithm {
	private String rithmString = "";
	private Map<String, Integer> vowellMap = new HashMap();
	
	public void calculateRithm(List<Line> lines) {
		for (Line l : lines) 
			if (!lines.get(lines.size()-1).equals(l))
				rithmString += l.getVowellCount() + "/";
			else
				rithmString += l.getVowellCount();
		
	}
	
	public int calculateRithmValue() {
		String[] splittedRithm = rithmString.split("/");
		for (String s : splittedRithm) 
			if (vowellMap.containsKey(s)) 
				vowellMap.put(s, vowellMap.get(s)+1);
			else
				vowellMap.put(s, 1);
		int max = 0;
		for (String s : vowellMap.keySet()) 
			max = Math.max(vowellMap.get(s), max);
		return max;
	}
	
	@Override
	public String toString() {
		return rithmString;
	}
	
	
}
