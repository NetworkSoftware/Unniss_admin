package pro.network.unnissadmin.coupon;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class CouponProduct implements Serializable {
    String id;
    String amt;
    String description;
    String coupon;
    String status;
    String isPercent;
    String percentage;
    String minimumOrder;

    public String getIsPercent() {
        return isPercent;
    }

    public void setIsPercent(String isPercent) {
        this.isPercent = isPercent;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public CouponProduct() {
    }

    public CouponProduct(String id, String amt, String status, String coupon, String description) {
        this.id = id;
        this.amt = amt;
        this.status = status;
        this.coupon = coupon;
        this.description = description;

    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(String minimumOrder) {
        this.minimumOrder = minimumOrder;
    }
}