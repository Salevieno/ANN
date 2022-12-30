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

import utilities.Utg;

public class MainANN extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final Dimension frameSize = new Dimension(700, 700);
	private ANN ann;

	public void addButtons()
	{
		/* Defining Button Icons */
		String imagesPath = ".\\Icons\\";
		ImageIcon playIcon = new ImageIcon(imagesPath + "playIcon.png");
		ImageIcon trainIcon = new ImageIcon(imagesPath + "testIcon.png");
		ImageIcon testIcon = new ImageIcon(imagesPath + "trainIcon.png");
		ImageIcon displayNeuronsIcon = new ImageIcon(imagesPath + "displayNeuronsIcon.png");
		ImageIcon displayGraphsIcon = new ImageIcon(imagesPath + "displayGraphsIcon.png");

		/* Defining Buttons */
		Color backgroundColor = new Color(100, 0, 80) ;
		Dimension buttonSize = new Dimension(32, 32) ;
		JButton playButton = Utg.addButton(playIcon, new int[2], buttonSize, backgroundColor);
		JButton trainButton = Utg.addButton(trainIcon, new int[2], buttonSize, backgroundColor);
		JButton testButton = Utg.addButton(testIcon, new int[2], buttonSize, backgroundColor);
		JButton displayNeuronsButton = Utg.addButton(displayNeuronsIcon, new int[2], buttonSize, backgroundColor);
		JButton displayGraphsButton = Utg.addButton(displayGraphsIcon, new int[2], buttonSize, backgroundColor);
		Container cp = getContentPane();
		cp.add(playButton);
		cp.add(trainButton);
		cp.add(testButton);
		cp.add(displayNeuronsButton);
		cp.add(displayGraphsButton);

		// Defining button actions
		playButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//RunTraining = !RunTraining;
				if (ann.state.equals(ANNStates.training))
				{
					//ann.state = ANNStates.paused ;
				}
				else if (ann.state.equals(ANNStates.paused))
				{
					//ann.state = ANNStates.training ;
					ann.repaint();
				}					
			}
		});
		trainButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//RunTraining = !RunTraining;
				if (ann.state.equals(ANNStates.training))
				{
					ann.state = ANNStates.paused ;
				}
				else if (ann.state.equals(ANNStates.paused))
				{
					ann.state = ANNStates.training ;
					ann.repaint();
				}					
			}
		});
		testButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//RunTraining = !RunTraining;
				if (ann.state.equals(ANNStates.testing))
				{
					ann.state = ANNStates.paused ;
				}
				else if (ann.state.equals(ANNStates.paused))
				{
					ann.state = ANNStates.testing ;
					ann.repaint();
				}					
			}
		});
		displayNeuronsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ann.showANN = !ann.showANN;
			}
		});
		displayGraphsButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ann.showGraphs = !ann.showGraphs;
			}
		});
	}
	
	public MainANN()
	{
		ann = new ANN(frameSize);
		this.setTitle("Rede neural");
		this.setSize(frameSize);
		this.setLayout(new FlowLayout());
		addButtons();
		this.add(ann);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static void main (String[] args) 
	{
		new MainANN();	
	}
}
