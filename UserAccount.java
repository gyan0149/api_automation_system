package com.example.apiTest.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "progate_user_account_tbl")
public class UserAccount {
    @Id
    @Column(name = "user_code")
    private int userCode;


    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    @Column(name = "connection_type")
    private String connectionType;

   
	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	// Getters and Setters
    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }


    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
