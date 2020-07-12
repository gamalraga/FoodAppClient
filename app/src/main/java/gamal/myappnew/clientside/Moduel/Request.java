package gamal.myappnew.clientside.Moduel;

import java.util.List;

import gamal.myappnew.clientside.DATEBASE.Cart;

public class Request {
    String Request_id,phone,name,adress,total,status, imageurl,comment,paymentStatus,lat,lng;
    List<Cart> foods;

    public Request(String phone, String name, String adress, String total, List<Cart> foods,String imageurl,String comment,String paymentStatus,String lat,String lng) {
        this.phone = phone;
        this.name = name;
        this.adress = adress;
        this.total = total;
        this.foods = foods;
        this.imageurl=imageurl;
        this.comment=comment;
    this.paymentStatus=paymentStatus;
    this.lat=lat;
    this.lng=lng;
        this.status="0";//default is 0:0:placed,1:shipping,2:shipped
    }

    public String getRequest_id() {
        return Request_id;
    }

    public void setRequest_id(String request_id) {
        Request_id = request_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Cart> getFoods() {
        return foods;
    }

    public void setFoods(List<Cart> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public Request() {
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

