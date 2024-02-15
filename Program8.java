//********************************************************************
//
//  Author:        Marshal Pfluger
//
//  Program #:     Program Eight
//
//  File Name:     Program8.java
//
//  Course:        COSC 4302 Operating Systems
//
//  Due Date:      11/27/2023
//
//  Instructor:    Prof. Fred Kumi
//
//  Java Version:  17
//
//  Description:    program that implements the FIFO, LRU, and optimal (OPT) page-replacement
//                  algorithms.  
//
//********************************************************************
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Program8 {
	
    public static void main(String[] args) {
    	Program8 obj = new Program8();
    	obj.developerInfo();
    	obj.runDemo();
    }
    
    //***************************************************************
    //
    //  Method:       runDemo
    // 
    //  Description:  Runs the program operations 
    //
    //  Parameters:   N/A
    //
    //  Returns:     N/A
    //
    //**************************************************************
    public void runDemo() {
    	
    	ArrayList<Integer> pageReference = null;
        boolean rerun = true;
    	do {
        	printOutput("Would you like to generate random numbers for the reference list?(y/n)");
        	
        	// Generate random numbers or allow user to enter string
        	if (userChoice().equalsIgnoreCase("y")){
        		printOutput("How many numbers would you like to generate?:");
        		pageReference = generateRandomNumbers(inputValidation(userChoice()));
        		printOutput(arrayListToString(pageReference));
        	}else {
                // Get user input for the page-reference string and number of page frames
                printOutput("Enter page-reference string (space-separated, only 0-9): ");
                pageReference = parseReference(userChoice());
        	}

        	// Prompt user to enter the number of frames
            printOutput("Enter the number of page frames: ");
            int numFrames = inputValidation(userChoice());

            // Instantiate object of pageReplacement class
            PageReplacement pageObj = new PageReplacement(pageReference, numFrames);
            
            // Test FIFO algorithm
            int fifoFaults = pageObj.runFIFO();
            printOutput("FIFO Page Faults: " + fifoFaults);

            // Test LRU algorithm
            int lruFaults = pageObj.runLRU();
            printOutput("LRU Page Faults: " + lruFaults);

            // Test Optimal algorithm
            int optimalFaults = pageObj.runOpt();
            printOutput("Optimal Page Faults: " + optimalFaults);
            
            // ALlow the user to run the program again
            printOutput("Would you like to run the program again?(enter to rerun or 0 to exit)");
            if(userChoice().equalsIgnoreCase("0"))
            	rerun = false;
            	
    	}while (rerun);
    	printOutput("Program has terminated, have a great day");
    }
    
    //***************************************************************
    //
    //  Method:       arrayListToString
    // 
    //  Description:  converts an arrayList of ints to a concatenated string 
    //
    //  Parameters:   ArrayList<Integer> list
    //
    //  Returns:     String
    //
    //**************************************************************
    private String arrayListToString(ArrayList<Integer> list) {
    	
    	// Instantiate new string builder
        StringBuilder stringBuilder = new StringBuilder();

        // loop through list and append to the string with a space seperation
        for (Integer number : list) {
            stringBuilder.append(number).append(" ");
        }

        return stringBuilder.toString();
    }
    
    //***************************************************************
    //
    //  Method:       generateRandomNumbers
    // 
    //  Description:  generates a list of random numbers for the program
    //
    //  Parameters:   int numberOfDigits
    //
    //  Returns:     ArrayList<Integer>
    //
    //**************************************************************
    public static ArrayList<Integer> generateRandomNumbers(int numberOfDigits) {

        ArrayList<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();

        // Generate list of random numbers
        for (int i = 0; i < numberOfDigits; i++) {
            randomNumbers.add(random.nextInt(10)); // Generates a random number between 0 and 9
        }

        return randomNumbers;
    }
    
    //***************************************************************
    //
    //  Method:       inputValidation (Non Static)
    // 
    //  Description:  validates the user input
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public int inputValidation(String userInput) {
    	
    	int userInt = 0;
    	boolean validation = true;
    	
    	// Make sure input is numeric
        do {
        	try {
        		userInt = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            // If user input is invalid prompt user to enter new input
        	validation = false;
            System.out.println("Invalid input. Please enter a valid number.");
            userInput = userChoice(); 
        }
        }while (!validation);
        return userInt;
    }
    
    //**************************************************************
    //
    //  Method:       parseReference
    //
    //  Description:  parses the reference string from user,
    //                Gets new input form user if invalid.
    //
    //  Parameters:   String pageReferenceString
    //
    //  Returns:      ArrayList<Integer>
    //
    //**************************************************************
    public ArrayList<Integer> parseReference(String pageReferenceString) {
    	
        boolean validation = true;
        ArrayList<Integer> pages = null;

        // loop to parse user reference string
        do {
            try {
            	validation = true;
                pages = Arrays.stream(pageReferenceString.split("\\s+"))
                        .map(Integer::parseInt)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                // Additional check for each element in the list
                for (int element : pages) {
                    if (element > 9 || element < 0) {
                        throw new NumberFormatException();
                    }
                }
                // If user input is invalid prompt to re enter 
            } catch (Exception e) {
                validation = false;
                printOutput("Your page-reference string was invalid");
                printOutput("Please try again, must be space-separated integers (0-9 only)");
                pageReferenceString = userChoice();
            }
        } while (!validation);

        return pages;
    }
    
    //**************************************************************
    //
    //  Method:       userChoice
    //
    //  Description:  gets input from user, closes scanner when program exits 
    //
    //  Parameters:   N/A
    //
    //  Returns:      String file
    //
    //**************************************************************	
    public String userChoice() {
    	String userChoice;
    	// Use Scanner to receive user input
    	Scanner userInput = new Scanner(System.in);
    	// Store user choice
    	userChoice = userInput.nextLine();
    	
    	// close scanner when program exits.
    	if (userChoice.equalsIgnoreCase("0")) {
    		userInput.close();
    		}
    	return userChoice;
    	}
    
	//***************************************************************
	//
	//  Method:       printOutput (Non Static)
	// 
	//  Description:  handles printing output
	//
	//  Parameters:   String output
	//
	//  Returns:      N/A
	//
	//***************************************************************
	public void printOutput(String output) {
		//Print the output to the terminal
		System.out.print("\n");
		System.out.println(output);
	}//End printOutput
    
    //***************************************************************
    //
    //  Method:       developerInfo (Non Static)
    // 
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public void developerInfo(){
       System.out.println("Name:    Marshal Pfluger");
       System.out.println("Course:  COSC 4302 Operating Systems");
       System.out.println("Project: Eight\n\n");
    } // End of the developerInfo method
}