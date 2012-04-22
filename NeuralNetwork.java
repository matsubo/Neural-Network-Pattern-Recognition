import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
/*
 * �쐬��: 2004/01/18
 * $Id: NeuralNetwork.java,v 1.3 2004/01/19 09:40:58 matsu Exp $
 */

/**
 * @author matsu
 *
 * �j���[�����l�b�g���[�N
 */
public class NeuralNetwork implements Serializable{

	/** ���͑w�̐� */
	private int IN = 8 * 8;
	/** �B��w�̐� */
	private int HID = 8 * 8;
	/** �o�͑w�̐� */
	private int OUT = 26;

	/** ����ȉ��̌덷�Ȃ���� */
	private double ERR_LIMIT = 0.1;

	private double ALPHA = 0.4;
	private double BETA = 0.3;

	/** �w�K�񐔂̍ő�l */
	private double TIMES = 50000;

	/** ���͑w�̏o�͒l */
	private double[] I = new double[IN];
	/** �B��w�̏o�͒l */
	private double[] H = new double[HID];
	/** �o�͑w�̏o�͒l */
	private double[] O = new double[OUT];

	/** ���͑w�ƉB��w�Ԃ̏d�� */
	private double[][] W = new double[HID][IN];
	/** �B��w�Əo�͑w�Ԃ̏d�� */
	private double[][] V = new double[OUT][HID];

	/** �B��w��臒l */
	private double[] theta = new double[HID];
	/** �o�͑w��臒l */
	private double[] gamma = new double[OUT];

	/** �o�͑w�̏o�͒l�Ƌ��t�M���̌덷 */
	private double[] delta = new double[OUT];
	/** �B��w�̏o�͒l�Əo�͑w����߂��Ă����l�̌덷 */
	private double[] sigma = new double[HID];
	/** ���t�M�� */
	private double[] T = new double[OUT];

	/**
	 * �d�݂�����������
	 */
	public void initWeight(double[][] w) {
		for (int i = 0; i < w.length; i++) {
			for (int j = 0; j < w[i].length; j++) {
				// [-0.1, 0.1] �̗���
				w[i][j] = (Math.random() - 0.5) * 2.0;
			}
		}
	}

	/**
	 * �d�݂�\������
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
	 * 臒l������������
	 */
	public void initThreshold(double[] threshold) {
		for (int i = 0; i < threshold.length; i++) {
			// [-0.1, 0.1] �̗���
			threshold[i] = (Math.random() - 0.5) * 2.0;
		}
	}

	/**
	 * 臒l��\������
	 */
	private void showThreshold(double[] threshold) {
		for (int i = 0; i < threshold.length; i++) {
			System.out.print(threshold[i] + " ");
		}
		System.out.println();
	}

	/**
	 * �V�O���C�h�֐�
	 */
	private double sigmoid(double x) {
		return 1.0 / (1 + Math.exp(-x));
	}

	/**
	 * ����
	 */
	public void forward(Dataset d) {
		// ���͑w�̏o�͂����߂�
		for (int i = 0; i < IN; i++) {
			Log.debug(String.valueOf(i));
			I[i] = d.getInput(i);
		}

		// ���ԑw�̏o�͂����߂�
		for (int j = 0; j < HID; j++) {
			double temp = 0.0;
			for (int i = 0; i < IN; i++)
				temp += W[j][i] * I[i];
			temp += theta[j];
			H[j] = sigmoid(temp);
		}

		// �o�͑w�̏o�͂����߂�
		for (int k = 0; k < OUT; k++) {
			double temp = 0.0;
			for (int j = 0; j < HID; j++)
				temp += V[k][j] * H[j];
			temp += gamma[k];
			O[k] = sigmoid(temp);
		}
	}

	/**
	 * �R�[�`
	 */
	public double back(Dataset data) {
		double error = 0.0;
		// �o�͑w�̌덷�̌v�Z
		for (int k = 0; k < OUT; k++) {
			T[k] = data.getTech(k);
			error += Math.abs(T[k] - O[k]);
			delta[k] = (T[k] - O[k]) * O[k] * (1 - O[k]);
		}
		// �B��w�̌덷�̌v�Z
		for (int j = 0; j < HID; j++) {
			double temp = 0.0;
			for (int k = 0; k < OUT; k++)
				temp += delta[k] * V[k][j];
			sigma[j] = temp * H[j] * (1 - H[j]);
		}
		// �B��w�Əo�͑w�Ԃ̏d�ݍX�V
		for (int k = 0; k < OUT; k++) {
			for (int j = 0; j < HID; j++)
				V[k][j] += ALPHA * delta[k] * H[j];
			gamma[k] += BETA * delta[k];
		}
		// ���͑w�ƉB��w�Ԃ̏d�ݍX�V
		for (int j = 0; j < HID; j++) {
			for (int i = 0; i < IN; i++)
				W[j][i] += ALPHA * sigma[j] * I[i];
			theta[j] += BETA * sigma[j];
		}

		return error;
	}

	/**
	 * data���w�K���� 
	 */
	public void learn(Vector dataset) {
		Log.info("<-- Learn");

		for (int loop = 0; loop < TIMES; loop++) {
			double error = 0.0;

			// �w�K
			for (Enumeration e = dataset.elements(); e.hasMoreElements();) {
				Dataset _dataset = (Dataset) e.nextElement();
				forward(_dataset);
				error += back(_dataset);
			}

			if(loop % 100 == 0)
				Log.info(loop + ": " + error);

			// ����������w�K���I��
			if (error < ERR_LIMIT)
				break;
		}
		Log.info("-->");
	}

	/**
	 * ���ʎ擾
	 */
	public double[] getResult() {
		return this.O;

	}

	/**
	 * ���C��
	 *
	 */
	public NeuralNetwork() {
		// �d�݂�������
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
