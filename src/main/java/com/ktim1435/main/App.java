package com.ktim1435.main;

import java.util.ArrayList;

import com.ktim1435.genetic.GeneticAlgorithm;
import com.ktim1435.language.Sentence;
import com.ktim1435.poem.Poem;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;


public class App 
{
    public static void main( String[] args )
    {
//    	ArrayList<Verse> verses = GeneticAlgorithm.getOneGeneration();
//    	for (Verse v:verses) {
//        	System.out.println(v + "\n");
//    	}

    	WordNet wn = new WordNet();
//    	Sentence s = new Sentence("Az ipafai papnak fa pipaja van.",wn);
//    	
//    	System.out.println(s.getTypesString());
    	
    	SentenceAnalyzer sa = new SentenceAnalyzer(wn);
    	sa.generateSentenceFile();

    	
    	//Poem p = new Poem("Az ipafai      papnak\nfapipaja van,\nezert az ipafai fapipa,\npapi fapipa.");
        //System.out.println(p);
    }
}
