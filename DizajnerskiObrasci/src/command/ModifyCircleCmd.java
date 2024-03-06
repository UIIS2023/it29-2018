package command;

import javax.swing.JOptionPane;

import geometry.Circle;

public class ModifyCircleCmd implements Command{
	
	private Circle oldState;
	private Circle newState;
	private Circle original;
	
	
	public ModifyCircleCmd() {
		
	}

	public ModifyCircleCmd(Circle oldState, Circle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		
		original = oldState.clone();
		
		oldState.getCenter().setX(newState.getCenter().getX());
		oldState.getCenter().setY(newState.getCenter().getY());
		
		try {
			oldState.setRadius(newState.getRadius());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "An error has occurred.", "Error", JOptionPane.ERROR_MESSAGE);
		}
		oldState.setColor(newState.getColor());
		oldState.setInnerColor(newState.getInnerColor());
		
	}

	@Override
	public void unexecute() {
		
		oldState.getCenter().setX(original.getCenter().getX());
		oldState.getCenter().setY(original.getCenter().getY());
		
		try {
			oldState.setRadius(original.getRadius());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldState.setColor(original.getColor());
		oldState.setInnerColor(original.getInnerColor());
		
	}

}
