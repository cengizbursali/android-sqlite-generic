package is.model;

import is.enums.WordLevels;
import is.enums.WordTypes;

public class Word {
	
	private int id;
	private String eng;
	private String tr;
	private WordLevels level;
	private WordTypes type;
	private Integer category;
	
	public Word(){
		super();
	}

	public Word(int _id, String _eng, String _tr, WordTypes _type, WordLevels _level, Integer _category){
		this.id = _id;
		this.eng = _eng;
		this.tr = _tr;
		this.type = _type;
		this.level = _level;
		this.category = _category;
	}
	
	public Word(String _eng, String _tr, WordTypes _type, WordLevels _level, Integer _category){
		this.eng = _eng;
		this.tr = _tr;
		this.type = _type;
		this.level = _level;
		this.category = _category;
	}

	public Word(int _id, String _eng, String _tr, WordTypes _type, WordLevels _level){
		this.id = _id;
		this.eng = _eng;
		this.tr = _tr;
		this.type = _type;
		this.level = _level;
	}
	
	public Word(String _eng, String _tr, WordTypes _type, WordLevels _level){
		this.eng = _eng;
		this.tr = _tr;
		this.type = _type;
		this.level = _level;
	}
	
	public Word(String _eng, String _tr, WordTypes _type){
		this.eng = _eng;
		this.tr = _tr;
		this.type = _type;
	}
	
	public Word(String _eng, String _tr){
		this.eng = _eng;
		this.tr = _tr;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEng() {
		return eng;
	}

	public void setEng(String eng) {
		this.eng = eng;
	}

	public String getTr() {
		return tr;
	}

	public void setTr(String tr) {
		this.tr = tr;
	}

	public WordLevels getLevel() {
		return level;
	}

	public void setLevel(WordLevels level) {
		this.level = level;
	}

	public WordTypes getType() {
		return type;
	}

	public void setType(WordTypes type) {
		this.type = type;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

}
