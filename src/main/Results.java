package main;

import java.util.ArrayList;
import java.util.List;

public abstract class Results
{
	//private double[][] output; // ANN current outputs
	//private double error; // current error

	private static List<Double> saveError = new ArrayList<>() ;	// record of the error that is saved in the output file
	private static List<Double> plotError = new ArrayList<>() ;	// record of the error that is displayed graphically on the screen
	
	public static void updatePlotError(double newError, int maxLength)
	{
		if (plotError.size() <= maxLength)
		{
			plotError.add(newError) ;
		}
		else
		{
			plotError.add(0, newError) ;
			plotError.remove(plotError.size() - 1) ;
		}
	}
	
	public static void recordSaveError(int currentIteration, int rate, double error)
	{
		if (currentIteration % rate == 0)
		{
			saveError.add(100 * error);
		}
	}
}
