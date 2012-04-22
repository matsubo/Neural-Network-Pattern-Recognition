import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
/*
 * 作成日: 2004/01/18
 * $Id: NeuralNetwork.java,v 1.3 2004/01/19 09:40:58 matsu Exp $
 */

/**
 * @author matsu
 *
 * ニューラルネットワーク
 */
public class NeuralNetwork implements Serializable{

	/** 入力層の数 */
	private int IN = 8 * 8;
	/** 隠れ層の数 */
	private int HID = 8 * 8;
	/** 出力層の数 */
	private int OUT = 26;

	/** これ以下の誤差なら収束 */
	private double ERR_LIMIT = 0.1;

	private double ALPHA = 0.4;
	private double BETA = 0.3;

	/** 学習回数の最大値 */
	private double TIMES = 50000;

	/** 入力層の出力値 */
	private double[] I = new double[IN];
	/** 隠れ層の出力値 */
	private double[] H = new double[HID];
	/** 出力層の出力値 */
	private double[] O = new double[OUT];

	/** 入力層と隠れ層間の重み */
	private double[][] W = new double[HID][IN];
	/** 隠れ層と出力層間の重み */
	private double[][] V = new double[OUT][HID];

	/** 隠れ層の閾値 */
	private double[] theta = new double[HID];
	/** 出力層の閾値 */
	private double[] gamma = new double[OUT];

	/** 出力層の出力値と教師信号の誤差 */
	private double[] delta = new double[OUT];
	/** 隠れ層の出力値と出力層から戻ってきた値の誤差 */
	private double[] sigma = new double[HID];
	/** 教師信号 */
	private double[] T = new double[OUT];

	/**
	 * 重みを初期化する
	 */
	public void initWeight(double[][] w) {
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				// [-0.1, 0.1] の乱数
				w[i][j] = (Math.random() - 0.5) * 2.0;
			}
		}
	}

	/**
	 * 重みを表示する
	 */
	public void showWeight(double[][] w) {
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				System.out.print(w[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * 閾値を初期化する
	 */
	public void initThreshold(double[] threshold) {
		for (int i = 0; i < threshold.length; i++) {
			// [-0.1, 0.1] の乱数
			threshold[i] = (Math.random() - 0.5) * 2.0;
		}
	}

	/**
	 * 閾値を表示する
	 */
	private void showThreshold(double[] threshold) {
		for (int i = 0; i < threshold.length; i++) {
			System.out.print(threshold[i] + " ");
		}
		System.out.println();
	}

	/**
	 * シグモイド関数
	 */
	private double sigmoid(double x) {
		return 1.0 / (1 + Math.exp(-x));
	}

	/**
	 * 実験
	 */
	public void forward(Dataset d) {
		// 入力層の出力を求める
		for (int i = 0; i < IN; i++) {
			Log.debug(String.valueOf(i));
			I[i] = d.getInput(i);
		}

		// 中間層の出力を求める
		for (int j = 0; j < HID; j++) {
			double temp = 0.0;
			for (int i = 0; i < IN; i++)
				temp += W[j][i] * I[i];
			temp += theta[j];
			H[j] = sigmoid(temp);
		}

		// 出力層の出力を求める
		for (int k = 0; k < OUT; k++) {
			double temp = 0.0;
			for (int j = 0; j < HID; j++)
				temp += V[k][j] * H[j];
			temp += gamma[k];
			O[k] = sigmoid(temp);
		}
	}

	/**
	 * コーチ
	 */
	public double back(Dataset data) {
		double error = 0.0;
		// 出力層の誤差の計算
		for (int k = 0; k < OUT; k++) {
			T[k] = data.getTech(k);
			error += Math.abs(T[k] - O[k]);
			delta[k] = (T[k] - O[k]) * O[k] * (1 - O[k]);
		}
		// 隠れ層の誤差の計算
		for (int j = 0; j < HID; j++) {
			double temp = 0.0;
			for (int k = 0; k < OUT; k++)
				temp += delta[k] * V[k][j];
			sigma[j] = temp * H[j] * (1 - H[j]);
		}
		// 隠れ層と出力層間の重み更新
		for (int k = 0; k < OUT; k++) {
			for (int j = 0; j < HID; j++)
				V[k][j] += ALPHA * delta[k] * H[j];
			gamma[k] += BETA * delta[k];
		}
		// 入力層と隠れ層間の重み更新
		for (int j = 0; j < HID; j++) {
			for (int i = 0; i < IN; i++)
				W[j][i] += ALPHA * sigma[j] * I[i];
			theta[j] += BETA * sigma[j];
		}

		return error;
	}

	/**
	 * dataを学習する 
	 */
	public void learn(Vector dataset) {
		Log.info("<-- Learn");

		for (int loop = 0; loop < TIMES; loop++) {
			double error = 0.0;

			// 学習
			for (Enumeration e = dataset.elements(); e.hasMoreElements();) {
				Dataset _dataset = (Dataset) e.nextElement();
				forward(_dataset);
				error += back(_dataset);
			}

			if(loop % 100 == 0)
				Log.info(loop + ": " + error);

			// 収束したら学習を終了
			if (error < ERR_LIMIT)
				break;
		}
		Log.info("-->");
	}

	/**
	 * 結果取得
	 */
	public double[] getResult() {
		return this.O;

	}

	/**
	 * メイン
	 *
	 */
	public NeuralNetwork() {
		// 重みを初期化
		initWeight(W);
		//ShowWeight(W);

		initWeight(V);
		initThreshold(theta);
		initThreshold(gamma);
	}

	public static void main(String[] args) {
		NeuralNetwork main = new NeuralNetwork();
	}
}
