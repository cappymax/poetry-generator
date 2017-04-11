package com.ktim1435.main;

import java.io.IOException;
import java.util.ArrayList;

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
    	GeneticAlgorithm ga = new GeneticAlgorithm(wn,sa);
    	ArrayList<Verse> verses = ga.getOneGeneration();
    	System.out.println();
    	for (Verse v:verses) {
        	System.out.println(v + "\n");
    	}

//    	Sentence s = new Sentence("Az ipafai papnak fa pipaja van.",wn);
////    	
//    	System.out.println(s.getRootsString());
    	


    	
//    	Verse p = new Verse("Az ipafai papnak\nfapipaja lesz,\nezert az ipafai fapipa,\npapi stressz.");
//    	System.out.println(p.getRhyme().calculateRhymeValue());
//        System.out.println(p);
    }
}
