package strategy;

public class SaveManager implements Save {
	
	private Save save;
	
	public SaveManager(Save save) {
		this.save = save;
	}

	@Override
	public void save(Object o) {
		save.save(o);
	}

}
