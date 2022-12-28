package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
	
	public static float Round(float num, int decimals)
	{
		return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).floatValue();
	}
	
	public static float Round(double num, int decimals)
	{
		return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).floatValue();
	}
	
	public static int[][] AddElemToArrayUpTo(int[][] OriginalArray, int[] Elem, int maxlength)
	{
		if (OriginalArray == null)
		{
			return new int[][] {Elem};
		}
		else
		{
			if (OriginalArray.length < maxlength)
			{
			    int[][] NewArray = new int[OriginalArray.length + 1][];
			    for (int i = 0; i <= OriginalArray.length - 1; i += 1)
		        {
		            NewArray[i] = OriginalArray[i];
		        }
		        NewArray[OriginalArray.length] = Elem;
		        return NewArray;
			}
			else
			{
				int[][] NewArray = new int[OriginalArray.length][];
			    for (int i = 0; i <= OriginalArray.length - 2; i += 1)
		        {
		            NewArray[i] = OriginalArray[i + 1];
		        }
		        NewArray[OriginalArray.length - 1] = Elem;
		        return NewArray;
			}
		}
	}
	
	public static double[] AddElemToArrayUpTo(double[] OriginalArray, double Elem, int maxlength)
	{
		if (OriginalArray == null)
		{
			return new double[] {Elem};
		}
		else
		{
			if (OriginalArray.length < maxlength)
			{
			    double[] NewArray = new double[OriginalArray.length + 1];
			    for (int i = 0; i <= OriginalArray.length - 1; i += 1)
		        {
		            NewArray[i] = OriginalArray[i];
		        }
		        NewArray[OriginalArray.length] = Elem;
		        return NewArray;
			}
			else
			{
				double[] NewArray = new double[OriginalArray.length];
			    for (int i = 0; i <= OriginalArray.length - 2; i += 1)
		        {
		            NewArray[i] = OriginalArray[i + 1];
		        }
		        NewArray[OriginalArray.length - 1] = Elem;
		        return NewArray;
			}
		}
	}
		
	public static double[][] AddElemToArrayUpTo(double[][] OriginalArray, double[] Elem, int maxlength)
	{
		if (OriginalArray == null)
		{
			return new double[][] {Elem};
		}
		else
		{
			if (OriginalArray.length < maxlength)
			{
			    double[][] NewArray = new double[OriginalArray.length + 1][];
			    for (int i = 0; i <= OriginalArray.length - 1; i += 1)
		        {
		            NewArray[i] = OriginalArray[i];
		        }
		        NewArray[OriginalArray.length] = Elem;
		        return NewArray;
			}
			else
			{
				double[][] NewArray = new double[OriginalArray.length][];
			    for (int i = 0; i <= OriginalArray.length - 2; i += 1)
		        {
		            NewArray[i] = OriginalArray[i + 1];
		        }
		        NewArray[OriginalArray.length - 1] = Elem;
		        return NewArray;
			}
		}
	}
	
	public static JButton AddButton(ImageIcon Icon, int[] Alignment, int[] Size, Color color)
	{
		JButton NewButton = new JButton();
		NewButton.setIcon(Icon);
		NewButton.setVerticalAlignment(Alignment[0]);
		NewButton.setHorizontalAlignment(Alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(new Dimension(Size[0], Size[1]));	
		return NewButton;
	}
	
	public static JButton AddButton(String Text, int[] Alignment, int[] Size, Color color)
	{
		JButton NewButton = new JButton(Text);
		NewButton.setVerticalAlignment(Alignment[0]);
		NewButton.setHorizontalAlignment(Alignment[1]);
		NewButton.setBackground(color);
		NewButton.setPreferredSize(new Dimension(Size[0], Size[1]));	
		return NewButton;
	}

	public static void SaveTextFile(String filename, double[][] Var)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter (filename + ".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 
			
			// Section 1
			for (int i = 0; i <= Var.length - 1; i += 1)
			{
				for (int j = 0; j <= Var.length - 1; j += 1)
				{
					bufferedWriter.write(String.valueOf(Var[i][j]));
				}
				bufferedWriter.newLine();	
			}
			bufferedWriter.close();
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filename + "'");
        }
	}

	public static void SaveTextFile(String filename, double[][][] Var)
	{
		try
		{	
			FileWriter fileWriter = new FileWriter (filename + ".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter); 
			
			for (int l = 0; l <= Var.length - 1; l += 1)
			{
				bufferedWriter.write("layer " + l);
				bufferedWriter.newLine();
				for (int i = 0; i <= Var[l].length - 1; i += 1)
				{
					for (int j = 0; j <= Var[l][i].length - 1; j += 1)
					{
						bufferedWriter.write(String.valueOf(Var[l][i][j]) + " ");
					}
					bufferedWriter.newLine();	
				}
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}		
		catch(IOException ex) 
		{
            System.out.println("Error writing to file '" + filename + "'");
        }
	}
	
	public static int[] OffsetFromPos(String Alignment, int l, int h)
	{
		int[] offset = new int[2];
		if (Alignment.equals("TopLeft"))
		{
			offset[0] = 0;
			offset[1] = 0;
		}
		if (Alignment.equals("BotLeft"))
		{
			offset[0] = 0;
			offset[1] = -h;
		}
		if (Alignment.equals("TopCenter"))
		{
			offset[0] = -l/2;
			offset[1] = 0;
		}
		if (Alignment.equals("BotCenter"))
		{
			offset[0] = -l/2;
			offset[1] = -h;
		}
		if (Alignment.equals("Center"))
		{
			offset[0] = -l/2;
			offset[1] = -h/2;
		}
		if (Alignment.equals("LeftCenter"))
		{
			offset[0] = 0;
			offset[1] = -h/2;
		}
		if (Alignment.equals("RightCenter"))
		{
			offset[0] = -l;
			offset[1] = -h/2;
		}
		if (Alignment.equals("BotRight"))
		{
			offset[0] = -l;
			offset[1] = -h;
		}
		if (Alignment.equals("TopRight"))
		{
			offset[0] = -l;
			offset[1] = 0;
		}
		return offset;
	}

	public static int TextL(String Text, Font TextFont, int size, Graphics G)
	{
		FontMetrics metrics = G.getFontMetrics(TextFont);
		return (int)(metrics.stringWidth(Text)*0.05*size);
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
	
	
	
	
	
	
	/*
	public static Color[] ColorPalette(int Palette)
	{
		Color[] color = new Color[28];
		if (Palette == 0)
		{
			color[0] = Color.cyan;		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = Color.magenta;	// Mage
			color[2] = Color.orange;	// Archer
			color[3] = Color.green;		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = Color.gray;		// Thief, metal, rock, unavailable stuff, player att window 1
			color[5] = Color.blue;		// ocean, text
			color[6] = Color.red;		// berry, blood, selected bag item, life, level, crit
			color[7] = Color.white;		// intro windows, snow land, choices menu, ok button, player eye
			color[8] = Color.lightGray;	// Elemental NPC color, part of window gradient backgrounds
			color[9] = Color.black;		// Contour, attack animation
			color[10] = Color.yellow;	// Satiation, fire element
			color[11] = Color.pink;		// Petal
			color[12] = new Color(0, 0, 128);	// Player legs
			color[13] = new Color(128, 0, 0);	// Player shoes
			color[14] = new Color(128, 128, 128);	// Player shirt, player arm
			color[15] = new Color(255, 179, 128);	// Player head (skin)
			color[16] = new Color(0, 128, 0);	// Grass contour color, player hair
			color[17] = new Color(200, 50, 200);	// pet 1
			color[18] = new Color(200, 200, 30);	// pet 2 and 3, map, gold color, chest reward animation
			color[19] = new Color(100, 50, 0);	// Soil, wood, bag menu, player window, pterodactile
			color[20] = new Color(150, 100, 0);	// Sand, menu 0, menu 2
			color[21] = new Color(180, 0, 180);	// Herb, exp, magic floor, shopping text
			color[22] = new Color(230, 230, 230);	// Neutral, air, light and snow elements
			color[23] = new Color(0, 50, 0);	// Herb contour
			color[24] = new Color(0, 150, 0);	// Status 1 (poison)
			color[25] = new Color(30, 200, 30);	// Bag text
			color[26] = new Color(0, 150, 255);	// Shopping menu
			color[27] = new Color(200, 200, 255);	// Sign window
		}
		else if (Palette == 1)
		{
			color[0] = new Color(27, 224, 233);		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = new Color(230, 46, 150);	// Mage
			color[2] = new Color(244, 162, 25);	// Archer
			color[3] = new Color(29, 237, 29);		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = new Color(109, 103, 97);		// Thief, metal, rock, unavailable stuff, player att window 1
			color[5] = new Color(63, 40, 231);		// ocean, text
			color[6] = new Color(236, 44, 44);		// berry, blood, selected bag item, life, level, crit
			color[7] = new Color(241, 233, 225);		// intro windows, snow land, choices menu, ok button, player eye
			color[8] = new Color(203, 195, 188);	// Elemental NPC color, part of window gradient backgrounds
			color[9] = new Color(11, 11, 11);		// Contour, attack animation
			color[10] = new Color(234, 237, 27);	// Satiation, fire element
			color[11] = new Color(232, 93, 143);		// Petal
			color[12] = new Color(78, 36, 193);	// Player legs
			color[13] = new Color(167, 28, 70);	// Player shoes
			color[14] = new Color(109, 103, 97);	// Player shirt, player arm
			color[15] = new Color(233, 158, 125);	// Player head (skin)
			color[16] = new Color(46, 192, 81);	// Grass contour color, player hair
			color[17] = new Color(194, 35, 144);	// pet 1
			color[18] = new Color(127, 192, 42);	// pet 2 and 3, map, gold color, chest reward animation
			color[19] = new Color(101, 61, 44);	// Soil, wood, bag menu, player window, pterodactile
			color[20] = new Color(143, 90, 52);	// Sand, menu 0, menu 2
			color[21] = new Color(155, 33, 128);	// Herb, exp, magic floor, shopping text
			color[22] = new Color(137, 249, 204);	// Neutral, air, light and snow elements
			color[23] = new Color(41, 88, 66);	// Herb contour
			color[24] = new Color(46, 192, 81);	// Status 1 (poison)
			color[25] = new Color(174, 242, 94);	// Bag text
			color[26] = new Color(34, 82, 194);	// Shopping menu
			color[27] = new Color(91, 247, 217);	// Sign window
		}
		else if (Palette == 2)
		{
			color[0] = new Color(61, 240, 226);		// Sky, crystal, menu 1, menu ornaments, player window, quest requirements, bestiary windows, knight
			color[1] = new Color(235, 38, 226);	// Mage, petal, pet 1, herb, exp, magic floor, shopping text
			color[2] = new Color(234, 237, 27);	// Archer, satiation, fire element
			color[3] = new Color(122, 236, 67);		// Animal, pet 0, grass, plant element, selected text, poison
			color[4] = new Color(164, 155, 147);		// Thief, metal, rock, wall, unavailable stuff, sign window, player att window 1
			color[5] = new Color(63, 40, 231);		// ocean, text
			color[6] = new Color(236, 44, 44);		// berry, blood, selected bag item, life, level, crit
			color[7] = new Color(241, 233, 225);		// Elemental NPC color, neutral and light elements, intro windows, choices menu, ok button, player eye
			color[8] = new Color(137, 249, 204);	// snow land, air and snow elements, part of window gradient backgrounds
			color[9] = new Color(11, 11, 11);		// Contour, attack animation
			color[10] = new Color(167, 28, 70);	// Lava, volcano
			color[11] = new Color(228, 89, 80); 
			color[12] = new Color(245, 117, 170);	// Player legs, herb contour
			color[13] = new Color(43, 45, 99);	// Player shoes
			color[14] = new Color(28, 162, 208);	// Player shirt, player arm
			color[15] = new Color(219, 251, 137);	// Player head (skin)
			color[16] = new Color(41, 88, 66);	// grass contour color, player hair
			color[17] = new Color(241, 199, 128);
			color[18] = new Color(171, 195, 49);	// pet 2 and 3, map, gold color, shopping menu, chest reward animation
			color[19] = new Color(143, 90, 52);	// Soil, wood, bag menu, stalactite, player window, pterodactile
			color[20] = new Color(242, 176, 63);	// Sand, menu 0, menu 2
			color[21] = new Color(247, 224, 143);
			color[22] = new Color(101, 131, 246);
			color[23] = new Color(141, 31, 159);	// Bag, shopping and crafting items text
			color[24] = new Color(48, 99, 97);	// 
			color[25] = new Color(76, 131, 42);	// 
			color[26] = new Color(242, 91, 168);	// 
			color[27] = new Color(105, 50, 50);	// 
		}
		return color;
	}
	
	public static String[][] ReadTextFile(String fileName)
	{
		String[][] Text = null; // [Cat][Pos]
		int cat = -1;
		try
		{	
			FileInputStream fileReader = new FileInputStream (fileName);
			InputStreamReader streamReader = new InputStreamReader(fileReader, StandardCharsets.UTF_8);
			BufferedReader bufferedReader = new BufferedReader(streamReader); 					
			String Line = bufferedReader.readLine();
			while (!Line.equals("_"))
			{
				if (Line.contains("*"))
				{
					cat += 1;
					Text = IncreaseArraySize(Text, 1);
	            }
				Text[cat] = AddElem(Text[cat], Line);
				Line = bufferedReader.readLine();
			}
			bufferedReader.close();
		}
		catch(FileNotFoundException ex) 
		{
            System.out.println("Unable to find file '" + fileName + "' (text file)");                
        }		
		catch(IOException ex) 
		{
            System.out.println("Error reading file '" + fileName + "' (text file)");                  
        }
		return Text;
	}
	
	public static String[][] ReadcsvFile(String FileName)
	{
		BufferedReader br = null;
        String line = "";
        String separator = ",";
        String[][] Input = null;
        try 
        {
            br = new BufferedReader(new FileReader(FileName));
            line = br.readLine();
            while (line != null & !line.contains("_")) 
            {
            	Input = AddElem(Input, line.split(separator));
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
        return Input;
	}
	
	public static String[][] IncreaseArraySize(String[][] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new String[size][];
		}
		else
		{
			String[][] NewArray = new String[OriginalArray.length + size][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			return NewArray;
		}
	}
	
	public static String[] AddElem(String[] OriginalArray, String NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[] {NewElem};
		}
		else
		{
			String[] NewArray = new String[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	
	public static double[] AddElem(double[] OriginalArray, double NewElem)
	{
		if (OriginalArray == null)
		{
			return new double[] {NewElem};
		}
		else
		{
			double[] NewArray = new double[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	
	public static String[][] AddElem(String[][] OriginalArray, String[] NewElem)
	{
		if (OriginalArray == null)
		{
			return new String[][] {NewElem};
		}
		else
		{
			String[][] NewArray = new String[OriginalArray.length + 1][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}

	public static int FindMin(int[] Vector)
	{
		int Min = Vector[0];
		for (int s = 1; s <= Vector.length - 1; s += 1)
        {
			if (Vector[s] < Min)
			{
				Min = Vector[s];
			}
        }		
		return Min;
	}
	
	public static double FindMin(double[] Vector)
	{
		double Min = Vector[0];
		for (int s = 1; s <= Vector.length - 1; s += 1)
        {
			if (Vector[s] < Min)
			{
				Min = Vector[s];
			}
        }		
		return Min;
	}
	
	public static int FindMax(int[] Vector)
	{
		int Max = Vector[0];
		for (int s = 1; s <= Vector.length - 1; s += 1)
        {
			if (Max < Vector[s])
			{
				Max = Vector[s];
			}
        }		
		return Max;
	}
	
	public static double FindMax(double[] Vector)
	{
		double Max = Vector[0];
		for (int s = 1; s <= Vector.length - 1; s += 1)
        {
			if (Max < Vector[s])
			{
				Max = Vector[s];
			}
        }		
		return Max;
	}

	public static double FindMax(double[][][] Array)
	{
		double Max = Array[0][0][0];
		for (int i = 0; i <= Array.length - 1; i += 1)
        {
			for (int j = 0; j <= Array[i].length - 1; j += 1)
	        {
				for (int k = 0; k <= Array[i][j].length - 1; k += 1)
		        {
					if (Max < Math.abs(Array[i][j][k]))
					{
						Max = Math.abs(Array[i][j][k]);
					}
		        }
	        }
        }		
		return Max;
	}
	
	public static int FindMinIndex(double[] Vector)
	{
		int MinId = -1;
		double Min = Vector[0];
		for (int s = 0; s <= Vector.length - 1; s += 1)
        {
			if (Vector[s] <= Min)
			{
				MinId = s;
				Min = Vector[s];
			}
        }
		return MinId;
	}
	
	public static double[][][] AddMatrix(double[][][] Array1, double[][][] Array2)
	{
		if (Array1 == null)
		{
			return Array2;
		}
		else if (Array2 == null)
		{
			return Array1;
		}
		else
		{
			double[][][] NewArray = new double[Array1.length][][];
			for (int i = 0; i <= Array1.length - 1; i += 1)
			{
				NewArray[i] = new double[Array1[i].length][];
				for (int j = 0; j <= Array1[i].length - 1; j += 1)
				{
					NewArray[i][j] = new double[Array1[i][j].length];
					for (int k = 0; k <= Array1[i][j].length - 1; k += 1)
					{
						NewArray[i][j][k] = Array1[i][j][k] + Array2[i][j][k];
					}	
				}
			}
			return NewArray;
		}
	}
		
	public static double Average(double[] values)
	{
		double avr = 0;
		for (int i = 0; i <= values.length - 1; i += 1)
		{
			avr += values[i];
		}
		avr = avr / (double)values.length;
		return avr;
	}
	
	public static double Prod(double[] vec, int id1, int id2)
	{
		double prod = 1;
		for (int i = id1; i <= id2 - 1; i += 1)
		{
			prod = prod * vec[i];
		}
		return prod;
	}
	
	public static int Prod(int[] vec, int id1, int id2)
	{
		int prod = 1;
		for (int i = id1; i <= id2 - 1; i += 1)
		{
			prod = prod * vec[i];
		}
		return prod;
	}
	
	public static double sig(double x)
	{
		return 1.0 / (1.0 + Math.exp(-x));
	}

	public static double dSig(double x)
	{
		return Math.exp(-x) / Math.pow((1.0 + Math.exp(-x)), 2);
	}
	
	public static double dotProd(double[] v1, double[] v2)
	{
		double sum = 0;
		if (v1.length != v2.length)
		{
			System.out.println("Vector product attempted with vectors of different lengths -> UtilGeral -> SumProd");
			return -9999;
		}
		else
		{
			for (int i = 0; i <= v1.length - 1; i += 1)
			{
				sum += v1[i]*v2[i];
			}
			return sum;
		}
	}	


	public static Neuron[] AddElem(Neuron[] OriginalArray, Neuron NewElem)
	{
		if (OriginalArray == null)
		{
			return new Neuron[] {NewElem};
		}
		else
		{
			Neuron[] NewArray = new Neuron[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
	
	public static double[] ScaledVector(double[] vector, double range, double amp)
	{
		double min = 0;
		double max = 1;
		double[] ScaledVector = new double[vector.length];
		for (int i = 0; i <= vector.length - 1; i += 1)
		{
			ScaledVector[i] = range + amp * (vector[i] - min) / (max - min);
		}
		return ScaledVector;
	}
	

	
	public static double[][] MatrixProd(double[][] matrixA, double[][] matrixB)
	{
		if (matrixA.length != matrixB[0].length)
		{
			System.out.println("Attempted to multiply matrices of different sizes at UtilGeral -> MatrixProd");
			System.out.println("MatrixA size: " + matrixA.length + " MatrixB size : " + matrixB[0].length);
			return null;
		}
		else
		{
			int size = matrixA.length;
			double product[][] = new double[size][size];
			for (int i = 0; i <= size - 1; i += 1) 
			{
				for (int j = 0; j <= size - 1; j += 1) 
				{
					product[i][j] = 0;
					for (int k = 0; k <= size - 1; k += 1) 
					{
						product[i][j] += matrixA[i][k] * matrixB[k][j];
					}
				}
			}		
			return product;
		}
	}
	
	public static double MaxAbs(double[] vector)
	{
		double max = 0;
		for (int i = 0; i <= vector.length - 1; i += 1)
		{
			if (max < vector[i])
			{
				max = vector[i];
			}			
		}
		
		return max;
	}
	
	public float RandomMult(float amplitude)
	{
		return (float)(Math.max(0, 1 - amplitude + 2*amplitude*Math.random()));
	}
	
	public boolean FindFile(String FileName)
	{
		BufferedReader br = null;
		try 
        {
            br = new BufferedReader(new FileReader(FileName));
        } 
        catch (FileNotFoundException e) 
        {
            return false;
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
		return true;
	}
	
	public int[][] IncreaseArraySize(int[][] OriginalArray, int size)
	{
		if (OriginalArray == null)
		{
			return new int[size][];
		}
		else
		{
			int[][] NewArray = new int[OriginalArray.length + size][];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			return NewArray;
		}
	}
	
	public Clip MusicFileToClip(File MusicFile)
	{
		Clip MusicClip = null;
		try 
 		{
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(MusicFile);
	        MusicClip = AudioSystem.getClip();
	        MusicClip.open(audioInputStream);
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error loading clip.");
 	        ex.printStackTrace();
 	    }
		
		return MusicClip;
	}
	
	public int[] ArrayWithValuesGreaterThan(int[] OriginalArray, int MinValue)
	{
		int NewArrayLength = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				++NewArrayLength;
			}
		}
		int[] NewArray = new int[NewArrayLength];
		int cont = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				NewArray[cont] = OriginalArray[i];
				++cont;
			}
		}
		return NewArray;
	}
	
	public int[] ArrayWithIndexesGreaterThan(int[] OriginalArray, int MinValue)
	{
		int NewArrayLength = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				++NewArrayLength;
			}
		}
		int[] NewArray = new int[NewArrayLength];
		int cont = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (MinValue < OriginalArray[i])
			{
				NewArray[cont] = i;
				++cont;
			}
		}
		return NewArray;
	}
	
	public int[][] ArrayWithFirstTermEqualTo(int FirstTerm, int[] OriginalFirstTerms, int[][] OriginalArray)
	{
		int NewArrayLength = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (OriginalFirstTerms[i] == (FirstTerm))
			{
				++NewArrayLength;
			}
		}
		int[][] NewArray = new int[NewArrayLength][];
		int cont = 0;
		for (int i = 0; i <= OriginalArray.length - 1; ++i)
		{
			if (OriginalFirstTerms[i] == FirstTerm)
			{
				NewArray[cont] = OriginalArray[i];
				++cont;
			}
		}
		return NewArray;
	}

	public int[] AddElem(int[] OriginalArray, int NewElem)
	{
		if (OriginalArray == null)
		{
			return new int[] {NewElem};
		}
		else
		{
			int[] NewArray = new int[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}

	public Color[] AddElem(Color[] OriginalArray, Color NewElem)
	{
		if (OriginalArray == null)
		{
			return new Color[] {NewElem};
		}
		else
		{
			Color[] NewArray = new Color[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}

	public int[] FindAllTextCat(String[][] AllText, String Language)
	{
		int[] AllTextCats = new int[AllText.length];
		String[] Cats = new String[63];
		if (Language.equals("P"))
		{
			Cats[0] = "* *";
			Cats[1] = "* Besti�rio *";
			Cats[2] = "* Novo jogo *";
			Cats[3] = "* Tutorial *";
			Cats[4] = "* Classes *";
			Cats[5] = "* ProClasses *";
			Cats[6] = "* Atributos *";
			Cats[7] = "* Estat�sticas do jogador *";
			Cats[8] = "* Propriedades dos atributos especiais *";
			Cats[9] = "* Coleta *";
			Cats[10] = "* Vit�ria *";
			Cats[11] = "* Equipamentos *";
			Cats[12] = "* Doutor *";
			Cats[13] = "* Vendedor de equipamentos *";
			Cats[14] = "* Vendedor de itens *";
			Cats[15] = "* Contrabandista *";
			Cats[16] = "* Banqueiro *";
			Cats[17] = "* Alquimista *";
			Cats[18] = "* Madeireiro *";
			Cats[19] = "* Mestre *";
			Cats[20] = "* Quest *";
			Cats[21] = "* Forjador *";
			Cats[22] = "* Fabricante *";
			Cats[23] = "* Salvador *";
			Cats[24] = "* Elemental *";
			Cats[25] = "* Navegador 1*";
			Cats[26] = "* Quest exp *";
			Cats[27] = "* Quest items *";
			Cats[28] = "* Partes do corpo *";
			Cats[29] = "* Tesouros *";
			Cats[30] = "* Menus da mochila *";
			Cats[31] = "* Pterodactile *";
			Cats[32] = "* Nomes dos continentes *";
			Cats[33] = "* Mensagem das placas *";
			Cats[34] = "* Menu de personaliza��o *";
			Cats[35] = "* Menu de quest *";
			Cats[36] = "* Menu de op��es *";
			Cats[37] = "* Menu de dicas *";
			Cats[38] = "* Dimens�es *";
			Cats[39] = "* Cores *";
			Cats[40] = "* Janela do jogador *";
			Cats[41] = "* Cidad�o 0 *";
			Cats[42] = "* Cidad�o 1 *";
			Cats[43] = "* Cidad�o 2 *";
			Cats[44] = "* Cidad�o 3 *";
			Cats[45] = "* Cidad�o 4 *";
			Cats[46] = "* Cidad�o 5 *";
			Cats[47] = "* Cidad�o 6 *";
			Cats[48] = "* Cidad�o 7 *";
			Cats[49] = "* Cidad�o 8 *";
			Cats[50] = "* Cidad�o 9 *";
			Cats[51] = "* Cidad�o 10 *";
			Cats[52] = "* Cidad�o 11 *";
			Cats[53] = "* Cidad�o 12 *";
			Cats[54] = "* Cidad�o 13 *";
			Cats[55] = "* Cidad�o 14 *";
			Cats[56] = "* Cidad�o 15 *";
			Cats[57] = "* Cidad�o 16 *";
			Cats[58] = "* Cidad�o 17 *";
			Cats[59] = "* Cidad�o 18 *";
			Cats[60] = "* Cidad�o 19 *";
			Cats[61] = "* Barra de habilidades *";
			Cats[62] = "* Navegador 2*";
			for (int i = 0; i <= AllText.length - 1; i += 1)
			{
				AllTextCats[i] = FindTextPos(AllText, Cats[i]);
			}
			return AllTextCats;
		}
		else if (Language.equals("E"))
		{
			Cats[0] = "* *";
			Cats[1] = "* Bestiary *";
			Cats[2] = "* New game *";
			Cats[3] = "* Tutorial *";
			Cats[4] = "* Classes *";
			Cats[5] = "* ProClasses *";
			Cats[6] = "* Attributes *";
			Cats[7] = "* Player stats *";
			Cats[8] = "* Properties of special attributes *";
			Cats[9] = "* Collect *";
			Cats[10] = "* Victory *";
			Cats[11] = "* Equipment *";
			Cats[12] = "* Doctor *";
			Cats[13] = "* Equipment seller *";
			Cats[14] = "* Item seller *";
			Cats[15] = "* Smuggler *";
			Cats[16] = "* Banker *";
			Cats[17] = "* Alchemist *";
			Cats[18] = "* Lumberjack *";
			Cats[19] = "* Master *";
			Cats[20] = "* Quest *";
			Cats[21] = "* Forger *";
			Cats[22] = "* Manufacturer *";
			Cats[23] = "* Savior *";
			Cats[24] = "* Elemental *";
			Cats[25] = "* Sailor 1*";
			Cats[26] = "* Quest exp *";
			Cats[27] = "* Quest items *";
			Cats[28] = "* Body parts *";
			Cats[29] = "* Treasures *";
			Cats[30] = "* Backpack menus *";
			Cats[31] = "* Pterodactile *";
			Cats[32] = "* Names of continents *";
			Cats[33] = "* Message from the plates *";
			Cats[34] = "* Customization menu *";
			Cats[35] = "* Quest menu *";
			Cats[36] = "* Options menu *";
			Cats[37] = "* Tip menu *";
			Cats[38] = "* Dimensions *";
			Cats[39] = "* Colors *";
			Cats[40] = "* player window *";
			Cats[41] = "* Citizen 0 *";
			Cats[42] = "* Citizen 1 *";
			Cats[43] = "* Citizen 2 *";
			Cats[44] = "* Citizen 3 *";
			Cats[45] = "* Citizen 4 *";
			Cats[46] = "* Citizen 5 *";
			Cats[47] = "* Citizen 6 *";
			Cats[48] = "* Citizen 7 *";
			Cats[49] = "* Citizen 8 *";
			Cats[50] = "* Citizen 9 *";
			Cats[51] = "* Citizen 10 *";
			Cats[52] = "* Citizen 11 *";
			Cats[53] = "* Citizen 12 *";
			Cats[54] = "* Citizen 13 *";
			Cats[55] = "* Citizen 14 *";
			Cats[56] = "* Citizen 15 *";
			Cats[57] = "* Citizen 16 *";
			Cats[58] = "* Citizen 17 *";
			Cats[59] = "* Citizen 18 *";
			Cats[60] = "* Citizen 19 *";
			Cats[61] = "* Skills bar *";
			Cats[62] = "* Sailor 2*";
			for (int i = 0; i <= AllText.length - 1; i += 1)
			{
				AllTextCats[i] = FindTextPos(AllText, Cats[i]);
			}
			return AllTextCats;
		}
		return null;	
	}
	
	public boolean IsAlphaNumeric(String input)
	{
		if(input != null)
		{
			if (input.equals("A") | input.equals("B")  | input.equals("C")  | input.equals("D") | input.equals("E") | input.equals("F") | input.equals("G") | input.equals("H") | input.equals("I") | input.equals("J") | input.equals("K") | input.equals("L") | input.equals("M") | input.equals("N") | input.equals("O") | input.equals("P") | input.equals("Q") | input.equals("R") | input.equals("S") | input.equals("T") | input.equals("U") | input.equals("V") | input.equals("W") | input.equals("X") | input.equals("Y") | input.equals("Z") | input.equals("0") | input.equals("1") | input.equals("2") | input.equals("3") | input.equals("4") | input.equals("5") | input.equals("6") | input.equals("7") | input.equals("8") | input.equals("9"))
			{
				return true;
			}
		}	
		return false;
	}

	public String[] FitText(String inputstring, int NumberOfChars)
	{
		String[] newstring = new String[inputstring.length()];
		int CharsExeeding = 0;		
		int i = 0;
		int FirstChar = 0;
		int LastChar = 0;
		do
		{
			FirstChar = i*NumberOfChars - CharsExeeding;
			LastChar = FirstChar + Math.min(NumberOfChars, Math.min((i + 1)*NumberOfChars, inputstring.length() - i*NumberOfChars) + CharsExeeding);
			char[] chararray = new char[NumberOfChars];
			inputstring.getChars(FirstChar, LastChar, chararray, 0);
			if (chararray[LastChar - FirstChar - 1] != ' ' & chararray[LastChar - FirstChar - 1] != '.' & chararray[LastChar - FirstChar - 1] != '?' & chararray[LastChar - FirstChar - 1] != '!' & chararray[LastChar - FirstChar - 1] != '/' & chararray[LastChar - FirstChar - 1] != ':')
			{
				for (int j = chararray.length - 1; 0 <= j; j += -1)
				{
					CharsExeeding += 1;
					LastChar += -1;
					if (chararray[j] == ' ' | chararray[j] == '.' | chararray[j] == '?' | chararray[j] == '!' | chararray[j] == '/' | chararray[j] == ':')
					{
						char[] chararray2 = new char[NumberOfChars];
						inputstring.getChars(Math.min(Math.max(0, FirstChar), inputstring.length()), LastChar, chararray2, 0);
						newstring[i] = String.valueOf(chararray2);
						CharsExeeding += -1;
						j = 0;
					}
				}
			}
			else
			{
				newstring[i] = String.valueOf(chararray);
			}
			i += 1;
		} while(LastChar != inputstring.length() & i != inputstring.length());		
		String[] newstring2 = new String[i];
		for (int j = 0; j <= newstring2.length - 1; j += 1)
		{
			newstring2[j] = newstring[j];
		}
		return newstring2;
	}

	public int IndexOf(int[] Array, int Value)
	{
		if (Array != null)
		{
			for (int i = 0; i <= Array.length - 1; i += 1)
			{
				if (Value == Array[i])
				{
					return i;
				}
			}
		}
		return -1;
	}

	public static int IndexOf(String[] Vector, String Value)
	{
		if (Vector != null)
		{
			for (int i = 0; i <= Vector.length - 1; ++i)
			{
				if (Vector[i].equals(Value))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public int FindTextPos(String[][] AllText, String Category)
	{
		if (AllText != null)
		{
			for (int i = 0; i <= AllText.length - 1; i += 1)
			{
				if (Category.equals(AllText[i][0]))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	public String[] RemoveString(String[] input)
	{
		String[] result = new String[input.length - 1];
		for (int i = 0; i <= input.length - 2; ++i)
		{
			result[i] = input[i];
		}
		return result;
	}
	
	public String[] AddString(String[] input, String NewMember)
	{
		String[] result = new String[input.length + 1];
		for (int i = 0; i <= input.length - 1; ++i)
		{
			result[i] = input[i];
		}
		result[input.length] = NewMember;
		return result;
	}
	
	public int[] AddVectorElem(int[] OriginalArray, int NewElem)
	{
		if (OriginalArray == null)
		{
			return new int[] {NewElem};
		}
		else
		{
			int[] NewArray = new int[OriginalArray.length + 1];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				NewArray[i] = OriginalArray[i];
			}
			NewArray[OriginalArray.length] = NewElem;
			return NewArray;
		}
	}
		
	public int[][] RotateMatrix(float[][] A, int[][] B)
	{
		int[][] C = new int[A.length][B[0].length];
		for (int i = 0; i <= A.length - 1; ++i)				// Rows
        { 
            for (int j = 0; j <= B[0].length - 1; ++j)		// Columns
            {
                for (int k = 0; k <= A[0].length - 1; ++k)	// Columns 
                {
                    C[i][j] += A[i][k]*B[k][j];
                }
            }
        }		
		return C;
	}
	
	public boolean ArrayContains(int[] Array, int value)
	{
		if (Array != null)
		{
			for (int i = 0; i <= Array.length - 1; i += 1)
			{
				if (Array[i] == value)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public int RandomCoord1D(int size, float MinCoord, float Range, int step)
	{
		return (int)(size*(Range*Math.random() + MinCoord)/step)*step;
	}
	
	public int[] RandomPos(int[] size, float[] MinCoord, float[] Range, int[] step)
	{
		return new int[] {(int)(size[0]*(Range[0]*Math.random() + MinCoord[0])/step[0])*step[0], (int)(size[1]*(Range[1]*Math.random() + MinCoord[1])/step[1])*step[1]};
	}
	
	public void PlayMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.loop(999);
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with playing sound.");
 	        ex.printStackTrace();
 	    }
 	}
 	
 	public void StopMusic(Clip MusicFile)
 	{
 		try 
 		{
	        MusicFile.stop();
 	    } 
 		catch(Exception ex) 
 		{
 	        System.out.println("Error with playing sound.");
 	        ex.printStackTrace();
 	    }
 	}
 	
 	public void SwitchMusic(Clip MusicFile1, Clip MusicFile2)
 	{
 		StopMusic(MusicFile1);
		PlayMusic(MusicFile2);
 	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

	public static Color GetPixelColor(BufferedImage Bufferedimage, int[] Pos)
	{
		int clr = Bufferedimage.getRGB(Pos[0], Pos[1]); 
		int red = (clr & 0x00ff0000) >> 16;
		int green = (clr & 0x0000ff00) >> 8;
		int blue = clr & 0x000000ff;
		return new Color(red, green, blue);
	}
	
	public Image SetPixelColor(Image File, int[] Pos, Color color)
	{
		BufferedImage BufferedFile = toBufferedImage(File);
		BufferedFile.setRGB(Pos[0], Pos[1], color.getRGB());
		return BufferedFile;
	}
	
	public Image ChangeImageColor(Image image, float[] area, Color newColor, Color OriginalColor)
	{
		int l = (int)(image.getWidth(null)), h = (int)(image.getHeight(null));
		BufferedImage BufferedFile = toBufferedImage(image);
		for (int i = (int)(area[0]*l); i <= (int)(area[2]*l) - 1; i += 1)
		{
			for (int j = (int)(area[1]*h); j <= (int)(area[3]*h) - 1; j += 1)
			{
				Color PreviousColor = GetPixelColor(BufferedFile, new int[] {i, j});
				if (PreviousColor.getRed() == OriginalColor.getRed() & PreviousColor.getGreen() == OriginalColor.getGreen() & PreviousColor.getBlue() == OriginalColor.getBlue())
				{	
					BufferedFile.setRGB(i, j, newColor.getRGB());
				}	
			}
		}
		
		Image A = BufferedFile;
		return A;
	}
	
	public float dist(int[] PosA, int[] PosB)
	{
		return (float)(Math.sqrt(Math.pow(PosB[0] - PosA[0], 2) + Math.pow(PosB[1] - PosA[1], 2)));
	}
	
	public boolean MouseIsInside(int[] MousePos, int[] RectPos, int L, int H)
	{
		if (RectPos[0] <= MousePos[0] & MousePos[1] <= RectPos[1] & MousePos[0] <= RectPos[0] + L & RectPos[1] - H <= MousePos[1])
		{
			return true;
		} 
		else
		{
			return false;
		}
	}
		
	public boolean isNumeric(String str) 
	{ 
	  try 
	  {  
	    Double.parseDouble(str);  
	    return true;
	  } catch(NumberFormatException e)
	  {  
	    return false;  
	  }  
	}
	
	public Object ConvertArray(Object[] OriginalArray, String From, String To)
	{
		if (OriginalArray != null)
		{
			if (!OriginalArray[0].equals("null"))
			{
				if (From.equals("String") & To.equals("int"))
				{
					int[] IntArray = new int[OriginalArray.length];
					for (int i = 0; i <= OriginalArray.length - 1; i += 1)
					{
						IntArray[i] = Integer.parseInt((String) OriginalArray[i]);
					}
					return IntArray;
				}
				else if (From.equals("String") & To.equals("float"))
				{
					float[] FloatArray = new float[OriginalArray.length];
					for (int i = 0; i <= OriginalArray.length - 1; i += 1)
					{
						FloatArray[i] = Float.parseFloat((String) OriginalArray[i]);
					}
					return FloatArray;
				}
				else if (From.equals("String") & To.equals("boolean"))
				{
					boolean[] BoolArray = new boolean[OriginalArray.length];
					for (int i = 0; i <= OriginalArray.length - 1; i += 1)
					{
						BoolArray[i] = ((String) OriginalArray[i]).equals("true");
					}
					return BoolArray;
				}
				else
				{
					return OriginalArray;
				}
			}
			else
			{
				return OriginalArray;
			}
		}
		else
		{
			return OriginalArray;
		}
	}
	
	public Object ConvertDoubleArray(Object[][] OriginalArray, String From, String To)
	{
		if (From.equals("String") & To.equals("int"))
		{
			int[][] IntArray = new int[OriginalArray.length][OriginalArray[0].length];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				for (int j = 0; j <= OriginalArray[i].length - 1; j += 1)
				{
					IntArray[i][j] = Integer.parseInt((String) OriginalArray[i][j]);
				}
			}
			return IntArray;
		}
		else if (From.equals("String") & To.equals("float"))
		{
			float[][] FloatArray = new float[OriginalArray.length][OriginalArray[0].length];
			for (int i = 0; i <= OriginalArray.length - 1; i += 1)
			{
				for (int j = 0; j <= OriginalArray[i].length - 1; j += 1)
				{
					FloatArray[i][j] = Float.parseFloat((String) OriginalArray[i][j]);
				}
			}
			return FloatArray;
		}
		else
		{
			return OriginalArray;
		}
	}
	
	public String[] toString(String[] str)
	{
		if (str != null)
		{
			String[] newstr = new String[str.length];
			for (int i = 0; i <= str.length - 1; ++i)
			{
				str[i] = str[i].replace(" ", "");
				if (i == 0)
				{
					str[i] = str[i].replace("[", "");
				}
				if (i == str.length - 1)
				{
					str[i] = str[i].replace("]", "");
				}
				newstr[i] = str[i];
			}
			return newstr;
		}
		else
		{
			return null;
		}
	}
	
	public String[][] deepToString(String[] str, int NumberOfColumns)
	{
		int NumberOfRows = str.length/NumberOfColumns;
		String[][] newstr = new String[NumberOfRows][NumberOfColumns];
		for (int i = 0; i <= NumberOfRows - 1; i += 1)
		{
			for (int j = 0; j <= NumberOfColumns - 1; j += 1)
			{
				String a = str[NumberOfColumns*i + j].replace(" ", "");
				a = a.replace("[", "");
				a = a.replace("]", "");
				a = a.replace(",", "");
				newstr[i][j] = a;
			}
		}
		return newstr;
	}
	
	public Color[] toColor(String[] StrReader)
	{
		int cont = 0;
		int red = 0, green = 0, blue = 0;
		Color[] NewColor = new Color[StrReader.length/3];
		for (int i = 0; i <= StrReader.length - 1; i += 1)
		{
			StrReader[i] = StrReader[i].replace(" ", "");
			StrReader[i] = StrReader[i].replace("java.awt.Color", "");
			StrReader[i] = StrReader[i].replace("[", "");
			StrReader[i] = StrReader[i].replace("]", "");
			if (StrReader[i].contains("r="))
			{
				StrReader[i] = StrReader[i].replace("r=", "");
				red = Integer.parseInt(StrReader[i]);
			}
			if (StrReader[i].contains("g="))
			{
				StrReader[i] = StrReader[i].replace("g=", "");
				green = Integer.parseInt(StrReader[i]);
			}
			if (StrReader[i].contains("b="))
			{
				StrReader[i] = StrReader[i].replace("b=", "");
				blue = Integer.parseInt(StrReader[i]);
				NewColor[cont] = new Color(red, green, blue);
				++cont;
			}
		}
		return NewColor;
	}

	public static void PrintVector(int[] Vector)
	{
		System.out.println("Vector");
		for (int i = 0; i <= Vector.length - 1; i += 1)
		{
			System.out.println(Vector[i]);
		}
		System.out.println();
	}
	
	public static void PrintVector(double[] Vector)
	{
		System.out.println("Vector");
		for (int i = 0; i <= Vector.length - 1; i += 1)
		{
			System.out.println(Vector[i]);
		}
		System.out.println();
	}
	
	public static void PrintArray(int[][] Array)
	{
		System.out.println("Array");
		for (int i = 0; i <= Array.length - 1; i += 1)
		{
			System.out.println(Arrays.toString(Array[i]));
		}
		System.out.println();
	}

	public static void PrintArray(double[][] Array)
	{
		System.out.println("Array");
		for (int i = 0; i <= Array.length - 1; i += 1)
		{
			System.out.println(Arrays.toString(Array[i]));
		}
		System.out.println();
	}
*/
	}
