package com.examflow.backend.enums;

public enum paymentStatus {

    DELETED(0, "DELETED"),
    OPEN_FOR_PAYMENT(1, "OPEN_FOR_PAYMENT"),
    PROCESSED(2, "PROCESSED"),
    REJECTED(3, "REJECTED");

    private final Integer statusSeq;
    private final String statusName;

    paymentStatus(Integer statusSeq, String statusName) {
        this.statusSeq = statusSeq;
        this.statusName = statusName;
    }

    public int getSequence() {
        return statusSeq;
    }

    public String getStatusName() {
        return statusName;
    }

    public static paymentStatus fromSequence(int sequence) {
        for (paymentStatus status : values()) {
            if (status.statusSeq == sequence) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status sequence: " + sequence);
    }

    public static paymentStatus fromStatusName(String statusName) {
        for (paymentStatus status : values()) {
            if (status.statusName == statusName) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status name: " + statusName);
    }

}
