package com.example.ethon.car_service_station.domain;

import java.io.Serializable;

/**
 * Created by Ethon on 2016/10/31.
 */

public class ContactDetails implements Serializable {

    private String tel;
    private String address;

    public ContactDetails() {
    }

    public ContactDetails(Builder builder) {
        this.address=builder.address;
        this.tel=builder.tel;
    }

    public static class Builder{
        private String tel;
        private String address;

        public Builder(String tel){this.tel=tel;}

        public Builder address(String value)
        {
            this.address=value;
            return this;
        }


        public Builder copy(ContactDetails value)
        {
            this.address=value.address;
            this.tel=value.tel;
            return this;
        }

        public ContactDetails build(){return new ContactDetails(this);}
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactDetails that = (ContactDetails) o;

        if (tel != null ? !tel.equals(that.tel) : that.tel != null) return false;
        return address != null ? address.equals(that.address) : that.address == null;

    }

    @Override
    public int hashCode() {
        int result = tel != null ? tel.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContactDetails{" +
                "tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
