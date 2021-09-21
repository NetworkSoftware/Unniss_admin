package pro.network.unnissadmin.product;

import java.io.Serializable;

/**
 * Created by ravi on 16/11/17.
 */

public class Product implements Serializable {
    String id;
    String brand;
    String model;
    String price;
    String ram;
    String rom;
    String name;
    String image;
    String description;
    String category;
    String rqty;
    String qty;
    String stock_update;
    String userId;
    String rqtyType;
    String categoryId;
    String fabric;
    String ideal;
    String occasion;
    String fit;
    String color;
    String size;
    String closure;
    String pocket;
    String pattern;
    String rise;
    String origin;
    String bestselling;
    String pricedrop;
    String variation;
    String delete_size;
    String variationId;

    public Product() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product(String brand, String model, String price,String rqty,String rqtyType, String ram, String rom, String name, String image, String description, String fabric,String ideal,
            String occasion,
            String fit,
            String color,
            String sizes,
            String closure,
            String pocket,
            String pattern,
            String rise,
            String origin,
                   String bestselling,
                   String pricedrop,String category,  String variation,String delete_size,String variationId) {
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.rqty = rqty;
        this.rqtyType = rqtyType;
        this.ram = ram;
        this.rom = rom;
        this.name = name;
        this.image = image;
        this.fabric = fabric;
        this.ideal = ideal;
        this.occasion = occasion;
        this.fit = fit;
        this.color = color;
        this.size = sizes;
        this.closure = closure;
        this.pocket = pocket;
        this.pattern = pattern;
        this.rise = rise;
        this.origin = origin;
        this.origin = bestselling;
        this.origin = pricedrop;
        this.category = category;
        this.variation = variation;
        this.delete_size = delete_size;
        this.variationId = variationId;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStock_update() {
        return stock_update;
    }

    public void setStock_update(String stock_update) {
        this.stock_update = stock_update;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getFabric() {
        return fabric;
    }

    public void setFabric(String fabric) {
        this.fabric = fabric;
    }

    public String getIdeal() {
        return ideal;
    }

    public void setIdeal(String ideal) {
        this.ideal = ideal;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getFit() {
        return fit;
    }

    public void setFit(String fit) {
        this.fit = fit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getClosure() {
        return closure;
    }

    public void setClosure(String closure) {
        this.closure = closure;
    }

    public String getPocket() {
        return pocket;
    }

    public void setPocket(String pocket) {
        this.pocket = pocket;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getBestselling() {
        return bestselling;
    }

    public void setBestselling(String bestselling) {
        this.bestselling = bestselling;
    }

    public String getPricedrop() {
        return pricedrop;
    }

    public void setPricedrop(String pricedrop) {
        this.pricedrop = pricedrop;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
    }

    public String getDelete_size() {
        return delete_size;
    }

    public void setDelete_size(String delete_size) {
        this.delete_size = delete_size;
    }

    public String getVariationId() {
        return variationId;
    }

    public void setVariationId(String variationId) {
        this.variationId = variationId;
    }
}