import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/*	JavaPainter
**
**	Simple paint program
**	originally written in December, 2013, for a college assignment
**
**	2017-06-08
**		modifying program slightly to put on git
**
**	Conor Stefanini
*/

public class JavaPainter extends JFrame {
	
	public static void main(String[] args) {
		//	initialise application
		new JavaPainter();
	}
	
	public JavaPainter()
	{
		//	jframe options
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(640, 480);
		this.setTitle("Java Painter");
		
		//	paint component panel
		Painter pal;
		pal = new Painter();
		
		//	set a border to include menu bar
		pal.setLayout(new BorderLayout());
		
		
		//	menu bar
		JMenuBar menubar = new JMenuBar();
		
		//	main image options menu
		JMenu mainOpts = new JMenu("Image", true);
		
		JMenuItem menuCle = new JMenuItem("Clear Image");
		mainOpts.add(menuCle).addActionListener(pal);
		JMenuItem menuUnd = new JMenuItem("Undo");
		mainOpts.add(menuUnd).addActionListener(pal);
		
		menubar.add(mainOpts);
		
		//	colour menu
		JMenu colour = new JMenu("Colours", true);
		
		JMenuItem menuBla = new JMenuItem("Black");
		colour.add(menuBla).addActionListener(pal);
		JMenuItem menuRed = new JMenuItem("Red");
		colour.add(menuRed).addActionListener(pal);
		colour.add(new JMenuItem("Yellow")).addActionListener(pal);
		JMenuItem menuGre = new JMenuItem("Green");
		colour.add(menuGre).addActionListener(pal);
		colour.add(new JMenuItem("Cyan")).addActionListener(pal);
		JMenuItem menuBlu = new JMenuItem("Blue");
		colour.add(menuBlu).addActionListener(pal);
		colour.add(new JMenuItem("Magenta")).addActionListener(pal);
		JMenuItem menuWhi = new JMenuItem("White");
		colour.add(menuWhi).addActionListener(pal);
		colour.add(new JMenuItem("Random")).addActionListener(pal);
		
		menubar.add(colour);
		
		//	shapes menu
		JMenu shape = new JMenu("Shapes", true);
		
		JMenuItem menuFre = new JMenuItem("FreeHand");
		shape.add(menuFre).addActionListener(pal);
		JMenuItem menuLin = new JMenuItem("Line");
		shape.add(menuLin).addActionListener(pal);
		JMenuItem menuCir = new JMenuItem("Circle");
		shape.add(menuCir).addActionListener(pal);
		JMenuItem menuOva = new JMenuItem("Oval");
		shape.add(menuOva).addActionListener(pal);
		JMenuItem menuSqu = new JMenuItem("Square");
		shape.add(menuSqu).addActionListener(pal);
		JMenuItem menuRec = new JMenuItem("Rectangle");
		shape.add(menuRec).addActionListener(pal);
		
		menubar.add(shape);
		
		//	shape options menu
		JMenu shapeOpts = new JMenu("Shape Style", true);
		
		JMenuItem menuOut = new JMenuItem("Outline");
		shapeOpts.add(menuOut).addActionListener(pal);
		JMenuItem menuFil = new JMenuItem("Fill");
		shapeOpts.add(menuFil).addActionListener(pal);
		
		menubar.add(shapeOpts);
		
		//	add menu bar to painting panel
		pal.add(menubar, BorderLayout.NORTH);
		
		//	add painting panel to main jframe
		this.add(pal);
		this.setContentPane(pal);
		
		//	make frame visible
		this.setVisible(true);
	}
	
}

class Painter extends JPanel implements MouseListener, 
	MouseMotionListener, ActionListener {
	
	//	colour used when drawing
	Color mainCol = new Color(0, 0, 0);
	//	shape that gets drawn
	String mainShape = "FreeHand";
	
	//	flag for when mouse is dragging to make a preview
	int drag = 0;
	//	flag to say when mouse is released and shape gets printed
	int print = 0;
	
	//	point that mouse gets pressed at
	Point pt1 = new Point(0, 0);
	//	point that mouse gets released at (or dragged to)
	Point pt2 = new Point(0, 0);
	//	one point previous to pt2 while mouse is being dragged
	//  	used to draw line with the freehand shape
	Point pt3 = new Point(0, 0);
	
	//	current width of paint panel
	int w = getSize().width;
	//	current height of paint panel
	int h = getSize().height;
	
	//	image with shapes printed on it
	Image thrownShapes;
	Graphics tShaa;
	//	undo buffer image
	Image undoThrownShapes;
	Graphics UTShaa;
	
	//	flag for if shape is outlined or filled
	//		0 for outline, 1 for filled
	int outFill = 0;
	//	clear image flag
	int clear = 0;
	//	undo last action flag
	int undo = 0;
	
	//	flag to display image information in strings on screen
	int debug = 0;
	//	flag for dragging while in freehand
	//  	so that last free hand shape can be undone
	int freeHandFlag = 0;
	//	random colour flag
	int randCol = 0;
	
	//	checks to see if image is null
	public void checkTShapes() {
		if (thrownShapes == null) {
			//	creates and resets image to white background
			thrownShapes = createImage(w, h);
			tShaa = thrownShapes.getGraphics();
			tShaa.setColor(Color.WHITE);
			tShaa.fillRect(0, 0, w, h);
			
			//	creates undo buffer
			undoThrownShapes = createImage(w, h);
			UTShaa = undoThrownShapes.getGraphics();
			//	white background
			UTShaa.setColor(Color.WHITE);
			UTShaa.fillRect(0, 0, w, h);
		}
	}
	
	
	public void Painter() {
		//	get width and height
		w = super.getSize().width;
		h = super.getSize().height;
		//	initialise images
		checkTShapes();
		
		//	mouse listeners
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//	refresh paint component
		repaint();
	}
		
	
	
	public void paintComponent(Graphics g) {
		
		//	run Painter function
		//		creates images and adds mouse listeners
		Painter();
		
		//	clear screen to white
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		
		//	put image on screen
		//		all drawings are written to the image, then the image
		//		is written to the screen, avoids flickering
		g.drawImage(thrownShapes, 0, 0, this);
		
		g.setColor(mainCol);
		//	colour is set to random
		if (randCol == 1) {
			//	initialise random colour
			Random jambon = new Random();
			mainCol = new Color(jambon.nextInt(256), 
				jambon.nextInt(256), jambon.nextInt(256));
		}
		
		//	print shape on to image
		if (print == 1) {
			//	if not a freehand shape set undo buffer
			if (freeHandFlag == 0) {
				UTShaa.drawImage(thrownShapes, 0, 0, null);
			}
			//	draw shape
			drawShape(tShaa);
			print = 0;
		}
		
		//draw shape while dragging mouse
		if (drag == 1) {
			drawShape(g);
		}
		
		//	put white rectangle on image to clear everything
		if (clear == 1) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, w, h);
			tShaa.setColor(Color.WHITE);
			tShaa.fillRect(0, 0, w, h);
			clear = 0;
		}
		
		//	undo last move
		if (undo == 1) {
			tShaa.drawImage(undoThrownShapes, 0, 0, null);
			undo = 0;
		}
		
		
		//	test lines for info
		if (debug == 1) {
			g.drawString("" + mainCol.toString(), 50, 50);
			g.drawString("" + mainShape, 50, 100);

			g.drawString("" + pt1.getX() + "  " + pt1.getY(), 50, 150);
			g.drawString("" + pt2.getX() + "  " + pt2.getY(), 50, 200);
			g.drawString("" + pt3.getX() + "  " + pt3.getY(), 50, 250);

			g.drawString("" + w + "   " + h, 250, 50);
			
		}
		
		
		
		
	}
	
	void drawShape(Graphics g2) {
		
		//	get difference between the two points regardless 
		//		of relative position
		int cW = pt1.getX() < pt2.getX() ? 
			(int)(pt2.getX() - pt1.getX()) : 
			(int)(pt1.getX() - pt2.getX());
		int cY = pt1.getY() < pt2.getY() ? 
			(int)(pt2.getY() - pt1.getY()) : 
			(int)(pt1.getY() - pt2.getY());
		
		g2.setColor(mainCol);
		
		
		if (mainShape.equals("FreeHand")) {
			//	draw line from previous pt2 to current pt2
			g2.drawLine((int)pt3.getX(), (int)pt3.getY(), 
				(int)pt2.getX(), (int)pt2.getY());
			//pt3.x = (int)pt2.getX();
			//pt3.y = (int)pt2.getY();
		} else if (mainShape.equals("Line")) {
			//	line from mouse press point to 
			//		mouse drag/release point
			g2.drawLine((int)pt1.getX(), (int)pt1.getY(), 
				(int)pt2.getX(), (int)pt2.getY());
		
		//	outlined shape
		} else if (outFill == 0) {
			
			if (mainShape.equals("Circle")) {
				//	pythagors therom to get circle (not oval) radius
				int circtacular = 2 * (int)(Math.sqrt(
					(Math.pow(cW, 2) + Math.pow(cY, 2))));
				//	draw oval with equal height and width (a circle)
				//		using pt1 as centre point, the drawOval method
				//		draws from the top left most point, rather than
				//		the mid point, hence subtracting the new radius
				g2.drawOval((int)(pt1.getX() - (circtacular / 2)), 
					((int)pt1.getY() - (circtacular / 2)), 
					circtacular, circtacular);
		
			} else if (mainShape.equals("Oval")) {
				//	draws oval
				//		the if statements are there to draw oval if 
				//		the mouse moved behind original position
				if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
					g2.drawOval((int)(pt1.getX() - cW), 
						(int)(pt1.getY()) - cY, cW, cY);
				} else if (pt2.y < pt1.y) {
					g2.drawOval((int)pt1.getX(), (int)pt1.getY() - cY, 
						cW, cY);
				} else if (pt2.x < pt1.x) {
					g2.drawOval((int)pt1.getX() - cW, (int)pt1.getY(), 
						cW, cY);
				} else {
					g2.drawOval((int)pt1.getX(), (int)pt1.getY(), 
						cW, cY);
				}
			} else if (mainShape.equals("Square")) {
				//	find longest of width and height and use it as 
				//		the square length
				int cSq = cW < cY ? 2 * cY : 2 * cW;
				//	draws square with 4 lines
				g2.drawLine((int)(pt1.getX() - (cSq / 2)), 
					(int)(pt1.getY() - (cSq / 2)), 
					(int)(pt1.getX() + (cSq / 2)), 
					(int)(pt1.getY() - (cSq / 2)));
				g2.drawLine((int)(pt1.getX() - (cSq / 2)), 
					(int)(pt1.getY() - (cSq / 2)), 
					(int)(pt1.getX() - (cSq / 2)), 
					(int)(pt1.getY() + (cSq / 2)));
				g2.drawLine((int)(pt1.getX() + (cSq / 2)), 
					(int)(pt1.getY() + (cSq / 2)), 
					(int)(pt1.getX() + (cSq / 2)), 
					(int)(pt1.getY() - (cSq / 2)));
				g2.drawLine((int)(pt1.getX() + (cSq / 2)), 
					(int)(pt1.getY() + (cSq / 2)), 
					(int)(pt1.getX() - (cSq / 2)), 
					(int)(pt1.getY() + (cSq / 2)));
			} else if (mainShape.equals("Rectangle")) {
				//	draws rectangle with 4 lines
				g2.drawLine((int)pt1.getX(), (int)pt1.getY(), 
					(int)pt2.getX(), (int)pt1.getY());
				g2.drawLine((int)pt1.getX(), (int)pt1.getY(), 
					(int)pt1.getX(), (int)pt2.getY());
				g2.drawLine((int)pt2.getX(), (int)pt2.getY(), 
					(int)pt2.getX(), (int)pt1.getY());
				g2.drawLine((int)pt2.getX(), (int)pt2.getY(), 
					(int)pt1.getX(), (int)pt2.getY());
			}
		} else if (outFill == 1) {
			
			if (mainShape.equals("Circle")) {
				//pythagors therom to get circle (not oval) radius
				int circtacular = 2 * (int)(Math.sqrt(
					(Math.pow(cW, 2) + Math.pow(cY, 2)	)));
				g2.fillOval((int)(pt1.getX() - (circtacular / 2)), 
					((int)pt1.getY() - (circtacular / 2)), 
					circtacular, circtacular);
			} else if (mainShape.equals("Oval")) {
				//draws oval
				//if statements there to draw oval if mouse moved behind original position
				if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
					g2.fillOval((int)pt1.getX() - cW, 
						(int)pt1.getY() - cY, cW, cY);
				} else if (pt2.y < pt1.y) {
					g2.fillOval((int)pt1.getX(), (int)pt1.getY() - cY, 
						cW, cY);
				} else if (pt2.x < pt1.x) {
					g2.fillOval((int)pt1.getX() - cW, (int)pt1.getY(), 
						cW, cY);
				} else {
					g2.fillOval((int)pt1.getX(), (int)pt1.getY(), 
						cW, cY);
				}
			} else if (mainShape.equals("Square")) {
				//	find longest of width and height and use it as 
				//		square length
				int cSq = cW < cY ? cY : cW;
				//	draw square
				g2.fillRect((int)(pt1.getX() - cSq), 
					(int)(pt1.getY() - cSq), 2 * cSq, 2 * cSq);
			} else if (mainShape.equals("Rectangle")) {
				//	draw rectangle
				//		if statements there to draw rectangle if mouse 
				//		moved behind original position
				if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
					g2.fillRect((int)pt1.getX() - cW, 
						(int)pt1.getY() - cY, cW, cY);
				} else if (pt2.y < pt1.y) {
					g2.fillRect((int)pt1.getX(), (int)pt1.getY() - cY, 
						cW, cY);
				} else if (pt2.x < pt1.x) {
					g2.fillRect((int)pt1.getX() - cW, (int)pt1.getY(), 
						cW, cY);
				} else {
					g2.fillRect((int)pt1.getX(), (int)pt1.getY(), 
						cW, cY);
				}
			}
		}
		//set pt3 for freehand drawing
		pt3.x = pt2.x;
		pt3.y = pt2.y;
		
	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		//menubar
		
		//get which menu item clicked
		String Command = e.getActionCommand();
		
		if (Command.equals("Clear Image")) {
			clear = 1;
		} else if (Command.equals("Undo")) {
			undo = 1;
		}
		
		if (Command.equals("Random")) {
			randCol = 1;
		} else {
			if (Command.equals("Black")) {
				mainCol = new Color(0, 0, 0);
				randCol = 0;
			} else if (Command.equals("Red")) {
				mainCol = new Color(255, 0, 0);
				randCol = 0;
			} else if (Command.equals("Yellow")) {
				mainCol = new Color(255, 255, 0);
				randCol = 0;
			} else if (Command.equals("Green")) {
				mainCol = new Color(0, 255, 0);
				randCol = 0;
			} else if (Command.equals("Cyan")) {
				mainCol = new Color(0, 255, 255);
				randCol = 0;
			} else if (Command.equals("Blue")) {
				mainCol = new Color(0, 0, 255);
				randCol = 0;
			} else if (Command.equals("Magenta")) {
				mainCol = new Color(255, 0, 255);
				randCol = 0;
			} else if (Command.equals("White")) {
				mainCol = new Color(255, 255, 255);
				randCol = 0;
			}
		}
		
		
		
		if (Command.equals("FreeHand")) {
			mainShape = "FreeHand";
		} else if (Command.equals("Line")) {
			mainShape = "Line";
		} else if (Command.equals("Circle")) {
			mainShape = "Circle";
		} else if (Command.equals("Oval")) {
			mainShape = "Oval";
		} else if (Command.equals("Square")) {
			mainShape = "Square";
		} else if (Command.equals("Rectangle")) {
			mainShape = "Rectangle";
		}
		
		if (Command.equals("Outline")) {
			outFill = 0;
		} else if (Command.equals("Fill")) {
			outFill = 1;
		}
		
		repaint();
	}
	
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) {
		//set pt1
		pt1.x = e.getX();
		pt1.y = e.getY();
		//set pt2
		pt2.x = e.getX();
		pt2.y = e.getY();
		//set pt3
		pt3.x = e.getX();
		pt3.y = e.getY();
		
		//messy code to deal with undo of freehand shape
		if (mainShape.equals("FreeHand")) {
			print = 1;
			freeHandFlag = 0;
		} else {
			freeHandFlag = 0;
		}
		
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//	update pt2
		pt2.x = e.getX();
		pt2.y = e.getY();
		//	set drag flag off to stop preview
		drag = 0;
		//	set print flag on to print shape
		print = 1;

		repaint();
	}

	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		//	update pt2
		pt2.x = e.getX();
		pt2.y = e.getY();
		//	set drag flag on to create preview
		drag = 1;
		//	to deal with undo of freehand shape
		if (mainShape.equals("FreeHand")) {
			//	print shape
			print = 1;
			//	stop refresh of undo buffer
			freeHandFlag = 1;
		}
		repaint();
	}

	public void mouseMoved(MouseEvent e) { }
	
}