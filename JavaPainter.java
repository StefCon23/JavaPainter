import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
**  JavaPainter.java
**
**  A simple paint program written in java
**  Uses Java Swing and Paint Component
**  Originally written from September to December, 2013
**
**  JavaPainter class implements the menu and the frame of 
**      the Drawing class
**
**  Conor Stefanini, 19 June 2015
*/

public class JavaPainter extends JFrame {
    
    public JavaPainter()
    {
        //jframe options
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640, 480);
        this.setTitle("main window title");
        
        //paint component panel
        Drawing d;
        d = new Drawing();
        
        //border to include menu bar
        d.setLayout(new BorderLayout());
        
        
        //menu bar
        JMenuBar menubar = new JMenuBar();
        
        //main options menu
        JMenu mainOpts = new JMenu("Main", true);
        
        JMenuItem menuCle = new JMenuItem("Clear Image");
        mainOpts.add(menuCle).addActionListener(d);
        JMenuItem menuUnd = new JMenuItem("Undo");
        mainOpts.add(menuUnd).addActionListener(d);
        
        menubar.add(mainOpts);
        
        //colour menu
        JMenu colour = new JMenu("Colours", true);
        
        JMenuItem menuBla = new JMenuItem("Black");
        colour.add(menuBla).addActionListener(d);
        JMenuItem menuRed = new JMenuItem("Red");
        colour.add(menuRed).addActionListener(d);
        colour.add(new JMenuItem("Yellow")).addActionListener(d);
        JMenuItem menuGre = new JMenuItem("Green");
        colour.add(menuGre).addActionListener(d);
        colour.add(new JMenuItem("Cyan")).addActionListener(d);
        JMenuItem menuBlu = new JMenuItem("Blue");
        colour.add(menuBlu).addActionListener(d);
        colour.add(new JMenuItem("Magenta")).addActionListener(d);
        JMenuItem menuWhi = new JMenuItem("White");
        colour.add(menuWhi).addActionListener(d);
        colour.add(new JMenuItem("Random")).addActionListener(d);
        
        menubar.add(colour);
        
        //shapes menu
        JMenu shape = new JMenu("Shapes", true);
        
        JMenuItem menuFre = new JMenuItem("FreeHand");
        shape.add(menuFre).addActionListener(d);
        JMenuItem menuLin = new JMenuItem("Line");
        shape.add(menuLin).addActionListener(d);
        JMenuItem menuCir = new JMenuItem("Circle");
        shape.add(menuCir).addActionListener(d);
        JMenuItem menuOva = new JMenuItem("Oval");
        shape.add(menuOva).addActionListener(d);
        JMenuItem menuSqu = new JMenuItem("Square");
        shape.add(menuSqu).addActionListener(d);
        JMenuItem menuRec = new JMenuItem("Rectangle");
        shape.add(menuRec).addActionListener(d);
        
        menubar.add(shape);
        
        //shape options menu
        JMenu shapeOpts = new JMenu("Shape Style", true);
        
        JMenuItem menuOut = new JMenuItem("Outline");
        shapeOpts.add(menuOut).addActionListener(d);
        JMenuItem menuFil = new JMenuItem("Fill");
        shapeOpts.add(menuFil).addActionListener(d);
        
        menubar.add(shapeOpts);
        
        
        //add menu bar to display panel
        d.add(menubar, BorderLayout.NORTH);
        
        //add display panel to main jframe
        this.add(d);
        
        this.setContentPane(d);
        
        //make frame visible
        this.setVisible(true);
    
    }
    
    public static void main(String[] args) {
        //initialise application
        new JavaPainter();
    }
    
    
}
