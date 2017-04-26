package edu.Frostburg.COSC485_Aaron_Davis_NFAtoDFA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * DFA - Deterministic Finite Automata
 * 
 * States are represented by an array
 * Transitions are specified as an array
 * 
 * @author aarondavis
 * end used instead of "final" 
 */
public class DFA {
	
	String start;
	Set<String> end;
	//Map used for transitions from states
	Map<String, Map<Character, String>> transitions;
	
	/**
	 * Constructor for DFA
	 * Trees used for traversal purposes - can be used for large
	 * @param state
	 * @param transition
	 */
	private DFA(String[] state, String[] transition){
		
		end = new TreeSet<>();
		transitions = new TreeMap<>();
		
		//For loop used for States
		for(String s: state) {
			String[] temp = s.split(",");
			
			if(temp.length > 1) {
				if(temp[1].equals("Start")) {
					start = temp[0];
				}
				else if(temp[1].equals("End")) {
					end.add(temp[0]);
				}
			}
		}
		
		//For loop used for Transitions
		for(String s: transition) {
			
			String[] temp = s.split(",");
			String current = temp[0];
			String next = temp[1];
			
			if(!transitions.containsKey(current)) {
				transitions.put(current, new TreeMap<Character, String>());
			}
			
			for(int i = 2; i < temp.length; i++) {
				transitions.get(current).put(temp[i].charAt(0), next);
			}
		}
		
		System.out.println("Starting state: " + start);
		System.out.println("Final states: " + end);
		System.out.println("Transition functions: " + transitions);
	}
	
	/**
	 * @param s - String to be matched
	 * @return True if DFA accepts string, False if otherwise
	 */
	private boolean Match(String s) {
		
		String current = start; // where we are now
		for (int i=0; i<s.length(); i++) {
			
			char c = s.charAt(i);
			if (!transitions.get(current).containsKey(c)) {
				System.out.println("This isn't a DFA!");
				return false;
			}
			current = transitions.get(current).get(c); // take a step according to c
		}
		
		return end.contains(current); // did we end up in one of the desired final states?
	}
	
	/**
	 * Validates string and prints correct message
	 * @param input
	 * @return 
	 */
	private String Validate(String[] inputs, String s, String str) {
		
		for (int i = 0; i < inputs.length; i++) {
			if(Match(s) == true){
				str = s + " : " + "was accepted by DFA";
				System.out.println(s + ": " + "was accepted by DFA");
			}
			else {
				str = s + " : " + "was not accepted by DFA";
				System.out.println(s + ": " + "was not accepted by DFA");
			}
		}
		return str;
	}
	
	/**
	 * Main method - also handles file
	 * @param args
	 * Can only handle up to 2 arguments at once
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		//String[] states = {"q0,Start","q0,End", "q1,", "q2,End", "q3,End"};
		//String[] transitions = {"q0,q1,a", "q0,q4,b", "q1,q4,a", "q1,q2,b", "q2,q3,a", "q2,q0,b", "q3,q1,a", "q3,q2,b", "q4,q4,a", "q4,q4,b"};
		
		File file = new File(args[0]);
		Scanner scan = new Scanner(file);
		scan.nextLine();
		scan.nextLine();
		
		String stuff = scan.nextLine().trim();
		stuff = stuff.substring(stuff.indexOf("{")+1, stuff.indexOf("}"));
		String[] state = stuff.split(",");
		for(int i = 0; i < state.length; i++){
			state[i] = state[i].trim();
		}
		state[0] += ",Start";
		
		scan.nextLine();
		
		stuff = scan.nextLine().trim();
		stuff = stuff.substring(stuff.indexOf("=")+1, stuff.indexOf(","));
		for(int i = 0; i < state.length; i++) {
			if(stuff.equals(state[0])) {
				
			}
		}
		//String[] startState = stuff.split(",");
		//System.out.println(startState.length);
		
		stuff = scan.nextLine();
		stuff = stuff.substring(stuff.indexOf("{")+1, stuff.indexOf("}"));
		String[] finalStates = stuff.split(",");
		for(int i = 0; i < finalStates.length; i++) {
			finalStates[i] = finalStates[i].trim();
		}
		
		for(int i = 0; i < state.length; i++) {
			for(int j = 0; j < finalStates.length; j++) {
				if(finalStates[j].substring(1, 2).equals(state[i].substring(1, 2))) {
					state[i] += ",End";
				}
			}
		}
		
		scan.nextLine();
		
		ArrayList<String> tempList = new ArrayList<>();
		while(scan.hasNextLine()){
			stuff = scan.nextLine().trim();
			
			if(stuff.equals("}")) break;
			
			stuff = stuff.substring(stuff.indexOf("(")+1, stuff.indexOf(")"));
			String[] tempArray = stuff.split("\n");
			
			for(String s: tempArray) {
				tempList.add(s);
			}
		}
		
		String[] transitions = new String[tempList.size()];
		tempList.toArray(transitions);
		
		for(int i = 0; i < transitions.length; i++) {
			transitions[i] = transitions[i].trim();
			//System.out.println(transitions[i]);
		}
		
		for(int i = 0; i < transitions.length; i++) {
			
			String ex = transitions[i].substring(4, 5);
			String ex2 = transitions[i].substring(transitions.length-3, transitions.length-1);
			
			StringBuilder sb = new StringBuilder(transitions[i]);
			sb.replace(3, 5, ex2);
			sb.replace(transitions.length-4, transitions.length, ex);
			
			transitions[i] = sb.toString();
		}
		scan.close();
		
		DFA dfa = new DFA(state, transitions);
		
		//String[] fromDFAText = {"aaaab"};
		file = new File(args[1]);
		scan = new Scanner(file);
		
		String[] fromDFAText = new String[1];
		File results = new File(args[2]);
		
		System.out.println();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(results));
		
		while(scan.hasNextLine()) {
			stuff = scan.nextLine();
	
				fromDFAText[0] = stuff;
				String str = dfa.Validate(fromDFAText, fromDFAText[0], stuff);
				
				writer.write(str);
				writer.newLine();
				
		} writer.close();
			} catch (IOException e) {
				System.out.println("Error occured");
			}
		scan.close();
	}
}
