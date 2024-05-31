package com.example.apiTest.Model;

public class UserReport {
    private long transactionId;
    private String state;
    private int statusCode;
    private String description;
    private int pdu;
	
   
    
   


	
    // Getters and Setters
    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPdu() {
        return pdu;
    }

    public void setPdu(int pdu) {
        this.pdu = pdu;
    }
    
   


	@Override
	public String toString() {
		return "UserReport [transactionId=" + transactionId + ", state=" + state + ", statusCode=" + statusCode
				+ ", description=" + description + ", pdu=" + pdu + ", corelationId=" + "]";
	}
}
