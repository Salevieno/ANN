package oldClasses;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.Timer;

import graphics.DrawFunctions;
import utilities.Utg;
import utilities.Uts;

public class ANN_withComponents extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	/*
	private Timer timer;		// Main timer of the ANN
	private Neuron[][] neuron;	// neurons [layer][id from top to bottom]
	
	private int Niter = 10000;
	private int[] Nneurons = new int[] {0, 3, 0};
	private int Nlayers = Nneurons.length;
	private double Lrate = 0.5;	// learning rate
	private double[][] input;
	private double[][] target;
	private double[][] output;
	private double[][][] Dweight;	// Delta weight, change in weight during the back propagation.   Dw[Layer][Index of origin neuron][Index of desination neuron]
	//private double[][] Dbias;		// Delta bias, change in bias during the back propagation.          Db[Layer][Index of the associated neuron]
	DrawFunctions DF;
	
	public ANN_withComponents(int[] WinDim) 
	{         	
		Initialization(Nlayers, Nneurons);
		ResetDWeight();
		Uts.PrintWeights(Nlayers, Nneurons, neuron);
		double[][][] PrevWeight = null;
		//PrintNeurons(Nlayers, Nneurons);
		for (int i = 0; i <= Niter - 1; i += 1)
		{
			//ResetDWeight();
			PrevWeight = WeightsMatrix();
			//System.out.println(Arrays.deepToString(PrevWeight));
			for (int inp = 0; inp <= input.length - 1; inp += 1)
			{
				//ForwardPropagationWithBias(input[inp], Nlayers, Nneurons, neuron);
				ForwardPropagation(input[inp], Nlayers, Nneurons, neuron, PrevWeight);
				//PrintNeurons(Nlayers, Nneurons);
				backpropagation(inp, Nlayers, Nneurons, neuron, Dweight);
				//UtilGeral.PrintDWeights(Nlayers, Nneurons, Dweight);
				//UpdateBiases(Nlayers, Nneurons, neuron, Lrate, Dweight);
				UpdateWeights(Nlayers, Nneurons, neuron, Lrate, Dweight);
			}
			if (i % 1000 == 0)
			{
				System.out.println("iter " + i + " error: " + Utg.Round((float)error(input, Nlayers, Nneurons, neuron, target, PrevWeight), 5));
			}
			Uts.PrintWeights(Nlayers, Nneurons, neuron);
		}
		//PrintNeurons(Nlayers, Nneurons);
		for (int inp = 0; inp <= input.length - 1; inp += 1)
		{
			//ForwardPropagationWithBias(input[inp], Nlayers, Nneurons, neuron);
			ForwardPropagation(input[inp], Nlayers, Nneurons, neuron, PrevWeight);
		}
		//PrintNeurons(Nlayers, Nneurons);
		PrevWeight = WeightsMatrix();
		output = outputs(input, Nlayers,  Nneurons, neuron, PrevWeight);
		System.out.println("error: " + Utg.Round((float)errorperc(input, Nlayers, Nneurons, neuron, target, PrevWeight), 2) + "%");
		int TimeDelay = 4;		// Delay for checking if the keyboard was used
		timer = new Timer(TimeDelay, this);
		timer.start();	// Game will start checking for keyboard events and go to the method paintComponent every "timer" miliseconds
		//addMouseListener(new MouseEventDemo());
		//addKeyListener(new TAdapter());
		//setFocusable(true);
	}
	
	public void Initialization(int Nlayers, int[] Nneurons)
	{
		neuron = new Neuron[Nlayers][];
		String[][] InputData = Utg.ReadcsvFile("input.txt");
		String[][] TargetData = Utg.ReadcsvFile("target.txt");
		if (InputData.length != TargetData.length)
		{
			System.out.println("Different number of input and target points");
		}
		else
		{
			input = new double[InputData.length - 1][];
			target = new double[TargetData.length - 1][];
			for (int i = 0; i <= InputData.length - 2; i += 1)
			{
				input[i] = new double[InputData[i + 1].length];
				for (int i2 = 0; i2 <= InputData[i + 1].length - 1; i2 += 1)
				{
					input[i][i2] = Double.parseDouble(InputData[i + 1][i2]);
				}
			}
			for (int i = 0; i <= TargetData.length - 2; i += 1)
			{
				target[i] = new double[TargetData[i + 1].length];
				for (int i2 = 0; i2 <= TargetData[i + 1].length - 1; i2 += 1)
				{
					target[i][i2] = Double.parseDouble(TargetData[i + 1][i2]);
				}
			}
		}
		Nneurons[0] = input[0].length;
		Nneurons[Nlayers - 1] = target[0].length;
		int id = 0;
		for (int layer = 0; layer <= Nlayers - 1; layer += 1)
		{
			double[] InitWeight = null;
			double InitBias = 0.5;
			double InitValue = 0;
			if (layer < Nlayers - 1)
			{
				InitWeight = new double[Nneurons[layer + 1]];
				Arrays.fill(InitWeight, 0.5);
			}
			for (int n = 0; n <= Nneurons[layer] - 1; n += 1)
			{
				neuron[layer] = Utg.AddElem(neuron[layer], new Neuron(id, layer, InitWeight, InitBias, InitValue));
				id += 1;
			}
		}
		//for (int l = 0; l <= Nlayers - 2; l += 1)
		//{
		//	for (int n = 0; n <= Nneurons[l] - 1; n += 1)
		//	{
		//		if (l == 0)
		//		{
		//			neuron[l][n].setweight(new double[] {0.1*n, 0.2*n});
		//		}
		//		else if (l == 1)
		//		{
		//			neuron[l][n].setweight(new double[] {0.3*n, 0.4*n});
		//		}
		//	}
		//}
		//Dbias = new double[Nlayers][];
		//for (int layer = 0; layer <= Nlayers - 1; layer += 1)
		//{
		//	Dbias[layer] = new double[Nneurons[layer]];
		//}
		output = new double[target.length][target[0].length];
		//Dbias = new double[Nlayers - 1][];
	}
	
	public void ResetDWeight()
	{
		Dweight = new double[Nlayers - 1][][];
		for (int layer = Nlayers - 1; 1 <= layer; layer += -1)
		{
			for (int wi = 0; wi <= Nneurons[layer - 1] - 1; wi += 1)
			{
				for (int wf = 0; wf <= Nneurons[layer] - 1; wf += 1)
				{
					Dweight[layer - 1] = new double[Nneurons[layer - 1]][Nneurons[layer]];
				}
			}
		}
	}
	
	public double[][][] WeightsMatrix()
	{
		double[][][] W = new double[Nlayers - 1][][];
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			W[layer] = new double[Nneurons[layer]][];
			for (int wi = 0; wi <= Nneurons[layer] - 1; wi += 1)
			{
				W[layer][wi] = neuron[layer][wi].getweight();
			}
		}
		return W;
	}
	
	public void PrintNeurons(int Nlayers, int[] Nneurons)
	{
		for (int layer = 0; layer <= Nlayers - 1; layer += 1)
		{
			System.out.println("*** layer: " + layer + " ***");
			for (int n = 0; n <= Nneurons[layer] - 1; n += 1)
			{
				System.out.println("id: neuron " + neuron[layer][n].getid());
				System.out.println("weights: " + Arrays.toString(neuron[layer][n].getweight()));
				System.out.println("bias: " + neuron[layer][n].getbias());
				System.out.println("value: " + neuron[layer][n].getvalue());
				System.out.println();
			}
			System.out.println();
		}
	}
	
	public void ForwardPropagation(double[] input, int Nlayers, int[] Nneurons, Neuron[][] neuron, double[][][] weight)
	{
		for (int n = 0; n <= Nneurons[0] - 1; n += 1)
		{
			neuron[0][n].setvalue(input[n]);
		}
		for (int layer = 1; layer <= Nlayers - 1; layer += 1)
		{
			for (int n2 = 0; n2 <= Nneurons[layer] - 1; n2 += 1)
			{
				double sum = 0;
				for (int n1 = 0; n1 <= Nneurons[layer - 1] - 1; n1 += 1)
				{
					//sum += neuron[layer - 1][n1].getvalue()*neuron[layer - 1][n1].getweight()[n2];
					sum += neuron[layer - 1][n1].getvalue()*weight[layer - 1][n1][n2];
				}
				neuron[layer][n2].setvalue(Utg.sig(sum));
			}
		}
	}
	
	public void ForwardPropagationWithBias(double[] input, int Nlayers, int[] Nneurons, Neuron[][] neuron)
	{
		for (int n = 0; n <= Nneurons[0] - 1; n += 1)
		{
			neuron[0][n].setvalue(input[n]);
		}
		for (int layer = 1; layer <= Nlayers - 1; layer += 1)
		{
			for (int n2 = 0; n2 <= Nneurons[layer] - 1; n2 += 1)
			{
				double sum = 0;
				for (int n1 = 0; n1 <= Nneurons[layer - 1] - 1; n1 += 1)
				{
					sum += neuron[layer - 1][n1].getvalue()*neuron[layer - 1][n1].getweight()[n2];
				}
				neuron[layer][n2].setvalue(Utg.sig(sum + neuron[layer][n2].getbias()));
			}
		}
	}
	
	public int[] CalcMultVec(int Nlayers, int[] Nneurons, double[][] target)
	{
		int[] multvec = new int[Nlayers];
		multvec[Nlayers - 1] = 1;
		multvec[Nlayers - 2] = target.length;
		for (int i = Nlayers - 3; 0 <= i; i += -1)
		{
			multvec[i] = multvec[i + 1]*Nneurons[i + 1];
		}
		return multvec;
	}
	
	public int NumberOfPaths(int N, int Nlayers, int[] Nneurons)
	{
		int NumPath = 1;
		for (int L = N + 1; L <= Nlayers - 1; L += 1)
		{
			NumPath = NumPath*Nneurons[L];
		}
		return NumPath;
	}
	
	public double dEdy(int inp, int n)
	{
		return -(target[inp][n] - neuron[Nlayers - 1][n].getvalue());
	}
	
	public double Dact2(double x)
	{
		return x*(1 - x);
	}
		
	public void backpropagation(int inp, int Nlayers, int[] Nneurons, Neuron[][] neuron, double[][][] Dweight)
	{
		int NumPaths = 0;
		int[] multvec = CalcMultVec(Nlayers, Nneurons, target);
		for (int layer = Nlayers - 1; 1 <= layer; layer += -1)						// propagate from targets to input
		{	
			//System.out.println("layer: " + layer);
			if (layer <= Nlayers - 2)
			{
				NumPaths = NumberOfPaths(layer, Nlayers, Nneurons);
			}
			for (int wi = 0; wi <= Nneurons[layer - 1] - 1; wi += 1)		// weight from neuron i
			{
				for (int wf = 0; wf <= Nneurons[layer] - 1; wf += 1)	// to neuron f in the next layer
				{
					if (layer < Nlayers - 1)
					{
						int[] Map = new int[Nlayers - layer + 1];
						double SumD = 0;
						for (int path = 0; path <= NumPaths - 1; path += 1)	// For each possible path, create a map
						{
							Map[0] = wi;
							Map[1] = wf;
							for (int m = Nlayers - layer; 2 <= m; m += -1)
							{
								Map[m] = (path / multvec[m + layer - 1]) % Nneurons[m + layer - 1];
							}
							//System.out.println(wi + " " + wf + ": map = " + Arrays.toString(Map));
							
							// Map created
							// Compute the products along the path and sum with the next path
							double ProdD = 1;
							for (int MapID = Map.length - 1; 2 <= MapID; MapID += -1)
							{
								//System.out.println(MapID);			
								double Dsig = Dact2(neuron[layer + MapID - 1][Map[MapID]].getvalue());
								double W = neuron[layer + MapID - 2][Map[MapID - 1]].getweight()[Map[MapID]];
								//System.out.println("ProdD " + wi + "_" + wf + ": " + dEdy(inp, Map[Map.length - 1]) + " " + Dsig + " " + W);
								ProdD = ProdD*dEdy(inp, Map[Map.length - 1])*Dsig*W;	// D1 = neuron[N][wi]*(1 - neuron[N][wi]), D2 = weight[N][wi][wf]
							}
							SumD += ProdD;	
							Map = new int[Nlayers - layer + 1];
						}
						double Dsig = Dact2(neuron[layer][wf].getvalue());
						double N = neuron[layer - 1][wi].getvalue();
						//System.out.println(wi + " " + wf + ": " + SumD + " " + Dsig + " " + N);
						//System.out.println(Map[Map.length - 1]);
						Dweight[layer - 1][wi][wf] = SumD*Dsig*N;
						//Dbias[layer - 1][wi] = SumD*Dsig*1;
					}
					else
					{
						double Dsig = Dact2(neuron[Nlayers - 1][wf].getvalue());
						double N = neuron[layer - 1][wi].getvalue();
						//System.out.println(wi + " " + wf + ": " + target[inp][wf] + " " + neuron[Nlayers - 1][wf].getvalue() + " " + dEdy(inp, wf) + " " + Dsig + " " + N);
						Dweight[layer - 1][wi][wf] = dEdy(inp, wf)*Dsig*N;
						//Dbias[layer - 1][wi] = dEdy(inp, wf)*Dsig;
					}
				}	
			}
		}
	}
	
	public void UpdateWeights(int Nlayers, int[] Nneurons, Neuron[][] neuron, double Lrate, double[][][] Dweight)
	{
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			for (int ni = 0; ni <= Nneurons[layer] - 1; ni += 1)
			{	
				double[] NewWeights = new double[Nneurons[layer + 1]];
				for (int nf = 0; nf <= Nneurons[layer+1] - 1; nf += 1)
				{
					NewWeights[nf] = neuron[layer][ni].getweight()[nf] - Lrate * Dweight[layer][ni][nf];
				}
				neuron[layer][ni].setweight(NewWeights);		
			}
		}		
	}
	
	//public void UpdateBiases(int Nlayers, int[] Nneurons, Neuron[][] neuron, double Lrate, double[][][] Dweight)
	//{
	//	for (int layer = 0; layer <= Nlayers - 2; layer += 1)
	//	{
	//		for (int ni = 0; ni <= Nneurons[layer] - 1; ni += 1)
	//		{	
	//			double NewBias = neuron[layer][ni].getbias() - Lrate * Dbias[layer][ni];
	//			neuron[layer][ni].setbias(NewBias);		
	//		}
	//	}		
	//}
	
	public double[][] outputs(double[][] input, int Nlayers, int[] Nneurons, Neuron[][] neuron, double[][][] PrevWeight)
	{
		double[][] output = new double[input.length][];
		for (int inp = 0; inp <= input.length - 1; inp += 1)
		{	
			ForwardPropagation(input[inp], Nlayers, Nneurons, neuron, PrevWeight);
			output[inp] = new double[Nneurons[Nlayers - 1]];
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				output[inp][n] = neuron[Nlayers - 1][n].getvalue();
			}
		}
		
		return output;
	}
	
	public double errorperc(double[][] input, int Nlayers, int[] Nneurons, Neuron[][] neuron, double[][] target, double[][][] PrevWeight)
	{
		double error = 0;		
		double[][] output = outputs(input, Nlayers, Nneurons, neuron, PrevWeight);
		for (int inp = 0; inp <= input.length - 1; inp += 1)
		{	
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				error += Math.abs(target[inp][n] - output[inp][n]);
			}		
		}
		error = 100 * error / (input.length * Nneurons[Nlayers - 1]);
		return error;
	}
	
	public double error(double[][] input, int Nlayers, int[] Nneurons, Neuron[][] neuron, double[][] target, double[][][] PrevWeight)
	{
		double error = 0;		
		double[][] output = outputs(input, Nlayers, Nneurons, neuron, PrevWeight);
		for (int inp = 0; inp <= input.length - 1; inp += 1)
		{	
			for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
			{
				error += 1 / 2.0 * Math.pow(target[inp][n] - output[inp][n], 2);
			}		
		}
		return error;
	}
	
	public void DrawStuff()
	{
		int inp = 0;
		System.out.println(Arrays.deepToString(target));
		System.out.println(Arrays.deepToString(output));
		for (int var = 0; var <= target[inp].length - 1; var += 1)
		{
			double[] ScaledTargets = Utg.ScaledVector(Utg.Transpose(target)[var], 0, 200);
			double[] ScaledOutputs = Utg.ScaledVector(Utg.Transpose(output)[var], 0, 200);
			DF.PlotPoints(new int[] {150, 250 + 250*var}, "Results var " + String.valueOf(var), 200, Color.cyan, Color.blue, ScaledTargets, ScaledOutputs);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
        super.paintComponent(g);
        //DF = new DrawFunctions(g);
        DrawStuff();
        Toolkit.getDefaultToolkit().sync();
        g.dispose();  
    }
    */
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
	
}
