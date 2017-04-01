package com.ktim1435.language;

public class Word {
	private String text = "";
	private String type = "";
	
	public Word() {
		// semmi
	}
	
	public Word(String text, String type) {
		this.text = text;
		this.type = type;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
