package is.enums;

import is.utils.StringUtils;

public enum WordLevels {
	VERY_EASY(1),
	EASY(2),
	NORMAL(3),
	HARD(4),
	VERY_HARD(5);
	

	private Integer id;

	private WordLevels(Integer _id) {
		id = _id;
	}

	public Integer getID() {
		return id;
	}

	@Override
	public String toString(){
		return StringUtils.toLowerCaseWithoutEachInitial(this.name(), "_");
	}
	
	public static String getEnumNameByValue(String value){
		WordLevels[] levels = WordLevels.values();
		for (WordLevels item : levels) {
			if(item.getID().toString().equals(value)){
				
				return item.name();
			}
		}
		return null;
	}
	
	public static WordLevels getEnumByValue(String value){
		WordLevels[] levels = WordLevels.values();
		for (WordLevels item : levels) {
			if(item.getID().toString().equals(value)){
				return item;
			}
		}
		return null;
	}
}
