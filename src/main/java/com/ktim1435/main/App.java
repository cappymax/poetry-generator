package com.ktim1435.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ktim1435.genetic.GeneticAlgorithm;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;


public class App 
{
    public static void main( String[] args )
    {
    	
    	WordNet wn = new WordNet();
    	
    	SentenceAnalyzer sa = new SentenceAnalyzer(wn);
    	try {
			sa.readSentenceFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
//    	HashMap<String,Integer> types = new HashMap();
//    	for (int i=0; i<100; i++) {
//    		String type = sa.getWord().getType();
//    		if (types.containsKey(type))
//    			types.put(type, types.get(type) + 1);
//    		else 
//    			types.put(type, 1);
//    	}
//    	System.out.println(types);
    	
//    	String text = sa.getWord().getText();
//    	System.out.println(text);
//    	
//    	sa.generateSentenceFile();
//    	
    	
    	System.out.println("****************\nInitialization\n*******************");
    	GeneticAlgorithm ga = new GeneticAlgorithm(wn,sa);
    	ArrayList<Verse> verses = ga.getFirstGeneration().getSpecimens();
//    	System.out.println("***************\nFinished\n*******************");
//    	
//    	System.out.println("****************\nFirst generation\n******************");
//    	for (Verse v:verses) {
//        	System.out.println(v + "" + v.getRithm().calculateRithmValue()  + " : " + v.getRhyme().calculateRhymeValue() + "\n");
//    	}
//    	
//    	verses = ga.getOneGeneration().getSpecimens();
//    	System.out.println("*****************\nSecond generation\n******************");
//    	for (Verse v:verses) {
//        	System.out.println(v + "" + v.getRithm().calculateRithmValue()  + " : " + v.getRhyme().calculateRhymeValue() + "\n");
//    	}
//    	
    	for (String s : sa.getAfterTypes("az").keySet())
    		System.out.println(s +" : "+ sa.getAfterTypes("az").get(s));
    	

    	
    	
//    	System.out.println(verses.get(0) + " \n ");
//    	verses.get(0).switchup();
//    	System.out.println(verses.get(0));

//    	Sentence s = new Sentence("Az ipafai papnak fa pipaja van.",wn);
////    	
//    	System.out.println(s.getRootsString());
    	


    	
//    	Verse p = new Verse("Az ipafai papnak\nfapipaja lehetne,\nezert az ipafai fapipa,\npapi stressz.");
//    	System.out.println(p.getRithm().calculateRithmValue() + p.getRhyme().calculateRhymeValue());
//        System.out.println(p);
    }
}
