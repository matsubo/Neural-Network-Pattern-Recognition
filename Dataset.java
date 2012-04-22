/*
 * 作成日: 2004/01/18
 * $Id: Dataset.java,v 1.2 2004/01/18 23:41:34 matsu Exp $
 */

/**
 * @author matsu
 *
 * ニューラルネットワークへ渡すデータセット
 */
public class Dataset {
	private double[] input;
	private double[] teach;

	public double getInput(int i) {
		return input[i];
	}

	public double getTech(int i) {
		return teach[i];
	}

	public Dataset(double[] input) {
		this.input = new double[input.length];
		for (int i = 0; i < input.length; i++)
			this.input[i] = input[i];
	}

	public Dataset(double[] input, double[] teach) {
		this.input = new double[input.length];
		this.teach = new double[teach.length];
		for (int i = 0; i < input.length; i++)
			this.input[i] = input[i];
		for (int t = 0; t < teach.length; t++)
			this.teach[t] = teach[t];

	}
}
