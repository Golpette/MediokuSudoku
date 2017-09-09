package com.puzzle.andrew.sudowordsandroid.crossword;

//import java.awt.font.Image;
//import java.awt.Toolkit;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {

	static private Context context;

	public ResourceLoader(Context context){
		this.context = context;
	}

	/*public static Image getImage(String filename){
		
		return Toolkit.getDefaultToolkit().getImage(rl.getClass().getResource(filename));
	}*/
	
	public static BufferedReader getBufferedReader(String filename){
		//InputStream in = ResourceLoader.class.getResourceAsStream(filename);
		BufferedReader input = null;
		try {
			InputStream in = context.getAssets().open(filename);
			input = new BufferedReader(new InputStreamReader(in));
		}catch(IOException ex){
			//Do something
		}
		return input;
	}

	
}
