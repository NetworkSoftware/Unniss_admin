package pro.network.unnissadmin.order;

public interface StatusListener {


    void onDeliveredClick(String id);
    void onWhatsAppClick(String phone);
    void onCallClick(String phone);
    void onCancelClick(String id);
    void onInvoice(Order id);
    void onProduct(Order id);
    void onWallet(Order order);
    void onTrackId(Order order);
    void onGOProduct(Order order);

}
