import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.event.MouseInputAdapter;

public class MouseListener extends MouseInputAdapter{

	boolean open = false;
	
	public void mousePressed(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    	try {
			SerialTest.passy(0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    public void mouseClicked(MouseEvent e){
    	int position = 0;
    	System.out.println("In mouseClicked");
    	if (open == true){
    		//close
    		position = 6;
    		System.out.println("open == true");
    	}
    	else if (open == false){
    		//open
    		position = 5;
    		System.out.println("open == false");
    	}
    	open = !open;
    	try {
			SerialTest.claw(position);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}
