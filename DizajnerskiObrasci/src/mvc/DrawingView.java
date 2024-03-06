package mvc;

import java.awt.Graphics;
import java.util.Iterator;

import javax.swing.JPanel;

import geometry.Shape;

public class DrawingView extends JPanel{
	public DrawingView() {
	}

	private static final long serialVersionUID = 1L;
	
	
	private DrawingModel model =  new DrawingModel();


	public void setModel(DrawingModel model) {
		this.model = model;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Iterator<Shape> it = model.getShapes().iterator();
		
		while(it.hasNext()) {
			it.next().draw(g);
		}
		
		//repaint(); jer se ovo vrti stalno
	}
}
