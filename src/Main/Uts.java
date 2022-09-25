package Main;

import java.awt.Color;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.ImageIcon;

import Components.Neuron;

public abstract class Uts 
{
	public static double[] VecMatrixProdWithBias(double[] vector, double[][] matrix, double[] bias)
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
					if (bias[j] < vector[j])
					{
						product[i] += vector[j] * matrix[i][j];
					}
				}
			}		
			return product;
		}
	}

	public static double[] ImagePixels(String FilePath)
	{
		Image image = new ImageIcon(FilePath).getImage();
		int ImageL = (image.getWidth(null)), ImageH = (image.getHeight(null));	// dimensions of the image in pixels
		double[] input = new double[ImageL * ImageH * 3];
		for (int i = 0; i <= ImageL - 1; i += 3)
		{
			for (int j = 0; j <= ImageH - 1; j += 3)
			{
				Color PixelColor = Utg.GetPixelColor(Utg.toBufferedImage(image), new int[] {i, j});
				input[i * ImageH + j] = PixelColor.getRed() / 255.0;
				input[i * ImageH + j + 1] = PixelColor.getGreen() / 255.0;
				input[i * ImageH + j + 2] = PixelColor.getBlue() / 255.0;
			}
		}
		return input;
	}
	
	public static double dEdy(int Nlayers, int inp, int n, double[][] target, double[][] neuronvalue)
	{
		return -(target[inp][n] - neuronvalue[Nlayers - 1][n]);
	}
	
	public static double act(double x)
	{
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	public static double Dact(double x)
	{
		return x*(1 - x);
	}

	public static int[] CreateMap(int path, int layer, int wi, int wf, int[] Nneurons, int[] multvec)
	{
		int Nlayers = Nneurons.length;
		int[] Map = new int[layer];
		for (int l = 0; l <= layer - 1; l += 1)
		{
			Map[l] = (path / multvec[Nlayers - l - 1]) % Nneurons[Nlayers - l - 1];
		}
		return Map;
	}

	public static double errorperc(double[][] input, int Nlayers, int[] Nneurons, double[][] output, double[][] target, double[][][] weight, double[][] bias)
	{
		double error = 0;		
		for (int t = 0; t <= target.length - 1; t += 1)
		{	
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				if (target[t][n] != 0)
				{
					error += Math.abs((target[t][n] - output[t][n]) / target[t][n]);
				}
				else
				{
					error += Math.abs((target[t][n] - output[t][n]) / 1);
				}
			}		
		}
		error = 100 * error / (target.length * Nneurons[Nlayers - 1]);
		return error;
	}
	
	public static double[] errorpoints(double[][] input, int Nlayers, int[] Nneurons, double[][] output, double[][] target, double[][][] weight, double[][] bias)
	{
		double[] error = new double[input.length];		
		for (int t = 0; t <= target.length - 1; t += 1)
		{	
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				if (target[t][n] != 0)
				{
					error[t] += Math.abs((target[t][n] - output[t][n]) / target[t][n]);
				}
				else
				{
					error[t] += Math.abs((target[t][n] - output[t][n]) / 1);
				}
			}
		}
		return error;
	}
	
	public static double error(double[][] input, int Nlayers, int[] Nneurons, double[][] output, double[][] target, double[][][] weight, double[][] bias)
	{
		double error = 0;		
		for (int inp = 0; inp <= input.length - 1; inp += 1)
		{	
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				error += 1 / 2.0 * Math.pow(target[inp][n] - output[inp][n], 2);
			}		
		}
		return error;
	}
	
	public static double[][][] GetWeights(double[][][] weight)
	{
		double[][][] Weight = new double[weight.length][][];
		for (int i = 0; i <= weight.length - 1; i += 1)
		{
			Weight[i] = new double[weight[i].length][];
			for (int j = 0; j <= weight[i].length - 1; j += 1)
			{
				Weight[i][j] = new double[weight[i][j].length];
				for (int k = 0; k <= weight[i][j].length - 1; k += 1)
				{
					Weight[i][j][k] = weight[i][j][k];
				}	
			}
		}
		
		return Weight;
	}	

	public static void PrintWeightsAndNeurons(double[][] neuronvalue, double[][][] weight)
	{
		System.out.println();
		System.out.println("*** Neurônios e pesos ***");
		System.out.println("neurons: " + Arrays.deepToString(neuronvalue));
		System.out.println("weights: " + Arrays.deepToString(weight));
		System.out.println();
	}

	public static void PrintWeights(int Nlayers, int[] Nneurons, Neuron[][] neuron)
	{
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			System.out.println("layer: " + layer);
			for (int n = 0; n <= Nneurons[layer] - 1; n += 1)
			{
				System.out.println(Arrays.toString(neuron[layer][n].getweight()));
			}
			System.out.println();
		}
	}
	
	public static void PrintDWeights(int Nlayers, int[] Nneurons, double[][][] Dweight)
	{
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			System.out.println("layer: " + layer);
			for (int n = 0; n <= Nneurons[layer] - 1; n += 1)
			{
				System.out.println(Arrays.toString(Dweight[layer][n]));
			}
			System.out.println();
		}
	}

	public static void PrintANN(double[][] neuronvalue, double[][][] weight, double[][][] Dweight, double[][] output, double[][] target, double errorperc)
	{
		System.out.println();
		System.out.println("*** Parâmetros da rede neural ***");
		System.out.println("neurônios: " + Arrays.deepToString(neuronvalue));
		System.out.println("pesos: " + Arrays.deepToString(weight));
		System.out.println("Dpesos: " + Arrays.deepToString(Dweight));
		System.out.println("resultados: " + Arrays.deepToString(output));
		System.out.println("targets: " + Arrays.deepToString(target));
		System.out.println("erro: " + errorperc + " %");
		System.out.println();
		System.out.println();
	}
}
