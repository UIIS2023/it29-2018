package mvc;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import adapter.HexagonAdapter;
import command.AddShapeCmd;
import command.BringToBackCmd;
import command.BringToFrontCmd;
import command.Command;
import command.ModifyCircleCmd;
import command.ModifyDonutCmd;
import command.ModifyHexagonCmd;
import command.ModifyLineCmd;
import command.ModifyPointCmd;
import command.ModifyRectangleCmd;
import command.RemoveShapeCmd;
import command.ToBackCmd;
import command.ToFrontCmd;
import drawing.DlgCircle;
import drawing.DlgDonut;
import drawing.DlgHexagonAdapter;
import drawing.DlgLine;
import drawing.DlgPoint;
import drawing.DlgRectangle;
import geometry.Circle;
import geometry.Donut;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Shape;
import observer.SelectedObjects;
import observer.SelectedObjectsUpdate;
import strategy.DrawingSaving;
import strategy.LogSaving;
import strategy.SaveManager;

public class DrawingController {
	
	private DrawingModel model;
	private DrawingFrame frame;	
	
	public Shape newShape;
	
	private Shape selected;
	private Point startPoint;
	private Stack<Command> stackUndo = new Stack<Command>();
	private Stack<Command> stackRedo = new Stack<Command>();
	
	private ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
	private SelectedObjects observable = new SelectedObjects();
	private SelectedObjectsUpdate observer;
	
	SaveManager save;
	LogSaving logSaving;
	DrawingSaving drawingSaving;
	
	int lineCounter = 0;
	int lineIndex = 0;

	
	
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		
		observer = new SelectedObjectsUpdate(frame);
		observable.addPropertyChangeListener(observer);

	}
	

	protected void thisMouseClicked(MouseEvent e) {
		
		 newShape = null;
			
		if (frame.getTglbtnSelection().isSelected()) {
			
			selectionMethod(e);

		} else if (frame.getTglbtnPoint().isSelected()) {
			newShape = new Point(e.getX(), e.getY(), false, frame.getBtnOutterColor().getBackground());
		} else if (frame.getTglbtnLine().isSelected()) {
			if (startPoint == null) {
				startPoint = new Point(e.getX(), e.getY());
			} else {
				newShape = new Line(startPoint, new Point(e.getX(), e.getY()), false,
						frame.getBtnOutterColor().getBackground());
				startPoint = null;
			}

		} else if (frame.getTglbtnCircle().isSelected()) {
			
			createDialogs("circle", e);
			

		} else if (frame.getTglbtnDonut().isSelected()) {
			
			createDialogs("donut", e);		

		} else if (frame.getTglbtnRectangle().isSelected()) {
			
			createDialogs("rectangle", e);

		} else if (frame.getTglbtnHexagon().isSelected()) {
			
			createDialogs("hexagon", e);
			
		}
		
		if (newShape != null) {
			AddShapeCmd addShape = new AddShapeCmd(newShape, model);
			addShape.execute();
			frame.defaultListModel.addElement("Added shape " + newShape.toString());
			stackUndo.push(addShape);
			frame.getBtnRedo().setVisible(false);
			frame.getBtnUndo().setVisible(true);
			stackRedo.clear();

		}
		frame.repaint();
	}
	

	
	protected void modify() {

		if (selected != null) {
			if (selected instanceof Point) {
				
				createDialogsModification("point");

			} else if (selected instanceof Line) {
				
				createDialogsModification("line");
		
			} else if (selected instanceof Rectangle) {
				
				createDialogsModification("rectangle");

			} else if (selected instanceof Donut) {
				
				createDialogsModification("donut");

			} else if (selected instanceof Circle) {
				
				createDialogsModification("circle");

			} else if (selected instanceof HexagonAdapter) {
				
				createDialogsModification("hexagon");
				
			}
			
			frame.repaint();
		}

		else {
			JOptionPane.showMessageDialog(null, "You must select the object first!");
		}
	}

	public void delete() {
		if (selected != null) {
			int option = JOptionPane.showConfirmDialog(null, "Are you sure that you want to delete this selection?", "Delete", JOptionPane.YES_NO_OPTION);
			if (option == 0) {
				RemoveShapeCmd removeShape = new RemoveShapeCmd(model, selectedShapes);
				frame.defaultListModel.addElement("Deleted shape " + selectedShapes.toString());

				removeShape.execute();
				
				observable.setListLenght(selectedShapes.size());


				stackUndo.push(removeShape);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				stackRedo.clear();

			} 
			frame.repaint();
		} else {
			JOptionPane.showMessageDialog(null, "You must select the object first!");
		}
	}
	
	protected void undo() {
		try {

			Command undoCommand = stackUndo.pop();
			frame.defaultListModel.addElement("Undo command");

			undoCommand.unexecute();
			
			stackRedo.push(undoCommand);
					
			
			frame.repaint();
			frame.getBtnRedo().setVisible(true);
			if (stackUndo.isEmpty()) {
				frame.getBtnUndo().setVisible(false);
			}

		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No more actions to undo!");
		}
	}

	protected void redo() {
		try {
			Command redoCommand = stackRedo.pop();
			frame.defaultListModel.addElement("Redo command");
			redoCommand.execute();
			stackUndo.push(redoCommand);
			
						
			frame.repaint();
			frame.getBtnUndo().setVisible(true);
			if (stackRedo.isEmpty()) {
				frame.getBtnRedo().setVisible(false);
			}

		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "No more actions to redo!");
		}

	}
	
	protected void toFront() {
		
		try {
			ToFrontCmd toFront = new ToFrontCmd(model, selected);
			toFront.execute();
			frame.repaint();
			stackUndo.push(toFront);
			frame.getBtnUndo().setVisible(true);
			frame.getBtnRedo().setVisible(false);
			stackRedo.clear();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Shape is already in the front", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
			
			frame.defaultListModel.addElement("ToFront " + selected.toString());

		
	}
	
	protected void toBack() {
		
		try {
			
			ToBackCmd toBack = new ToBackCmd(model, selected);
			toBack.execute();
			frame.repaint();
			stackUndo.push(toBack);
			frame.getBtnUndo().setVisible(true);
			frame.getBtnRedo().setVisible(false);
			stackRedo.clear();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Shape is already in the back", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
			
			frame.defaultListModel.addElement("ToBack " + selected.toString());

		
	}

	protected void bringToFront() {
		
		if(model.getShapes().lastIndexOf(selected) == model.getShapes().size() - 1) {
			JOptionPane.showMessageDialog(null, "Shape is already in the front!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}					

		BringToFrontCmd bringToFront = new BringToFrontCmd(model, selected);
		bringToFront.execute();
		frame.defaultListModel.addElement("BringToFront " + selected.toString());
		frame.repaint();
		stackUndo.push(bringToFront);
		frame.getBtnUndo().setVisible(true);
		frame.getBtnRedo().setVisible(false);
		stackRedo.clear();

	}

	protected void bringToBack() {
		
		if(model.getShapes().lastIndexOf(selected) == 0) {
			JOptionPane.showMessageDialog(null, "Shape is already in the back!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		BringToBackCmd bringToBack = new BringToBackCmd(model, selected);
		bringToBack.execute();
		frame.defaultListModel.addElement("BringToBack " + selected.toString());
		frame.repaint();
		stackUndo.push(bringToBack);
		frame.getBtnUndo().setVisible(true);
		frame.getBtnRedo().setVisible(false);
		stackRedo.clear();

	}
	
	private void createDialogs(String type, MouseEvent e) {
		
		switch (type) {
		case "circle":
			DlgCircle dlgC = new DlgCircle();
			
			dlgC.getTxtX().setEditable(false);
			dlgC.getTxtY().setEditable(false);
			
			dlgC.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
			dlgC.getBtnOuterColor().setBackground(frame.getBtnOutterColor().getBackground());
			dlgC.setModal(true);
			dlgC.getTxtX().setText(Integer.toString(e.getX()));
			dlgC.getTxtY().setText(Integer.toString(e.getY()));			
			
			dlgC.setVisible(true);
			if (!dlgC.isOk())
				return;
			try {
				newShape = dlgC.getCircle();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Wrong data type", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			break;
		case "donut":
			DlgDonut dlgD = new DlgDonut();

			dlgD.getTxtX().setEditable(false);
			dlgD.getTxtY().setEditable(false);
			
			dlgD.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
			dlgD.getBtnOuterColor().setBackground(frame.getBtnOutterColor().getBackground());
			dlgD.getTxtX().setText(Integer.toString(e.getX()));
			dlgD.getTxtY().setText(Integer.toString(e.getY()));
			dlgD.setModal(true);
			dlgD.setVisible(true);
			
			
			if (!dlgD.isOk()) {
				return;
			}
			
			try {
				newShape = dlgD.getDonut();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "rectangle":
			DlgRectangle dlgR = new DlgRectangle();
			
			dlgR.getTxtX().setEditable(false);
			dlgR.getTxtY().setEditable(false);
			
			
			dlgR.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
			dlgR.getBtnOuterColor().setBackground(frame.getBtnOutterColor().getBackground());
			dlgR.setModal(true);
			dlgR.getTxtX().setText(Integer.toString(e.getX()));
			dlgR.getTxtY().setText(Integer.toString(e.getY()));
			dlgR.setVisible(true);
			
			if (!dlgR.isOk()) {
				return;
			}
			try {
				newShape = dlgR.getRectangle();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Wrong data type", "Error", JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "hexagon":
			DlgHexagonAdapter dlgH = new DlgHexagonAdapter();
			
			dlgH.getTxtX().setEditable(false);
			dlgH.getTxtY().setEditable(false);
			
			dlgH.getBtnInnerColor().setBackground(frame.getBtnInnerColor().getBackground());
			dlgH.getBtnOuterColor().setBackground(frame.getBtnOutterColor().getBackground());
			dlgH.setModal(true);
			dlgH.getTxtX().setText(Integer.toString(e.getX()));
			dlgH.getTxtY().setText(Integer.toString(e.getY()));

			dlgH.setVisible(true);
			
			if (!dlgH.isOk())
				return;
			try {
				newShape = dlgH.getHexagon();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Wrong data type", "Error", JOptionPane.ERROR_MESSAGE);
			}
			break;

		default:
			break;
		}
	}
	
	private void createDialogsModification(String type) {
		switch (type) {
		case "point":
			Point point = (Point) selected;
			DlgPoint dlgP = new DlgPoint();
			dlgP.getTxtX().setText(Integer.toString(((Point) selected).getX()));
			dlgP.getTxtY().setText(Integer.toString(((Point) selected).getY()));
			dlgP.getBtnColor().setBackground(point.getColor());
			dlgP.setModal(true);
			dlgP.setVisible(true);
			if (dlgP.isOk) {
				ModifyPointCmd modifyPoint = new ModifyPointCmd(point, dlgP.getPoint());
				modifyPoint.execute();
				stackUndo.push(modifyPoint);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
			
			break;
		case "line":
			Line line = (Line) selected;
			DlgLine dlgL = new DlgLine();
			dlgL.getTxtStartPointX().setText(Integer.toString(((Line) selected).getStartPoint().getX()));
			dlgL.getTxtStartPointY().setText(Integer.toString(((Line) selected).getStartPoint().getY()));
			dlgL.getTxtEndPointY().setText(Integer.toString(((Line) selected).getEndPoint().getY()));
			dlgL.getTxtEndPointX().setText(Integer.toString(((Line) selected).getEndPoint().getX()));
			dlgL.getBtnColor().setBackground(line.getColor());
			dlgL.setModal(true);
			dlgL.setVisible(true);
			if (dlgL.isOk) {
				ModifyLineCmd modifyLine = new ModifyLineCmd(line, dlgL.getLine());
				modifyLine.execute();
				stackUndo.push(modifyLine);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
			
			break;
		case "rectangle":
			Rectangle rectangle = (Rectangle) selected;
			DlgRectangle dlgR = new DlgRectangle();
			dlgR.getTxtX().setText(Integer.toString(((Rectangle) selected).getUpperLeftPoint().getX()));
			dlgR.getTxtY().setText(Integer.toString(((Rectangle) selected).getUpperLeftPoint().getY()));
			dlgR.getTxtHeight().setText(Integer.toString(((Rectangle) selected).getHeight()));
			dlgR.getTxtWidth().setText(Integer.toString(((Rectangle) selected).getWidth()));
			dlgR.getBtnInnerColor().setBackground(rectangle.getInnerColor());
			dlgR.getBtnOuterColor().setBackground(rectangle.getColor());

			dlgR.setModal(true);
			dlgR.setVisible(true);
			if (dlgR.isOk) {
				ModifyRectangleCmd modifyRectangle = new ModifyRectangleCmd(rectangle, dlgR.getRectangle());
				modifyRectangle.execute();
				stackUndo.push(modifyRectangle);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
			
			
			break;
		case "donut":
			Donut donut = (Donut) selected;
			DlgDonut dlgD = new DlgDonut();
			dlgD.getTxtX().setText(Integer.toString(((Donut) selected).getCenter().getX()));
			dlgD.getTxtY().setText(Integer.toString(((Donut) selected).getCenter().getY()));
			dlgD.getTxtInner().setText(Integer.toString(((Donut) selected).getInnerRadius()));
			dlgD.getTxtOuter().setText(Integer.toString(((Donut) selected).getRadius()));
			dlgD.getBtnOuterColor().setBackground(donut.getColor());
			dlgD.getBtnInnerColor().setBackground(donut.getInnerColor());
			dlgD.setModal(true);
			dlgD.setVisible(true);
			if (dlgD.isOk) {
				ModifyDonutCmd modifyDonut = new ModifyDonutCmd(donut, dlgD.getDonut());
				modifyDonut.execute();
				stackUndo.push(modifyDonut);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
			
			
			break;
		case "circle":
			Circle circle = (Circle) selected;
			DlgCircle dlgC = new DlgCircle();
			dlgC.getTxtX().setText(Integer.toString(((Circle) selected).getCenter().getX()));
			dlgC.getTxtY().setText(Integer.toString(((Circle) selected).getCenter().getY()));
			dlgC.getTxtRadius().setText(Integer.toString(((Circle) selected).getRadius()));
			dlgC.getBtnInnerColor().setBackground(circle.getInnerColor());
			dlgC.getBtnOuterColor().setBackground(circle.getColor());
			dlgC.setModal(true);
			dlgC.setVisible(true);
			if (dlgC.isOk) {
				ModifyCircleCmd modifyCircle = new ModifyCircleCmd(circle, dlgC.getCircle());
				modifyCircle.execute();
				stackUndo.push(modifyCircle);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
				
			break;
			
		case "hexagon":
			HexagonAdapter hexagon = (HexagonAdapter) selected;
			DlgHexagonAdapter dlgH = new DlgHexagonAdapter();
			dlgH.getTxtX().setText(Integer.toString(((HexagonAdapter) selected).getHexagon().getX()));
			dlgH.getTxtY().setText(Integer.toString(((HexagonAdapter) selected).getHexagon().getY()));
			dlgH.getTxtRadius().setText(Integer.toString(((HexagonAdapter) selected).getHexagon().getR()));
			dlgH.getBtnInnerColor().setBackground(hexagon.getHexagon().getAreaColor());
			dlgH.getBtnOuterColor().setBackground(hexagon.getHexagon().getBorderColor());
			dlgH.setModal(true);
			dlgH.setVisible(true);
			if (dlgH.isOk) {
				ModifyHexagonCmd modifyHexagonCmd = new ModifyHexagonCmd(hexagon, dlgH.getHexagon());
				modifyHexagonCmd.execute();
				stackUndo.push(modifyHexagonCmd);
				stackRedo.clear();
				frame.getBtnUndo().setVisible(true);
				frame.getBtnRedo().setVisible(false);
				
				frame.defaultListModel.addElement("Modified shape " + selected.toString());

			}
			
			
			break;

		default:
			JOptionPane.showMessageDialog(null, "There was an error during the seleciton.", "Error", JOptionPane.ERROR_MESSAGE);
			break;
		}
	}
	
	private void selectionMethod(MouseEvent e) {
		selected = null;
		Iterator<Shape> it = model.getShapes().iterator();

		while (it.hasNext()) {
			Shape shape = it.next();

			if (shape.contains(e.getX(), e.getY())) {
				selected = shape;

			}
		}

		if (selected != null) {

			if (selected.isSelected()) {
				selected.setSelected(false);
				
				frame.defaultListModel.addElement("Deselected element " + selected.toString());
				
				selectedShapes.remove(selected);
				observable.setListLenght(selectedShapes.size());
				
				if(observable.getListLenght() != 0) {
					selected =  selectedShapes.get(observable.getListLenght() - 1);

				} 
							

			} else {
				selected.setSelected(true);
				
				frame.defaultListModel.addElement("Selected element " + selected.toString());

				selectedShapes.add(selected);
				observable.setListLenght(selectedShapes.size());

			}

		}
	}
	
	protected void saveLog() {
		
		logSaving = new LogSaving();
		save = new SaveManager(logSaving);
		
		save.save(frame.defaultListModel);
		
	}
	
	protected void loadLog() {
		
		frame.getBtnLoadNext().setVisible(true);
	
		JFileChooser fileChooser = new JFileChooser();
				
			if(fileChooser.showOpenDialog(null) == JFileChooser.OPEN_DIALOG) {
			
				File file = fileChooser.getSelectedFile();
				
				String line;
				
				FileReader fileReader;
			
			
			try {
				fileReader = new FileReader(file);
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "File was not chosen.", "Error", JOptionPane.ERROR_MESSAGE);
				return;		
				}
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			try {
				while((line = bufferedReader.readLine()) != null) {
					frame.defaultListModel.addElement(line);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "There was a problem with reading the next line.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			try {
				bufferedReader.close();
				fileReader.close();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "A problem has occured.", "Error", JOptionPane.ERROR_MESSAGE);
			}
				
			
		}		
		
	}
	
	protected void loadNext() {
		
		Shape shape = null;
		Command command = null;
		
		
		lineCounter++;
		
		frame.getList().setSelectedIndex(lineCounter - 1);
		frame.getList().ensureIndexIsVisible(lineCounter);
		
		if(lineIndex < frame.defaultListModel.getSize()) {
			
			String line = frame.defaultListModel.get(lineIndex);
			String[] parsedLine = line.split(" ");
			
			if(parsedLine[0].equals("Added")) {
				
				shape = this.makeShape(parsedLine);
				
				command = new AddShapeCmd(shape, model);
				
			}
			
			if (parsedLine[0].equals("Deleted")) {

				command = new RemoveShapeCmd(model, selectedShapes);

			}
			if (parsedLine[0].equals("Modified")) {

				Shape oldShape = selectedShapes.get(0);

				Shape newShape = this.makeShape(parsedLine);
				
				

				if (oldShape instanceof Point) {
					command = new ModifyPointCmd((Point) oldShape, (Point) newShape);
				}

				else if (oldShape instanceof Line) {
					command = new ModifyLineCmd((Line) oldShape, (Line) newShape);
				} else if (oldShape instanceof Circle) {
					command = new ModifyCircleCmd((Circle) oldShape, (Circle) newShape);
				} else if (oldShape instanceof Rectangle) {
					command = new ModifyRectangleCmd((Rectangle) oldShape, (Rectangle) newShape);
				} else if (oldShape instanceof Donut) {
					command = new ModifyDonutCmd((Donut) oldShape, (Donut) newShape);
				} else if (oldShape instanceof HexagonAdapter) {
					command = new ModifyHexagonCmd((HexagonAdapter) oldShape, (HexagonAdapter) newShape);
				}
				
				


			}

			if (parsedLine[0].equals("Selected")) {
				Shape selectedShape = this.makeShape(parsedLine);
				
				int index = model.getShapes().indexOf(selectedShape);
				
				shape = model.get(index);
				shape.setSelected(true);
				selectedShapes.add(shape);
				
				observable.setListLenght(selectedShapes.size());

			}
			
			if (parsedLine[0].equals("Deselected")) {
				Shape selectedShape = this.makeShape(parsedLine);
				int index = model.getShapes().indexOf(selectedShape);
				shape = model.get(index);
				shape.setSelected(false);
				selectedShapes.remove(shape);
				
				observable.setListLenght(selectedShapes.size());


			}

			if (parsedLine[0].equals("ToFront")) {

				shape = selectedShapes.get(0);

				command = new ToFrontCmd(model, shape);
			}

			if (parsedLine[0].equals("ToBack")) {
				shape = selectedShapes.get(0);
				command = new ToBackCmd(model, shape);
			}

			if (parsedLine[0].equals("BringToFront")) {
				shape = selectedShapes.get(0);
				command = new BringToFrontCmd(model, shape);
			}

			if (parsedLine[0].equals("BringToBack")) {
				shape = selectedShapes.get(0);
				command = new BringToBackCmd(model, shape);
			}

			if (command != null) {

				command.execute();
				stackUndo.push(command);
			}

			if (parsedLine[0].equals("Undo")) {
				Command undoCmd = stackUndo.pop();
				undoCmd.unexecute();

				stackRedo.push(undoCmd);
			}

			if (parsedLine[0].equals("Redo")) {
				Command redoCmd = stackRedo.pop();
				redoCmd.execute();
				stackUndo.push(redoCmd);
			}

			lineIndex++;
			frame.repaint();

		}
		if (lineCounter == frame.defaultListModel.getSize()) {
			frame.getBtnLoadNext().setVisible(false);
		}
		
		
	}
	
	public Shape makeShape(String[] parsedLine) {
		
		if(parsedLine[2].equals("Point:")) {
			return new Point(Integer.parseInt(parsedLine[4]), Integer.parseInt(parsedLine[6]), false, new Color(Integer.parseInt(parsedLine[9])));
		
	} else if (parsedLine[2].equals("Line:")) {

		Point startPoint = new Point(Integer.parseInt(parsedLine[5]), Integer.parseInt(parsedLine[7]));
		Point endPoint = new Point(Integer.parseInt(parsedLine[11]), Integer.parseInt(parsedLine[13]));
		return new Line(startPoint, endPoint, false, new Color(Integer.parseInt(parsedLine[16])));

	} else if (parsedLine[2].equals("Rectangle:")) {
		Point upperLeftPoint = new Point(Integer.parseInt(parsedLine[6]), Integer.parseInt(parsedLine[8]));
		return new Rectangle(upperLeftPoint, Integer.parseInt(parsedLine[14]), Integer.parseInt(parsedLine[11]), false,
				new Color(Integer.parseInt(parsedLine[22])), new Color(Integer.parseInt(parsedLine[18])));
	} else if (parsedLine[2].equals("Circle:")) {
		Point center = new Point(Integer.parseInt(parsedLine[4]), Integer.parseInt(parsedLine[6]));
		return new Circle(center, Integer.parseInt(parsedLine[9]), false, new Color(Integer.parseInt(parsedLine[17])),
				new Color(Integer.parseInt(parsedLine[13])));
	} else if (parsedLine[2].equals("Donut:")) {
		Point center = new Point(Integer.parseInt(parsedLine[4]), Integer.parseInt(parsedLine[6]));
		return new Donut(center, Integer.parseInt(parsedLine[9]), Integer.parseInt(parsedLine[13]), false,
				new Color(Integer.parseInt(parsedLine[21])), new Color(Integer.parseInt(parsedLine[17])));
} 
	else if (parsedLine[2].equals("Hexagon:")) {
		Point center = new Point(Integer.parseInt(parsedLine[4]), Integer.parseInt(parsedLine[6]));
		return new HexagonAdapter(center, Integer.parseInt(parsedLine[9]), false,  new Color(Integer.parseInt(parsedLine[17])),
				new Color(Integer.parseInt(parsedLine[13])));
	}

	return null;
		
	}
	
	protected void saveDrawing() {
		drawingSaving = new DrawingSaving();
		save = new SaveManager(drawingSaving);
		
		save.save(model.getShapes());
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadDrawing() throws ClassNotFoundException {
		
		
		JFileChooser fileChooser = new JFileChooser();
				
			if(fileChooser.showOpenDialog(null) == JFileChooser.OPEN_DIALOG) {
				
				FileInputStream f;
				
				try {
					f = new FileInputStream(fileChooser.getSelectedFile());
					ObjectInputStream ois = new ObjectInputStream(f);
					model.getShapes().addAll((ArrayList<Shape>)ois.readObject());
					ois.close();
					f.close();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "A problem has occured.", "Error", JOptionPane.ERROR_MESSAGE);

				}
				
				
			}
			
			frame.repaint();
		
	}
	
	
		
}
	


