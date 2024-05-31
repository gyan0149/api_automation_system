package com.example.apiTest.Model;

import java.math.BigInteger;
import java.security.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "progate_mdr_tbl")
public class UserMdr {
	
	
	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMessagePdu() {
		return messagePdu;
	}

	public void setMessagePdu(Integer messagePdu) {
		this.messagePdu = messagePdu;
	}

	public Integer getUser_code() {
		return user_code;
	}

	public void setUser_code(Integer user_code) {
		this.user_code = user_code;
	}

	public String getMessage_state() {
		return message_state;
	}

	public void setMessage_state(String message_state) {
		this.message_state = message_state;
	}

	public String getExtraAttributes() {
		return extraAttributes;
	}

	public void setExtraAttributes(String extraAttributes) {
		this.extraAttributes = extraAttributes;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public BigInteger getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(BigInteger campaignId) {
		this.campaignId = campaignId;
	}

	public Boolean getIsUnicode() {
		return isUnicode;
	}

	public void setIsUnicode(Boolean isUnicode) {
		this.isUnicode = isUnicode;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getProcessedDate() {
		return processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Timestamp submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getDlrDetails() {
		return dlrDetails;
	}

	public void setDlrDetails(String dlrDetails) {
		this.dlrDetails = dlrDetails;
	}

	public String getLevel1DltPrice() {
		return level1DltPrice;
	}

	public void setLevel1DltPrice(String level1DltPrice) {
		this.level1DltPrice = level1DltPrice;
	}

	public String getLevel1Price() {
		return level1Price;
	}

	public void setLevel1Price(String level1Price) {
		this.level1Price = level1Price;
	}

	public String getUserPrice() {
		return userPrice;
	}

	public void setUserPrice(String userPrice) {
		this.userPrice = userPrice;
	}

	public BigInteger getUserCost() {
		return userCost;
	}

	public void setUserCost(BigInteger userCost) {
		this.userCost = userCost;
	}

	public BigInteger getUserRefund() {
		return userRefund;
	}

	public void setUserRefund(BigInteger userRefund) {
		this.userRefund = userRefund;
	}

	public String getFinalErrorCode() {
		return finalErrorCode;
	}

	public void setFinalErrorCode(String finalErrorCode) {
		this.finalErrorCode = finalErrorCode;
	}

	@Id
	@Column(name = "message_id")
    private Long messageId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "message_pdu")
    private Integer messagePdu; 
    
	@Column(name = "user_code")
    private Integer user_code;
	
	private String message_state;
	
	
	
	   
	    @Column(name = "extra_attributes", columnDefinition = "TEXT")
	    private String extraAttributes;

	    @Column(name = "batch_id")
	    private BigInteger batchId;

	    @Column(name = "campaign_id")
	    private BigInteger campaignId;

	    @Column(name = "is_unicode")
	    private Boolean isUnicode;

	   
	    @Column(name = "route_id")
	    private Integer routeId;

	    @Column(name = "source_id")
	    private String sourceId;

	    @Column(name = "sender_name")
	    private String senderName;

	    @Column(name = "channel")
	    private String channel;

	    @Column(name = "email")
	    private String email;

	    @Column(name = "operator")
	    private String operator;

	    @Column(name = "country")
	    private String country;

	    @Column(name = "processed_date")
	    private Date processedDate;

	    @Column(name = "code")
	    private String code;

	    @Column(name = "submission_date")
	    private Timestamp submissionDate;

	    @Column(name = "message_text", columnDefinition = "TEXT")
	    private String messageText;

	    @Column(name = "dlr_details", columnDefinition = "TEXT")
	    private String dlrDetails;

	    @Column(name = "level1_dlt_price")
	    private String level1DltPrice;

	    @Column(name = "level1_price")
	    private String level1Price;

	    @Column(name = "user_price")
	    private String userPrice;

	    @Column(name = "user_cost")
	    private BigInteger userCost;

	   
	    @Column(name = "user_refund")
	    private BigInteger userRefund;

	    @Column(name = "final_error_code")
	    private String finalErrorCode;

	  
	
	}

