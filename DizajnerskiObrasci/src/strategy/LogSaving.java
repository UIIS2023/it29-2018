package strategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class LogSaving implements Save {
	
	DefaultListModel<String> defaultListModel;

	@SuppressWarnings("unchecked")
	@Override
	public void save(Object o) {

		this.defaultListModel = (DefaultListModel<String>) o;
		JFileChooser fileChooser = new JFileChooser();
		
		if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			
				try {
					String path = file.getPath() + ".txt";
					file = new File(path);
					
					FileWriter fileWriter = new FileWriter(file.getPath(), true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					PrintWriter printWriter = new PrintWriter(bufferedWriter);
					
					int index = 0;
					while(index < defaultListModel.size()) {
						printWriter.write(defaultListModel.get(index) + "\n");
						index++;
					}
					
					printWriter.close();
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "A problem has occured.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				
		}
		
		
	}

}
