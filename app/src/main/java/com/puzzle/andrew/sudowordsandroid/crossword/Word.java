package com.puzzle.andrew.sudowordsandroid.crossword;
import java.util.*;

/**
 * Holds list of words and definitions from input file.
 * Word Object holds word, definition (singleDef for now)
 * and an ArrayList of multiple definitions for later on
 */

public class Word {

	String word;
	String singleDef;
	int noDefinitions;
	ArrayList<String> definitions;
	
	public Word(String word, ArrayList<String> definitions, int noDefinitions){
		this.word = word;
		this.definitions = definitions;
		this.noDefinitions = noDefinitions;
	}
	
	public Word(String word, String singleDef){
		this.word = word;
		this.singleDef = singleDef;
	}
	
	public String getWord(){
		return this.word;
	}
	
	public String getDef(){
		return this.singleDef;
	}
	
	public String getDefinition(int def){
		return this.definitions.get(def);
	}
	
	public int getNoDefinitions(ArrayList<String> definitions){
		return definitions.size();
	}
}
