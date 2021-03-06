package pro.network.unnissadmin.order;

import java.io.Serializable;
import java.util.ArrayList;

import pro.network.unnissadmin.product.Product;

/**
 * Created by ravi on 16/11/17.
 */

public class Order implements Serializable {
    String id;
    String items;
    String quantity;
    String price;
    String status;
    String user;
    String cashback;
    String name;
    String phone;
    String address;
    String reson;
    String trackId;
    ArrayList<Product> productBeans;
    String createdOn;
    String addressOrg,comments,payment,paymentId,delivery,deliveryTime,grandCost,shipCost;
    String amount,toPincode,createdon,totalAmt,rqty,rqtyType,size,image,payment_method_title,couponCost,variationId;

    public Order() {
    }

    public Order(String items, String quantity, String price, String status, String name, String phone, String address, String reson, ArrayList<Product> productBeans, String createdOn) {
        this.items = items;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.reson = reson;
        this.productBeans = productBeans;
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

    public ArrayList<Product> getProductBeans() {
        return productBeans;
    }

    public void setProductBeans(ArrayList<Product> productBeans) {
        this.productBeans = productBeans;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getAddressOrg() {
        return addressOrg;
    }

    public void setAddressOrg(String addressOrg) {
        this.addressOrg = addressOrg;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getGrandCost() {
        return grandCost;
    }

    public void setGrandCost(String grandCost) {
        this.grandCost = grandCost;
    }

    public String getShipCost() {
        return shipCost;
    }

    public void setShipCost(String shipCost) {
        this.shipCost = shipCost;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToPincode() {
        return toPincode;
    }

    public void setToPincode(String toPincode) {
        this.toPincode = toPincode;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRqty() {
        return rqty;
    }

    public void setRqty(String rqty) {
        this.rqty = rqty;
    }

    public String getRqtyType() {
        return rqtyType;
    }

    public void setRqtyType(String rqtyType) {
        this.rqtyType = rqtyType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPayment_method_title() {
        return payment_method_title;
    }

    public void setPayment_method_title(String payment_method_title) {
        this.payment_method_title = payment_method_title;
    }

    public String getCouponCost() {
        return couponCost;
    }

    public void setCouponCost(String couponCost) {
        this.couponCost = couponCost;
    }

    public String getVariationId() {
        return variationId;
    }

    public void setVariationId(String variationId) {
        this.variationId = variationId;
    }
}