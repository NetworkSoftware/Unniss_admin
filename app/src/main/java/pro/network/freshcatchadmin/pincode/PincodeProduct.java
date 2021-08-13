package pro.network.freshcatchadmin.pincode;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class PincodeProduct implements Serializable {
    String id;
    String pincode;



    public PincodeProduct() {
    }



    public PincodeProduct(String id, String pincode) {
        this.id = id;
         this.pincode = pincode;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}