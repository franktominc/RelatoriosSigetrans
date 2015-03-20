package models;

/**
 * Created by ftominc on 19/03/15.
 */
public class ReportData {
    private String data;
    private Integer quantity;

    public ReportData(String data, Integer quantity) {
        this.data = data;
        this.quantity = quantity;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString(){
        return "['" + data + "':" +"" +
                + quantity + "]";
    }
}
