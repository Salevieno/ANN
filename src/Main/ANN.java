package Main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import Graphics.DrawFunctions;

public class ANN extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JPanel jPanel2;
	private int[] WinDim; // dimensions of the main window

	private int iter; // number of the current iteration
	private int Niter; // maximum number of iterations
	private int[] Nneurons; // number of neurons in each layer
	private int Nlayers; // number of layers
	private double Lrate; // learning rate
	private double[][] input; // input data
	private double[][] target; // target data
	private double[][] output; // ANN current outputs
	private double[][] neuronvalue; // value of each neuron (after applying the activation function)
	private double[][][] Weight; // weights
	private double[][][] Dweight; // Delta weight, change in weight during the back propagation. Dw[Layer][Index
									// of origin neuron][Index of desination neuron]
	private double[][] Bias; // biases
	private double error; // current error
	private double derror; // current change in error
	private double errortol; // minimum error (stop if lower)
	private double derrortol; // minimum variation in error between iterations (stop if lower)
	private int[] multvec; // product of the number of neurons in each layer, starts from output

	private boolean RunTraining = false, ShowANN = true, ShowGraphs = true;
	private boolean ApplyBias = false, AdaptativeLrate = true;
	private double[] SaveError = null;
	private double[] PlotError = null;
	private Color[] ColorPalette;
	private ANNTraining Train;
	private DrawFunctions DF;

	public ANN(int[] WinDim) {
		this.WinDim = WinDim;
		initComponents(); // Creates a JPanel inside the JFrame
		AddButtons();
		Initialization();
		setTitle("Rede neural"); // Set main window title
		setSize(WinDim[0], WinDim[1]); // Set main window size
		setVisible(true); // Show main window
	}

	public void Initialization() {
		String[][] ParametersData = Utg.ReadcsvFile("ANNparameters.txt");
		Niter = Integer.parseInt(ParametersData[1][0]);
		Nneurons = new int[ParametersData[2].length + 2];
		Nlayers = ParametersData[2].length + 2;
		for (int layer = 1; layer <= ParametersData[2].length; layer += 1) {
			Nneurons[layer] = Integer.parseInt(ParametersData[2][layer - 1]);
		}
		Lrate = Double.parseDouble(ParametersData[3][0]);
		derrortol = Double.parseDouble(ParametersData[4][0]);
		errortol = Double.parseDouble(ParametersData[5][0]);
		error = errortol + 0.01;
		derror = derrortol + 1;

		String[][] InputData = Utg.ReadcsvFile("input.txt");
		String[][] TargetData = Utg.ReadcsvFile("target.txt");
		if (InputData.length != TargetData.length) {
			System.out.println("Different number of input and target points");
		} else {
			input = new double[InputData.length - 1][];
			target = new double[TargetData.length - 1][];
			for (int i = 0; i <= InputData.length - 2; i += 1) {
				input[i] = new double[InputData[i + 1].length];
				for (int i2 = 0; i2 <= InputData[i + 1].length - 1; i2 += 1) {
					input[i][i2] = Double.parseDouble(InputData[i + 1][i2]);
				}
			}
			for (int i = 0; i <= TargetData.length - 2; i += 1) {
				target[i] = new double[TargetData[i + 1].length];
				for (int i2 = 0; i2 <= TargetData[i + 1].length - 1; i2 += 1) {
					target[i][i2] = Double.parseDouble(TargetData[i + 1][i2]);
				}
			}
		}

		/*
		 * input = new double[10][]; target = new double[10][]; for (int i = 0; i <= 10
		 * - 1; i += 1) { input[i] = Uts.ImagePixels(".\\InputImages\\Input" + (i + 1) +
		 * ".jpg"); } target[0] = new double[] {0}; target[1] = new double[] {1};
		 * target[2] = new double[] {0}; target[3] = new double[] {1}; target[4] = new
		 * double[] {0}; target[5] = new double[] {1}; target[6] = new double[] {0};
		 * target[7] = new double[] {1}; target[8] = new double[] {0}; target[9] = new
		 * double[] {1};
		 */

		Nneurons[0] = input[0].length;
		Nneurons[Nlayers - 1] = target[0].length;
		Weight = new double[Nlayers - 1][][];
		Dweight = new double[Nlayers - 1][][];
		for (int layer = 0; layer <= Nlayers - 2; layer += 1) {
			Weight[layer] = new double[Nneurons[layer + 1]][Nneurons[layer]];
			Dweight[layer] = new double[Nneurons[layer + 1]][Nneurons[layer]];
		}
		Bias = new double[Nlayers][];
		neuronvalue = new double[Nlayers][];
		for (int layer = 0; layer <= Nlayers - 1; layer += 1) {
			Bias[layer] = new double[Nneurons[layer]];
			neuronvalue[layer] = new double[Nneurons[layer]];
		}
		for (int layer = 0; layer <= Nlayers - 2; layer += 1) {
			for (int ni = 0; ni <= Nneurons[layer + 1] - 1; ni += 1) {
				for (int nf = 0; nf <= Nneurons[layer] - 1; nf += 1) {
					// Weight[layer][ni][nf] = 0.1 * (layer + ni);
					Weight[layer][ni][nf] = Math.random();
				}
			}
		}
		for (int layer = 0; layer <= Nlayers - 1; layer += 1) {
			for (int ni = 0; ni <= Nneurons[layer] - 1; ni += 1) {
				neuronvalue[layer][ni] = 0;
				Bias[layer][ni] = 0.05;
			}
		}
		output = new double[target.length][target[0].length];
		multvec = Utg.CalcProdVec(Nlayers, Nneurons, target);
		iter = 0;
		Train = new ANNTraining();
		ColorPalette = Utg.ColorPalette(2);
	}

	public void AddButtons() {
		/* Defining Button Icons */
		String ImagesPath = ".\\Icons\\";
		ImageIcon PlayIcon = new ImageIcon(ImagesPath + "PlayIcon.png");
		ImageIcon NNIcon = new ImageIcon(ImagesPath + "NNIcon.png");
		ImageIcon GraphsIcon = new ImageIcon(ImagesPath + "GraphsIcon.png");

		/* Defining Buttons */
		Color BackgroundColor = Color.cyan;
		JButton PlayButton = Utg.AddButton(PlayIcon, new int[2], new int[] { 30, 30 }, BackgroundColor);
		JButton NNButton = Utg.AddButton(NNIcon, new int[2], new int[] { 30, 30 }, BackgroundColor);
		JButton GraphsButton = Utg.AddButton(GraphsIcon, new int[2], new int[] { 30, 30 }, BackgroundColor);
		Container cp = getContentPane();
		cp.add(PlayButton);
		cp.add(NNButton);
		cp.add(GraphsButton);

		/* Defining button actions */
		PlayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RunTraining = !RunTraining;
			}
		});
		NNButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowANN = !ShowANN;
			}
		});
		GraphsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowGraphs = !ShowGraphs;
			}
		});
	}

	public void RunTraining() {
		if (RunTraining) {
			if (iter <= Niter) {
				if (iter == 0) {
					Uts.PrintWeightsAndNeurons(neuronvalue, Weight);
				}
				/*
				 * There is another method, which is recording the weights here and using them
				 * in the forward propagation in the loop (the weights do not change during the
				 * iteration) However, the current method seems to perform better, converging in
				 * less iterations
				 */
				double preverror = Utg.Round(Uts.errorperc(input, Nlayers, Nneurons, output, target, Weight, Bias), 2)
						/ 100;
				for (int in = 0; in <= input.length - 1; in += 1) {
					neuronvalue = Train.ForwardPropagation(input[in], Nneurons, Weight, Bias, ApplyBias);
					Dweight = Train.backpropagation(in, Nneurons, neuronvalue, Weight, target, multvec);
					Weight = Train.UpdateWeights(Nneurons, Lrate, Weight, Dweight);
					for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1) {
						output[in][n] = neuronvalue[Nlayers - 1][n];
					}
				}

				error = Utg.Round(Uts.errorperc(input, Nlayers, Nneurons, output, target, Weight, Bias), 2) / 100;
				if (AdaptativeLrate) {
					Lrate = Math.max(Math.min(Lrate + error / 100.0, 0.5), 0.05);
				}
				if (iter % 1 == 0) {
					System.out.println("iter: " + (iter + 1) + " erro médio: " + Utg.Round(100 * error, 2) + "%");
					Utg.SaveTextFile("Weights", Weight);
				}
				derror = Math.abs(error - preverror);
				int inp = 0;
				for (int var = 0; var <= target[inp].length - 1; var += 1) {
					PlotError = Utg.AddElemToArrayUpTo(PlotError, 100 * error, 2000);
				}
				if (iter % 100 == 0) {
					SaveError = Utg.AddElem(SaveError, 100 * error);
				}
				// System.out.println(Arrays.deepToString(output));
				iter += 1;
			}

			if (iter == Niter) {
				for (int in = 0; in <= input.length - 1; in += 1) {
					Train.ForwardPropagation(input[in], Nneurons, Weight, Bias, ApplyBias);
					output[in] = new double[Nneurons[Nlayers - 1]];
					for (int n = 0; n <= Nneurons[Nlayers - 1] - 1; n += 1) {
						output[in][n] = neuronvalue[Nlayers - 1][n];
					}
				}
				double errorperc = Utg.Round(Uts.errorperc(input, Nlayers, Nneurons, output, target, Weight, Bias), 2);
				Uts.PrintANN(neuronvalue, Weight, Dweight, output, target, errorperc);
				Utg.SaveTextFile("Error", SaveError);
			}
		}
	}

	public void DrawStuff(int[] WinDim)
	{
		int inp = 0;
		int[] NGraphs = new int[] { 1, 1 };
		int[][] GraphPos = new int[NGraphs[0] * NGraphs[1]][2];

		DF.DrawMenu(new int[] { 200, 100 }, "Center", 300, 100, 2, new Color[] { ColorPalette[5], ColorPalette[8] }, ColorPalette[9]); // ANN info menu
		DF.DrawANNInfo(new int[] { 60, 70 }, iter, Nneurons, Uts.errorperc(input, Nlayers, Nneurons, output, target, Weight, Bias), ApplyBias, ColorPalette[9]);
		if (ShowANN)
		{
			DF.DrawMenu(new int[] { 350, 300 }, "Center", 500, 200, 2, new Color[] { ColorPalette[5], ColorPalette[17] }, ColorPalette[9]); // ANN menu
			DF.DrawANN(new int[] { 100, 200 }, new int[] { 500, 200 }, Nneurons, neuronvalue, Weight, true, ColorPalette[22]);
		}
		if (ShowGraphs)
		{
			DF.DrawMenu(new int[] { 125, 530 }, "Center", 200 * NGraphs[0], 200 * NGraphs[1], 2, new Color[] { ColorPalette[25], ColorPalette[12] }, ColorPalette[9]); // Graphs menu
			System.out.println(Arrays.toString(NGraphs));
			for (int graphx = 0; graphx <= NGraphs[0] - 1; graphx += 1)
			{
				for (int graphy = 0; graphy <= NGraphs[1] - 1; graphy += 1)
				{
					System.out.println(Arrays.toString(GraphPos[graphx * NGraphs[1] + graphy]));
					GraphPos[graphx * NGraphs[1] + graphy] = new int[] { 125 + 130 * graphx, 530 + 130 * graphy };
				}
			}
			int GraphSize = Math.min(150 / NGraphs[0], 150 / NGraphs[1]);
			for (int graph = 0; graph <= NGraphs[0] * NGraphs[1] - 1; graph += 1)
			{
				// double[] ScaledTargets = Utg.ScaledVector(Utg.Transpose(target)[graph], 0, 200);
				// double[] ScaledOutputs = Utg.ScaledVector(Utg.Transpose(output)[graph], 0, 200);
				double[] ScaledTargets = Utg.Transpose(target)[graph];
				double[] ScaledOutputs = Utg.Transpose(output)[graph];
				// DF.DrawMenu(GraphPos[graph], "Center", GraphSize, GraphSize, 2, new Color[]
				// {ColorPalette[25], ColorPalette[11]}, Color.black);
				DF.PlotPoints(new int[] { GraphPos[graph][0] - GraphSize / 2, GraphPos[graph][1] + GraphSize / 2 }, "Results var " + String.valueOf(graph), GraphSize, Color.cyan, Color.blue, ScaledTargets, ScaledOutputs);
			}
			DF.DrawMenu(new int[] { 575, 525 }, "Center", 150, 150, 2, new Color[] { ColorPalette[25], ColorPalette[12] }, ColorPalette[9]); // Error menu
			// DF.DrawDynGraph(new int[] {525, 575}, "error (%)", new double[][] {PlotError}, new Color[] {ColorPalette[5]});
		}
	}

	class Panel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		Panel() {
			setPreferredSize(new Dimension(700, 700)); // set a preferred size for the custom panel.
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			DF = new DrawFunctions(g);
			if (RunTraining) {
				RunTraining();
			}
			DrawStuff(WinDim);
			repaint();
		}
	}

	private void initComponents() {
		jPanel2 = new Panel(); // we want a custom Panel2, not a generic JPanel!
		jPanel2.setBackground(new Color(250, 240, 220));
		jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		jPanel2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {

			}

			public void mouseReleased(MouseEvent evt) {
				// mouseReleased(evt);
			}
		});
		jPanel2.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent evt) {
				// mouseDragged(evt);
			}
		});
		jPanel2.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent evt) {

			}
		});
		jPanel2.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent evt) {
				int key = evt.getKeyCode();
				System.out.println(1);
				if (key == KeyEvent.VK_ESCAPE) {

				}
			}

			@Override
			public void keyReleased(KeyEvent evt) {

			}

			@Override
			public void keyTyped(KeyEvent evt) {

			}
		});
		this.setContentPane(jPanel2); // add the component to the frame to see it!
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
