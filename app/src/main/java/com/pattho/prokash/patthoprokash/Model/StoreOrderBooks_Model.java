package com.pattho.prokash.patthoprokash.Model;

public class StoreOrderBooks_Model {




    private String author;
    private String bName;
    private String bid;
    private String cover;
    private String price;
    private String quantity;

    private String Txid;
    private String uid;


    public StoreOrderBooks_Model() {
    }

    public StoreOrderBooks_Model(String author, String bName, String bid, String cover, String price, String quantity, String Txid, String uid) {
        this.author = author;
        this.bName = bName;
        this.bid = bid;
        this.cover = cover;
        this.price = price;
        this.quantity = quantity;
        this.Txid = Txid;
        this.uid = uid;
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

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getPrice() {
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

    public String getTxid() {
        return Txid;
    }

    public void setTxid(String txid) {
        this.Txid = txid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
