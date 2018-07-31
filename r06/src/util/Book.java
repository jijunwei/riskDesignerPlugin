package util;

public class Book {
	public String id;
	public String name;
	public String author;
	public Integer year;
	public Double price;
	public String language;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	 public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	@Override
	    public String toString() {
	        return "Book [id=" + id + ", name=" + name + ", author=" + author
	                + ", year=" + year + ", price=" + price +", language=" + language+ "]";
	    }
}
