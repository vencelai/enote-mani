package com.broton.enote.repository;

import java.math.BigInteger;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.ManagerResultDto;
import com.broton.enote.entity.Manager;

@Repository
@Transactional
public interface ManagerRepository extends JpaRepository<Manager, BigInteger> {

	/** 取得平台維護人員資料列表
	 * @param name
	 * @param pageable
	 * @return
	 */
	Page<ManagerResultDto> getManagerList(String userId, String userName, Pageable pageable);
	
	// 檢查帳號是否已存在
	//@Query(value = "Select Count(*) From manager Where AES_DECRYPT(UNHEX(user_id), 'btencdec') = :userId ", nativeQuery = true)
	@Query(value = "Select Count(*) From manager Where user_id = :userId ", nativeQuery = true)
	int getManagerIdCount(@Param("userId") String userId);
	
	// 由帳號取得平台維護人員資料 id
	//@Query(value = "Select id From manager Where AES_DECRYPT(UNHEX(user_id), 'btencdec') = :userId ", nativeQuery = true)
	@Query(value = "Select id From manager Where user_id = :userId ", nativeQuery = true)
	BigInteger getManagerById(@Param("userId") String userId);
	
	// 平台維護人員登入檢核
	//@Query(value = "Select id From manager Where AES_DECRYPT(UNHEX(user_id), 'btencdec') = :userId And AES_DECRYPT(UNHEX(user_password), 'btencdec') = :userPassword And active = 1 ", nativeQuery = true)
	@Query(value = "Select id From manager Where user_id = :userId And AES_DECRYPT(UNHEX(user_password), 'btencdec') = :userPassword And active = 1 ", nativeQuery = true)
	BigInteger managerLoginCheck(@Param("userId") String userId, @Param("userPassword") String password);
}
