import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MineSweep {
	private JFrame frame;
	private Map map;
	private int[][] status;
	private int x = 10;
	private int y = 10;
	private int count = 0;
	private int countOfFlag = 0;
	private int unitSize = 20;
	private int frameWidth = 500;
	private int frameHeight = (int)(frameWidth*1.35);
	
	private JLabel label0 = new JLabel();
	private JLabel label1 = new JLabel();
	private Font font = new Font("宋体", Font.BOLD, (this.frameHeight-this.frameWidth-55)/2-10);
	
	public MineSweep() {
		status = new int[y+2][x+2];
		int randomNumber;
		for ( int i = 0; i < y+2; i++ ) {
			for ( int j = 0; j < x+2; j++ ) {
				status[i][j] = 0;
			}
		}
		for ( int i = 1; i < y+1;  i++ ) {
			for ( int j = 1; j < x+1; j++ ) {
				randomNumber = (int)(Math.random()*10);
				if ( randomNumber == 9 ) {
					count++;
					status[i][j] = randomNumber;
				}
			}
		}
		for ( int i = 1; i < y+1;  i++ ) {
			for ( int j = 1; j < x+1; j++ ) {
				if ( status[i][j] != 9 ) {
					status[i][j] = count(i, j);
				}
			}
		}
		map = new Map();
		label0.setFont(font);
		label0.setForeground(Color.RED);
		label1.setFont(font);
		label1.setForeground(Color.black);
	}
	
	
	class Map extends JPanel {
		private int panelSize = frameWidth-20;
		private int paintX = 0;
		private int paintY = 0;
		
		public Map() {
			unitSize = (int)Math.min(panelSize/x, panelSize/y);
			
			paintX = (int)((frameWidth-unitSize*x)/2);
			paintY = (int)((frameWidth-unitSize*y)/2);
			setPreferredSize(new Dimension(frameWidth,frameWidth));
			setLayout(null);
		}
		
		public void paintComponent(Graphics g) {
			removeAll();
			super.paintComponent(g);
			
			for ( int i = 1; i < y+1; i++ ) {
				for ( int j = 1; j < x+1; j++ ) {
					Unit u = new Unit(i, j);
					u.setBounds(paintX+(j-1)*unitSize, paintY+(i-1)*unitSize, unitSize, unitSize);
					this.add(u);
				}
			}
		}
		
		class Unit extends JLabel implements MouseListener {
			private int i;
			private int j;
			
			public Unit(int i, int j) {
				this.i = i;
				this.j = j;
				this.addMouseListener(this);
				
				ImageIcon image = new ImageIcon("Images/facingDown.png");
				if ( status[i][j] > 9 && status[i][j] < 20 ) {
					image = new ImageIcon("Images/"+(status[i][j]-10)+".png");
				}
				else if ( status[i][j] >= 20 ){
					image = new ImageIcon("Images/flagged.png");
				}
				image.setImage(image.getImage().getScaledInstance(unitSize, unitSize, Image.SCALE_DEFAULT));
				this.setIcon(image);
				
			}
			
			
			
			public void mouseReleased(MouseEvent e) {
				if ( status[i][j] < 10 && e.getButton() == 1 ) {
					if ( status[i][j] == 9 ) {
						//display();
					}
					else {
						//cleanAll(i, j);
					}
					status[i][j] += 10;
					cleanAll(i, j);
					frame.repaint();
				}
				else if ( e.getButton() == 3 ) {
					if ( status[i][j] > 20 ) {
						status[i][j] -= 20;
						countOfFlag--;
						label1.setText("Flags: "+countOfFlag);
						frame.repaint();
					}
					else if ( status[i][j] < 10 ) {
						status[i][j] += 20;
						countOfFlag++;
						label1.setText("Flags: "+countOfFlag);
						frame.repaint();
					}
				}
			}
			
			public void mousePressed(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
		}
		
		
	}
	
	public void go() {
		frame = new JFrame("Mine Sweep");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
				
		JMenuBar bar = new JMenuBar();
		JMenu setting = new JMenu("Settings");
		JMenuItem sizeMenu = new JMenuItem("Size");
		sizeMenu.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
				JPanel panelInput = new JPanel();
				panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));
				JPanel panelWidth = new JPanel();
				JLabel width = new JLabel("Width");
				JTextField widthIn = new JTextField(10);
				panelWidth.add(width);
				panelWidth.add(widthIn);
				JPanel panelHeight = new JPanel();
				JLabel height = new JLabel("height");
				JTextField heightIn = new JTextField(10);
				panelHeight.add(height);
				panelHeight.add(heightIn);
				panelInput.add(panelWidth);
				panelInput.add(panelHeight);
				JOptionPane.showMessageDialog(null, panelInput);
				int newX = 10;
				int newY = 10;
				try {
					newX = Integer.parseInt(widthIn.getText());
					newY = Integer.parseInt(heightIn.getText());
				} finally {
					if ( newX > 10 && newY > 10 ) {
						x = newX;
						y = newY;
						status = new int[y+2][x+2];
						int randomNumber;
						for ( int i = 0; i < y+2; i++ ) {
							for ( int j = 0; j < x+2; j++ ) {
								status[i][j] = 0;
							}
						}
						for ( int i = 1; i < y+1;  i++ ) {
							for ( int j = 1; j < x+1; j++ ) {
								randomNumber = (int)(Math.random()*10);
								if ( randomNumber == 9 ) {
									count++;
									status[i][j] = randomNumber;
								}
							}
						}
						for ( int i = 1; i < y+1;  i++ ) {
							for ( int j = 1; j < x+1; j++ ) {
								if ( status[i][j] != 9 ) {
									status[i][j] = count(i, j);
								}
							}
						}
						map = new Map();
						label0.setText("Mines: "+count);
						label1.setText("Flags: "+countOfFlag);
						frame.repaint();
					}
				}
				
			}
		});
		
		setting.add(sizeMenu);
		bar.add(setting);
		frame.setJMenuBar(bar);
		
		
		
		label0.setText("Mines: "+this.count);
		label1.setText("Flags: "+this.countOfFlag);
		label0.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		label1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.add(label0);
		panel1.add(label1);
		panel1.setBounds(0, 0, this.frameWidth, this.frameHeight-this.frameWidth-55);
		frame.add(panel1);
		map.setBounds(0,this.frameHeight-this.frameWidth-55,this.frameWidth,this.frameWidth);
		
		frame.add(map);
		
		
		
		frame.setVisible(true);
				
		frame.setSize(this.frameWidth, this.frameHeight);
		frame.setLocationRelativeTo(null);
		
		frame.setResizable(false);
			
	}
	
	private int count(int i, int j) {
		int[] findI = {i-1,i-1,i-1,i,i,i+1,i+1,i+1};
		int[] findJ = {j-1,j,j+1,j-1,j+1,j-1,j,j+1};
		int countU = 0;
		for (int k = 0; k < 8; k++ ) {
			if ( status[findI[k]][findJ[k]] == 9 ) countU++;
		}
		return countU;
	}
	
	private void clean(int i, int j) {
		if ( i < 1 || i > y || j < 1 || j > x ) return;
		if ( status[i][j] != 10 ) {
			return;
		}
		int[] findI = {i-1,i-1,i-1,i,i,i+1,i+1,i+1};
		int[] findJ = {j-1,j,j+1,j-1,j+1,j-1,j,j+1};
		for ( int k = 0; k < 8; k++ ) {
			if ( status[findI[k]][findJ[k]] < 9 ) {
				status[findI[k]][findJ[k]] += 10;
				if (  status[findI[k]][findJ[k]] == 10 ) clean(findI[k],findJ[k]);
			}
		}
	}
	
	private void cleanAll(int i, int j) {
		if ( i < 1 || i > y || j < 1 || j > x ) return;
		if ( status[i][j] == 10 ) clean(i,j);
		else{
			int[] findI = {i-1,i-1,i-1,i,i,i+1,i+1,i+1};
			int[] findJ = {j-1,j,j+1,j-1,j+1,j-1,j,j+1};
			for ( int k = 0; k < 8; k++ ) {
				if ( status[findI[k]][findJ[k]] == 0 ) {
					status[findI[k]][findJ[k]] += 10;
					clean(findI[k],findJ[k]);
				}
			}
		}
	}
	
		
	
	public static void main(String[] args) {
		MineSweep gui = new MineSweep();
		gui.go();
	}
}