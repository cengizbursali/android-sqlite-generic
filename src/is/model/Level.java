package is.model;

import is.enums.WordLevels;

public class Level {
	
	private WordLevels level;
	private String description;
	
	public Level(){
		
	}
	
	public Level(WordLevels _level){
		level = _level;
	}
	
	public Level(String _id){
		level = WordLevels.getEnumByValue(_id);
	}
	
	public WordLevels getLevel() {
		return level;
	}
	
	public void setLevel(WordLevels level) {
		this.level = level;
	}
	
	public String getDescription() {
		description = WordLevels.getEnumNameByValue(level.getID().toString());
		return description;
	}

}
