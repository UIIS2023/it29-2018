package observer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SelectedObjects {
	
	private int listLenght;
	private PropertyChangeSupport propertyChangeSupport;
	
	public SelectedObjects() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) 
	{
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener)
	{
		propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
	}

	public void setListLenght(int listLenght) {
		
		propertyChangeSupport.firePropertyChange("lenght", this.listLenght, listLenght);
		this.listLenght = listLenght;
	}

	public int getListLenght() {
		return listLenght;
	}
	
	

}
