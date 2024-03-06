package observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import mvc.DrawingFrame;

public class SelectedObjectsUpdate implements PropertyChangeListener{

	private int listLenght;
	private DrawingFrame frame;
	
	public SelectedObjectsUpdate(DrawingFrame frame) {
		this.frame = frame;
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getPropertyName().equals("lenght")) {
			this.listLenght = (int) arg0.getNewValue();
			updateButton();
		}
		
	}


	private void updateButton() {
		
		if(listLenght == 1) {
			frame.getTglbtnDelete().setVisible(true);
			frame.getTglbtnModify().setVisible(true);
			frame.getBtnBringToFront().setVisible(true);
			frame.getBtnToBack().setVisible(true);
			frame.getBtnToFront().setVisible(true);
			frame.getBtnBringToBack().setVisible(true);
			
		} else if (listLenght > 1) {
			frame.getTglbtnDelete().setVisible(true);
			frame.getTglbtnModify().setVisible(false);
			frame.getBtnBringToBack().setVisible(false);
			frame.getBtnBringToFront().setVisible(false);
			frame.getBtnToBack().setVisible(false);
			frame.getBtnToFront().setVisible(false);
			
		} else {
			frame.getTglbtnDelete().setVisible(false);
			frame.getTglbtnModify().setVisible(false);
			frame.getBtnBringToBack().setVisible(false);
			frame.getBtnBringToFront().setVisible(false);
			frame.getBtnToBack().setVisible(false);
			frame.getBtnToFront().setVisible(false);
			
		}
		
	}
	

}
