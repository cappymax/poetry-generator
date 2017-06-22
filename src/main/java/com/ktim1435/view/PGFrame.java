package com.ktim1435.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

import com.ktim1435.genetic.GeneticAlgorithm;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;

public class PGFrame extends JFrame{
	private WordNet wn = null;
	private SentenceAnalyzer sa = null;
	private JPanel panel;
	private JPanel mid;
	private JPanel north;
	private JPanel gens;
	private JPanel spec;
	private JPanel mutPerc;
	private JPanel domainLine;
	private JPanel currentStatus;
	private JPanel buttons;
	
	private JLabel introduction;
	
	private JLabel numberOfGenerationsLabel;
	private JSpinner numberOfGenerations;
	
	private JLabel 	numberOfSpecimensLabel;
	private JSpinner numberOfSpecimens;
	
	

	
	private JLabel mutationPercentageLabel;
	private JSpinner mutationPercentage;
	private JLabel percentageSign;
	
	private JLabel domainLabel;
	private JComboBox<String> domainSelect;
	
	private JLabel status;
	
	private JButton start;
	private JButton maintenance;
	
	public PGFrame() {
		this.setTitle("Poetry Generator");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setSize(1000, 1000);
    	
    	panel = new JPanel();
    	panel.setLayout(new BorderLayout());
    	
    	north = new JPanel();
    	introduction = new JLabel("Hello and wellcome, to a poetry generator!");
    	introduction.setFont(new java.awt.Font(null,java.awt.Font.BOLD,30));
    	north.add(introduction);
    	
    	mid = new JPanel();
    	mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
    
    	//generation count line
    	gens = new JPanel();
    	
    	numberOfGenerations = new JSpinner();
    	JComponent editor = (JSpinner.DefaultEditor) numberOfGenerations.getEditor();
        JFormattedTextField ftf = ((JSpinner.DefaultEditor) editor).getTextField();
        ftf.setColumns(4);
    	numberOfGenerations.setFont(new java.awt.Font(null, 0, 20));
    	numberOfGenerations.setValue(100);
    	
    	numberOfGenerationsLabel = new JLabel("Set the number of desired generations:");
    	numberOfGenerationsLabel.setFont(new java.awt.Font(null, 0, 20));
    	
    	gens.add(numberOfGenerationsLabel);
    	gens.add(numberOfGenerations);
    	mid.add(gens);
    	
    	//number of specimens
  
    	spec = new JPanel();
    	
    	numberOfSpecimens = new JSpinner();
    	JComponent editor3 = (JSpinner.DefaultEditor) numberOfSpecimens.getEditor();
        JFormattedTextField ftf3 = ((JSpinner.DefaultEditor) editor3).getTextField();
        ftf3.setColumns(4);
    	numberOfSpecimens.setFont(new java.awt.Font(null, 0, 20));
    	numberOfSpecimens.setValue(20);
    	
    	numberOfSpecimensLabel = new JLabel("Set the number of desired specimens:");
    	numberOfSpecimensLabel.setFont(new java.awt.Font(null, 0, 20));
    	
    	spec.add(numberOfSpecimensLabel);
    	spec.add(numberOfSpecimens);
    	mid.add(spec);
    	
    	//mutation percent line
    	mutPerc = new JPanel();
    	
    	mutationPercentage = new JSpinner();
    	JComponent editor2 = (JSpinner.DefaultEditor) mutationPercentage.getEditor();
        JFormattedTextField ftf2 = ((JSpinner.DefaultEditor) editor2).getTextField();
        ftf2.setColumns(3);
    	mutationPercentage.setFont(new java.awt.Font(null, 0, 20));
    	mutationPercentage.setValue(5);
    	
    	mutationPercentageLabel = new JLabel("Set desired chance of mutation:");
    	mutationPercentageLabel.setFont(new java.awt.Font(null, 0, 20));
    	
    	percentageSign = new JLabel("%");
    	percentageSign.setFont(new java.awt.Font(null, 0, 20));
    	
    	mutPerc.add(mutationPercentageLabel);
    	mutPerc.add(mutationPercentage);
    	mutPerc.add(percentageSign);    	
    	
    	mid.add(mutPerc);   	
    	
    	//domain line
    	domainLine = new JPanel();
    	domainLabel = new JLabel("Set the desired domain:");
    	domainLabel.setFont(new java.awt.Font(null, 0, 20));
    	domainLine.add(domainLabel);
    	
    	String[] domains = { "ANYTHING", "LOVE", "FREETIME" };
    	domainSelect = new JComboBox<String>(domains);
    	domainSelect.setFont(new java.awt.Font(null, 0, 20));
    	domainLine.add(domainSelect);
    	
    	mid.add(domainLine);
    	
    	//informative text line
    	currentStatus = new JPanel();
    	status = new JLabel("Waiting on user input");
    	status.setFont(new java.awt.Font(null, 0, 20));
    	
    	currentStatus.add(status);
    	mid.add(currentStatus);
    	
    	//buttons line
    	buttons = new JPanel();
    	
    	start = new JButton();
    	start.setText("Start simulating");
    	start.setFont(new java.awt.Font(null, 0, 20));
    	
    	start.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent ev) {
				Thread t = new Thread(new Runnable() {
					
					public void run() {
						start.setEnabled(false);
						status.setText("Starting the simulation...Creating WordNet");
						if (wn == null)
							wn = new WordNet();
						
						if (sa == null) {
					    	sa = new SentenceAnalyzer(wn);
							status.setText("Starting the simulation...Reading senteces.txt");
					    	try {
								sa.readSentenceFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
				    	status.setText("Starting the simulation...Generating first generation");
				    	int specCount = Integer.parseInt(numberOfSpecimens.getValue().toString());
				    	int mutPerc = Integer.parseInt(mutationPercentage.getValue().toString());
				    	String domain = domainSelect.getSelectedItem().toString();
				    	int genCount = Integer.parseInt(numberOfGenerations.getValue().toString());
				    	GeneticAlgorithm ga = new GeneticAlgorithm(wn,sa,specCount,mutPerc,domain);
				    	ArrayList<Verse> verses = ga.getFirstGeneration().getSpecimens();
				    	status.setText("Finished setup...Starting genetic algorithm");
				    	//write to file statistics
				    	try {
				    		PrintWriter writer = new PrintWriter("statistics.txt", "UTF-8");
					    	for (int i = 0; i < genCount; i++){
					    		String val = verses.get(0).getRithm().calculateRithmValue() + ":" + verses.get(0).getRhyme().calculateRhymeValue()+":" + verses.get(0).calculateSemantics(sa) +":"+ verses.get(0).calculateDomainScore(sa, domain);
						    	writeToStatisticsFile(calcAvrgVal(verses), writer);
					    		status.setText("Genetic algorithm...Generation number: "+ i +"...Current best poem value: "+val);
					    		verses = ga.getOneGeneration().getSpecimens();
					    	}
					    	
					    	writer.close();
				    	} catch (Exception e) {
				    		status.setText("Genetic algorithm...Error writing to statistics");
				    	}
				    	JFrame verseFrame = new JFrame();
				    	verseFrame.setLayout(new BorderLayout());
				    	verseFrame.setSize(500, 500);
				    	String[] lines = verses.get(0).toString().split("\n");
				    	JLabel[] verseLabels = new JLabel[lines.length];
				    	JPanel[] versePanels = new JPanel[lines.length];
				    	verseFrame.setVisible(true);
				    	JPanel versePanel = new JPanel(); 
				    	versePanel.setLayout(new BoxLayout(versePanel, BoxLayout.Y_AXIS));
				    	for (int i = 0; i < lines.length; i++) {
				    		versePanels[i] = new JPanel();
				    		verseLabels[i] = new JLabel(lines[i]);
				    		verseLabels[i].setFont(new java.awt.Font(null, 0, 20));
				    		versePanels[i].add(verseLabels[i]);
				    		versePanel.add(versePanels[i]);
				    	}
				    	verseFrame.add(versePanel,BorderLayout.CENTER);
						start.setEnabled(true);
					}




				});
				t.start();
				
				
				
			}
		});
    	
    	buttons.add(start);
    	
    	maintenance = new JButton();
    	maintenance.setText("Start maintenance");
    	maintenance.setFont(new java.awt.Font(null, 0, 20));
    	
    	maintenance.setEnabled(false);
    	
    	maintenance.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(new Runnable() {
					
					public void run() {
						maintenance.setEnabled(false);
						status.setText("Starting the maintenance...Creating WordNet");
						
						WordNet wn = new WordNet();
						SentenceAnalyzer sa = new SentenceAnalyzer(wn);
						
						status.setText("Generating sentences.txt");
						sa.generateSentenceFile(); 
						
						status.setText("Maintenance finished");
						maintenance.setEnabled(true);
					}
				});
				
				t.start();
				
			}
		});
    	
    	buttons.add(maintenance);
    	
    	mid.add(buttons);
    	
    	panel.add(north, BorderLayout.NORTH);
    	panel.add(mid, BorderLayout.CENTER);
    	
    	this.add(panel);
    	this.setVisible(true);
	}
	
	private String calcAvrgVal(ArrayList<Verse> verses) {
		int rithm=0,rhyme=0,semantics=0,domain=0;
		for (Verse v:verses){
			rithm += v.getRithm().calculateRithmValue();
			rhyme += v.getRhyme().calculateRhymeValue();
			semantics += v.calculateSemantics(sa);
			domain += v.calculateDomainScore(sa, domainSelect.getSelectedItem().toString());
		}
		return (double) rithm / (double) verses.size() + ":" + (double) rhyme / (double) verses.size() +
				":" + (double) semantics / (double) verses.size() + ":" + (double) domain / (double) verses.size();
	}
	
	private void writeToStatisticsFile(String val, PrintWriter writer) {    
	
		    writer.println(val);
	}
	
	
}
