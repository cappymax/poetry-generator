package com.ktim1435.main;

import com.ktim1435.poem.Poem;
import com.ktim1435.wordnet.WordNet;


public class App 
{
    public static void main( String[] args )
    {
    	WordNet wn = new WordNet();
    	try {
    		String word = wn.getWord("random");
			System.out.println(word + " - " + wn.getType(word));
		} catch (Exception e) {
			e.printStackTrace();
		}
    	//Poem p = new Poem("Az ipafai      papnak\nfapipaja van,\nezert az ipafai fapipa,\npapi fapipa.");
        //System.out.println(p);
    }
}
