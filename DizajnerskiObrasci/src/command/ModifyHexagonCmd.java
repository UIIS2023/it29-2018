package command;

import adapter.HexagonAdapter;

public class ModifyHexagonCmd implements Command{
	
	private HexagonAdapter oldState;
	private HexagonAdapter newState;
	private HexagonAdapter original;
	
	public ModifyHexagonCmd() {
		
	}
	
	public ModifyHexagonCmd(HexagonAdapter oldState, HexagonAdapter newState) {
		this.oldState = oldState;
		this.newState = newState;
	}


	@Override
	public void execute() {

		original = oldState.clone();
		
		oldState.getHexagon().setX(newState.getHexagon().getX());
		oldState.getHexagon().setY(newState.getHexagon().getY());
		oldState.getHexagon().setR(newState.getHexagon().getR());
		oldState.getHexagon().setAreaColor(newState.getHexagon().getAreaColor());
		oldState.getHexagon().setBorderColor(newState.getHexagon().getBorderColor());
		
	}

	@Override
	public void unexecute() {

		 oldState.getHexagon().setX(original.getHexagon().getX());
		 oldState.getHexagon().setY(original.getHexagon().getY());
		 oldState.getHexagon().setR(original.getHexagon().getR());
		 oldState.getHexagon().setAreaColor(original.getHexagon().getAreaColor());
		 oldState.getHexagon().setBorderColor(original.getHexagon().getBorderColor());
		
	}

}
