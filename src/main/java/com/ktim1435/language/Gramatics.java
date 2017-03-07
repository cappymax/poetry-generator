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
		lines = uppercaseFirstLetter(lines);
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
			l.setText(l.getText().replaceAll("\\s+", " "));
			if (l.getText().charAt(0) == ' ')
				l.setText(l.getText().substring(1, l.getText().length()));
			newLines.add(l);
		}
		return newLines;
	}

	private static List<Line> uppercaseFirstLetter(List<Line> lines) {
		List<Line> newLines = new ArrayList<Line>();
		for (Line l : lines) {
			l.setText(Character.toUpperCase(l.getText().charAt(0)) + l.getText().substring(1, l.getText().length()));
			newLines.add(l);
		}
		return newLines;
	}

	private static List<Line> setPunctuation(List<Line> lines) {
		// TODO Set punctuation
		return lines;
	}
}
