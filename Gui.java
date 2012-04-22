import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * �쐬��: 2004/01/18
 *�@$Id: Gui.java,v 1.4 2004/01/19 09:40:57 matsu Exp $
 */

/**
 * @author matsu
 *
 * �����F��GUI��Control
 */
public class Gui
	extends JFrame
	implements MouseMotionListener, ActionListener {

	/** �F�������\���̈�*/
	private JTextField text = new JTextField();

	/** �����F���p�l�� */
	private JPanel field_panel;

	/** mode */
	private JButton learning_button = new JButton("Learning mode");

	/** mode */
	private JButton recognition_button = new JButton("Recognition mode");

	/** ���Z�b�g�{�^�� */
	private JButton reset_button = new JButton("RESET");

	/** OK�{�^�� */
	private JButton ok_button = new JButton("OK");

	/** �����\���p�l�� */
	private JPanel text_panel = new JPanel();

	/** �����I���p�l�� */
	private JPanel select_panel;

	/** �Z���N�g�{�b�N�X */
	private JComboBox select;

	/** Container */
	private Container c;

	/** NeuralNetwork */
	private NeuralNetwork neural;
	
	/** �w�K�f�[�^ */
	Vector vector = new Vector();

	// �A���t�@�x�b�g�z��
	private static final String[] alpha_array =
		new String[] {
			"A",
			"B",
			"C",
			"D",
			"E",
			"F",
			"G",
			"H",
			"I",
			"J",
			"K",
			"L",
			"M",
			"N",
			"O",
			"P",
			"Q",
			"R",
			"S",
			"T",
			"U",
			"V",
			"W",
			"X",
			"Y",
			"Z" };

	/** ���� */
	private static final int x = 8;
	/** ���� */
	private static final int y = 8;

	/** �ꏡ�̑傫�� */
	private static final int x_width = 20;

	/** �ꏡ�̑傫�� */
	private static final int y_width = 20;

	/** ������\�����邩 */
	private static final int DISP = 5;

	/** �w�K�f�[�^ */
	private static final String learning_file = "learning.txt";

	/** ���͏��i�[ */
	private int[][] input = new int[x][y];

	/** ���[�h��` */
	public static final int LEARNING = 0;
	public static final int RECOGNITION = 1;

	/** ���[�h */
	private int mode = 0;
	
	/** �t�@�C������w�K���邩 */
	private static boolean LEARN_FROM_FILE = false;

	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		//Create and set up the window.
		Gui frame = new Gui();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Display the window.
		frame.setVisible(true);
	}

	/**
	 * GUI�쐬
	 *
	 */
	public Gui() {
		super("Pattern learning.");

		init();

		// �j���[�����l�b�g���[�N
		neural = new NeuralNetwork();
		if(LEARN_FROM_FILE == true)
			study();

		// �T�C�Y�ݒ�
		//setBounds(50, 50, 300, 500);
		//setSize(300,500);

		// Container���擾
		c = getContentPane();
		c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

		c.add(makeButtonPane());
		c.add(makeSelectBox());
		c.add(makeField());
		c.add(makeButton());

		makeDisplayLine();

		//validate();
		pack();

	}

	/**
	 * ������
	 * @return
	 */
	public void init() {
		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < input[i].length; j++)
				input[i][j] = 0;
		}
	}

	/**
	 * ���[�h�I��
	 * @return
	 */
	public Container makeButtonPane() {
		JPanel panel = new JPanel();

		learning_button.addActionListener(this);
		recognition_button.addActionListener(this);

		panel.add(learning_button);
		panel.add(recognition_button);

		return panel;
	}

	/**
	 * �w�K�����镶���I��
	 * @return
	 */
	public Container makeSelectBox() {
		select_panel = new JPanel();

		select = new JComboBox(alpha_array);

		select_panel.add(select);
		//panel.setPreferredSize(new Dimension(100, 50));
		//panel.setBackground(Color.blue);

		return select_panel;
	}

	/**
	 * ���ʕ\���̈�
	 * @return
	 */
	public Container makeDisplayLine() {

		text.setPreferredSize(new Dimension(250, 20));

		text_panel.add(this.text);

		return text_panel;
	}

	/**
	 * ���̓t�B�[���h
	 * @return
	 */
	public Container makeField() {
		field_panel = new JPanel() {
			public void paintComponent(Graphics g) {
					// ���ڕ`��
	for (int i = 0; i <= x; i++) {
					// �c��
					g.drawLine(i * x_width, 0, i * x_width, y * y_width);
					// ����
					g.drawLine(0, i * x_width, x * x_width, i * x_width);
				}

				// �F�`��
				for (int i = 0; i < x; i++) {
					for (int j = 0; j < y; j++) {
						if (input[i][j] == 0) {
							//��
							g.setColor(Color.WHITE);
						} else {
							g.setColor(Color.BLACK);

						}

						g.fillRect(
							j * y_width + 1,
							i * x_width + 1,
							x_width - 1,
							y_width - 1);

						g.setColor(Color.BLACK);
					}
				}

			}
		};
		field_panel.addMouseMotionListener(this);
		field_panel.setPreferredSize(new Dimension(x * x_width, y * y_width));
		return field_panel;
	}

	/**
	 * OK�{�^��
	 * @return
	 */
	public Container makeButton() {
		JPanel panel = new JPanel();

		reset_button.addActionListener(this);
		ok_button.addActionListener(this);

		panel.add(reset_button);
		panel.add(ok_button);
		return panel;
	}

	/**
	 * ���[�h��ς���
	 *
	 */
	public void changeMode(int next_mode) {

		c.remove(1);

		if (next_mode == LEARNING) {
			c.add(select_panel, 1);
		} else if (next_mode == RECOGNITION) {
			c.add(text_panel, 1);
		}

		pack();
		mode = next_mode;
	}

	/**
	 * �w�K
	 */
	public void study() {
		Log.info("<-- Learn from data");

		Vector _vector = new Vector();

		try {
			// �t�@�C������ǂݍ���
			BufferedReader br =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(learning_file)));

			// �w�K�f�[�^�쐬
			String msg;
			int i = 0;
			while ((msg = br.readLine()) != null) {
				for (int j = 0; j < msg.length(); j++) {
					double[] _teach = new double[alpha_array.length];
					double[] _input = new double[x * y];

					//init
					for (int h = 0; h < _teach.length; h++) {
						_teach[h] = 0;
						if (j == i)
							_teach[h] = 1;
					}
					for (int h = 0; h < x * y; h++) {
						_input[h] = Double.parseDouble(String.valueOf(msg.charAt(h)));
					}
					
					_vector.add(new Dataset(_input, _teach));

				}
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		neural.learn(_vector);
		
		
		
		Log.info("-->");

	}

	/**
	 * �}�E�X�A�N�V����
	 */
	public void mouseDragged(MouseEvent e) {

		// �N���b�N�ꏊ����
		if (e.getSource().equals(field_panel)) {
			if (e.getX() < x * x_width
				&& e.getY() < y * y_width
				&& 0 < e.getX()
				&& 0 < e.getY()) {

				input[e.getY() / y_width][e.getX() / x_width] = 1;
				field_panel.repaint();
				//Log.debug(e.getX()/x_width+":"+e.getY()/y_width);
			}
		}
	}

	/**
	 * �}�E�X�A�N�V����
	 */
	public void mouseMoved(MouseEvent e) {
		//Log.debug(e.toString());
	}

	/**
	 * �{�^���A�N�V����
	 */
	public void actionPerformed(ActionEvent e) {
		// Reset
		if (e.getSource().equals(reset_button)) {
			init();
			field_panel.repaint();
		}

		// OK
		else if (e.getSource().equals(ok_button)) {
			if (mode == LEARNING) {

				double[] _teach = new double[alpha_array.length];
				double[] _input = new double[x * y];

				// Input�f�[�^
				for (int i = 0; i < x; i++)
					for (int j = 0; j < y; j++) {
						_input[i * x + j] = input[i][j];
						System.out.print(input[i][j]);
					}

				// ���t�f�[�^
				for (int i = 0; i < alpha_array.length; i++) {
					_teach[i] = 0;
					if (select.getSelectedIndex() == i)
						_teach[i] = 1;
				}

				Dataset dataset = new Dataset(_input, _teach);

				vector.add(dataset);

				// �w�K
				neural.learn(vector);
				
				init();
				field_panel.repaint();

			} else if (mode == RECOGNITION) {
				double[] _input = new double[x * y];

				// Input�f�[�^
				for (int i = 0; i < x; i++)
					for (int j = 0; j < y; j++)
						_input[i * x + j] = input[i][j];

				Dataset dataset = new Dataset(_input);

				// �F��
				neural.forward(dataset);

				// ���ʎ擾
				double[] result = neural.getResult();

				// Sort
				int current = 0;
				int[] key = new int[result.length];

				// key init
				for (int i = 0; i < result.length; i++) {
					key[i] = i;
				}

				while (current < result.length) {
					for (int search = current + 1;
						search < result.length;
						search++) {
						if (result[current] < result[search]) {
							double tmp = result[current];
							result[current] = result[search];
							result[search] = tmp;

							int tmp_int = key[current];
							key[current] = key[search];
							key[search] = tmp_int;
						}
					}
					current++;
				}

				StringBuffer disp_text = new StringBuffer();
				for (int i = 0; i < DISP; i++) {
					disp_text.append(alpha_array[key[i]] + " ");
				}
				text.setText(disp_text.toString());
				pack();

				Log.info(alpha_array[key[0]]);
			}
			
			

		}
		// mode 
		else if (e.getSource().equals(learning_button)) {
			changeMode(LEARNING);
		}
		// mode
		else if (e.getSource().equals(recognition_button)) {
			changeMode(RECOGNITION);
		}
	}
}
