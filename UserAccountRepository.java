package com.example.apiTest.Repository;

import com.example.apiTest.Model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	@Query("SELECT ua.amount FROM UserAccount ua WHERE ua.userCode = :userCode AND ua.connectionType IN ('sms_wallet', 'wallet')")
    BigInteger findAmountByUserCode(@Param("userCode") int userCode);
	
	
}
