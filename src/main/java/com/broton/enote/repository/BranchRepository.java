package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.BranchListResultDto;
import com.broton.enote.entity.Branch;

@Repository
@Transactional
public interface BranchRepository extends JpaRepository<Branch, BigInteger> {

	/** 取得據點列表
	 * @return
	 */
	List<BranchListResultDto> getBranchList();
	
	//檢查據點名稱是否已存在
	@Query(value = "Select Count(*) From branch Where branch_name = :branch_name ", nativeQuery = true)
	int getBranchNameCount(@Param("branch_name") String branchName);
	
	@Query(value = "Select id From branch Where branch_name = :branch_name ", nativeQuery = true)
	BigInteger getBranchIdByName(@Param("branch_name") String branchName);
	
	// 取得據點id對映的 erp branch id
	@Query(value = "Select erp_id From branch Where id = :id ", nativeQuery = true)
	String getBranchErpId(@Param("id") BigInteger id);
	
	// 取得ip取得所屬的據點id
	@Query(value = "Select id From branch Where network like :network ", nativeQuery = true)
	BigInteger getBranchIdByNetwork(@Param("network") String network);
		
}
