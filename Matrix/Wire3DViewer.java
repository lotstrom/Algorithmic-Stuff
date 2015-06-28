package Matrix;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Move your model around with W, S, A, D (pan left/right and up/down)
 * Use Q and E to rotate around y-axis, 1 and 3 to rotate around x-axis
 * and Z and X to rotatearound z-axis.
 * 
 * Zoom with mousewheel up and down.
 */
 
class Wire3DViewer extends JPanel {

	private static final long serialVersionUID = 1L;
	final static JFrame frame = new JFrame("Wire3DViewer");
	private static double scale = 20;
	private static double zoomVel = 0.;
	private static double xPan = 0.;
	private static double yPan = 0.;
	private static double xVel = 0.;
	private static double yVel = 0.;
	private static double zVel = 0.;
	private static double friction = .95;
	
	private static double xRotVel = 0.;
	private static double xRot = 0.;
	private static double yRotVel = 0.;
	private static double yRot = 0.;
	private static double zRotVel = 0.;
	private static double zRot = 0.;

	private static int invert = 1;
	private static boolean keyW = false;
	private static boolean keyA = false;
	private static boolean keyS = false;
	private static boolean keyD = false;
	private static boolean keyE = false;
	private static boolean keyQ = false;
	private static boolean key1 = false;
	private static boolean key3 = false;
	private static boolean keyZ = false;
	private static boolean keyX = false;

	static class Line {
		double x1, y1, z1, x2, y2, z2;

		Line(double x1_, double y1_, double z1_, double x2_, double y2_,
				double z2_) {
			x1 = x1_;
			y1 = y1_;
			z1 = z1_;
			x2 = x2_;
			y2 = y2_;
			z2 = z2_;
		}
	}

	static Collection<Line> readModel(String filename) throws Exception {
		Collection<Line> lines = new LinkedList<Line>();
		FileInputStream fstream = new FileInputStream(filename);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null) {
			String[] ss = strLine.split(" ");
			lines.add(new Line(Float.parseFloat(ss[0]),
					Float.parseFloat(ss[1]), Float.parseFloat(ss[2]), Float
							.parseFloat(ss[3]), Float.parseFloat(ss[4]), Float
							.parseFloat(ss[5])));
		}
		return lines;
	}

	Collection<Line> lines;
	static Matrix translatecamera;
	static Matrix project;
	static Matrix viewportscale;
	static Matrix viewporttranslate;
	static Matrix transform;
	static String lastActive = "";

	static void updateTransform() {
		double[][] scaleMatrix = {
				{1, 0, 0, 0},
				{0, 1, 0, 0},
				{0, 0, 1, scale},
				{0, 0, 0, 1}
		};
		Matrix scaleM = new Matrix(scaleMatrix);
		double[][] translateMatrix = {
				{1, 0, 0, xPan},
				{0, 1, 0, yPan},
				{0, 0, 1, 0},
				{0, 0, 0, 1}
		};
		Matrix translateM = new Matrix(translateMatrix);
		double[][] xRotation = { 
				{ 1, 0, 0, 0 },
				{ 0, Math.cos(xRot), -Math.sin(xRot), 0 },
				{ 0, Math.sin(xRot), Math.cos(xRot), 0 }, 
				{ 0, 0, 0, 1 }
		};
		Matrix xRotationM = new Matrix(xRotation);
		double[][] yRotation = { 
				{ Math.cos(yRot), 0, Math.sin(yRot), 0 },
				{ 0, 1, 0, 0 }, 
				{ -Math.sin(yRot), 0, Math.cos(yRot), 0 },
				{ 0, 0, 0, 1 } 
		};
		Matrix yRotationM = new Matrix(yRotation);
		double[][] zRotation = { 
				{ Math.cos(zRot), -Math.sin(zRot), 0, 0 },
				{ Math.sin(zRot), Math.cos(zRot), 0, 0 }, 
				{ 0, 0, 1, 0 },
				{ 0, 0, 0, 1 } 
		};
		Matrix zRotationM = new Matrix(zRotation);
		
		double[][] ato = {
				{ 1, 0, 0, 0 },
				{ 0, 1, 0, 0 },
				{ 0, 0, 1, 0 },
				{ 0, 0, 0, 1 }
				};
		
		translatecamera = translateM.mult(scaleM.mult(xRotationM.mult(yRotationM.mult(zRotationM))));
		transform = project.mult(translatecamera);
		transform = viewportscale.mult(transform);
		transform = viewporttranslate.mult(transform);
	}

	public Wire3DViewer(String fileName) {
		try {
			lines = readModel(fileName);
		} catch (Exception e) {
			System.out.println(e);
		}
		setPreferredSize(new Dimension(800, 600));
		double[][] ato = {
				{ 1, 0, 0, 0. },
				{ 0, 1, 0, 0. },
				{ 0, 0, 1, 0 },
				{ 0, 0, 0, 1 }
				};
		translatecamera = new Matrix(ato);
		double w = 1, h = 1, far = 100, near = 2;
		double[][] aprj = {
				{ near / w, 0, 0, 0 },
				{ 0, near / h, 0, 0. },
				{ 0, 0, -(far + near) / (far - near), -2 * far * near / (far - near) },
				{ 0, 0, -1, 0 }
				};
		project = new Matrix(aprj);
		double[][] a = {
				{ 600.0, 0, 0, 0. },
				{ 0, 600.0, 0, 0. },
				{ 0, 0, 1.0, 0 },
				{ 0, 0, 0, 1 }
				};
		viewportscale = new Matrix(a);
		double[][] b = {
				{ 1, 0, 0, 400. },
				{ 0, 1, 0, 300. },
				{ 0, 0, 1, 0 },
				{ 0, 0, 0, 1 }
				};
		viewporttranslate = new Matrix(b);		
		updateTransform();
	}

	void drawLines(Graphics g) {
		for (Line l : lines) {
			double[][] a1 = {
					{ l.x1 },
					{ l.y1 },
					{ l.z1 },
					{ 1.00 }
					};
			double[][] a2 = {
					{ l.x2 },
					{ l.y2 },
					{ l.z2 },
					{ 1.00 }
					};
			
			Matrix v1 = new Matrix(a1);
			Matrix v2 = new Matrix(a2);
			
			Matrix tv1 = transform.mult(v1);
			Matrix tv2 = transform.mult(v2);
			
			int x1 = (int) (tv1.get(1, 1) / tv1.get(4, 1));
			int y1 = (int) (tv1.get(2, 1) / tv1.get(4, 1));
			int x2 = (int) (tv2.get(1, 1) / tv2.get(4, 1));
			int y2 = (int) (tv2.get(2, 1) / tv2.get(4, 1));
			
			if (tv1.get(3, 1) < -10 && tv2.get(3, 1) < -10) {
				g.drawLine(x1, y1, x2, y2);
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		keys();
		drawLines(g);
		update();
	}

	public void update() {
		xRot += xRotVel;
		yRot += yRotVel;
		zRot += zRotVel;
		xPan += xVel * invert;
		yPan += yVel * invert;
		scale += zoomVel;
		zoomVel *= friction;
		xRotVel *= friction;
		yRotVel *= friction;
		zRotVel *= friction;
		xVel *= friction;
		yVel *= friction;
		zVel *= friction;
		updateTransform();
		frame.repaint();
	}

	public static void keys() {
		if (keyE)
			yRotVel += .01;
		if (keyQ)
			yRotVel -= .01;
		if (key1)
			xRotVel += .01;
		if (key3)
			xRotVel -= .01;
		if (keyZ)
			zRotVel += .01;
		if (keyX)
			zRotVel -= .01;

		if (keyW)
			yVel += .01;
		if (keyA)
			xVel += .01;
		if (keyS)
			yVel -= .01;
		if (keyD)
			xVel -= .01;
		if (keyZ)
			zVel -= .01;
		if (keyX)
			zVel += .01;
	}	
	
	public static void main(String[] argv) {
		
		final String fileName = "src/assignment7/models/cube.txt";
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame.setContentPane(new Wire3DViewer(fileName));
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				frame.addKeyListener(new KeyListener() {
					@Override
					public void keyPressed(KeyEvent arg0) {
						// TODO Auto-generated method stub
						int keyCode = arg0.getKeyCode();
						switch (keyCode) {
						case KeyEvent.VK_A:
							keyA = true;
							break;
						case KeyEvent.VK_D:
							keyD = true;
							break;
						case KeyEvent.VK_W:
							keyW = true;
							break;
						case KeyEvent.VK_S:
							keyS = true;
							break;
						case KeyEvent.VK_E:
							keyE = true;
							break;
						case KeyEvent.VK_Q:
							keyQ = true;
							break;
						case KeyEvent.VK_1:
							key1 = true;
							break;
						case KeyEvent.VK_3:
							key3 = true;
							break;
						case KeyEvent.VK_Z:
							keyZ = true;
							break;
						case KeyEvent.VK_X:
							keyX = true;
							break;
						}
						frame.repaint();
						updateTransform();
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						// TODO Auto-generated method stub
						int keyCode = arg0.getKeyCode();
						switch (keyCode) {
						case KeyEvent.VK_A:
							keyA = false;
							break;
						case KeyEvent.VK_D:
							keyD = false;
							break;
						case KeyEvent.VK_W:
							keyW = false;
							break;
						case KeyEvent.VK_S:
							keyS = false;
							break;
						case KeyEvent.VK_E:
							keyE = false;
							break;
						case KeyEvent.VK_Q:
							keyQ = false;
							break;
						case KeyEvent.VK_1:
							key1 = false;
							break;
						case KeyEvent.VK_3:
							key3 = false;
							break;
						case KeyEvent.VK_Z:
							keyZ = false;
							break;
						case KeyEvent.VK_X:
							keyX = false;
							break;
						}
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						// TODO Auto-generated method stub
					}
				});
				frame.addMouseWheelListener(new MouseWheelListener() {
					@Override
					public void mouseWheelMoved(MouseWheelEvent arg0) {
						// TODO Auto-generated method stub
						double noches = arg0.getWheelRotation();
						if (noches > 0) {
							zoomVel -= -noches / 5;
							updateTransform();
							frame.repaint();
						} else {
							zoomVel += noches / 5;
							updateTransform();
							frame.repaint();
						}
					}
				});
			}
		});
	}
}
