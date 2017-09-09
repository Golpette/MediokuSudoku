package com.puzzle.andrew.sudowordsandroid.crossword;
import java.util.ArrayList;

/**
 * It takes as input some 'toWorkWith' string in grid that could potentially fit
 * a word, subject to the constraints of the letters already present, and tries
 * to find the **biggest** word that satisfies these constraints.
 
 *
 * To Andy: I added boolean biggestFirst into fitWord() so we have option of fitting the 
 * biggest possible word into the space, or a word of a random (allowed) size
 *
 */

public class Search {

	// Given a "toWorkWith" String, search word list for suitable entries
	public Search(ArrayList<Word> wordlist, String toWorkWith) {               //is this totally redundant now?? 
		String wordFound = "";
		String definition = "";
		String hold = toWorkWith;
		String[] word_and_def = new String[2];
		
		//Make list of all possible lengths the desired word could be
		ArrayList<Integer> possSizes = new ArrayList<Integer>();
		for (int i = 2; i < hold.length() - 1; i++) {
			if (hold.charAt(i + 1) == '_') {
				int possL = (i + 1);
				possSizes.add(possL);
			}
		}
		possSizes.add(hold.length());
		
		while (possSizes.size() > 0) {
			
			//pick a size randomly -- EDITED, SEE BELOW
			//int rand_point = (int)( Math.random() * possSizes.size() );
			//int size = possSizes.get( rand_point );

			// Changed above so as to always try and fit the biggest word possible first
			
			int size = possSizes.get(possSizes.size() - 1);
			
			//create template
			String hold_2 = "";
			for (int j = 0; j < size; j++) {
				hold_2 = hold_2 + hold.charAt(j);
			}
			boolean allBlank = true;
			for (int t = 0; t < hold_2.length(); t++) {
				if (hold_2.charAt(t) != '_') {
					allBlank = false;
				}
			}
			
			// Check word list --- do this in a random way. Start at a random position in the list,
			// and go forward or back with equal probability
			int rand_start_point = (int) (Math.random() * wordlist.size());
			// Choose to cycle forward (0), or back (1) randomly
			int forw_bk = (int) (Math.random() * 2);
			if (forw_bk == 0) {
				for (int w = rand_start_point; w < wordlist.size(); w++) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					//check sizes match				
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							//check existing characters match
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = wordlist.size() + 5;	 //(so as to break loop 
						possSizes.clear();			 //Should probably use 'break' or 'continue')
					}
				}//end *FORWARD* for loop	
			} // if(frw_bk == 0 )
			
			// else cycle through words list in reverse direction
			else if (forw_bk == 1) {
				for (int w = rand_start_point; w > 0; w--) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					//check sizes match				
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							//check existing characters match
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = -3;// (so as to break loop **BREAK!!**)
						possSizes.clear();
					}
				}//end *BACKWARD* for loop	
			}//if( forw_bk == 1)
			
			//Check if there are other possible sizes the word could take
			if (possSizes.size() != 0) {
				//possSizes.remove( rand_point );
				// Remove the largest size, and thus try next largest
				possSizes.remove(possSizes.size() - 1);
			}
		}// end while loop
		
		word_and_def[0] = wordFound;
		word_and_def[1] = definition;
	}

	
	
	
	
	
	
	
	
	public static String[] findWord(ArrayList<Word> wordlist, String toWorkWith, boolean biggestFirst) {
		String wordFound = "";
		String definition = "";
		String hold = toWorkWith;
		String[] word_and_def = new String[2];
		ArrayList<Integer> possSizes = new ArrayList<Integer>();
		for (int i = 2; i < hold.length() - 1; i++) {
			if (hold.charAt(i + 1) == '_') {
				int possL = (i + 1);
				possSizes.add(possL);
			}
		}
		possSizes.add(hold.length());
		
		while (possSizes.size() > 0) {
			
            // Steve: don't always choose biggest word --------------------
            int size;
            int rndm_sz=0;
            if(biggestFirst){ //choose biggest possible word
                size = possSizes.get(possSizes.size() - 1);
            }
            else{  // Choose random word size to fit:
                rndm_sz = (int)(Math.random()*possSizes.size()) ;   // this needs to be removed from list if it fails
                size = possSizes.get( rndm_sz  );
            }      
            //-------------------------------------------------------------	
			
            
			String hold_2 = "";
			for (int j = 0; j < size; j++) {
				hold_2 = hold_2 + hold.charAt(j);
			}
			boolean allBlank = true;
			for (int t = 0; t < hold_2.length(); t++) {
				if (hold_2.charAt(t) != '_') {
					allBlank = false;
				}
			}
			int rand_start_point = (int) (Math.random() * wordlist.size());
			int forw_bk = (int) (Math.random() * 2);
			if (forw_bk == 0) {
				for (int w = rand_start_point; w < wordlist.size(); w++) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = wordlist.size() + 5;
						possSizes.clear();
					}
				}
			} 
			else if (forw_bk == 1) {
				for (int w = rand_start_point; w > 0; w--) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = -3;
						possSizes.clear();
					}
				}
			} 
			if (possSizes.size() != 0) {
				
                // Steve:  remove the appropriate failed size -----------------
                if(biggestFirst){
            		possSizes.remove(possSizes.size() - 1);
                }else{
                    possSizes.remove( rndm_sz );
                }//--------------------------------------------------				
				
			}
		}
		word_and_def[0] = wordFound;
		word_and_def[1] = definition;
		return word_and_def;

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Steve: method that forces each word added to be connected to at least one other,
	//       thus ensuring we get connected grid on first attempt.
	public static String[] findWord_forceConnection(ArrayList<Word> wordlist, String toWorkWith, boolean biggestFirst, boolean firstWord  ) {
		String wordFound = "";
		String definition = "";
		String hold = toWorkWith;
		String[] word_and_def = new String[2];

		
		// edit to force connectivity: only a possible size if the space already contains at least 1 letter -----------
		ArrayList<Integer> possSizes = new ArrayList<Integer>();

        String zzz = ""+hold.charAt(0);  zzz = zzz+hold.charAt(1);   
		for (int i = 2; i < hold.length() - 1; i++) {
            zzz=zzz+hold.charAt(i);
			if (hold.charAt(i + 1) == '_' ) {
				if( !firstWord ){
                    boolean empty = true;
                    for( int jj=0; jj<zzz.length(); jj++ ){  //only a possible length if it contains 1 other char
                        if( zzz.charAt(jj) != '_' ){
                            empty = false;
                        }
                    }
                    if( !empty ){
        				int possL = (i + 1);
	        			possSizes.add(possL);
                    }
                }else{
       				int possL = (i + 1);
        			possSizes.add(possL);
                }
			}
		}

        boolean empty = true;
        for( int jj=0; jj<zzz.length(); jj++ ){  //only a possible length if it contains 1 other char
            if( zzz.charAt(jj) != '_' ){
               empty = false;
            }
        }
//        if( hold.charAt( hold.length()-1 ) != '_' ){   //if first word, allow max size        
        if( !empty ){   //if first word, allow max size
    		possSizes.add(hold.length());
        }
        // -----------------------------------------------------------------------------------




		while (possSizes.size() > 0) {


            // Steve: don't always choose biggest word --------------------
            int size;
            int rndm_sz=0;
            if(biggestFirst){
                size = possSizes.get(possSizes.size() - 1);
            }
			//int size = possSizes.get(possSizes.size() - 1);
            else{
                // Totally random:
                rndm_sz = (int)(Math.random()*possSizes.size()) ;   // this needs to be removed from list if it fails
                size = possSizes.get( rndm_sz  );
            }      
            //-------------------------------------------------------------




			String hold_2 = "";
			for (int j = 0; j < size; j++) {
				hold_2 = hold_2 + hold.charAt(j);
			}
			boolean allBlank = true;
			for (int t = 0; t < hold_2.length(); t++) {
				if (hold_2.charAt(t) != '_') {
					allBlank = false;
				}
			}
			int rand_start_point = (int) (Math.random() * wordlist.size());
			int forw_bk = (int) (Math.random() * 2);
			if (forw_bk == 0) {
				for (int w = rand_start_point; w < wordlist.size(); w++) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = wordlist.size() + 5;
						possSizes.clear();
					}
				}
			} 
			else if (forw_bk == 1) {
				for (int w = rand_start_point; w > 0; w--) {
					String try_word = wordlist.get(w).getWord();
					boolean match = true;
					if (try_word.length() == size) {
						if (allBlank) {
							wordFound = try_word;
							definition = wordlist.get(w).getDef();
							match = true;
						} else {
							for (int c = 0; c < size; c++) {
								if (hold_2.charAt(c) != '_') {
									if (hold_2.charAt(c) != try_word.charAt(c)) {
										match = false;
									}
								}
							}
						}
					} else {
						match = false;
					}
					if (match == true) {
						wordFound = try_word;
						definition = wordlist.get(w).getDef();
						w = -3;
						possSizes.clear();
					}
				}
			} 
			if (possSizes.size() != 0) {



                // Steve:  remove the failed size -----------------
                if(biggestFirst){
            		possSizes.remove(possSizes.size() - 1);
                }else{
                    possSizes.remove( rndm_sz );
                }
                //--------------------------------------------------




			}
		}
		word_and_def[0] = wordFound;
		word_and_def[1] = definition;
		return word_and_def;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
