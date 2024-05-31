package com.example.apiTest.Repository;

import com.example.apiTest.Model.UserMdr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMdrRepository extends JpaRepository<UserMdr, Long> {

    @Query("SELECT um.description FROM UserMdr um WHERE um.messageId = :transactionId")
    String findDescriptionByTransactionId(@Param("transactionId") Long transactionId);
    
    
    @Query("SELECT um.user_code FROM UserMdr um WHERE um.messageId = :transactionId")
    Integer findUserCodeByTransactionId(@Param("transactionId") Long transactionId);
    
    @Query("SELECT um.message_state FROM UserMdr um WHERE um.messageId = :transactionId")
    String findmessageStateByTransactionId(@Param("transactionId") Long transactionId);   
    
    @Query("SELECT um FROM UserMdr um WHERE um.messageId = :transactionId")
    List<UserMdr> findByMessageId(@Param("transactionId") Long transactionId);
}
