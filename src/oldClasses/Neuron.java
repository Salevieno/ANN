package oldClasses;

public class Neuron 
{
	private int id;
	private int layer;
	private double[] weight;
	private double bias;
	private double value;
	
	public Neuron (int id, int layer, double[] weight, double bias, double value)
	{
		this.id = id;
		this.layer = layer;
		this.weight = weight;
		this.bias = bias;
		this.value = value;
	}

	public int getid() {return id;}
	public int getlayer() {return layer;}
	public double[] getweight() {return weight;}
	public double getvalue() {return value;}
	public double getbias() {return bias;}
	public void setid(int i) {id = i;}
	public void setlayer(int l) {layer = l;}
	public void setweight(double[] w) {weight = w;}
	public void setbias(double b) {bias = b;}
	public void setvalue(double v) {value = v;}
}
