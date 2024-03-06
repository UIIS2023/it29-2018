package command;


import geometry.Shape;
import mvc.DrawingModel;

public class ToBackCmd implements Command{
	
	private DrawingModel model;
	private Shape shape;
	private Shape tempShape;
	
	public ToBackCmd() {
		
	}
	
	public ToBackCmd(DrawingModel model, Shape shape) {
		this.model = model;
		this.shape = shape;
	}
	
	@Override
	public void execute() {

			int index = model.getShapes().indexOf(shape);
			tempShape = model.getShapes().get(index-1);
			model.getShapes().set(index, tempShape);
			model.getShapes().set(index-1, shape);
		
	}

	@Override
	public void unexecute() {
		
		int index = model.getShapes().indexOf(shape);
		tempShape = model.getShapes().get(index+1);
		model.getShapes().set(index, tempShape);
		model.getShapes().set(index+1, shape);

	}

}
