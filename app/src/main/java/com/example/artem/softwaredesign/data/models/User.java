package com.example.artem.softwaredesign.data.models;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String newsSource;
    private int count;
    public User(int id, String firstName, String lastName, String email, String phone, String password,
                String newsResource, int count){
        this(id, firstName, lastName, email, phone, password, newsResource);
        this.count = count;
    }

    public User(String firsName, String lastName, String email, String phone){
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public User(int id, String firstName, String lastName, String email, String phone, String password, String newsSource){
        this(id, firstName, lastName, email, phone, password);
        this.newsSource = newsSource;
    }

    public User(int id, String firstName, String lastName, String email, String phone, String password){
        this(firstName, lastName, email, phone);
        this.password = password;
        this.id = id;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getPassword() {
        return this.password;
    }

    public int getId(){
        return this.id;
    }

    public String getNewsSource(){
        return this.newsSource;
    }

    @Override
    public boolean equals(Object object){
        if (!super.equals(object)){
            return  false;
        }

        if (this == object){
            return true;
        }
        if (this.getClass() != object.getClass()){
            return false;
        }
        User checkedUser = (User)object;
        return this.firstName.equals(checkedUser.getLastName()) &&
                this.lastName.equals(checkedUser.getLastName()) &&
                this.phone.equals(checkedUser.getPhone()) &&
                this.email.equals(checkedUser.getEmail());

    }

    public void setNewsSource(String newsSource){
        this.newsSource = newsSource;
    }

    public int getCount() {
        return count;
    }

}
