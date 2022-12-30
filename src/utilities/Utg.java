package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.json.simple.parser.JSONParser;

public abstract class Utg 
{
	
	public static void SaveTextFile(String filename, List<Double> Var)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter (filename + ".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 
			
			for (int i = 0; i <= Var.size() - 1; i += 1)
			{
				bufferedWriter.write(i + "	" + Var.get(i));
				bufferedWriter.newLine();	
			}			
			bufferedWriter.close();
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filename + "'");
        }
	}
	
	public static String[][] ReadcsvFile(String FileName)
	{
		BufferedReader br = null;
        String line = "";
        String separator = ",";
        List<List<String>> Input = new ArrayList<>();
        try 
        {
            br = new BufferedReader(new FileReader(FileName));
            line = br.readLine();
            while (line != null & !line.contains("_")) 
            {
            	Input.add(Arrays.asList(line.split(separator)));
            	line = br.readLine();
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        String[][] result = new String[Input.size()][];
        for (int i = 0; i <= result.length - 1; i += 1)
        {
        	result[i] = new String[Input.get(i).size()];
        	for (int j = 0; j <= result[i].length - 1; j += 1)
        	{
        		result[i][j] = Input.get(i).get(j);
        	}
        }
        return result;
	}
	
	public static Object ReadJson(String filePath)
	{
		JSONParser parser = new JSONParser();
        try
        {
            Object jsonData = parser.parse(new FileReader(filePath));
            return jsonData ;
        }
        catch(FileNotFoundException fe)
        {
            fe.printStackTrace();
            return null ;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null ;
        }
	}
	
	public static int[] CalcProdVec(int Nlayers, int[] Nneurons, double[][] target)
	{
		int[] multvec = new int[Nlayers];
		multvec[Nlayers - 1] = 1;
		for (int i = Nlayers - 1; 1 <= i; i += -1)
		{
			multvec[i - 1] = multvec[i]*Nneurons[i];
		}
		return multvec;
	}
	
	public static float Round(double num, int decimals)
	{
		return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).floatValue();
	}

	public static JButton addButton(ImageIcon icon, int[] alignment, Dimension size, Color color)
	{
		JButton NewButton = new JButton();
		NewButton.setIcon(icon);
		NewButton.setVerticalAlignment(alignment[0]);
		NewButton.setHorizontalAlignment(alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(size);	
		return NewButton;
	}
	
	public static Point OffsetFromPos(AlignmentPoints Alignment, int l, int h)
	{
		Point offset = new Point(0, 0) ;
		switch (Alignment)
		{
			case topLeft:
			{
				offset = new Point(0, 0) ;
				
				break ;
			}
			case centerLeft:
			{
				offset = new Point(0, -h / 2) ;
				
				break ;
			}
			case bottomLeft:
			{
				offset = new Point(0, -h) ;
				
				break ;
			}
			case topCenter:
			{
				offset = new Point(-l / 2, 0) ;
				
				break ;
			}
			case center:
			{
				offset = new Point(-l / 2, -h / 2) ;
				
				break ;
			}
			case bottomCenter:
			{
				offset = new Point(-l / 2, -h) ;
				
				break ;
			}
			case topRight:
			{
				offset = new Point(-l, 0) ;
				
				break ;
			}
			case centerRight:
			{
				offset = new Point(-l,  -h / 2) ;
				
				break ;
			}
			case bottomRight:
			{
				offset = new Point(-l,  -h) ;
				
				break ;
			}
		
		}
			
		return offset ;
	}

	public static int TextL(String text, Font font, Graphics graphics)
	{
		FontMetrics metrics = graphics.getFontMetrics(font) ;
		return (int) (metrics.stringWidth(text)) ;
	}
	
	public static int TextH(int TextSize)
	{
		return (int)(0.8*TextSize);
	}
	
	public static double[][] Transpose(double[][] OriginalArray)
	{
		double[][] NewArray = new double[OriginalArray[0].length][OriginalArray.length];
		for (int i = 0; i <= NewArray.length - 1; i += 1)
		{
			for (int j = 0; j <= NewArray[i].length - 1; j += 1)
			{
				NewArray[i][j] = OriginalArray[j][i];
			}
		}
		return NewArray;
	}

	public static double[] VecMatrixProd(double[] vector, double[][] matrix)
	{
		if (vector.length != matrix[0].length)
		{
			System.out.println("Attempted to multiply matrices of different sizes at UtilGeral -> MatrixProd");
			System.out.println("Vector size: " + vector.length + " Matrix size : " + matrix[0].length);
			return null;
		}
		else
		{
			double product[] = new double[matrix.length];
			for (int i = 0; i <= matrix.length - 1; i += 1) 
			{
				for (int j = 0; j <= vector.length - 1; j += 1) 
				{
					product[i] += vector[j] * matrix[i][j];
				}
			}		
			return product;
		}
	}
	
}
