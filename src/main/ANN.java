package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	protected ANNStates state ;
	
	public static final Color[] COLOR_PALETTE = new Color[]
								{
										new Color(63, 40, 231),
										new Color(137, 249, 204),
										new Color(11, 11, 11),
										new Color(245, 117, 170),
										new Color(241, 199, 128),
										new Color(101, 131, 246),
										new Color(76, 131, 42)
								};

	private int currentIteration; 		// number of the current iteration
	private int maxNumberIterations; 	// maximum number of iterations
	private int[] numberNeurons;	 	// number of neurons in each layer
	private int numberLayers; 			// number of layers
	private double learningRate; 		// learning rate
	private double[][] input; 			// input data
	private double[][] targets; 		// target data
	private double[][] outputs; 		// ANN current outputs
	private double[][] neuronvalues;	// value of each neuron (after applying the activation function)
	private double[][][] weights; 		// weights
	private double[][][] dWeights; 		// Delta weight, change in weight during the back propagation. Dw[Layer][Index of origin neuron][Index of desination neuron]
	private double[][] biases; 			// biases
	private double error; 				// current error
	//private double accuracyChangeRate; 			// current rate of change in accuracy
	private double errortol; 					// minimum error (stop if lower)
	//private double accuracyChangeRateTolerance; // minimum variation in accuracy between iterations (stop if lower)
	private int[] multvec; 						// product of the number of neurons in each layer, starts from output

	protected boolean showANN ;
	protected boolean showGraphs ;
	protected boolean applyBias ;
	protected boolean adaptativeLrate ;
	
	private List<Double> saveError ;

	public ANN(Dimension panelDimension)
	{
		this.setBackground(new Color(250, 240, 220));
		this.setPreferredSize(panelDimension);
		this.setVisible(true);
		
		initialize();

		timer = new Timer(0, this);
		timer.start();
	}
	
	public void initialize()
	{		
		// initialize ann parameters
		state = ANNStates.paused ;
		//RunTraining = true;
		showANN = true;
		showGraphs = true;
		applyBias = false;
		adaptativeLrate = true;
		saveError = new ArrayList<>();
		
		// getting parameters file data
		JSONObject parametersData = (JSONObject) Utg.ReadJson("Parameters.json") ;
		maxNumberIterations = (int) (long) parametersData.get("NumberMaxIterations") ;
		JSONArray numberNeuronsData = (JSONArray) parametersData.get("NumberNeurons") ;
		numberNeurons = new int[numberNeuronsData.size() + 2];
		numberLayers = numberNeuronsData.size() + 2;
		for (int layer = 1; layer <= numberNeuronsData.size(); layer += 1)
		{
			numberNeurons[layer] = (int) (long) numberNeuronsData.get(layer - 1);
		}
		learningRate = (double) parametersData.get("InitialLearningRate") ;
		//accuracyChangeRateTolerance = (double) parametersData.get("AccuracyChangeRateTolerance") ;
		errortol = (double) parametersData.get("ErrorTotalTolerance") ;
		error = errortol + 0.01;
		//accuracyChangeRate = accuracyChangeRateTolerance + 0.0001;
		
		// getting input and target files data
		String[][] InputData = Utg.ReadcsvFile("input.txt");
		String[][] TargetData = Utg.ReadcsvFile("target.txt");
		if (InputData.length == TargetData.length)
		{
			input = new double[InputData.length - 1][];
			targets = new double[TargetData.length - 1][];
			for (int point = 0; point <= InputData.length - 2; point += 1)
			{
				input[point] = new double[InputData[point + 1].length];
				for (int i = 0; i <= InputData[point + 1].length - 1; i += 1)
				{
					input[point][i] = Double.parseDouble(InputData[point + 1][i]);
				}
			}
			for (int point = 0; point <= TargetData.length - 2; point += 1)
			{
				targets[point] = new double[TargetData[point + 1].length];
				for (int i = 0; i <= TargetData[point + 1].length - 1; i += 1)
				{
					targets[point][i] = Double.parseDouble(TargetData[point + 1][i]);
				}
			}
		}
		else
		{
			System.out.println("Different number of input and target points");
		}

		// initializing neurons, weights and biases
		numberNeurons[0] = input[0].length;
		numberNeurons[numberLayers - 1] = targets[0].length;
		weights = new double[numberLayers - 1][][];
		dWeights = new double[numberLayers - 1][][];
		for (int layer = 0; layer <= numberLayers - 2; layer += 1)
		{
			weights[layer] = new double[numberNeurons[layer + 1]][numberNeurons[layer]];
			dWeights[layer] = new double[numberNeurons[layer + 1]][numberNeurons[layer]];
		}
		biases = new double[numberLayers][];
		neuronvalues = new double[numberLayers][];
		for (int layer = 0; layer <= numberLayers - 1; layer += 1)
		{
			biases[layer] = new double[numberNeurons[layer]];
			neuronvalues[layer] = new double[numberNeurons[layer]];
		}
		for (int layer = 0; layer <= numberLayers - 2; layer += 1)
		{
			for (int ni = 0; ni <= numberNeurons[layer + 1] - 1; ni += 1)
			{
				for (int nf = 0; nf <= numberNeurons[layer] - 1; nf += 1)
				{
					 weights[layer][ni][nf] = 0.1 * (layer + ni);
					//weights[layer][ni][nf] = Math.random();
				}
			}
		}
		for (int layer = 0; layer <= numberLayers - 1; layer += 1)
		{
			for (int ni = 0; ni <= numberNeurons[layer] - 1; ni += 1)
			{
				neuronvalues[layer][ni] = 0;
				biases[layer][ni] = 0.05;
			}
		}
		
		// initialize output and auxiliary variables
		outputs = new double[targets.length][targets[0].length];
		multvec = Utg.CalcProdVec(numberLayers, numberNeurons, targets);
		currentIteration = 0;
	}

	private void updateLearningRate()
	{
		learningRate = Math.max(Math.min(learningRate + error / 100.0, 0.5), 0.05);
	}
	
	private void updateOutputs(int point, int numberLayers, double[] lastLayerNeurons)
	{
		for (int n = 0; n <= outputs[point].length - 1; n += 1)
		{
			outputs[point][n] = lastLayerNeurons[n];
		}
	}
	
	protected void runTraining()
	{
		if (currentIteration <= maxNumberIterations) // accuracyChangeRateTolerance <= accuracyChangeRate
		{
			if (currentIteration == 0)
			{
				Uts.PrintWeightsAndNeurons(neuronvalues, weights);
			}
			/*
			 * There is another method, which is recording the weights here and using them
			 * in the forward propagation in the loop (the weights do not change during the
			 * iteration) However, the current method seems to perform better, converging in
			 * less iterations
			 */
			
			// record the error in the previous iteration
			//double previousError = Utg.Round(Uts.calcErrorPerc(outputs, targets), 2) / 100;
			
			// perform the training itself
			for (int point = 0; point <= input.length - 1; point += 1)
			{
				neuronvalues = Training.forwardPropagation(input[point], numberNeurons, weights, biases, applyBias);
				dWeights = Training.backpropagation(point, numberNeurons, neuronvalues, weights, targets, multvec);
				weights = Training.updateWeights(numberNeurons, learningRate, weights, dWeights);
				updateOutputs(point, numberLayers, neuronvalues[numberLayers - 1]) ;
			}

			// record the results of this training iteration
			error = Utg.Round(Uts.calcErrorPerc(outputs, targets), 2) / 100;
			if (adaptativeLrate)
			{
				updateLearningRate();				
			}
			//accuracyChangeRate = Math.abs(error - previousError);
			Results.updatePlotError(100 * error, 10) ;
			Results.recordSaveError(currentIteration, 100, error) ;
			currentIteration += 1;
			repaint();
		}

		if (currentIteration == maxNumberIterations)
		{
			for (int point = 0; point <= input.length - 1; point += 1)
			{
				Training.forwardPropagation(input[point], numberNeurons, weights, biases, applyBias);
				updateOutputs(point, numberLayers, neuronvalues[numberLayers - 1]) ;
			}
			error = Utg.Round(Uts.calcErrorPerc(outputs, targets), 2) / 100;
			double errorperc = 100 * error ;
			Uts.PrintANN(neuronvalues, weights, dWeights, outputs, targets, errorperc);
			Utg.SaveTextFile("Error", saveError);
		}
	}
	
	protected void runTesting()
	{
		// performs a forward propagation for each input point and updates the outputs
		for (int point = 0; point <= input.length - 1; point += 1)
		{
			neuronvalues = Training.forwardPropagation(input[point], numberNeurons, weights, biases, applyBias);
			updateOutputs(point, numberLayers, neuronvalues[numberLayers - 1]) ;
		}
		
		// records the total error of the testing
		error = Utg.Round(Uts.calcErrorPerc(outputs, targets), 2) / 100;
	}

	private void drawStuff()
	{
		Point infoMenuPos = new Point(180, 60) ;
		Dimension infoMenuSize = new Dimension(300, 100) ;
		int paddingY = 10 ;
		DrawFunctions.DrawMenu(infoMenuPos, infoMenuSize, new Color[] { ANN.COLOR_PALETTE[0], ANN.COLOR_PALETTE[1] }, ANN.COLOR_PALETTE[2]);
		DrawFunctions.DrawANNInfo(new Point(infoMenuPos.x - infoMenuSize.width / 2, infoMenuPos.y - infoMenuSize.height / 2 + paddingY), currentIteration, numberNeurons, 100 * error, applyBias, COLOR_PALETTE[2]);
		if (showANN)
		{
			Point annPos = new Point(350, 250) ;
			Dimension annSize = new Dimension(500, 200) ;
			DrawFunctions.DrawMenu(annPos, annSize, new Color[] { COLOR_PALETTE[0], COLOR_PALETTE[4] }, COLOR_PALETTE[2]); // ANN menu
			DrawFunctions.DrawANN(new Point(annPos.x - annSize.width / 2, annPos.y - annSize.height / 2), annSize, numberNeurons, neuronvalues, weights, true, COLOR_PALETTE[5]);
		}
		if (showGraphs)
		{
			// creates a grid of graphs
			Point graphsMenuPos = new Point(125, 480) ;
			int[] numberGraphs = new int[] { 1, 1 };	// number of graphs in the x and y directions (rows and columns)
			int[][] posGraphs = new int[numberGraphs[0] * numberGraphs[1]][2];
			DrawFunctions.DrawMenu(graphsMenuPos, new Dimension(200 * numberGraphs[0], 200 * numberGraphs[1]), new Color[] { COLOR_PALETTE[6], COLOR_PALETTE[3] }, COLOR_PALETTE[2]); // Graphs menu
			//System.out.println(Arrays.toString(NGraphs));
			for (int graphx = 0; graphx <= numberGraphs[0] - 1; graphx += 1)
			{
				for (int graphy = 0; graphy <= numberGraphs[1] - 1; graphy += 1)
				{
					//System.out.println(Arrays.toString(GraphPos[graphx * NGraphs[1] + graphy]));
					posGraphs[graphx * numberGraphs[1] + graphy] = new int[] {graphsMenuPos.x + 130 * graphx, graphsMenuPos.y + 130 * graphy};
				}
			}
			int GraphSize = Math.min(150 / numberGraphs[0], 150 / numberGraphs[1]);
			for (int graph = 0; graph <= numberGraphs[0] * numberGraphs[1] - 1; graph += 1)
			{
				// double[] ScaledTargets = Utg.ScaledVector(Utg.Transpose(target)[graph], 0, 200);
				// double[] ScaledOutputs = Utg.ScaledVector(Utg.Transpose(output)[graph], 0, 200);
				List<Double> ScaledTargets = new ArrayList<>();
				List<Double> ScaledOutputs = new ArrayList<>();
				double[] target = Utg.Transpose(targets)[graph];
				double[] output = Utg.Transpose(outputs)[graph];
				for (int i = 0; i <= targets.length - 1; i+= 1)
				{
					ScaledTargets.add(target[i]);
				}
				for (int i = 0; i <= outputs.length - 1; i+= 1)
				{
					ScaledOutputs.add(output[i]);
				}
				// DF.DrawMenu(GraphPos[graph], "Center", GraphSize, GraphSize, 2, new Color[]
				// {ColorPalette[6], ColorPalette[11]}, Color.black);
				DrawFunctions.PlotPoints(new int[] { posGraphs[graph][0] - GraphSize / 2, posGraphs[graph][1] + GraphSize / 2 }, "Results var " + String.valueOf(graph), GraphSize, Color.cyan, Color.blue, ScaledTargets, ScaledOutputs);
			}
			Point targetGraphCenter = new Point(graphsMenuPos.x + 450, graphsMenuPos.y) ;
			Dimension targetMenuSize = new Dimension(150, 150) ;
			int targetGraphSize = 100 ;
			DrawFunctions.DrawMenu(targetGraphCenter, targetMenuSize, new Color[] { COLOR_PALETTE[6], COLOR_PALETTE[3] }, COLOR_PALETTE[2]); // Error menu
			DrawFunctions.DrawTargetGraph(targetGraphCenter, targetGraphSize, Uts.calcError(outputs, targets)) ;
			DrawFunctions.DrawAccuracyBar(new Point(graphsMenuPos.x + 320, graphsMenuPos.y + targetMenuSize.height / 2), new Dimension ( 20, targetGraphSize ), Uts.errorperc2(outputs, targets)) ;

			//System.out.println("accuracy change rate: " + accuracyChangeRateTolerance + " <= " + accuracyChangeRate);
			// DF.DrawDynGraph(new int[] {525, 575}, "error (%)", new double[][] {PlotError}, new Color[] {ColorPalette[0]});			
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		DrawFunctions.setG((Graphics2D) g);
		switch (state)
		{
			case training:
			{
				runTraining();
				
				break ;
			}
			case testing:
			{
				// TODO implement testing
				
				break ;
			}
			case usable:
			{
				// TODO implement usable
				
				break ;
			}
			case paused:
			{				
				break ;
			}
		}
		drawStuff();
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
			//repaint();
		}
	}
}
