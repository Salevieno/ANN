package main;

import utilities.Utg;
import utilities.Uts;

public abstract class Training
{
	public static double[][] ForwardPropagation(double[] input, int[] Nneurons, double[][][] weight, double[][] bias, boolean ApplyBias)
	{
		int Nlayers = Nneurons.length;
		double[][] neuronvalue = new double[Nlayers][];
		neuronvalue[0] = input;
		for (int layer = 1; layer <= Nlayers - 1; layer += 1)
		{
			neuronvalue[layer] = Utg.VecMatrixProd(neuronvalue[layer - 1], weight[layer - 1]);
			if (ApplyBias)
			{
				neuronvalue[layer] = Uts.VecMatrixProdWithBias(neuronvalue[layer - 1], weight[layer - 1], bias[layer - 1]);
			}
			for (int n = 0; n <= Nneurons[layer] - 1; n += 1)
			{
				neuronvalue[layer][n] = Uts.act(neuronvalue[layer][n]);
			}
		}
		return neuronvalue;
	}

	public static double[][][] backpropagation(int inp, int[] Nneurons, double[][] neuronvalue, double[][][] weight, double[][] target, int[] multvec)
	{
		int Nlayers = Nneurons.length;
		double[][][] Dweight = new double[Nlayers - 1][][];
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			Dweight[layer] = new double[Nneurons[layer + 1]][Nneurons[layer]];
		}
		for (int layer = Nlayers - 1; 1 <= layer; layer += -1)			// propagate from targets to input
		{	
			for (int wi = 0; wi <= Nneurons[layer - 1] - 1; wi += 1)	// weight from neuron i
			{
				for (int wf = 0; wf <= Nneurons[layer] - 1; wf += 1)	// to neuron f in the next layer
				{
					if (layer < Nlayers - 1)
					{
						//System.out.println();
						//System.out.println((layer - 1) + " W" + wi + wf + " Npaths: " + multvec[layer]);
						double SumD = 0;
						for (int path = 0; path <= multvec[layer] - 1; path += 1)	// For each possible path, create a map
						{
							int[] Map = Uts.CreateMap(path, Nlayers - layer, wi, wf, Nneurons, multvec);
							Map[Map.length - 1] = wf;
							//System.out.println(Arrays.toString(Map));
							
							double ProdD = 1;
							for (int MapID = 1; MapID <= Map.length - 1; MapID += 1)
							{
								double Dsig = Uts.Dact(neuronvalue[layer + MapID][Map[Map.length - MapID - 1]]);
								double W = weight[layer + MapID - 1][Map[Map.length - MapID - 1]][Map[Map.length - MapID]];	// lWif est� registrado como lWfi
								//System.out.println("path: " + path + " prod_"+ wi + "" + wf + ": " + Dsig + " * " + W);
								ProdD = ProdD * Dsig * W;
							}
							SumD += ProdD * Uts.dEdy(Nlayers, inp, Map[0], target, neuronvalue);
							//System.out.println("SumD = " + ProdD + " * " + dEdy(inp, Map[0]) + " = " + SumD);
						}
						double Dsig = Uts.Dact(neuronvalue[layer][wf]);
						double N = neuronvalue[layer - 1][wi];
						//System.out.println("Dweight = " + SumD + " * " + Dsig + " * " + N + " = " + (SumD * Dsig * N));
						Dweight[layer - 1][wf][wi] = SumD * Dsig * N;
					}
					else
					{
						double Dsig = Uts.Dact(neuronvalue[Nlayers - 1][wf]);
						double N = neuronvalue[layer - 1][wi];
						Dweight[layer - 1][wf][wi] = Uts.dEdy(Nlayers, inp, wf, target, neuronvalue) * Dsig * N;
					}
				}	
			}
		}
		
		return Dweight;
	}
	
	public static double[][][] UpdateWeights(int[] Nneurons, double Lrate, double[][][] weight, double[][][] Dweight)
	{
		int Nlayers = Nneurons.length;
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			for (int ni = 0; ni <= Nneurons[layer] - 1; ni += 1)
			{	
				for (int nf = 0; nf <= Nneurons[layer + 1] - 1; nf += 1)
				{
					weight[layer][nf][ni] += - Lrate * Dweight[layer][nf][ni];
				}	
			}
		}
		
		return weight;
	}
}
