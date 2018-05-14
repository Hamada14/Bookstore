package server.database.entities;

public class Book {
	
	public Book(String bookISBN, String bookTitle, String publicationYear, float sellingPrice, String category,
			boolean inStock) {
		super();
		this.bookISBN = bookISBN;
		this.bookTitle = bookTitle;
		this.publicationYear = publicationYear;
		this.sellingPrice = sellingPrice;
		Category = category;
		this.inStock = inStock;
	}
	private String bookISBN;
    private String bookTitle;
    private String publicationYear;
    private float sellingPrice;
    private String Category;
    private boolean inStock;
    
    public Book() {
    	
    }
    public Book(String title) {
    	this.bookTitle = title;
    }
    
	public boolean isInStock() {
		return inStock;
	}
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	public String getBookISBN() {
		return bookISBN;
	}
	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}
	public String getBookTitle() {
		return bookTitle;
	}
	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}
	public String getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
	public float getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(float sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}

}
