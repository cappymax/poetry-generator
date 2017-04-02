package com.ktim1435.language;

public class Word {
	private String text = "";
	private String type = "";
	private String root = "";

	public Word() {
		// semmi
	}
	
	public Word(String text, String type) {
		this.text = text;
		this.type = type;
	}
	
	public Word(String text, String root, String type) {
		this.text = text;
		this.root = root;
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
	
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
