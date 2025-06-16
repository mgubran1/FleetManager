package com.semitruck;

import java.time.LocalDate;

public class Load {
    private String loadId;
    private String driverId;
    private String driverName;
    private LocalDate pickupDate;
    private LocalDate deliveryDate;
    private String pickupAddress;
    private String deliveryAddress;
    private String origin;
    private String destination;
    private double rate;
    private String notes;
    private String customerName;
    private String customerContact;
    private String status;
    private int miles;
    private String commodity;
    private double weight;
    private String trailerType;
    private String poNumber;
    private String brokerName;
    private double ratePerMile;
    private String invoiceNumber;
    private boolean paid;
    private String documentPath;

    // Full constructor
    public Load(String loadId, String driverId, String driverName, LocalDate pickupDate, LocalDate deliveryDate,
                String pickupAddress, String deliveryAddress, String origin, String destination, double rate,
                String notes, String customerName, String customerContact, String status, int miles, String commodity,
                double weight, String trailerType, String poNumber, String brokerName, double ratePerMile,
                String invoiceNumber, boolean paid, String documentPath) {
        this.loadId = loadId;
        this.driverId = driverId;
        this.driverName = driverName;
        this.pickupDate = pickupDate;
        this.deliveryDate = deliveryDate;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.origin = origin;
        this.destination = destination;
        this.rate = rate;
        this.notes = notes;
        this.customerName = customerName;
        this.customerContact = customerContact;
        this.status = status;
        this.miles = miles;
        this.commodity = commodity;
        this.weight = weight;
        this.trailerType = trailerType;
        this.poNumber = poNumber;
        this.brokerName = brokerName;
        this.ratePerMile = ratePerMile;
        this.invoiceNumber = invoiceNumber;
        this.paid = paid;
        this.documentPath = documentPath;
    }

    // Getters and setters for all fields
    public String getLoadId() { return loadId; }
    public void setLoadId(String loadId) { this.loadId = loadId; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerContact() { return customerContact; }
    public void setCustomerContact(String customerContact) { this.customerContact = customerContact; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getMiles() { return miles; }
    public void setMiles(int miles) { this.miles = miles; }

    public String getCommodity() { return commodity; }
    public void setCommodity(String commodity) { this.commodity = commodity; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getTrailerType() { return trailerType; }
    public void setTrailerType(String trailerType) { this.trailerType = trailerType; }

    public String getPoNumber() { return poNumber; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }

    public String getBrokerName() { return brokerName; }
    public void setBrokerName(String brokerName) { this.brokerName = brokerName; }

    public double getRatePerMile() { return ratePerMile; }
    public void setRatePerMile(double ratePerMile) { this.ratePerMile = ratePerMile; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
}