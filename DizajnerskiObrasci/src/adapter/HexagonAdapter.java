package adapter;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import geometry.Point;
import geometry.Shape;
import hexagon.Hexagon;

public class HexagonAdapter extends Shape implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hexagon hexagon = new Hexagon(10, 10, 10); 
		
	public HexagonAdapter()
	{
		
	}
	
	public HexagonAdapter(Point center, int radius) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
		hexagon.setR(radius);
	}
	
	public HexagonAdapter(Point center, int radius, boolean selected) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
		hexagon.setR(radius);
		hexagon.setSelected(selected);
	}
	
	public HexagonAdapter(Point center, int radius, boolean selected, Color color) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
		hexagon.setR(radius);
		hexagon.setSelected(selected);
		hexagon.setBorderColor(color);
	}
	
	public HexagonAdapter(Point center, int radius, boolean selected, Color color, Color innerColor) {
		hexagon.setX(center.getX());
		hexagon.setY(center.getY());
		hexagon.setR(radius);
		hexagon.setSelected(selected);
		hexagon.setBorderColor(color);
		hexagon.setAreaColor(innerColor);
	}
	

	@Override
	public void moveBy(int byX, int byY) {
		hexagon.setX(hexagon.getX() + byX);
		hexagon.setY(hexagon.getY() + byY);
		
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof Hexagon) {
			return (hexagon.getR() - ((Hexagon) o).getR());
		}
		return 0;
	}

	@Override
	public boolean contains(int x, int y) {
		return hexagon.doesContain(x, y);
	}

	@Override
	public void draw(Graphics g) {
		hexagon.paint(g);
	}
	
	public String toString() {
		return "Hexagon: center=( " + hexagon.getX() + " , " + hexagon.getY() + 
				" ), radius= " + hexagon.getR() + " , inner color= " + hexagon.getAreaColor().getRGB() + " , outer color= " + hexagon.getBorderColor().getRGB();
	}
		
	public HexagonAdapter clone() {
		HexagonAdapter hexa = new HexagonAdapter(new Point(10,10),  10);
		hexa.hexagon.setX(this.hexagon.getX());
		hexa.hexagon.setY(this.hexagon.getY());
		hexa.hexagon.setR(this.hexagon.getR());
		hexa.hexagon.setAreaColor(this.hexagon.getAreaColor());
		hexa.hexagon.setBorderColor(this.hexagon.getBorderColor());
		
		return hexa;
	}
	
	public boolean equals(Object obj) {
		if (obj instanceof HexagonAdapter) {
			HexagonAdapter temp = (HexagonAdapter) obj;
			if (this.getHexagon().getR() == temp.getHexagon().getR() && this.getHexagon().getX() == temp.getHexagon().getX() && this.getHexagon().getY() == temp.getHexagon().getY()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Hexagon getHexagon() {
		return hexagon;
	}

	public void setHexagon(Hexagon hexagon) {
		this.hexagon = hexagon;
	}
	
	public void setSelected(boolean selected) {
		hexagon.setSelected(selected);
	}
	
	public boolean isSelected() {
		return hexagon.isSelected();
	}
	
		
	
	

}
