package com.ktim1435.main;

import java.io.IOException;
import java.util.ArrayList;

import com.ktim1435.genetic.GeneticAlgorithm;
import com.ktim1435.language.Sentence;
import com.ktim1435.poem.Line;
import com.ktim1435.poem.Poem;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;


public class App 
{
    public static void main( String[] args )
    {
    	
//    	WordNet wn = new WordNet();
//    	GeneticAlgorithm ga = new GeneticAlgorithm(wn);
//    	ArrayList<Verse> verses = ga.getOneGeneration();
//    	for (Verse v:verses) {
//        	System.out.println(v + "\n");
//    	}

//    	Sentence s = new Sentence("Az ipafai papnak fa pipaja van.",wn);
////    	
//    	System.out.println(s.getRootsString());
    	
//    	SentenceAnalyzer sa = new SentenceAnalyzer(wn);
//    	try {
//			sa.readSentenceFile();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

//    	sa.generateSentenceFile();

    	
    	Verse p = new Verse("Az ipafai papnak\nfapipaja lesz,\nezert az ipafai fapipa,\npapi stressz.");
    	System.out.println(p.getRhyme().calculateRhymeValue());
//        System.out.println(p);
    }
}
