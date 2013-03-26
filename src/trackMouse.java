import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
 
@SuppressWarnings("serial")
public class trackMouse extends JPanel implements MouseMotionListener {
    
	blankArea blankArea;
	int[] directionx = {0,0};
	int[] directiony = {0,0};
    static final String NEWLINE = System.getProperty("line.separator");
    static JTextArea textArea;
    static JFrame frame;
    static JSplitPane splitPane;
    static JMenuItem init;
    static JMenuItem showLog;
    static JMenuItem clearLog;
    static JMenuItem disconnect;
    static boolean logVisible = true;
    
    public static void main(String[] args) {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        try {
			SerialTest.main(null);
		} 
        catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void createAndShowGUI() {
    	Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    	int width = rect.width;
    	int height = rect.height;
    	
    	frame = new JFrame("GCRA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        JComponent newContentPane = new trackMouse();
        newContentPane.setOpaque(true);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, newContentPane, scrollPane);
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(height * 4/5);
		Dimension minimumSizeListener = new Dimension(width, height * 9/10);
		Dimension minimumSizeLog = new Dimension(width, height * 1/10);
		newContentPane.setMinimumSize(minimumSizeListener);
		scrollPane.setMinimumSize(minimumSizeLog);
		
		JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu log = new JMenu("Log");
        menuBar.add(file);
        menuBar.add(log);
       
        init = new JMenuItem("Initialize");
        showLog = new JMenuItem("Hide Log");
        clearLog = new JMenuItem("Clear Log");
        disconnect = new JMenuItem("Disconnect");
        
        file.add(init);
        file.add(disconnect);
        log.add(showLog);
        log.add(clearLog);
        frame.setJMenuBar(menuBar);
        
		frame.add(splitPane);
        frame.setSize(width, height);
        frame.setVisible(true);
        
        menuListener();
    }
    
    public static void fillLog(String output){
    	textArea.append(output + NEWLINE);
    }
    
    public static void menuListener(){
    	Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    	final int height = rect.height;
    	
    	init.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = ("Initializing");
                fillLog(output);
                
                for (int i = 1; i < 5; i++) {
                	try {
                		SerialTest.write(i);
                		Thread.sleep(1500);
					} 
                	catch (InterruptedException e1){
                        e1.printStackTrace();
                    }
                }
                SerialTest.write(8);
            }
        });
    	disconnect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = ("");
                if (SerialTest.connected == false){
            		//Connect
                	output = ("Connect");
                	disconnect.setText("Disconnect");
                	SerialTest.connected = true;
            	}
            	else if (SerialTest.connected == true){
            		//Disconnect
            		output = ("Disconnect");
            		disconnect.setText("Connect");
            		SerialTest.connected = false;
            	}
                fillLog(output);
            }
        });
    	showLog.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = ("");
                
                if (logVisible == true){
            		//hide log
                	output = ("Hide Log");
                	showLog.setText("Show Log");
                	splitPane.setDividerLocation(height);
            	}
            	else if (logVisible == false){
            		//show log
            		output = ("Show Log");
            		showLog.setText("Hide Log");
            		splitPane.setDividerLocation(height * 4/5);
            	}
            	logVisible = !logVisible;
            	fillLog(output);
            }
        });
    	clearLog.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = ("Clear Log");
                fillLog(output);
                textArea.setText("");
            }
        });
    }

	public trackMouse() {
        super(new GridLayout(1,0));
        blankArea = new blankArea(Color.black);
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