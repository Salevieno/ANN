package main;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import utilities.Utg;

public class MainANN extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final Dimension frameSize = new Dimension(700, 700);
	private static JPanel ANNpanel;
	
	private boolean RunTraining = true, ShowANN = true, ShowGraphs = true;

	public void AddButtons()
	{
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

		// Defining button actions
		PlayButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				RunTraining = !RunTraining;
				System.out.println(1);
			}
		});
		NNButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowANN = !ShowANN;
			}
		});
		GraphsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ShowGraphs = !ShowGraphs;
			}
		});
	}
	
	public MainANN()
	{
		ANNpanel = new ANN(new Dimension(700, 600));
		this.setTitle("Rede neural");
		this.setSize(frameSize);
		this.setLayout(new FlowLayout());
		AddButtons();
		this.add(ANNpanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main (String[] args) 
	{
		new MainANN();	
	}
}
