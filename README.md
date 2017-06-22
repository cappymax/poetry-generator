# poetry-generator
A poetry generator, using genetic programming to create Hungarian poems.

You will need the following installed on your system:

Java, Maven

To run the application, follow these steps:

Step no.0 : 
	If there is no .jar file in the /target directory then, in the main directory open a command prompt, and type in the following:
		mvn install
		
Step no.1 :
	In the target directory run the poetry-generator-1.0.0-SNAPSHOT.jar file, for this you can:
	
	a) Doulbe click the .jar file, the application will run
	b) Open a console and type in the following:
		java -jar poetry-generator-1.0.0-SNAPSHOT.jar
		
Step no.2 :
	Select the parameters of the program:
		Number of specimens, number of generations, chance of mutation, domain of the poems. To this date there are the following type of domains supported: Anything, Love, Free Time
		
Step no.3 :
	Click on the run button, the label above the run button will tell you the current status of the algorithm. Keep in mind that your first run will start out slowly due to initializtion.
	
Step no.4 :
	Wait for your simulation to finish.
	
Step no.5 :
	Enjoy your new poem. The program will open a new window with a one verse hungarian poem. If you are unsatisfied by the result just run a new simulation.

NOTES:
	Please note, that for the application to run in a correct manner, you will need the following files next to your .jar:
		sentences.txt
		wnxml.xml
		huwn.xml
		
	The current value of best poem score is composed of these scores:
		Rhyme score : Rithm score : Semantic score : Domain score
		
	Please note, that the creator of this program is Képes Tamás-Zsolt, with the guidance of dr. Bodó Zalán.
	We have to thank the WordNet crew, both the english and the hungarian one for their great tool, the WordNet, that helped us enormly during our work. 