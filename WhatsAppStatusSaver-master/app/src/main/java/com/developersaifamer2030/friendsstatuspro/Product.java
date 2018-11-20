package com.developersaifamer2030.friendsstatuspro;

/**
 * Created by MahmoudAhmed on 4/9/2018.
 */

public class Product {
    private String id;
    private String Name;
    private String City;
      private String Num;
    private String Sug;
    private String downloadimgurl;




    public Product(String Name, String Address, String Sug,String Num ,String downloadimgurl) {
        this.Name = Name;
        this.City = Address;
         this.Num = Num;
        this.Sug = Sug;
        this.downloadimgurl=downloadimgurl;
    }

    public Product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getAddress() {
        return City;
    }

    public void setAddress(String Address) {
        this.City = Address;
    }


    public String getNum() {
        return Num;
    }

    public void setNum(String Num) {
        this.Num = Num;
    }


    public String getSug() {
        return Sug;
    }

    public void setSug(String Sug) {
        this.Sug = Sug;
    }
    public String getDownloadimgurl() {
        return downloadimgurl;
    }
    public void setDownloadimgurl(String downloadimgurl) {
        this.downloadimgurl = downloadimgurl;
    }


}
