package com.ktim1435.poetry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ktim1435.poem.Line;

public class Rithm {
	private String rithmString = "";
	private Map<String, Integer> vowellCountMap = new HashMap();
	
	public void calculateRithm(List<Line> lines) {
		rithmString = "";
		for (Line l : lines) 
			if (!lines.get(lines.size()-1).equals(l))
				rithmString += l.getVowellCount() + "/";
			else
				rithmString += l.getVowellCount();
		
	}
	
	public int calculateRithmValue() {
		String[] splittedRithm = rithmString.split("/");
		vowellCountMap.clear();
		
		for (String s : splittedRithm) 
			if (vowellCountMap.containsKey(s)) 
				vowellCountMap.put(s, vowellCountMap.get(s)+1);
			else
				vowellCountMap.put(s, 1);
		int max = 0;
		for (String s : vowellCountMap.keySet()) 
			if (vowellCountMap.get(s) > max) 
				max = vowellCountMap.get(s);

		return max;
	}
	
	@Override
	public String toString() {
		return rithmString;
	}
	
	
}
