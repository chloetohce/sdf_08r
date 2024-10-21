
import java.util.*;

public class Product {
    private final long id;
    private final String prodName;
    private final String prodDesc;
    private final String prodCategory;
    private final Date date;
    private final float price;
    
    public Product(long id, String prodName, String prodDesc, String prodCategory, Date date, float price) {
        this.id = id;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodCategory = prodCategory;
        this.date = date;
        this.price = price;
    }
    
    public long getId() {
        return id;
    }
    public String getProdDesc() {
        return prodDesc;
    }
    public String getProdCategory() {
        return prodCategory;
    }
    public Date getDate() {
        return date;
    }

    public float getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return this.prodName;
    }

    
}