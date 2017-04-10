package com.ktim1435.poetry;

import java.util.ArrayList;
import java.util.List;

import com.ktim1435.poem.Line;

public class Rhyme {
	static public String PAIRED_RHYME = "1122";
	static public String CROSS_RHYME = "1212";
	static public String BUSH_RHYME = "1111";
	static public String HUGGING_RHYME = "1221";

	
	private String rhymeString = "";
	
	
	/**
	 * Returns the calculated rhyme pattern of a list of lines.
	 * @param lines
	 * @return
	 */
	public String calculateRhyme(List<Line> lines) {
		String rhyme = "";
		for (Line l : lines) 
			rhyme += l.getLastVowell();
		
		boolean[] matchedChars = new boolean[rhyme.length()];
		char replacement = '1';
		
		for (int i = 0; i < rhyme.length(); i++) 
			if (!matchedChars[i]) {
				matchedChars[i] = true;
				rhyme = rhyme.replace(rhyme.charAt(i), replacement);
				for (int j = i+1; j < rhyme.length(); j++)
					matchedChars[j] = rhyme.charAt(j) == rhyme.charAt(i) || matchedChars[j];
				replacement =  (char) (((int) replacement) + 1);
			}
		
		rhymeString = rhyme;
		return rhyme;
	}
	
	public int calculateRhymeValue() {
		ArrayList<Character> chars = new ArrayList();
		int distinct = 0;
		for (char c: rhymeString.toCharArray()) {
			if (!chars.contains(c)) {
				distinct++;
				chars.add(c);
			}
		}
		if (distinct == 4) {
			return 0;	
		}
		if (distinct == 3) {
			return 1;
		}
		return 2;
		
		
	}
	
	@Override
	public String toString() {
		return rhymeString;
	}
}
