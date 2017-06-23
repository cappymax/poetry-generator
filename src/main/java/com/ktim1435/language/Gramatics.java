package com.ktim1435.language;

import java.util.ArrayList;
import java.util.List;

import com.ktim1435.poem.Line;

public class Gramatics {
	public static String END_OF_LINE = "\n";
	public static String FREE_LINE = "\n\n";

	public static List<Line> correctLines(List<Line> lines) {
		lines = removeEmptyLines(lines);
		lines = removeUnwantedSpaces(lines);
		lines = removeEmptyLines(lines);
		lines = uppercaseFirstLetters(lines);
		lines = setPunctuation(lines);
		return lines;
	}

	private static List<Line> removeEmptyLines(List<Line> lines) {
		List<Line> newLines = new ArrayList<Line>();
		for (Line l : lines) {
			if (l.getText().length() != 0) {
				newLines.add(l);
			}
		}
		return newLines;
	}

	private static List<Line> removeUnwantedSpaces(List<Line> lines) {
		List<Line> newLines = new ArrayList<Line>();
		for (Line l : lines) {
			l.setText(removeExcessSpaces(l.getText()));
			if (l.getText().charAt(0) == ' ')
				l.setText(l.getText().substring(1, l.getText().length()));
			newLines.add(l);
		}
		return newLines;
	}

	public static String removeExcessSpaces(String text) {
		return text.replaceAll("\\s+", " ");
	}
	

	private static List<Line> uppercaseFirstLetters(List<Line> lines) {
		List<Line> newLines = new ArrayList<Line>();
		for (Line l : lines) {
			l.setText(uppercaseFirstLetter(l.getText()));
			newLines.add(l);
		}
		return newLines;
	}

	public static String uppercaseFirstLetter(String text) {
		return Character.toUpperCase(text.charAt(0)) + text.substring(1, text.length());
	}

	private static List<Line> setPunctuation(List<Line> lines) {
		return lines;
	}
}
