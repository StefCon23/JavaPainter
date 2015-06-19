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
import javax.swing.JPanel;

/**
**  Drawing.java
**
**  A simple paint program written in java
**  Uses Java Swing and Paint Component
**  Originally written from September to December, 2013
**
**  This Drawing class uses the paint component to display graphics
**      It is used by the JavaPainter class
**
**  Conor Stefanini, 19 June 2015
*/

class Drawing extends JPanel 
    implements MouseListener, MouseMotionListener, ActionListener {
    
    //colour used when drawing
    Color mainCol = new Color(0, 0, 0);
    //shape that gets drawn
    String mainShape = "FreeHand";
    
    //flag for when mouse is dragging to make a preview
    int drag = 1;
    //flag to say when mouse is released and shape gets printed
    int print = 1;
    
    //point that mouse gets pressed at
    Point pt1 = new Point(0, 0);
    //point that mouse gets released at (or dragged to)
    Point pt2 = new Point(0, 0);
    //one point previous to pt2 while mouse is being dragged
    //  used to draw line with the freehand shape
    Point pt3 = new Point(0, 0);
    
    //current width of paint panel
    int w = getSize().width;
    //current height of paint panel
    int h = getSize().height;
    
    //image with shapes printed on it
    Image thrownShapes;
    Graphics tShaa;
    //undo buffer image
    Image undoThrownShapes;
    Graphics UTShaa;
    
    //flag for if shape is outlined or filled
    int outFill = 0;
    //clear image flag
    int clear = 1;
    //undo buffer flag
    int undo = 1;
    
    //flag to display image information in strings on screen
    int debug = 1;
    //flag for dragging while in freehand
    //  so that last free hand shape can be undone
    int freeHandFlag = 1;
    //random colour flag
    int randCol = 1;
    
    //checks to see if image is null
    public void checkTShapes() {
        
        if (thrownShapes == null) {
            //creates and resets image to white background
            thrownShapes = createImage(w, h);
            tShaa = thrownShapes.getGraphics();
            
            tShaa.setColor(Color.WHITE);
            tShaa.fillRect(0, 0, w, h);
            
            //creates undo buffer
            undoThrownShapes = createImage(w, h);
            UTShaa = undoThrownShapes.getGraphics();
            //white background
            UTShaa.setColor(Color.WHITE);
            UTShaa.fillRect(0, 0, w, h);
        }
    }
    
    
    public void Drawing() {
        //get width and height
        w = super.getSize().width;
        h = super.getSize().height;
        //initialise images
        checkTShapes();
        
        //mouse listeners
        addMouseListener(this);
        addMouseMotionListener(this);
        
        //refresh paint component
        repaint();
    }
        
    
    
    public void paintComponent(Graphics g) {
        
        //run drawing function
        //  creates images and adds mouse listeners
        Drawing();
        
        //clear screen to white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        
        //put image on screen
        g.drawImage(thrownShapes, 0, 0, this);
        
        
        g.setColor(mainCol);
        //if colour is set to random
        if (randCol == 0) {
            //initialise random colour
            Random jambon = new Random();
            mainCol = new Color(jambon.nextInt(256), 
                jambon.nextInt(256), jambon.nextInt(256));
        }
        
        
        //print shape on to image
        if (print == 0) {
            //if not a freehand shape set undo buffer
            if (freeHandFlag == 1) {
                UTShaa.drawImage(thrownShapes, 0, 0, null);
            }
            //draw shape
            drawShape(tShaa);
            print = 1;
        }
        
        //draw shape while dragging mouse
        if (drag == 0) {
            drawShape(g);
        }
        
        //put white rectangle on image to clear everything
        if (clear == 0) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, w, h);
            tShaa.setColor(Color.WHITE);
            tShaa.fillRect(0, 0, w, h);
            clear = 1;
        }
        
        //undo last move
        if (undo == 0) {
            tShaa.drawImage(undoThrownShapes, 0, 0, null);
            undo = 1;
        }
        
        
        //test lines for info
        if (debug == 0) {
            g.drawString("" + mainCol.toString(), 50, 50);
            g.drawString("" + mainShape, 50, 100);

            g.drawString("" + pt1.getX() + "  " + pt1.getY(), 50, 150);
            g.drawString("" + pt2.getX() + "  " + pt2.getY(), 50, 200);
            g.drawString("" + pt3.getX() + "  " + pt3.getY(), 50, 250);

            g.drawString("" + w + "   " + h, 250, 50);
            
        }
        
        
        
        
    }
    
    void drawShape(Graphics g2) {
        
        g2.setColor(mainCol);
        
        //outlined shape
        if (outFill == 0) {
            // get difference between the two points regardless 
            //      of relative position
            int cW = pt1.getX() < pt2.getX() ? 
                (int)(pt2.getX() - pt1.getX()) : 
                (int)(pt1.getX() - pt2.getX());
            int cY = pt1.getY() < pt2.getY() ? 
                (int)(pt2.getY() - pt1.getY()) : 
                (int)(pt1.getY() - pt2.getY());
            
            g2.setColor(mainCol);
            
            if (mainShape.equals("FreeHand")) {
            
                //draw line from previous pt2 to current pt2
                g2.drawLine((int)pt3.getX(), 
                    (int)pt3.getY(), (int)pt2.getX(), (int)pt2.getY());
                //pt3.x = (int)pt2.getX();
                //pt3.y = (int)pt2.getY();
                
            } else if (mainShape.equals("Line")) {
                
                //line from mouse press point to mouse drag/release point
                g2.drawLine((int)pt1.getX(), 
                    (int)pt1.getY(), (int)pt2.getX(), (int)pt2.getY());
                    
            } else if (mainShape.equals("Circle")) {

                //pythagors therom to get circle (not oval) radius
                int circtacular = 2 * 
                    (int)(Math.sqrt((Math.pow(cW, 2) + Math.pow(cY, 2))));
                g2.drawOval((int)(pt1.getX() - (circtacular / 2)),
                    ((int)pt1.getY() - (circtacular / 2)), 
                    circtacular, circtacular);

            } else if (mainShape.equals("Oval")) {

                //draws oval
                //if statements there to draw oval if mouse moved behind original position
                if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
                    g2.drawOval((int)pt1.getX() - cW, 
                        (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.y < pt1.y) {
                    g2.drawOval((int)pt1.getX(), 
                        (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.x < pt1.x) {
                    g2.drawOval((int)pt1.getX() - cW, 
                        (int)pt1.getY(), cW, cY);
                } else {
                    g2.drawOval((int)pt1.getX(), 
                        (int)pt1.getY(), cW, cY);
                }

            } else if (mainShape.equals("Square")) {
                //find longest of width and height and use it as square length
                int cSq = cW < cY ? 2 * cY : 2 * cW;
                //draws square with 4 lines
                g2.drawLine((int)(pt1.getX() - (cSq / 2)), (int)(pt1.getY() - (cSq / 2)), (int)(pt1.getX() + (cSq / 2)), (int)(pt1.getY() - (cSq / 2)));
                g2.drawLine((int)(pt1.getX() - (cSq / 2)), (int)(pt1.getY() - (cSq / 2)), (int)(pt1.getX() - (cSq / 2)), (int)(pt1.getY() + (cSq / 2)));
                g2.drawLine((int)(pt1.getX() + (cSq / 2)), (int)(pt1.getY() + (cSq / 2)), (int)(pt1.getX() + (cSq / 2)), (int)(pt1.getY() - (cSq / 2)));
                g2.drawLine((int)(pt1.getX() + (cSq / 2)), (int)(pt1.getY() + (cSq / 2)), (int)(pt1.getX() - (cSq / 2)), (int)(pt1.getY() + (cSq / 2)));
            } else if (mainShape.equals("Rectangle")) {
                //draws rectangle with 4 lines
                g2.drawLine((int)pt1.getX(), (int)pt1.getY(), (int)pt2.getX(), (int)pt1.getY());
                g2.drawLine((int)pt1.getX(), (int)pt1.getY(), (int)pt1.getX(), (int)pt2.getY());
                g2.drawLine((int)pt2.getX(), (int)pt2.getY(), (int)pt2.getX(), (int)pt1.getY());
                g2.drawLine((int)pt2.getX(), (int)pt2.getY(), (int)pt1.getX(), (int)pt2.getY());
            }
        } else if (outFill == 1) {
            //filled shape
            
            // get difference between the two points regardless of relative position
            int cW = pt1.getX() < pt2.getX() ? (int)(pt2.getX() - pt1.getX()) : (int)(pt1.getX() - pt2.getX());
            int cY = pt1.getY() < pt2.getY() ? (int)(pt2.getY() - pt1.getY()) : (int)(pt1.getY() - pt2.getY());
            
            g2.setColor(mainCol);
            
            if (mainShape.equals("FreeHand")) {
                //draw line from previous pt2 to current pt2
                g2.drawLine((int)pt3.getX(), (int)pt3.getY(), (int)pt2.getX(), (int)pt2.getY());
                //pt3.x = (int)pt2.getX();
                //pt3.y = (int)pt2.getY();
            } else if (mainShape.equals("Line")) {
                //line from mouse press point to mouse drag/release point
                g2.drawLine((int)pt1.getX(), (int)pt1.getY(), (int)pt2.getX(), (int)pt2.getY());
            } else if (mainShape.equals("Circle")) {
                //pythagors therom to get circle (not oval) radius
                int circtacular = 2 * (int)(Math.sqrt((Math.pow(cW, 2) + Math.pow(cY, 2))));
                g2.fillOval((int)(pt1.getX() - (circtacular / 2)), ((int)pt1.getY() - (circtacular / 2)), circtacular, circtacular);
            } else if (mainShape.equals("Oval")) {
                //draws oval
                //if statements there to draw oval if mouse moved behind original position
                if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
                    g2.fillOval((int)pt1.getX() - cW, (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.y < pt1.y) {
                    g2.fillOval((int)pt1.getX(), (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.x < pt1.x) {
                    g2.fillOval((int)pt1.getX() - cW, (int)pt1.getY(), cW, cY);
                } else {
                    g2.fillOval((int)pt1.getX(), (int)pt1.getY(), cW, cY);
                }
            } else if (mainShape.equals("Square")) {
                //find longest of width and height and use it as square length
                int cSq = cW < cY ? cY : cW;
                //draw square
                g2.fillRect((int)(pt1.getX() - cSq), (int)(pt1.getY() - cSq), 2 * cSq, 2 * cSq);
            } else if (mainShape.equals("Rectangle")) {
                //draw rectangle
                //if statements there to draw rectangle if mouse moved behind original position
                if ((pt2.y < pt1.y) && (pt2.x < pt1.x)) {
                    g2.fillRect((int)pt1.getX() - cW, (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.y < pt1.y) {
                    g2.fillRect((int)pt1.getX(), (int)pt1.getY() - cY, cW, cY);
                } else if (pt2.x < pt1.x) {
                    g2.fillRect((int)pt1.getX() - cW, (int)pt1.getY(), cW, cY);
                } else {
                    g2.fillRect((int)pt1.getX(), (int)pt1.getY(), cW, cY);
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
            clear = 0;
        } else if (Command.equals("Undo")) {
            undo = 0;
        }
        
        if (Command.equals("Black")) {
            mainCol = new Color(0, 0, 0);
            randCol = 1;
        } else if (Command.equals("Red")) {
            mainCol = new Color(255, 0, 0);
            randCol = 1;
        } else if (Command.equals("Yellow")) {
            mainCol = new Color(255, 255, 0);
            randCol = 1;
        } else if (Command.equals("Green")) {
            mainCol = new Color(0, 255, 0);
            randCol = 1;
        } else if (Command.equals("Cyan")) {
            mainCol = new Color(0, 255, 255);
            randCol = 1;
        } else if (Command.equals("Blue")) {
            mainCol = new Color(0, 0, 255);
            randCol = 1;
        } else if (Command.equals("Magenta")) {
            mainCol = new Color(255, 0, 255);
            randCol = 1;
        } else if (Command.equals("White")) {
            mainCol = new Color(255, 255, 255);
            randCol = 1;
        } else if (Command.equals("Random")) {
            randCol = 0;
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
            print = 0;
            freeHandFlag = 1;
        } else {
            freeHandFlag = 1;
        }
        
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //update pt2
        pt2.x = e.getX();
        pt2.y = e.getY();
        //set drag flag off to stop preview
        drag = 1;
        //set print flag on to print shape
        print = 0;

        repaint();
    }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        //update pt2
        pt2.x = e.getX();
        pt2.y = e.getY();
        //set drag flag on to create preview
        drag = 0;
        
        //to deal with undo of freehand shape
        if (mainShape.equals("FreeHand")) {
            //print shape
            print = 0;
            //stop refresh of undo buffer
            freeHandFlag = 0;
        }
        
        repaint();
    }

    public void mouseMoved(MouseEvent e) { }
    
}
