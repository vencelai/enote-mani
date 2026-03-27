package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.broton.enote.dto.DoctorResultListDto;
import com.broton.enote.entity.Doctor;

@Repository
@Transactional
public interface DoctorRepository extends JpaRepository<Doctor, BigInteger> {

	/** 取得醫師列表
	 * @return
	 */
	List<DoctorResultListDto> getDoctorList();
		
}
