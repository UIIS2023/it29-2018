package command;

import java.util.ArrayList;
import java.util.Iterator;

import geometry.Shape;
import mvc.DrawingModel;

public class RemoveShapeCmd implements Command {

	
	private DrawingModel model;
	private ArrayList <Shape> temp = new ArrayList <Shape>();
	private ArrayList <Shape> selected = new ArrayList <Shape>();
	Shape shape;
	
	public RemoveShapeCmd(DrawingModel model, ArrayList<Shape> selected)
	{
		this.selected = selected;
		temp.addAll(selected);
		this.model = model;
	}
	@Override
	public void execute() {
		
		Iterator<Shape> it = temp.iterator();
		while(it.hasNext())
		{	
			shape = it.next();	
			shape.setSelected(false);
			selected.remove(shape);
			this.model.remove(shape);
		}	
	}

	@Override
	public void unexecute() {

		selected.addAll(temp);
		Iterator<Shape> it = temp.iterator();
		while(it.hasNext()){
			
		Shape shape = it.next();
		shape.setSelected(true);
		model.add(shape);
		
		}
		
		
		
	}
}
