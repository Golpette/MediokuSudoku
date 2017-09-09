package com.puzzle.andrew.sudowordsandroid.wordsearch;
import java.io.*;
import java.util.*;

import com.puzzle.andrew.sudowordsandroid.crossword.*;

public class WordSearchGenerator{

	int wordsearchSize;
	int gridSize;
	int x;     
	int y;
	int difficulty;
	String[][] grid;
	String dictionary1 = "", dictionary2 = "";
	ArrayList<String> acrossClues;
	ArrayList<String> downClues;
	ArrayList<Word> words;	
	
	public WordSearchGenerator(int wordsearchSize, int difficulty, String dictionary1, String dictionary2, String languageFrom, String languageTo, String currentTopic) throws IOException{
		this.difficulty = difficulty;
		this.wordsearchSize = wordsearchSize;
		this.dictionary1 = dictionary1;
		this.dictionary2 = dictionary2;
		gridSize = wordsearchSize + 2;
		x = gridSize;     
		y = gridSize;
		grid = new String[x][y];
		acrossClues = new ArrayList<String>();
		downClues = new ArrayList<String>();
		ArrayList<Word> words = new ArrayList<Word>();
				System.out.println("!!Dictionary: "+ dictionary1 + " " + dictionary2);
		words = ReadWords.getWordsandDefs(dictionary1, dictionary2);
		
		
		
		grid = new String[x][y];		
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = "_";
				}
			}	
			
			ArrayList<Entry> entries = new ArrayList<Entry>();
			
			
			
			// Fit specific number of words
			
			//int num_words_to_fit = 3;
			int num_words_to_fit = (int) (Math.pow(wordsearchSize, 2)/10)+8 ;  // TODO: CHOOSE THIS DEPENDING ON GRID SIZE
			//System.out.println("Number of Words in Puzzle: " + num_words_to_fit);

			int count_tries = 0;
			
			while( entries.size() < num_words_to_fit   && count_tries<10000 ){  //stop infinite loop
				count_tries++;	

				int direc = (int)(Math.random() * difficulty);    // TODO: USE THIS NUMBER TO CONTROL "DIFFICULTY (i.e. backwards/diagonal etc)
														// AR: could simply set the size to choose between.  Easy= 0,1, Medium = 0,1,2,3, Hard = 0,1,2,3,4,5,6,7
				
				//##delete for snaking
				//int direc = 9;
				//###
				

				if( direc==0 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "across" );
				}
				else if( direc==1 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "down" );
				}
				else if( direc==2 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "diagonal" );
				}
				else if( direc==3 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "BLTRdiagonal" );
				}
				else if( direc==4 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "backwards" );
				}
				else if( direc==5 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "up" );
				}
				else if( direc==6 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "backwardsdiagonal" );
				}
				else if( direc==7 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "backwardsBLTRdiagonal" );
				}
				else if(direc >= 8 ){
					//System.out.println("snaking");
					new FitWords_wordsearch(grid, x, y, words, entries, "snaking" );
					
				}	
			}
			acrossClues.clear();
			downClues.clear();
			int c=1;
			
			for(int i = 1; i < y-1; i++){   //STEVE: modified from crossword. Don't care about direction or clue numbers
				for(int j = 1; j < x-1; j++){
					for(int current=0; current <entries.size(); current++){
						if(entries.get(current).getX() == j && entries.get(current).getY() == i){
							acrossClues.add(Integer.toString(c)+ ". " + entries.get(current).getDefinition());	
							c++;
						}
					}				
				}
			}

			/*
			* *************** ANDROID
			* */
			//This is where to implement XML version for Android
		//new DrawWordSearch(grid, x, y, acrossClues, downClues, entries, difficulty, languageFrom, languageTo, currentTopic);


	}
}
