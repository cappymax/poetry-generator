package com.ktim1435.language;

public class Word implements Comparable{
	private String text = "";
	private String type = "";
	private String root = "";
	private String domain ="";;

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
	
	public Word(String text, String root, String type, String domain) {
		this.text = text;
		this.root = root;
		this.type = type;
		this.domain  = domain;
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
	
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	@Override
	public String toString() {
		return text;
	}

	public int compareTo(Object o) {
		
		return o.toString().compareTo(this.toString());
	}
	
}
