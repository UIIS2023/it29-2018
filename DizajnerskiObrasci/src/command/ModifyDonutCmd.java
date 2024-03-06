package command;

import geometry.Donut;

public class ModifyDonutCmd implements Command{
	
	private Donut oldState;
	private Donut newState;
	private Donut original;
	
	public ModifyDonutCmd()
	{
		
	}
	
	public ModifyDonutCmd(Donut oldState,Donut newState)
	{
		this.oldState=oldState;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldState.setColor(newState.getColor());
		oldState.setInnerColor(newState.getInnerColor());
		oldState.setInnerRadius(newState.getInnerRadius());
		
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
		oldState.setInnerRadius(original.getInnerRadius());
		
	}

}
