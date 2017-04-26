package edu.Frostburg.COSC485_Aaron_Davis_NFAtoDFA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * NFA - Nondeterministic Finite Automata
 * 
 * Everything is represented similar to DFA
 * @author aarondavis
 *
 */
public class NFA {

	String start;
	Set<String> end;
	//Allowed multiple transitions from state to a character
	Map<String, Map<Character, List<String>>> transitions;
	
	/**
	 * Constructor to properly build NFA
	 * @param state states in NFA
	 * @param transition - transitions in DFA
	 */
	private NFA(String[] state, String[] transition) {
		
		end = new TreeSet<>();
		transitions = new TreeMap<>();
		
		//For loop for States
		for(String s: state) {
			 String[] temp = s.split(",");
			 
			 if(temp.length > 1){
				 if(temp[1].equals("Start")) {
					 start = temp[0];
				 }
				 
				 else if(temp[1].equals("End")) {
					 end.add(temp[0]);
				 }
			 }
		}
		
		//For loop for Transitions
		for(String s: transition) {
			
			String[] temp = s.split(",");
			String current = temp[0];
			String next = temp[1];
			
			if(!transitions.containsKey(current)) {
				transitions.put(current, new TreeMap<Character, List<String>>());
			}
			
			for(int i = 2; i < temp.length; i++) {
				char c = temp[i].charAt(0);
				
				//List of next states in this constructor
				if(!transitions.get(current).containsKey(c)) {
					transitions.get(current).put(c, new ArrayList<String>());
				}
				
				transitions.get(current).get(c).add(next);
			}
		}
		
		System.out.println("Starting state:" + start);
		System.out.println("Final State: " + end);
		System.out.println("Transition functions: " + transitions);
	}
	
	/**
	 * This different from the match method from the DFA because our NFA can have current states
	 * @param s - checks string
	 * @return True is String is accepted by NFA, false if not
	 */
	private boolean Match(String s) {
		
		Set<String> current = new TreeSet<String>();
		current.add(start);
		for (int i=0; i< s.length(); i++) {
			
			char c = s.charAt(i);
			Set<String> nextStates = new TreeSet<String>();
			
			for (String state : current)
				if (transitions.get(state).containsKey(c))
					nextStates.addAll(transitions.get(state).get(c));
			if (nextStates.isEmpty())
				return false; // no way forward for this input
			
			current = nextStates;
		}
		
		//End up in multiple states -- accept if any is an end state
		for (String state : current) {
			if (end.contains(state)) 
				return true;
		}
		return false;
	}
	
	/**
	 * Same thing as DFA methods
	 * @param input array argument(used for command line arguments
	 */
	private String Validate(String[] input, String str) {
		
		for(String s: input) {
			
			if(Match(s) == true) {
				System.out.println(s + " : " + "was accepted by the NFA.");
			}
			else {
				System.out.println(s + " : " + "was not accepcted by the NFA.");
			}
		}
		
		return str;
	}
	
	/**
	 * Main method for NFA class
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		//String[] states = {"q0,Start","q0,End", "q1", "q2,End"};
		//String[] transitions = {"q0,q1,a", "q1,q2,b", "q2,q0,a", "q2,q0,b", "q2,q0,\0"};
		
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
		scan.nextLine();
		
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
		}
		
		for(int i = 0; i < transitions.length; i++) {
			
			String ex = transitions[i].substring(4, 5).trim();
			String ex2 = transitions[i].substring(transitions.length+2, transitions.length+4);
			
			if(ex.equals("e")) {
				ex = "\0";
			}
			
			StringBuilder sb = new StringBuilder(transitions[i]);
			sb.replace(3, 5, ex2);
			sb.replace(6, 9, ex);
			
			transitions[i] = sb.toString();
			//System.out.println(transitions[i]);
		}
		scan.close();
		
		NFA nfa = new NFA(state, transitions);
		
		file = new File(args[1]);
		scan = new Scanner(file);
		
		String[] fromDFAText = new String[1];
		File results = new File(args[2]);
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(results));
		
		while(scan.hasNextLine()) {
			stuff = scan.nextLine();
	
				fromDFAText[0] = stuff;
				String str = nfa.Validate(fromDFAText, "");
				
				writer.append(str);
				//writer.close();
		} writer.close();
			} catch (IOException e) {
				System.out.println("Error occured");
			}
		scan.close();
	}
}
