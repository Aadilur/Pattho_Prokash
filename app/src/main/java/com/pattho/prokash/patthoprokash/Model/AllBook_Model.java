package com.pattho.prokash.patthoprokash.Model;

import android.app.Application;

import java.util.List;

public class AllBook_Model {

    Application application;

    private String author;
    private String author_img;
    private String book_name;
    private String condition;
    private String cover_img;
    private String description;
    private String id;
    private String new_price;
    private String page;
    private String price;
    private long time;
    private int status;
    private List<String> category;
    private List<String> genre;
    private List<String> keyword;

    public AllBook_Model(Application application) {
        this.application = application;
    }

    public AllBook_Model() {

    }

    public AllBook_Model
            ( String author,
              String author_img, String book_name, String condition,
              String cover_img, String description, String id, String new_price,
              String page, String price, long time, int status, List<String> category,
              List<String> genre, List<String> keyword) {


        this.author = author;
        this.author_img = author_img;
        this.book_name = book_name;
        this.condition = condition;
        this.cover_img = cover_img;
        this.description = description;
        this.id = id;
        this.new_price = new_price;
        this.page = page;
        this.price = price;
        this.time = time;
        this.status = status;
        this.category = category;
        this.genre = genre;
        this.keyword = keyword;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }
}
