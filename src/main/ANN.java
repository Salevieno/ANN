package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import graphics.DrawFunctions;
import utilities.Utg;
import utilities.Uts;

public class ANN extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private Timer timer;
	
	private static final Color[] COLOR_PALETTE = new Color[]
								{
										new Color(63, 40, 231),
										new Color(137, 249, 204),
										new Color(11, 11, 11),
										new Color(245, 117, 170),
										new Color(241, 199, 128),
										new Color(101, 131, 246),
										new Color(76, 131, 42)
								};

	private int iter; 			// number of the current iteration
	private int Niter; 			// maximum number of iterations
	private int[] Nneurons;	 	// number of neurons in each layer
	private int Nlayers; 		// number of layers
	private double Lrate; 		// learning rate
	private double[][] input; 	// input data
	private double[][] target; 	// target data
	private double[][] output; 	// ANN current outputs
	private double[][] neuronvalue; // value of each neuron (after applying the activation function)
	private double[][][] Weight; 	// weights
	private double[][][] Dweight; 	// Delta weight, change in weight during the back propagation. Dw[Layer][Index of origin neuron][Index of desination neuron]
	private double[][] Bias; 	// biases
	private double error; 		// current error
	private double derror; 		// current change in error
	private double errortol; 	// minimum error (stop if lower)
	private double derrortol; 	// minimum variation in error between iterations (stop if lower)
	private int[] multvec; 		// product of the number of neurons in each layer, starts from output

	private boolean RunTraining ;
	private boolean ShowANN ;
	private boolean ShowGraphs ;
	private boolean ApplyBias ;
	private boolean AdaptativeLrate ;
	private List<Double> SaveError ;
	private double[] PlotError ;

	public ANN(Dimension panelDimension)
	{
		this.setBackground(new Color(250, 240, 220));
		this.setPreferredSize(panelDimension);
		this.setVisible(true);
		
		RunTraining = true;
		ShowANN = true;
		ShowGraphs = true;
		ApplyBias = false;
		AdaptativeLrate = true;
		SaveError = new ArrayList<>();
		PlotError = null;
		
		initialize();

		timer = new Timer(0, this);
		timer.start();
	}
	
	public void initialize()
	{
		// getting parameters file data
		JSONObject parametersData = (JSONObject) Utg.ReadJson("Parameters.json") ;
		Niter = (int) (long) parametersData.get("NumberMaxIterations") ;
		JSONArray numberNeuronsData = (JSONArray) parametersData.get("NumberNeurons") ;
		Nneurons = new int[numberNeuronsData.size() + 2];
		Nlayers = numberNeuronsData.size() + 2;
		for (int layer = 1; layer <= numberNeuronsData.size(); layer += 1)
		{
			Nneurons[layer] = (int) (long) numberNeuronsData.get(layer - 1);
		}
		Lrate = (double) parametersData.get("InitialLearningRate") ;
		derrortol = (double) parametersData.get("ErrorVariationTolerance") ;
		errortol = (double) parametersData.get("ErrorTotalTolerance") ;
		error = errortol + 0.01;
		derror = derrortol + 1;
		
		// getting input and target files data
		String[][] InputData = Utg.ReadcsvFile("input.txt");
		String[][] TargetData = Utg.ReadcsvFile("target.txt");
		if (InputData.length == TargetData.length)
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
		else
		{
			System.out.println("Different number of input and target points");
		}

		// initializing neurons, weights and biases
		Nneurons[0] = input[0].length;
		Nneurons[Nlayers - 1] = target[0].length;
		Weight = new double[Nlayers - 1][][];
		Dweight = new double[Nlayers - 1][][];
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			Weight[layer] = new double[Nneurons[layer + 1]][Nneurons[layer]];
			Dweight[layer] = new double[Nneurons[layer + 1]][Nneurons[layer]];
		}
		Bias = new double[Nlayers][];
		neuronvalue = new double[Nlayers][];
		for (int layer = 0; layer <= Nlayers - 1; layer += 1)
		{
			Bias[layer] = new double[Nneurons[layer]];
			neuronvalue[layer] = new double[Nneurons[layer]];
		}
		for (int layer = 0; layer <= Nlayers - 2; layer += 1)
		{
			for (int ni = 0; ni <= Nneurons[layer + 1] - 1; ni += 1)
			{
				for (int nf = 0; nf <= Nneurons[layer] - 1; nf += 1)
				{
					// Weight[layer][ni][nf] = 0.1 * (layer + ni);
					Weight[layer][ni][nf] = Math.random();
				}
			}
		}
		for (int layer = 0; layer <= Nlayers - 1; layer += 1)
		{
			for (int ni = 0; ni <= Nneurons[layer] - 1; ni += 1)
			{
				neuronvalue[layer][ni] = 0;
				Bias[layer][ni] = 0.05;
			}
		}
		
		// initialize output and auxiliary variables
		output = new double[target.length][target[0].length];
		multvec = Utg.CalcProdVec(Nlayers, Nneurons, target);
		iter = 0;
		//ColorPalette = Utg.ColorPalette(2);
	}

	public void RunTraining()
	{
		if (iter <= Niter)
		{
			if (iter == 0)
			{
				Uts.PrintWeightsAndNeurons(neuronvalue, Weight);
			}
			/*
			 * There is another method, which is recording the weights here and using them
			 * in the forward propagation in the loop (the weights do not change during the
			 * iteration) However, the current method seems to perform better, converging in
			 * less iterations
			 */
			double preverror = Utg.Round(Uts.errorperc(output, target), 2) / 100;
			for (int in = 0; in <= input.length - 1; in += 1)
			{
				neuronvalue = Training.ForwardPropagation(input[in], Nneurons, Weight, Bias, ApplyBias);
				Dweight = Training.backpropagation(in, Nneurons, neuronvalue, Weight, target, multvec);
				Weight = Training.UpdateWeights(Nneurons, Lrate, Weight, Dweight);
				for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
				{
					output[in][n] = neuronvalue[Nlayers - 1][n];
				}
			}

			error = Utg.Round(Uts.errorperc(output, target), 2) / 100;
			if (AdaptativeLrate)
			{
				Lrate = Math.max(Math.min(Lrate + error / 100.0, 0.5), 0.05);
			}
			if (iter % 1 == 0)
			{
				//System.out.println("iter: " + (iter + 1) + " erro médio: " + Utg.Round(100 * error, 2) + "%");
				Utg.SaveTextFile("Weights", Weight);
			}
			derror = Math.abs(error - preverror);
			int inp = 0;
			for (int var = 0; var <= target[inp].length - 1; var += 1)
			{
				PlotError = Utg.AddElemToArrayUpTo(PlotError, 100 * error, 2000);
			}
			if (iter % 100 == 0)
			{
				SaveError.add(100 * error);
			}
			// System.out.println(Arrays.deepToString(output));
			iter += 1;
		}

		if (iter == Niter)
		{
			for (int in = 0; in <= input.length - 1; in += 1)
			{
				Training.ForwardPropagation(input[in], Nneurons, Weight, Bias, ApplyBias);
				output[in] = new double[Nneurons[Nlayers - 1]];
				for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1)
				{
					output[in][n] = neuronvalue[Nlayers - 1][n];
				}
			}
			double errorperc = Utg.Round(Uts.errorperc(output, target), 2);
			Uts.PrintANN(neuronvalue, Weight, Dweight, output, target, errorperc);
			Utg.SaveTextFile("Error", SaveError);
		}
	}

	public void DrawStuff()
	{
		//int inp = 0;
		int[] NGraphs = new int[] { 1, 1 };
		int[][] GraphPos = new int[NGraphs[0] * NGraphs[1]][2];

		DrawFunctions.DrawMenu(new Point( 200, 100 ), "Center", 300, 100, 2, new Color[] { COLOR_PALETTE[0], COLOR_PALETTE[1] }, COLOR_PALETTE[2]); // ANN info menu
		DrawFunctions.DrawANNInfo(new Point( 60, 70 ), iter, Nneurons, Uts.errorperc(output, target), ApplyBias, COLOR_PALETTE[2]);
		if (ShowANN)
		{
			DrawFunctions.DrawMenu(new Point( 350, 300 ), "Center", 500, 200, 2, new Color[] { COLOR_PALETTE[0], COLOR_PALETTE[4] }, COLOR_PALETTE[2]); // ANN menu
			DrawFunctions.DrawANN(new Point( 100, 200 ), new int[] { 500, 200 }, Nneurons, neuronvalue, Weight, true, COLOR_PALETTE[5]);
		}
		if (ShowGraphs)
		{
			DrawFunctions.DrawMenu(new Point( 125, 530 ), "Center", 200 * NGraphs[0], 200 * NGraphs[1], 2, new Color[] { COLOR_PALETTE[6], COLOR_PALETTE[3] }, COLOR_PALETTE[2]); // Graphs menu
			//System.out.println(Arrays.toString(NGraphs));
			for (int graphx = 0; graphx <= NGraphs[0] - 1; graphx += 1)
			{
				for (int graphy = 0; graphy <= NGraphs[1] - 1; graphy += 1)
				{
					//System.out.println(Arrays.toString(GraphPos[graphx * NGraphs[1] + graphy]));
					GraphPos[graphx * NGraphs[1] + graphy] = new int[] { 125 + 130 * graphx, 530 + 130 * graphy };
				}
			}
			int GraphSize = Math.min(150 / NGraphs[0], 150 / NGraphs[1]);
			for (int graph = 0; graph <= NGraphs[0] * NGraphs[1] - 1; graph += 1)
			{
				// double[] ScaledTargets = Utg.ScaledVector(Utg.Transpose(target)[graph], 0, 200);
				// double[] ScaledOutputs = Utg.ScaledVector(Utg.Transpose(output)[graph], 0, 200);
				List<Double> ScaledTargets = new ArrayList<>();
				List<Double> ScaledOutputs = new ArrayList<>();
				double[] targets = Utg.Transpose(target)[graph];
				double[] outputs = Utg.Transpose(output)[graph];
				for (int i = 0; i <= targets.length - 1; i+= 1)
				{
					ScaledTargets.add(targets[i]);
				}
				for (int i = 0; i <= outputs.length - 1; i+= 1)
				{
					ScaledOutputs.add(outputs[i]);
				}
				// DF.DrawMenu(GraphPos[graph], "Center", GraphSize, GraphSize, 2, new Color[]
				// {ColorPalette[6], ColorPalette[11]}, Color.black);
				DrawFunctions.PlotPoints(new int[] { GraphPos[graph][0] - GraphSize / 2, GraphPos[graph][1] + GraphSize / 2 }, "Results var " + String.valueOf(graph), GraphSize, Color.cyan, Color.blue, ScaledTargets, ScaledOutputs);
			}
			DrawFunctions.DrawMenu(new Point ( 575, 525 ), "Center", 150, 150, 2, new Color[] { COLOR_PALETTE[6], COLOR_PALETTE[3] }, COLOR_PALETTE[2]); // Error menu
			// DF.DrawDynGraph(new int[] {525, 575}, "error (%)", new double[][] {PlotError}, new Color[] {ColorPalette[0]});
			
			DrawFunctions.DrawTargetGraph(new Point(575, 525), 100, Uts.calcError(output, target)) ;
			System.out.println(Uts.errorperc2(output, target));
			DrawFunctions.DrawAccuracyBar(new Point( 450, 600 ), new Dimension ( 20, 100 ), Uts.errorperc2(output, target)) ;
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		DrawFunctions.setG((Graphics2D) g);
		if (RunTraining)
		{
			RunTraining();
		}
		DrawStuff();
	}
	
	/*private void initComponents()
	{
		this.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent evt)
			{

			}

			public void mouseReleased(MouseEvent evt)
			{
				// mouseReleased(evt);
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent evt)
			{
				// mouseDragged(evt);
			}
		});
		this.addMouseWheelListener(new MouseWheelListener()
		{
			@Override
			public void mouseWheelMoved(MouseWheelEvent evt)
			{

			}
		});
		this.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent evt)
			{
				int key = evt.getKeyCode();
				System.out.println("Key pressed");
				if (key == KeyEvent.VK_ESCAPE)
				{

				}
			}

			@Override
			public void keyReleased(KeyEvent evt)
			{

			}

			@Override
			public void keyTyped(KeyEvent evt)
			{

			}
		});
	}*/

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == timer)
		{
			repaint();
		}
	}
}
