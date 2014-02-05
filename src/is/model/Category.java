package is.model;

public class Category {

	private Integer id;
	private String categoryName;
	private String categoryDescription;

	public Category(){

	}

	public Category(String _name, String _description){
		categoryName = _name;
		categoryDescription = _description;
	}

	public Category(Integer _id, String _name, String _description){
		id = _id;
		categoryName = _name;
		categoryDescription = _description;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryDescription() {
		return categoryDescription;
	}

	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}

}
