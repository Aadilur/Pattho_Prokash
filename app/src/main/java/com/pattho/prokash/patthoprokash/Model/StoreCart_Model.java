package com.pattho.prokash.patthoprokash.Model;

public class StoreCart_Model {

    private String bid;
    private String price;
    private String quantity;


    private String author;
    private String bName;
    private String cover;

    public StoreCart_Model() {
    }

    public StoreCart_Model(String bid, String price, String quantity) {
        this.bid = bid;
        this.price = price;
        this.quantity = quantity;
    }

    public StoreCart_Model(String bid, String price, String quantity, String author, String bName, String cover) {
        this.bid = bid;
        this.price = price;
        this.quantity = quantity;
        this.author = author;
        this.bName = bName;
        this.cover = cover;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
