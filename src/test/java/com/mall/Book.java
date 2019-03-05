package com.mall;

public class Book {
   private String bName;
   private int bNo;
   private Integer bPrice;
   public static int bNoCount;
   public static int nums;

   public static void main (String[] args){
    Book[] books=new Book[3];
    books[0]=new Book("JavaScript高级程序设计第三版",52);
    books[1]=new Book("基于Node搭建亿级高并发流量可靠服务",99);
    books[2]=new Book("React组件封装思想",99);
    for (Book book:books){
        System.out.println(book.getbNo());
    }
   }
    public Book(String bName, Integer bPrice) {
        this.bNo=bNoCount++;
        this.bName = bName;
        this.bPrice = bPrice;
    }
    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public int getbNo() {
        return bNo;
    }

    public void setbNo(int bNo) {
        this.bNo = bNo;
    }

    public Integer getbPrice() {
        return bPrice;
    }

    public void setbPrice(Integer bPrice) {
        this.bPrice = bPrice;
    }


}
