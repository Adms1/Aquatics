package water.works.waterworks.model;

public class AllInstructorItems {
	
	private String title;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	private boolean selected;
	public AllInstructorItems(){}
	 
	 public boolean isSelected() {
	  return selected;
	 }
	 public void setSelected(boolean selected) {
	  this.selected = selected;
	 }

	public AllInstructorItems(String title){
		this.title = title;
	}
	
	public AllInstructorItems(String title, boolean isCounterVisible, String count){
		this.title = title;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
		selected = false;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
