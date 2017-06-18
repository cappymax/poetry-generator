package com.ktim1435.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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

import com.ktim1435.genetic.GeneticAlgorithm;
import com.ktim1435.poem.Verse;
import com.ktim1435.wordnet.SentenceAnalyzer;
import com.ktim1435.wordnet.WordNet;

public class PGFrame extends JFrame{
	
	private JPanel panel;
	private JPanel mid;
	private JPanel north;
	private JPanel gens;
	private JPanel mutPerc;
	private JPanel domainLine;
	private JPanel currentStatus;
	private JPanel buttons;
	
	private JLabel introduction;
	
	private JLabel numberOfGenerationsLabel;
	private JSpinner numberOfGenerations;
	
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
    	
    	String[] domains = { "ANYTHING", "LOVE" };
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
						
						WordNet wn = new WordNet();
						
//						ArrayList<String> domains = wn.getDomains();
				    	
//				    	for(String s : domains)
//						System.out.println(s);

				    	SentenceAnalyzer sa = new SentenceAnalyzer(wn);
						status.setText("Starting the simulation...Reading senteces.txt");
				    	try {
							sa.readSentenceFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
				    	
				    	status.setText("Starting the simulation...Generating first generation");
				    	int mutPerc = Integer.parseInt(mutationPercentage.getValue().toString());
				    	String domain = domainSelect.getSelectedItem().toString();
				    	GeneticAlgorithm ga = new GeneticAlgorithm(wn,sa,mutPerc,domain);
				    	ArrayList<Verse> verses = ga.getFirstGeneration().getSpecimens();
				    	status.setText("Finished setup...Starting genetic algorithm");
				    	//write to file statistics

				    	for (int i = 0; i < Integer.parseInt(numberOfGenerations.getValue().toString()); i++){
				    		String val = verses.get(0).getRithm().calculateRithmValue() + ":" + verses.get(0).getRhyme().calculateRhymeValue()+":" + verses.get(0).calculateSemantics(sa) +":"+ verses.get(0).calculateDomainScore(sa, domain);
					    	status.setText("Genetic algorithm...Generation number: "+ i +"...Current best poem value: "+val);
				    		verses = ga.getOneGeneration().getSpecimens();
				    	}
				    	
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
	
}
