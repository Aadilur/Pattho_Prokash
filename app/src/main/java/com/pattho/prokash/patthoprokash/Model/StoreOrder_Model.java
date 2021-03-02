package com.pattho.prokash.patthoprokash.Model;

import java.util.HashMap;

public class StoreOrder_Model {

   private String  Txid;
//    private HashMap<String ,HashMap<String ,Object>> books;
    private String contact;
    private String paymentAccount;
    private String paymentMethod;
    private String shipping;
    private String status;
    private long time;
    private String totalPrice;
    private String uid;





    public StoreOrder_Model() {
    }



    public StoreOrder_Model(String txid,  String contact, String paymentAccount, String paymentMethod, String shipping, String status, long time, String totalPrice, String uid) {
        Txid = txid;
//        this.books = books;
        this.contact = contact;
        this.paymentAccount = paymentAccount;
        this.paymentMethod = paymentMethod;
        this.shipping = shipping;
        this.status = status;
        this.time = time;
        this.totalPrice = totalPrice;
        this.uid = uid;
    }

    public String getTxid() {
        return Txid;
    }

    public void setTxid(String txid) {
        Txid = txid;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
