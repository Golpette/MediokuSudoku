package com.puzzle.andrew.sudowordsandroid.crossword;

//import java.awt.font.Image;
//import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

	
	static ResourceLoader rl = new ResourceLoader();
	
	/*public static Image getImage(String filename){
		
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource(filename));
	}*/
	
	public static BufferedReader getBufferedReader(String filename){
		InputStream in = ResourceLoader.class.getResourceAsStream(filename);
		//InputStream in = getAssets().open("YourTextFile.txt");
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		return input;
	}

	
}
