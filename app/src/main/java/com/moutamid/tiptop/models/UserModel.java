package com.moutamid.tiptop.models;

public class UserModel {
    String ID, name, username, email, password, company, jobTitle, image;
    double walletMoney;
    boolean isTipper, isSubscribed;
    BankDetails bankDetails;

    public UserModel() {
    }

    public UserModel(String ID, String name, String username, String email, String password, String company, String jobTitle, double walletMoney, String image, boolean isTipper, boolean isSubscribed) {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.company = company;
        this.walletMoney = walletMoney;
        this.jobTitle = jobTitle;
        this.image = image;
        this.isTipper = isTipper;
        this.isSubscribed = isSubscribed;
    }

    public UserModel(String ID, String name, String username, String email, String password, String company, String jobTitle, String image, double walletMoney, boolean isTipper, boolean isSubscribed, BankDetails bankDetails) {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.company = company;
        this.jobTitle = jobTitle;
        this.image = image;
        this.walletMoney = walletMoney;
        this.isTipper = isTipper;
        this.isSubscribed = isSubscribed;
        this.bankDetails = bankDetails;
    }

    public double getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(double walletMoney) {
        this.walletMoney = walletMoney;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public boolean isTipper() {
        return isTipper;
    }

    public void setTipper(boolean tipper) {
        isTipper = tipper;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }
}
