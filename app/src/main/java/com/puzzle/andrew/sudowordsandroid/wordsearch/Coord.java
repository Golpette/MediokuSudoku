package com.puzzle.andrew.sudowordsandroid.wordsearch;

public class Coord {
	// Coordinate objects for storing snake letter positions
	
	public int xcoord;
	public int ycoord;
	
	
	public Coord(int xc, int yc){
		this.xcoord=xc;
		this.ycoord=yc;
	}
	
	public Coord( Coord c ){
		this.xcoord = c.getX();
		this.ycoord = c.getY();
	}
	

	// THE .contains() METHOD IMPLICITLY CALLS EQUALS FUNTCION
	// WHICH MUST BE OVERRIDDEN FOR NON-PRIMITIVE TYPES!
	@Override
	public boolean equals(Object o) 
	{
	    if (o instanceof Coord) 
	    {
	      Coord c = (Coord) o;
	      if ( this.xcoord==c.xcoord  &&  this.ycoord==c.ycoord ) //whatever here
	         return true;
	    }
	    return false;
	}
	
	

	public static Coord add(Coord c1, Coord c2){
		int x3 = c1.getX()+c2.getX();
		int y3 = c1.getY()+c2.getY();
		Coord c3 = new Coord( x3, y3 );
		return c3;
	}	
	
	public int getX(){
		return this.xcoord;
	}
	public int getY(){
		return this.ycoord;
	}

}
