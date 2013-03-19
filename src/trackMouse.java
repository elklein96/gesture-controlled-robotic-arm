import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
 
@SuppressWarnings("serial")
public class trackMouse extends JPanel implements MouseMotionListener {
    
	blankArea blankArea;
	int[] directionx = {0,0};
	int[] directiony = {0,0};
    static final String NEWLINE = System.getProperty("line.separator");
    static JTextArea textArea;
    static JFrame frame;
    static JFrame frameLog;
    
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        try {
			SerialTest.main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
     
    private static void createAndShowGUI() {
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	int width = gd.getDisplayMode().getWidth();
    	int height = gd.getDisplayMode().getHeight();
    	
    	frame = new JFrame("trackMouse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frameLog = new JFrame("Log");
        
        JComponent newContentPane = new trackMouse();
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frameLog.add(scrollPane); 
        
        frame.setSize(width * 9/10, height);
        frameLog.setSize(width/10, height);
        
        frame.setVisible(true);
        frameLog.setVisible(true);
        
        frame.setLocation(width/10, 0);
    }
    
    public static void fillLog(String output){
    	textArea.append(output + NEWLINE);
    	textArea.setCaretPosition(textArea.getDocument().getLength());
    }

	public trackMouse() {
        super(new GridLayout(1,0));
        blankArea = new blankArea(Color.WHITE);
        add(blankArea);
         
        blankArea.addMouseMotionListener(this);
        
        MouseListener mouseListener = new MouseListener();
        blankArea.addMouseListener(mouseListener);
         
        setPreferredSize(new Dimension(java.awt.Toolkit.getDefaultToolkit().getScreenSize()));
    }
     
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
    	int y = e.getY();
    	
    	directionx[0] = directionx[1];
    	directionx[1] = x;
    	int direcx = directionx[0] - directionx[1];
    	
    	try {
			SerialTest.passx(direcx);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
    	directiony[0] = directiony[1];
    	directiony[1] = y;
    	int direcy = directiony[1] - directiony[0];
    	
    	try {
			SerialTest.passy(direcy);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
	@Override
	public void mouseMoved(MouseEvent e) {
	}
}