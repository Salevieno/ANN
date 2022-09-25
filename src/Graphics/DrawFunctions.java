package Graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.Arrays;

import Main.Utg;

public class DrawFunctions
{
    Font TextFont = new Font("SansSerif", Font.PLAIN, 20);
	Font BoldTextFont = new Font("SansSerif", Font.BOLD, 20);
	int stdStroke = 2;
	private Graphics2D G;
	
	public DrawFunctions(Graphics g)
	{
		G = (Graphics2D) g;
	}
	public void DrawImage(Image File, int[] Pos, String Alignment)
	{       
		if (File != null)
		{
			int l = (int)(File.getWidth(null)), h = (int)(File.getHeight(null));
			if (Alignment.equals("Left"))
			{
				G.drawImage(File, Pos[0], Pos[1] - h, l, h, null);
			}
			if (Alignment.equals("Center"))
			{
				G.drawImage(File, Pos[0] - l/2, Pos[1] - h/2, l, h, null);
			}
			if (Alignment.equals("Right"))
			{
				G.drawImage(File, Pos[0] - l, Pos[1] - h, l, h, null);
			}
	        //Ut.CheckIfPosIsOutsideScreen(Pos, new int[] {ScreenL + 55, ScreenH + 19}, "An image is being drawn outside window");
		}
	}
	public void DrawText(int[] Pos, String Text, String Alignment, float angle, String Style, int size, Color color)
    {
		float TextLength = Utg.TextL(Text, TextFont, size, G), TextHeight = Utg.TextH(size);
    	int[] Offset = new int[2];
		AffineTransform a = null;	// Rotate rectangle
		AffineTransform backup = G.getTransform();
		if (Alignment.equals("Left"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0] - 0.5*TextLength, Pos[1] + 0.5*TextHeight);	// Rotate text
    	}
		else if (Alignment.equals("Center"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1] + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Utg.TextL(Text, BoldTextFont, size, G)/2;
    		Offset[1] = Utg.TextH(size)/2;
    	}
    	else if (Alignment.equals("Right"))
    	{
			a = AffineTransform.getRotateInstance(-angle*Math.PI/180, Pos[0], Pos[1] + 0.5*TextHeight);	// Rotate text
    		Offset[0] = -Utg.TextL(Text, BoldTextFont, size, G);
    	}
    	if (Style.equals("Bold"))
    	{
    		G.setFont(new Font(BoldTextFont.getName(), BoldTextFont.getStyle(), size));
    	}
    	else
    	{
    		G.setFont(new Font(TextFont.getName(), TextFont.getStyle(), size));
    	}
    	if (0 < Math.abs(angle))
    	{
    		G.setTransform(a);
    	}
    	G.setColor(color);
    	G.drawString(Text, Pos[0] + Offset[0], Pos[1] + Offset[1]);
        G.setTransform(backup);
    }
	public void DrawLine(int[] PosInit, int[] PosFinal, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawLine(PosInit[0], PosInit[1], PosFinal[0], PosFinal[1]);
    	G.setStroke(new BasicStroke(stdStroke));
    }
	public void DrawPolyLine(int[] x, int[] y, int thickness, Color color)
    {
    	G.setColor(color);
    	G.setStroke(new BasicStroke(thickness));
    	G.drawPolyline(x, y, x.length);
    	G.setStroke(new BasicStroke(stdStroke));
    }
	public void DrawPolygon(int[] x, int[] y, boolean fill, Color ContourColor, Color FillColor)
    {
    	G.setColor(ContourColor);
    	G.drawPolygon(x, y, x.length);
    	if (fill)
    	{
    		G.setColor(FillColor);
        	G.fillPolygon(x, y, x.length);
    	}
    }
	public void DrawRoundRect(int[] Pos, String Alignment, int l, int h, int Thickness, int ArcWidth, int ArcHeight, Color color, Color ContourColor, boolean contour)
	{
		int[] offset = Utg.OffsetFromPos(Alignment, l, h);
		G.setStroke(new BasicStroke(Thickness));
		if (contour)
		{
			G.setColor(ContourColor);
			G.fillRoundRect(Pos[0] + offset[0] - Thickness, Pos[1] + offset[1] - Thickness, l + 2*Thickness, h + 2*Thickness, ArcWidth, ArcHeight);
		}
		if (color != null)
		{
			G.setColor(color);
			G.fillRoundRect(Pos[0] + offset[0], Pos[1] + offset[1], l, h, ArcWidth, ArcHeight);
		}
		G.setStroke(new BasicStroke(1));
	}
    public void DrawRoundRect(int[] Pos, String Alignment, int l, int h, int Thickness, int ArcWidth, int ArcHeight, Color[] colors, Color ContourColor, boolean contour)
	{
		int[] offset = Utg.OffsetFromPos(Alignment, l, h);
		G.setStroke(new BasicStroke(Thickness));
		if (contour)
		{
			G.setColor(ContourColor);
			G.fillRoundRect(Pos[0] + offset[0] - Thickness, Pos[1] + offset[1] - Thickness, l + 2*Thickness, h + 2*Thickness, ArcWidth, ArcHeight);
		}
		if (colors[0] != null & colors[1] != null)
		{
		    GradientPaint gradient = new GradientPaint(Pos[0] + offset[0], Pos[1] + offset[1], colors[0], offset[0], offset[1] + h, colors[1]);
		    G.setPaint(gradient);
			G.fillRoundRect(Pos[0] + offset[0], Pos[1] +  offset[1], l, h, ArcWidth, ArcHeight);
		}
		G.setStroke(new BasicStroke(1));
	}
	public void DrawPoint(int[] Pos, int size, boolean fill, int Thickness, Color ContourColor, Color FillColor)
    {
    	G.setColor(ContourColor);
		G.setStroke(new BasicStroke(Thickness));
    	G.drawOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    	if (fill)
    	{
        	G.setColor(FillColor);
        	G.fillOval(Pos[0] - size/2, Pos[1] - size/2, size, size);
    	}
		G.setStroke(new BasicStroke(1));
    }
	public void DrawArrow(int[] Pos, int size, double theta, boolean fill, double ArrowSize, Color color)
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
	public void DrawColorPalette(Color[] Palette)
	{
		int[] Pos = new int[] {(int) (30), (int) (410)};
		int Nx = 4;
		int L = 20, H = 20;
		DrawRoundRect(Pos, "TopLeft", Nx * L + 10, (Palette.length / Nx + 1) * H + 10, 1, 5, 5, Palette[7], Palette[7], true);
		for (int i = 0; i <= Palette.length - 1; i += 1)
		{
			int[] ColorPos = new int[] {(int) (Pos[0] + 5 + L / 2 + (i % Nx) * L), (int) (Pos[1] + 5 + H / 2 + i / Nx * H)};
			DrawRoundRect(ColorPos, "Center", L, H, 1, 5, 5, Palette[i], Palette[i], false);
		}
	}
	
    public void DrawMenu(int[] Pos, String Alignment, int l, int h, int Thickness, Color[] colors, Color ContourColor)
    {
    	int border = 3;
    	DrawRoundRect(Pos, Alignment, l, h, Thickness, 5, 5, colors[0], ContourColor, true);
    	DrawRoundRect(Pos, Alignment, l - 2*border, h - 2*border, Thickness, 5, 5, colors[1], ContourColor, true);
    }
	public void DrawGrid(int[] InitPos, int[] FinalPos, int NumSpacing)
	{
		int LineThickness = 1;
		int[] Length = new int[] {FinalPos[0] - InitPos[0], InitPos[1] - FinalPos[1]};
		for (int i = 0; i <= NumSpacing - 1; i += 1)
		{
			DrawLine(new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1]}, new int[] {InitPos[0] + (i + 1)*Length[0]/NumSpacing, InitPos[1] - Length[1]}, LineThickness, Color.black);						
			DrawLine(new int[] {InitPos[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, new int[] {InitPos[0] + Length[0], InitPos[1] - (i + 1)*Length[1]/NumSpacing}, LineThickness, Color.black);						
		}
	}
	public void DrawGraph(int[] Pos, String Title, int size, Color color)
	{
		int asize = 8 * size / 100;
		double aangle = Math.PI * 30 / 180.0;
		DrawText(new int[] {Pos[0] + size/2, (int) (Pos[1] - size - 13 - 2)}, Title, "Center", 0, "Bold", 13, Color.cyan);
		DrawLine(Pos, new int[] {Pos[0], (int) (Pos[1] - size - asize)}, 2, color);
		DrawLine(Pos, new int[] {(int) (Pos[0] + size + asize), Pos[1]}, 2, color);
		DrawArrow(new int[] {Pos[0] + size + asize, Pos[1]}, asize, aangle, false, 0.4 * asize, color);
		DrawArrow(new int[] {Pos[0], Pos[1] - size - asize}, asize, aangle, false, 0.4 * asize, color);
		//DrawPolyLine(new int[] {Pos[0] - asize, Pos[0], Pos[0] + asize}, new int[] {(int) (Pos[1] - 1.1*size) + asize, (int) (Pos[1] - 1.1*size), (int) (Pos[1] - 1.1*size) + asize}, 2, ColorPalette[4]);
		//DrawPolyLine(new int[] {(int) (Pos[0] + 1.1*size - asize), (int) (Pos[0] + 1.1*size), (int) (Pos[0] + 1.1*size - asize)}, new int[] {Pos[1] - asize, Pos[1], Pos[1] + asize}, 2, ColorPalette[4]);
		DrawGrid(Pos, new int[] {Pos[0] + size, Pos[1] - size}, 10);
	}
	public void PlotPoints(int[] Pos, String Title, int size, Color fillColor, Color contourColor, double[] x, double[] y)
	{
		DrawGraph(Pos, Title, size, Color.black);
		double xmin = Utg.FindMin(x);
		double xmax = Utg.FindMax(x);
		double ymin = Utg.FindMin(y);
		double ymax = Utg.FindMax(y);
		
		xmin = 0;
		xmax = 1;
		ymin = 0;
		ymax = 1;
		for (int p = 0; p <= x.length - 1; p += 1)
		{
			if (xmax != xmin)
			{
				x[p] = (x[p] - xmin) / (xmax - xmin) * size;
			}
			else
			{
				x[p] = 0;
			}
			if (ymax != ymin)
			{
				y[p] = (y[p] - ymin) / (ymax - ymin) * size;
			}
			else
			{
				y[p] = 0;
			}
			DrawPoint(new int[] {(int) (Pos[0] + x[p]), (int) (Pos[1] - y[p])}, 6, true, 1, contourColor, fillColor);
		}
	}
	public void DrawDynGraph(int[] Pos, String Title, double[][] Var, Color[] color)
	{
		int size = 100;
		double MaxEver = 0;
		int NumPoints;
		if (Var != null)
		{
			NumPoints = Var.length;
			if (Var[0] != null)
			{
				MaxEver = Utg.MaxAbs(Var[0]);
			}
		}
		else
		{
			NumPoints = 0;
		}
		DrawGraph(Pos, Title, size, Color.black);
		if (Var != null)
		{
			if (Var[0] != null)
			{
				if (1 <= Var.length)
				{
					for (int j = 0; j <= Var.length - 1; j += 1)
					{
						NumPoints = Var[j].length;
						int[] x = new int[NumPoints], y = new int[NumPoints];
						for (int i = 0; i <= Var[j].length - 1; i += 1)
						{
							x[i] = Pos[0] + size * i / (Var[j].length - 1);
							y[i] = Pos[1] - (int) (size * Var[j][i] / (float) MaxEver);
						}
						DrawText(new int[] {(int) (Pos[0] - 30), (int) (Pos[1] - 1*size)}, String.valueOf(Utg.Round(MaxEver, 2)), "Center", 0, "Bold", 13, color[j]);
						DrawText(new int[] {(int) (Pos[0] + size + 30), (int) (y[y.length - 1])}, String.valueOf(Utg.Round(Var[j][Var[j].length - 1], 2)), "Center", 0, "Bold", 13, color[j]);
						DrawPolyLine(x, y, 2, color[j]);
					}
				}
			}
		}
	}
	public void DrawANNInfo(int[] Pos, int iter, int[] Nneurons, double errorperc, boolean ApplyBias, Color TextColor)
	{
		int FontSize = 13;
		int sy = 15;
		DrawText(Pos, "*** Parâmetros da rede neural ***", "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new int[] {Pos[0], Pos[1] + 1 * sy}, "Número de neurônios: " + String.valueOf(Arrays.toString(Nneurons)), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new int[] {Pos[0], Pos[1] + 2 * sy}, "Bias: " + String.valueOf(ApplyBias), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new int[] {Pos[0], Pos[1] + 3 * sy}, "Iteração: " + String.valueOf(iter), "Left", 0, "Bold", FontSize, TextColor);
		DrawText(new int[] {Pos[0], Pos[1] + 4 * sy}, "Erro: " + String.valueOf(Utg.Round(errorperc, 2)) + "%", "Left", 0, "Bold", FontSize, TextColor);
	}
	public void DrawANN(int[] pos, int[] size, int[] Nneurons, double[][] neuronvalue, double[][][] weight, boolean DrawLines, Color color)
	{
		int FontSize = 13;
		int NeuronSize = 30;
		int sx = (size[0] - NeuronSize * Nneurons.length) / (Nneurons.length + 1);
		
		double MaxWeight = Utg.FindMax(weight);
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
						int[] NeuronPos1 = new int[] {pos[0] + (l - 1) * (sx + NeuronSize) + sx + NeuronSize / 2, pos[1] + n1 * (sy1 + NeuronSize) + sy1 + NeuronSize / 2};
						int[] NeuronPos2 = new int[] {pos[0] + l * (sx + NeuronSize) + sx + NeuronSize / 2, pos[1] + n2 * (sy2 + NeuronSize) + sy2 + NeuronSize / 2};
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
				int[] NeuronPos = new int[] {pos[0] + l * (sx + NeuronSize) + sx + NeuronSize / 2, pos[1] + n * (sy + NeuronSize) + sy + NeuronSize / 2};
				DrawPoint(NeuronPos, NeuronSize, true, 2, Color.black, color);
				DrawText(NeuronPos, String.valueOf(Utg.Round(neuronvalue[l][n], 2)), "Center", 0, "Bold", FontSize, Color.black);
			}
		}
	}
}
