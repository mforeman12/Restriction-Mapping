package Foreman3;

import java.util.ArrayList;
import java.util.Collections;

//Program: Foreman3
//Course: Current Topics
//Description: Implements Brute Force and Another Brute Force Algorithms to 
//find all possible restriction mappings X for a given set L. 
//Author: Matt Foreman
//Revised: 10/5/15
//Language: Java
//IDE: NetBeans 8.0.1
//****************************************************************************
//****************************************************************************
//Class: Foreman3
//Description: Constant and ArrayList declaration. Also contains main method.
public class Foreman3 {

    ArrayList<Integer> L = new ArrayList<>(); //always contains L.
    ArrayList<Integer> PossibleElementsOfX = new ArrayList<>(); //contains
    //unique elements in L, or every consecutive integer between 0
    //and M, depending on which algorithm is used.
    ArrayList<ArrayList> SolutionArray = new ArrayList<>(); //An ArrayList
    //containing each solution ArrayList.
    int n;
    int M;
    int iterationsOfDX; //total number of sets of DX that are generated
    ArrayList<Integer> iterationsOfLegitX = new ArrayList<>(); 
    //ArrayList of each iteration on which a legitimate X was found

    int invalidL; //used to break from the program if L is invalid.

//Method: Foreman3
//Description: Constructor method for the program. Constant and ArrayList 
//declaration. Handles different program modes; also handles errors. 
//Parameters: ArrayList<Integer> l - the list that will be loaded into L
//            int mode - determines which algorithm to use  
//Returns: nothing
//Calls: findN
//********************************************************************
    public Foreman3(ArrayList<Integer> l, int mode) {
        L.clear(); //reset all globals every time the constructor is called
        PossibleElementsOfX.clear();
        SolutionArray.clear();
        n = 0;
        M = 0;
        iterationsOfDX = 0;
        iterationsOfLegitX.clear();
        invalidL = 0;

        L = (ArrayList<Integer>) l.clone(); //instantiate L and M
        Collections.sort(L);
        M = L.get(L.size() - 1);

        if (mode == 1) { //if using Brute Force, generate every int between
            //0 and M and add it to PossibleElementsOfX
            for (int i = 1; i <= M; i++) {
                PossibleElementsOfX.add(i);

            }
        } else { //else, only add the unique elements from L, excluding M
            for (int i = 0; i < L.size(); i++) {
                if (!PossibleElementsOfX.contains(L.get(i))) {
                    PossibleElementsOfX.add(L.get(i));
                }

            }
        }
        PossibleElementsOfX.remove(PossibleElementsOfX.size() - 1); //Remove
        //M from PossibleElementsOfX
        int a = findN(); //calculate n and determine whether it is valid
        if (a == 0) {
            System.out.println("Error: invalid L. "
                    + "No possible X for this L. Please try again.");
            invalidL = 1;
        } else {
            n = a;
        }

    }

//Method: main
//Description: Drives the program.
//Parameters: None 
//Returns: nothing
//Calls: Foreman3
//       generateX
//********************************************************************
    public static void main(String[] args) {
        int run = 1;
        while (run == 1) { //loop until user exits
            KeyboardInputClass keyboardInput = new KeyboardInputClass();
            TextFileClass text = new TextFileClass();

            text.getFileName("Specify the text file to be read:");
            text.getFileContents();

            String[] lString = text.text[0].replaceAll( 
                    //place each integer from L into a String array
                    
                    "\\{", "").replaceAll("\\}", "").split(",");

            int algorithmSelect = keyboardInput.getInteger(
                    true, 1, 1, 2, "\nWhich algorithm would you "
                    + "like to use? \n1: Brute Force \n2: Another Brute Force");

            ArrayList<Integer> tempList = new ArrayList();

            for (int i = 0; i < lString.length; i++) { 
                //add values from L into a temp list
                
                tempList.add(Integer.parseInt(lString[i]));

            }

            Foreman3 instance = new Foreman3(tempList, algorithmSelect);
            if (instance.invalidL == 1) { //Reset if L is invalid

            } else {
                tempList.clear();
                instance.generateX(tempList); 
                //Clear tempList and use it to start the
                //recursive algorithm

                System.out.println("");

                System.out.println("Total number of generated possible X's:  "
                        + instance.iterationsOfDX);
                System.out.println("");

                System.out.println("The Legitimate X's are: ");

                for (int i = 0; i < instance.SolutionArray.size(); i++) { 
                    //Loop through solution ArrayList and print all solutions
                   
                    System.out.print("{");

                    for (int j = 0; j < instance.SolutionArray.get(i).size();
                            j++) {
                        if (j < instance.SolutionArray.get(i).size() - 1) {
                            System.out.print(
                                    instance.SolutionArray.get(i).get(j) + ",");
                        } else {
                            System.out.print(
                                    instance.SolutionArray.get(i).get(j) + "}");
                        }

                    }
                    System.out.print("  Found at iteration " + 
                            instance.iterationsOfLegitX.get(i));
                    System.out.println("");

                }
            }

            if (keyboardInput.getInteger(true, 2, 1, 2,
                    "\nEnter another text file? \n1: Yes \n2: "
                    + "Exit (default: exit)\n") == 2) {
                run = 2;
            }

        }
    }

//Method: findN
//Description: Calculates n based on L, using the formula given in class.
//Parameters: None
//Returns: int - returns the value of n, or zero if n is a decimal number.
//Calls: none
//********************************************************************
    public int findN() {
       double tempN = ((Math.sqrt(8 * L.size() + 1) + 1) / 2); //Simplified Quad
        //formula which is guaranteed to exclude the extraneous case
        if (tempN == (int) tempN) { //if n is an int, return it
            return (int) tempN;
        } else { //otherwise, return 0 for error handling
            return 0;
        }
    }

//Method: generateX
//Description: recursively generates all possible restriction mappings X from 
//the possibleElementsOfX ArrayList. This ArrayList varies depending on the
//algorithm being used. 
//Parameters: ArrayList<Integer> X 
//Returns: nothing
//Calls: testForSolution
//       generateX
//********************************************************************
    public void generateX(ArrayList<Integer> X) {

        if (X.isEmpty()) { //Add the lower bound to the initial X
            X.add(0);
        }

        int indexOfNextPossibleElement //The index of the next unique element 
                //to be added to X
                = PossibleElementsOfX.indexOf(X.get(X.size() - 1)) + 1;
        int remainingNumberInX = n - X.size() - 1; //The remaining number of
        //empty spaces in X, not including the upper bound

        if (X.size() < n - 1) {//If X.size == n - 1, we want to add the upper
            //bound and compare it to L

                               //Here we start i at the position of the next 
            //possible element from PossibleElementsOfX, and
            //increment it, checking each loop that there
            //are still enough elements between i and the 
            //end of PossibleElementsOfX to fill all of the
            //remaining blank spaces in our current X 
            //(excluding the upper bound).
            for (int i = indexOfNextPossibleElement;
                    PossibleElementsOfX.size() - i >= remainingNumberInX; i++) {

                X.add(PossibleElementsOfX.get(i)); //add the current element to
                //x
                generateX(X);                      //recursively call generateX
                //with the new X     
                X.remove(X.size() - 1);            //reset X to what is was
                //before the recursive call 

            }
        }
        if (X.size() == n - 1) {
            M = L.get(L.size() - 1);
            testForSolution(X);
        }

    }

//Method: testForSolution
//Description: Creates an ArrayList containing the current restriction mapping
//to be tested as a possible solution. This restriction mapping is then used
//to create a new ArrayList for deltaX, which is then compared to L.
//Parameters: ArrayList<Integer> X
//Returns: nothing
//Calls: none
//********************************************************************
    public void testForSolution(ArrayList<Integer> X) {
        iterationsOfDX++; //increment our iteration counter

        ArrayList<Integer> testX = (ArrayList<Integer>) X.clone(); //Clone X for
        //testing
        testX.add(M); //add the upper bound to testX

        ArrayList<Integer> deltaX = new ArrayList();

        for (int i = 0; i < testX.size() - 1; i++) {
            for (int j = i + 1; j < testX.size(); j++) {
                deltaX.add(testX.get(j) - testX.get(i)); //Populate deltaX for
                //comparison

            }

        }

        Collections.sort(deltaX);
        if (deltaX.equals(L)) { //If X is a legitimate mapping
            iterationsOfLegitX.add(iterationsOfDX); //Add the current value of
    //the iteration to an ArrayList of positions where we found a legitimate X
            SolutionArray.add(testX); //Add it to the ArrayList of solutions
        }

    }

}
