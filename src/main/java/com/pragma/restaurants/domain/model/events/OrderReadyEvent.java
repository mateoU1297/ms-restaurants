package com.pragma.restaurants.domain.model.events;

import java.io.Serializable;

public class OrderReadyEvent implements Serializable {
    private Long orderId;
    private Long clientId;
    private String clientPhone;
    private String securityPin;

    public OrderReadyEvent() {
    }

    public OrderReadyEvent(Long orderId, Long clientId, String clientPhone, String securityPin) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.clientPhone = clientPhone;
        this.securityPin = securityPin;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

}