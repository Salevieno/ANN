package utilities;

import java.util.Arrays;

public abstract class Uts 
{
	
	public static void PrintWeightsAndNeurons(double[][] neuronvalue, double[][][] weight)
	{
		System.out.println();
		System.out.println("*** Neurônios e pesos ***");
		System.out.println("neurons: " + Arrays.deepToString(neuronvalue));
		System.out.println("weights: " + Arrays.deepToString(weight));
		System.out.println();
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

	public static double[] calcError(double[][] output, double[][] target)
	{
		double[] error = new double[output.length] ;
		for (int t = 0; t <= target.length - 1; t += 1)
		{	
			if ((target[t][0] + output[t][0]) != 0)
			{
				error[t] = Math.abs((target[t][0] - output[t][0]) / (target[t][0] + output[t][0]));
			}
			else
			{
				error[t] = 0 ;
			}
		}
		
		return error;
	}

	public static double errorperc2(double[][] output, double[][] target)
	{
		double error = 0;
		int numberOutput = output[0].length ;
		for (int t = 0; t <= target.length - 1; t += 1)
		{	
			for (int n = 0; n <= numberOutput - 1; n += 1)
			{
				if ((output[t][n] + target[t][n]) != 0)
				{
					error += Math.abs((target[t][n] - output[t][n]) / (output[t][n] + target[t][n]));
				}
				else
				{
					error += 0;
				}
			}		
		}
		error = error / (target.length * numberOutput);
		return error;
	}

	public static double calcErrorPerc(double[][] output, double[][] target)
	{
		double error = 0;
		int numberOutput = output[0].length ;
		for (int t = 0; t <= target.length - 1; t += 1)
		{	
			for (int n = 0; n <= numberOutput - 1; n += 1)
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
		error = 100 * error / (target.length * numberOutput);
		return error;
	}
	
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

}
