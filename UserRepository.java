package com.example.apiTest.Repository;

import com.example.apiTest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u.userCode FROM User u WHERE u.userId = :userId")
    Integer findUserCodeByUserId(@Param("userId") String userId);

    @Query("SELECT u.userId FROM User u WHERE u.userCode = :userCode")
    String findUserIdByUserCode(@Param("userCode") Integer userCode);

    @Query("SELECT u.parentUserCode FROM User u WHERE u.userId = :userId")
    Integer findParentUserCodeByUserId(@Param("userId") String userId);

    @Query("SELECT u.transUnitPrice FROM User u WHERE u.userId = :userId")
    BigDecimal findTransUnitPriceByUserId(@Param("userId") String userId);

    @Query("SELECT u.promoUnitPrice FROM User u WHERE u.userId = :userId")
    BigDecimal findPromoUnitPriceByUserId(@Param("userId") String userId);

    @Query("SELECT u.groupPermission FROM User u WHERE u.userId = :userId")
    String findGroupPermissionByUserId(@Param("userId") String userId);

    @Query("SELECT u.extraAttributes FROM User u WHERE u.userId = :userId")
    String findExtraAttributesByUserId(@Param("userId") String userId);

    @Query(value = "SELECT JSON_EXTRACT(extra_attributes, '$.\"user.sms.dlt.price\"') " +
                   "FROM progate_user_tbl WHERE user_id = :userId", nativeQuery = true)
    String findUserSmsDltPriceByUserId(@Param("userId") String userId);
}
