package com.ktim1435.poem;

public class Line implements PoeticElement {
	private String text = "";

	public Line(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString() {
		return text;
	}
	
	public String getLastVowell() {
		String [] texts = text.toLowerCase().split("[^aeiouáéőúóüöű]");
		return texts[texts.length-1];
	}
}
