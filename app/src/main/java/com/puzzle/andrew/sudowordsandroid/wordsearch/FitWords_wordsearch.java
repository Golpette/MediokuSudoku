package com.puzzle.andrew.sudowordsandroid.wordsearch;
import java.util.*;

import com.puzzle.andrew.sudowordsandroid.crossword.*;

import java.lang.Math;

/**
 * Fitting diagonal words in wordsearch
 * 
 */
public class FitWords_wordsearch {
	
	int MIN_WORD_LENGTH = 4;  // min length of allowed words
	

	//'Random fit approach' to try and fit words ACROSS into grid. Updates ArrayList<Entry> and grid all at once
	public FitWords_wordsearch(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries, String dir) {
		
		
		boolean biggestFirst = false;  
		//TODO: we don't want lots of little (3-letter words) but we don't always want to fit the biggest possible.
		//   Make some appropriate word-length weighting here.
		
		
		int init_x = (int) (Math.random() * (xLength - 2) + 1);
	    int init_y = (int) (Math.random() * (yLength - 2) + 1);
	    
    	ArrayList<Coord> visited = new ArrayList<Coord>(); //needed to track snakes

    	visited.clear();
		

		//Make sure space on LHS is empty, otherwise do nothing
		String toWorkWith = "";
		if ( dir.equals("across") ) {
			
			// Max length of word due to grid size 
			int maxPossLength = (xLength - 1) - init_x;
			// Get the configuration of grid sites 'across' from the random initial site
			// Will end looking like:  "_____" or "_B__A___" etc...
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y];		
				Coord cur = new Coord(init_x+i, init_y);
				visited.add(cur);
			}
		} else if( dir.equals("down") ) { 
			int maxPossLength = (yLength - 1) - init_y;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x][init_y + i];		
				Coord cur = new Coord(init_x, init_y+i);
				visited.add(cur);
			}
		} else if( dir.equals("diagonal") ){
			int maxX = (xLength - 1) - init_x;
			int maxY = (yLength - 1) - init_y;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y + i];		
				Coord cur = new Coord(init_x+i, init_y+i);
				visited.add(cur);
			}
		} else if( dir.equals("backwards") ){
			int maxPossLength = init_x - 1 ;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y];	
				Coord cur = new Coord(init_x-i, init_y);
				visited.add(cur);
			}
		} else if( dir.equals("up") ){
			int maxPossLength = init_y - 1 ;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x ][init_y - i ];	
				Coord cur = new Coord(init_x, init_y-i);
				visited.add(cur);
			}
		}else if( dir.equals("backwardsdiagonal") ){
			int maxX = init_x - 1;
			int maxY = init_y - 1;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y - i];	
				Coord cur = new Coord(init_x-i, init_y-i);
				visited.add(cur);
			}
		}else if( dir.equals("backwardsBLTRdiagonal") ){
			int maxX = init_x - 1;
			int maxY = (yLength - 1) - init_y;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y + i];		
				Coord cur = new Coord(init_x-i, init_y+i);
				visited.add(cur);
			}
		}else if( dir.equals("BLTRdiagonal") ){
			int maxX= (xLength - 1) - init_x;
			int maxY = init_y - 1;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y - i];		
				Coord cur = new Coord(init_x+i, init_y-i);
				visited.add(cur);
			}
		}
		//}/////////////////////////////////Attempt at snaking words
	    else if(dir.equals("snaking")){		

	    	int curx=init_x;  int cury=init_y;
	    	Coord current_coord = new Coord(curx, cury);
	    	
	    	//visited.clear();
	    	visited.add(current_coord);
	    	toWorkWith = toWorkWith + grid[curx][cury];
	    	
	    	boolean growing = true;
	    	
	    	while(growing  && toWorkWith.length()<xLength){
			    //ArrayList<Coord> possible_neighbours = getPossNeighbours(curx, cury);
			    ArrayList<Coord> possible_direcs = new ArrayList<Coord>();
			    // use 4 nns
			    possible_direcs.add(new Coord(1,0));    possible_direcs.add(new Coord(-1,0));
			    possible_direcs.add(new Coord(0,1));    possible_direcs.add(new Coord(0,-1));
			    

    			boolean hasNeighbours = true;
    			while(hasNeighbours){
    				int rndm = (int)(Math.random()*possible_direcs.size()  );
    				Coord next_space = Coord.add(current_coord, possible_direcs.get(rndm)); 
    				
    				if( next_space.getX()<xLength-2  &&  next_space.getY()<yLength-2  &&
    						next_space.getX()>0  &&  next_space.getY()>0 &&
    						!visited.contains( next_space )  ){
    					
    					visited.add(next_space);
    					toWorkWith = toWorkWith + grid[next_space.getX()][next_space.getY()];
    					
    					current_coord = new Coord(next_space);	    					
    					hasNeighbours=false; //exit loop and start from next_space			
    				}
    				else{//nn not suitable
    					possible_direcs.remove(rndm);
    					if(possible_direcs.size()==0){ hasNeighbours=false; growing=false; }		
    				}
    				
    			}
	    		
	    	}
	 	
		}
		else{
			System.out.println("WORDSEARCH DIRECTION NOT DEFINED");  System.exit(1);
		}
		
		
		
		
		
		// Now need to fit something to the "toWorkWith" String.
		// Uses **SearchWords.java**
		String[] word_and_def = new String[2];
		String word = "";
		String definition = "";

		// Get max possible word length
		int maxWordLength = toWorkWith.length();
		if (maxWordLength >= MIN_WORD_LENGTH && toWorkWith.contains("_")) {
			
			word_and_def = Search.findWord(wordlist, toWorkWith, biggestFirst);   //new parameter here
			
			word = word_and_def[0];
			definition = word_and_def[1];
		}
		if (!word.equals("") && word.length()>=MIN_WORD_LENGTH) {

			// Only add entry if it's not been seen before.
			boolean seenBefore = false;
			for (int j = 0; j < entries.size(); j++) {
				if(word.toLowerCase().contains(entries.get(j).getWord().toLowerCase())){
					seenBefore = true;
				}
			}
			if (!seenBefore) {

				if( !dir.equals("snaking" ) ){
					// Then fit has been found. Need to modify grid
					for (int g = 0; g < word.length(); g++) {
						if ( dir.equals("across") ) {
							grid[init_x + g][init_y] = "" + word.charAt(g);
						} else if( dir.equals("down") ) {
							grid[init_x][init_y + g] = "" + word.charAt(g);
						} else if( dir.equals( "diagonal" ) ){
							grid[init_x + g][init_y + g] = "" + word.charAt(g);						
						} else if( dir.equals( "backwards" ) ){
							grid[init_x - g][init_y ] = "" + word.charAt(g);						
						} else if( dir.equals( "up" ) ){
							grid[init_x ][init_y - g ] = "" + word.charAt(g);						
						}else if( dir.equals("backwardsdiagonal")  ){
							grid[init_x - g][init_y - g] = "" + word.charAt(g);						
						}else if( dir.equals("BLTRdiagonal")  ){
							grid[init_x + g][init_y - g] = "" + word.charAt(g);	
						}else if( dir.equals("backwardsBLTRdiagonal")  ){
							grid[init_x - g][init_y + g] = "" + word.charAt(g);	
						}
					}
				}
				else if( dir.equals("snaking")  ){					
					int letter=0;
					for( Coord c: visited ){
						if( letter>=word.length() ){ break; }
						grid[c.getX()][c.getY()] = ""+word.charAt(letter);
						letter++;
					}		
				}
					
				

				// Update Entries list
				Entry entry = new Entry(init_x, init_y, true, word, definition, dir, visited); //across boolean not sensible for wordsearches
				entries.add(entry);
			}
		}
	}
	
	
	
	

	
	
	
	
}
