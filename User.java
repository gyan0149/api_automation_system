package com.example.apiTest.Model;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table(name = "progate_user_tbl")
public class User {
    @Id
    @Column(name = "user_code")
    private int userCode;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "parent_user_code")
    private int parentUserCode;

    @Column(name = "trans_unit_price")
    private BigDecimal transUnitPrice;

    @Column(name = "promo_unit_price")
    private BigDecimal promoUnitPrice;

    @Column(name = "group_permission")
    private String groupPermission;

    @Column(name = "extra_attributes")
    private String extraAttributes;

    @Transient
    private String userSmsDltPrice;

    // Getters and Setters

    public String getUserSmsDltPrice() {
		return userSmsDltPrice;
	}

	public void setUserSmsDltPrice(String userSmsDltPrice) {
		this.userSmsDltPrice = userSmsDltPrice;
	}

	public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getParentUserCode() {
        return parentUserCode;
    }

    public void setParentUserCode(int parentUserCode) {
        this.parentUserCode = parentUserCode;
    }

    public BigDecimal getTransUnitPrice() {
        return transUnitPrice;
    }

    public void setTransUnitPrice(BigDecimal transUnitPrice) {
        this.transUnitPrice = transUnitPrice;
    }

    public BigDecimal getPromoUnitPrice() {
        return promoUnitPrice;
    }

    public void setPromoUnitPrice(BigDecimal promoUnitPrice) {
        this.promoUnitPrice = promoUnitPrice;
    }

    public String getGroupPermission() {
        return groupPermission;
    }

    public void setGroupPermission(String groupPermission) {
        this.groupPermission = groupPermission;
    }

    public String getExtraAttributes() {
        return extraAttributes;
    }

    public void setExtraAttributes(String extraAttributes) {
        this.extraAttributes = extraAttributes;
    }

   
}
