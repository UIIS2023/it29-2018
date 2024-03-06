package strategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import geometry.Shape;

public class DrawingSaving implements Save{
	
	ArrayList<Shape> list;
	
	public DrawingSaving() {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(Object o) {

		this.list = (ArrayList<Shape>) o;
		
		JFileChooser chooser = new JFileChooser();
		
		if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			FileOutputStream f;
			try {
				f = new FileOutputStream(chooser.getSelectedFile());
				ObjectOutputStream oos = new ObjectOutputStream(f);
				oos.writeObject(list);
				oos.close();
				f.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "A file could not be opened.", "Error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "A problem has occured.", "Error", JOptionPane.ERROR_MESSAGE);

			}
			
			
			
			
		}
	}
	
}
