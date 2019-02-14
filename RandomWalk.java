import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.Point;

public class RandomWalk {

	private boolean done = false;
	private static int gridSize;
	
	private static int x;
	private static int y;
    private static Random random ;
    ArrayList<Point> path = new ArrayList<Point>();
    
	/**
	 * 
	 * method RandomWalk(int gridSize) is used to assign the user grid size 
	 * to the variable gridSize , generate a new random number with no seed and 
	 * place the initial points into the array list.
	 */
  	public RandomWalk(int gridSize)
  	{
  		random = new Random();
  		this.gridSize=gridSize;
  		
  		Point start = new Point(0,gridSize-1);
  		
  		x =start.x;
  	    y =start.y;
  	    
  	    path.add(start);
  	}
  	/**
	 * 
	 * method RandomWalk(int gridSize) is used to assign the user grid size 
	 * to the variable gridSize , generate a new random number with the given seed and 
	 * place the initial points into the array list.
	 */
  	public RandomWalk(int gridSize, long seed)
  	{
  	
  		random = new Random(seed);
  		this.gridSize=gridSize;

  		
  		Point start = new Point(0,gridSize-1);
  		
  		x =start.x;
  	    y =start.y;
  	    
  	    path.add(start);
  	}
  	
  	/**
  	 * method step() is designed to generate a new step within the range of the grid 
  	 * size and check to make sure it is not at the end of the greed. if it is at the end point
  	 * it is to set the boolean done to true and move on.
  	 */
  	public void step()
  	{
  		int t = random.nextInt(2);

  		if (t==0 && x!=(gridSize-1)){
  		
  				
  			x+=random.nextInt(2);
  				
  		}
  		else
  			if(y!=0)
  		{
 
  			y-=random.nextInt(2);
  			
  		}
  		
  			
  		Point p1 = new Point(x, y);
  		path.add(p1);
  		
  		if ((x==gridSize-1)&&(y==0))
  			done=true;
  		

  	}
  	/**
  	 * the method createWalk() is designed with a while loop to check the 
  	 * boolean done if it is not true then to run the set method again
  	 * if it is set to true then to end the loop.
  	 */
  	public void createWalk()
  	{
  		while (!isDone())
  		{
  			step();
  		}
  	}

  	/**
  	 * 
  	 * the method isDone() is the method to check to see if the program is done.
  	 */
  	public boolean isDone()
  	{
  		
		return done;
  		
  	}
  	/**
  	 * 
  	 * the method getPath() is the method to return the array list.
  	 */
  	public ArrayList<Point> getPath()
  	{
		return path;
  		
  	}
  	/**
  	 * the method toString() is used to give a string when called upon.
  	 */
  	public String toString()
  	{
  		String s="";
  		for(int i = 0; i<path.size();i++)
  		{
  			s+="["+path.get(i).x+","+path.get(i).y+"]"+" ";
  		}
		return s;
  		
  	}
  	

}