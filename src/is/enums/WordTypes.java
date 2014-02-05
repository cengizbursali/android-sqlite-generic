package is.enums;

import is.utils.StringUtils;

public enum WordTypes {
	Noun(1),
	Verb(2),
	Adjective(3),
	Adverb(4),
	Conjuction(5),
	Phrasal_Verb(6),
	Idiom(7),
	Jargon(8),
	Pronoun(9),
	Preposition(10);

	private Integer id;

	private WordTypes(Integer _id) {
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
		WordTypes[] words = WordTypes.values();
		for (WordTypes item : words) {
			if(item.getID().toString().equals(value)){
				return item.name();
			}
		}
		return null;
	}
	
	public static WordTypes getEnumByValue(String value){
		WordTypes[] words = WordTypes.values();
		for (WordTypes item : words) {
			if(item.getID().toString().equals(value)){
				return item;
			}
		}
		return null;
	}
};
