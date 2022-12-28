package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import utilities.Utg;

public abstract class DrawFunctions
{
	private static final Font STD_FONT = new Font("SansSerif", Font.PLAIN, 20);
	private static final Font STD_BOLD_FONT = new Font("SansSerif", Font.BOLD, 20);
	private static int stdStroke = 2;
	private static Graphics2D graphics;
		
	public static void setG(Graphics2D g)
	{
		graphics = g;
	}
	public static void DrawText(Point Pos, String Text, String Alignment, float angle, String Style, int size, Color color)
    {
		float TextLength = Utg.TextL(Text, STD_FONT, size, graphics), TextHeight = Utg.TextH(size);
    	int[] Offset = new int[2];
		AffineTransform a = null;	// Rotate rectangle
		AffineTransform backup = graphics.getTransform();
		if (Alignment.equals("Left"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos.x - 0.5*TextLength, Pos.y + 0.5*TextHeight);	// Rotate text
    	}
		else if (Alignment.equals("Center"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos.x, Pos.y + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Utg.TextL(Text, STD_BOLD_FONT, size, graphics)/2;
    		Offset[1] = Utg.TextH(size)/2;
    	}
		else if (Alignment.equals("BotCenter"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos.x, Pos.y + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Utg.TextL(Text, STD_BOLD_FONT, size, graphics)/2;
    		Offset[1] = Utg.TextH(size);
    	}
    	else if (Alignment.equals("Right"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos.x, Pos.y + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Utg.TextL(Text, STD_BOLD_FONT, size, graphics);
    	}
    	if (Style.equals("Bold"))
    	{
    		graphics.setFont(new Font(STD_BOLD_FONT.getName(), STD_BOLD_FONT.getStyle(), size));
    	}
    	else
    	{
    		graphics.setFont(new Font(STD_FONT.getName(), STD_FONT.getStyle(), size));
    	}
    	if (0 < Math.abs(angle))
    	{
    		graphics.setTransform(a);
    	}
    	graphics.setColor(color);
    	graphics.drawString(Text, Pos.x + Offset[0], Pos.y + Offset[1]);
        graphics.setTransform(backup);
    }
	public static void DrawPoint(Point pos, int size, int stroke, Color contourColor, Color fillColor)
    {
		graphics.setStroke(new BasicStroke(stroke));
    	if (fillColor != null)
    	{
        	graphics.setColor(fillColor);
        	graphics.fillOval(pos.x - size/2, pos.y - size/2, size, size);
    	}
    	if (contourColor != null)
    	{
        	graphics.setColor(contourColor);
        	graphics.drawOval(pos.x - size/2, pos.y - size/2, size, size);
    	}
		graphics.setStroke(new BasicStroke(1));
    }
	public static void DrawLine(int[] PosInit, int[] PosFinal, int thickness, Color color)
    {
    	graphics.setColor(color);
    	graphics.setStroke(new BasicStroke(thickness));
    	graphics.drawLine(PosInit[0], PosInit[1], PosFinal[0], PosFinal[1]);
    	graphics.setStroke(new BasicStroke(stdStroke));
    }
	public static void DrawRoundRect(Point Pos, String Alignment, int l, int h, int Thickness, int ArcWidth, int ArcHeight, Color color, Color ContourColor)
	{
		int[] offset = Utg.OffsetFromPos(Alignment, l, h);
		graphics.setStroke(new BasicStroke(Thickness));
		if (ContourColor != null)
		{
			graphics.setColor(ContourColor);
			graphics.drawRoundRect(Pos.x + offset[0] - Thickness, Pos.y + offset[1] - Thickness, l + 2*Thickness, h + 2*Thickness, ArcWidth, ArcHeight);
		}
		if (color != null)
		{
			graphics.setColor(color);
			graphics.fillRoundRect(Pos.x + offset[0], Pos.y + offset[1], l, h, ArcWidth, ArcHeight);
		}
		graphics.setStroke(new BasicStroke(1));
	}
	public static void DrawCircle(Point center, int diameter, int stroke, Color color, Color contourColor)
	{
		graphics.setColor(color) ;
		graphics.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphics.fillOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		if (contourColor != null)
		{
			graphics.setColor(contourColor) ;
			graphics.drawOval(center.x - diameter/2, center.y - diameter/2, diameter, diameter) ;
		}
		graphics.setStroke(new BasicStroke(stdStroke)) ;
	}
	public static void DrawPolygon(int[] x, int[] y, boolean fill, Color ContourColor, Color FillColor)
    {
    	graphics.setColor(ContourColor);
    	graphics.drawPolygon(x, y, x.length);
    	if (fill)
    	{
    		graphics.setColor(FillColor);
        	graphics.fillPolygon(x, y, x.length);
    	}
    }
	

	public static void DrawTarget(Point center, int size)
	{
		DrawCircle(center, size, 1, Color.red, Color.black) ;
		DrawCircle(center, (int) (0.75 * size), 1, Color.black, null) ;
		DrawCircle(center, (int) (0.5 * size), 1, Color.red, null) ;
		DrawCircle(center, (int) (0.25 * size), 1, Color.black, null) ;
	}
	public static void PlotPointsOnTarget(Point center, int targetSize, double[] distToCenter)
	{
		int size = 4 ;
		for (int i = 0 ; i <= distToCenter.length - 1; i += 1)
		{
			double angle = 2 * Math.PI * Math.random() ;
			Point pos = new Point((int) (center.x + distToCenter[i] * targetSize * Math.cos(angle)),
					(int) (center.y + distToCenter[i] * targetSize * Math.sin(angle))) ;
			DrawPoint(pos, size, 1, Color.black, Color.cyan) ;
		}
	}
	public static void DrawTargetGraph(Point center, int size, double[] distToCenter)
	{
		DrawTarget(center, size) ;
		PlotPointsOnTarget(center, size / 2, distToCenter) ;
	}
	public static void DrawAccuracyBar(Point botCenter, Dimension size, double error)
	{
		double accuracy = 1 - error ;
		DrawText(new Point(botCenter.x, botCenter.y - size.height - 30), "Confian�a", "BotCenter", 0, "Bold", 14, Color.black) ;
		DrawText(new Point(botCenter.x - size.width - 10, botCenter.y - size.height), "100%", "Center", 0, "Bold", 13, new Color(0, 200, 0)) ;
		DrawRoundRect(botCenter, "BotCenter", size.width, size.height, stdStroke, 5, 5, null, Color.black) ;
		DrawRoundRect(botCenter, "BotCenter", size.width, (int) (accuracy * size.height), stdStroke, 5, 5, Color.green, null) ;
	}
	public static void DrawGrid(int[] InitPos, int[] FinalPos, int NumSpacing)
	{
		int LineThickness = 1;
		int[] Length = new int[] {FinalPos[0] - InitPos[0], InitPos[1] - FinalPos[1]};
		for (int i = 0; i <= NumSpacing - 1; i += 1)
		{
			DrawLine(new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]}, new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]}, LineThickness, Color.black);						
			DrawLine(new int[] {InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, new int[] {InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, LineThickness, Color.black);						
		}
	}
	public static void DrawArrow(int[] Pos, int size, double theta, boolean fill, double ArrowSize, Color color)
    {
    	double open = 0.8;
    	int ax1 = (int)(Pos[0] - open*size*Math.cos(theta) - ArrowSize/3.5*Math.sin(theta));
    	int ay1 = (int)(Pos[1] + open*size*Math.sin(theta) - ArrowSize/3.5*Math.cos(theta));
    	int ax2 = Pos[0];
    	int ay2 = Pos[1];
     	int ax3 = (int)(Pos[0] - open*size*Math.cos(theta) + ArrowSize/3.5*Math.sin(theta));
     	int ay3 = (int)(Pos[1] + open*size*Math.sin(theta) + ArrowSize/3.5*Math.cos(theta));
     	DrawPolygon(new int[] {ax1, ax2, ax3}, new int[] {ay1, ay2, ay3}, fill, color, color);
    }
	public static void DrawGraph(int[] Pos, String Title, int size, Color color)
	{
		int asize = 8 * size / 100;
		double aangle = Math.PI * 30 / 180.0;
		DrawText(new Point(Pos[0] + size/2, (int) (Pos[1] - size - 13 - 2)), Title, "Center", 0, "Bold", 13, Color.cyan);
		DrawLine(Pos, new int[] {Pos[0], (int) (Pos[1] - size - asize)}, 2, color);
		DrawLine(Pos, new int[] {(int) (Pos[0] + size + asize), Pos[1]}, 2, color);
		DrawArrow(new int[] {Pos[0] + size + asize, Pos[1]}, asize, aangle, false, 0.4 * asize, color);
		DrawArrow(new int[] {Pos[0], Pos[1] - size - asize}, asize, aangle, false, 0.4 * asize, color);
		//DrawPolyLine(new int[] {Pos[0] - asize, Pos[0], Pos[0] + asize}, new int[] {(int) (Pos[1] - 1.1*size) + asize, (int) (Pos[1] - 1.1*size), (int) (Pos[1] - 1.1*size) + asize}, 2, ColorPalette[4]);
		//DrawPolyLine(new int[] {(int) (Pos[0] + 1.1*size - asize), (int) (Pos[0] + 1.1*size), (int) (Pos[0] + 1.1*size - asize)}, new int[] {Pos[1] - asize, Pos[1], Pos[1] + asize}, 2, ColorPalette[4]);
		DrawGrid(Pos, new int[] {Pos[0] + size, Pos[1] - size}, 10);
	}
	public static void PlotPoints(int[] Pos, String Title, int size, Color fillColor, Color contourColor, List<Double> x, List<Double> y)
	{
		DrawGraph(Pos, Title, size, Color.black);
		double xmin = Collections.min(x);
		double xmax = Collections.max(x);
		double ymin = Collections.min(y);
		double ymax = Collections.max(y);
		
		xmin = 0;
		xmax = 1;
		ymin = 0;
		ymax = 1;
		for (int p = 0; p <= x.size() - 1; p += 1)
		{
			if (xmax != xmin)
			{
				x.set(p, (x.get(p) - xmin) / (xmax - xmin) * size);
			}
			else
			{
				x.set(p, 0.0);
			}
			if (ymax != ymin)
			{
				y.set(p, (y.get(p) - ymin) / (ymax - ymin) * size);
			}
			else
			{
				y.set(p, 0.0);
			}
			DrawPoint(new Point((int) (Pos[0] + x.get(p)), (int) (Pos[1] - y.get(p))), 6, 1, contourColor, fillColor);
		}
	}
    public static void DrawMenu(Point Pos, String Alignment, int l, int h, int Thickness, Color[] colors, Color ContourColor)
    {
    	int border = 3;
    	DrawRoundRect(Pos, Alignment, l, h, Thickness, 5, 5, colors[0], ContourColor);
    	DrawRoundRect(Pos, Alignment, l - 2*border, h - 2*border, Thickness, 5, 5, colors[1], ContourColor);
    }
	public static void DrawANNInfo(Point Pos, int iter, int[] Nneurons, double errorperc, boolean ApplyBias, Color TextColor)
	{
		int FontSize = 13;
		int sy = 15;
		DrawText(Pos, "*** Par�metros da rede neural ***", "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new Point(Pos.x, Pos.y + 1 * sy), "N�mero de neur�nios: " + String.valueOf(Arrays.toString(Nneurons)), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new Point(Pos.x, Pos.y + 2 * sy), "Bias: " + String.valueOf(ApplyBias), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new Point(Pos.x, Pos.y + 3 * sy), "Itera��o: " + String.valueOf(iter), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new Point(Pos.x, Pos.y + 4 * sy), "Erro: " + String.valueOf(Utg.Round(errorperc, 2)) + "%", "Left", 0, "Bold", FontSize, TextColor);
	}
	public static void DrawANN(Point pos, int[] size, int[] Nneurons, double[][] neuronvalue, double[][][] weight, boolean DrawLines, Color color)
	{
		int FontSize = 13;
		int NeuronSize = 30;
		int sx = (size[0] - NeuronSize * Nneurons.length) / (Nneurons.length + 1);
		
		// find maximum weight
		double MaxWeight = weight[0][0][0];
		for (int i = 0; i <= weight.length - 1; i += 1)
        {
			for (int j = 0; j <= weight[i].length - 1; j += 1)
	        {
				for (int k = 0; k <= weight[i][j].length - 1; k += 1)
		        {
					if (MaxWeight < Math.abs(weight[i][j][k]))
					{
						MaxWeight = Math.abs(weight[i][j][k]);
					}
		        }
	        }
        }
		
		if (DrawLines)
		{
			for (int l = 1; l <= Nneurons.length - 1;l += 1)
			{
				int sy1 = (size[1] - NeuronSize * Nneurons[l - 1]) / (Nneurons[l - 1] + 1);
				int sy2 = (size[1] - NeuronSize * Nneurons[l]) / (Nneurons[l] + 1);
				for (int n1 = 0; n1 <= Nneurons[l - 1] - 1; n1 += 1)
				{
					for (int n2 = 0; n2 <= Nneurons[l] - 1; n2 += 1)
					{
						int[] NeuronPos1 = new int[] {pos.x + (l - 1) * (sx + NeuronSize) + sx + NeuronSize / 2, pos.y + n1 * (sy1 + NeuronSize) + sy1 + NeuronSize / 2};
						int[] NeuronPos2 = new int[] {pos.x + l * (sx + NeuronSize) + sx + NeuronSize / 2, pos.y + n2 * (sy2 + NeuronSize) + sy2 + NeuronSize / 2};
						if (0 < weight[l - 1][n2][n1])
						{
							DrawLine(NeuronPos1, NeuronPos2, 2, new Color(0, 0, 0, (int) (255 * Math.abs(weight[l - 1][n2][n1]) / MaxWeight)));
						}
						else
						{
							DrawLine(NeuronPos1, NeuronPos2, 2, new Color(255, 0, 0, (int) (255 * Math.abs(weight[l - 1][n2][n1]) / MaxWeight)));
						}
					}
				}
			}
		}
		
		for (int l = 0; l <= Nneurons.length - 1; l += 1)
		{
			int sy = (size[1] - NeuronSize * Nneurons[l]) / (Nneurons[l] + 1);
			for (int n = 0; n <= Nneurons[l] - 1; n += 1)
			{
				Point NeuronPos = new Point(pos.x + l * (sx + NeuronSize) + sx + NeuronSize / 2, pos.y + n * (sy + NeuronSize) + sy + NeuronSize / 2);
				DrawPoint(NeuronPos, NeuronSize, 2, Color.black, color);
				DrawText(NeuronPos, String.valueOf(Utg.Round(neuronvalue[l][n], 2)), "Center", 0, "Bold", FontSize, Color.black);
			}
		}
	}
}
