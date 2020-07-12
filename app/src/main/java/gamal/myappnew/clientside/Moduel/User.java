package gamal.myappnew.clientside.Moduel;

public class User {
    String username,bio,imageurl,phone,id,address;
    boolean IsStaff;

    public User(String username, String bio, String imageurl, String phone, String id,String address,boolean IsStaff) {
        this.username = username;
        this.bio = bio;
        this.imageurl = imageurl;
        this.phone = phone;
        this.id = id;
        this.address=address;
        this.IsStaff=IsStaff;
    }

    public String getAddress() {
        return address;
    }

    public boolean getisStaff() {
        return IsStaff;
    }

    public void setStaff(boolean staff) {
        IsStaff = staff;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
