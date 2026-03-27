package com.broton.enote.repository;

import java.math.BigInteger;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.broton.enote.bo.CustomerIdNameBO;
import com.broton.enote.bo.EmployeeIdBO;
import com.broton.enote.bo.StageIdBO;
import com.broton.enote.dto.StageDto;
import com.broton.enote.dto.StageGroupDto;
import com.broton.enote.dto.StageListDto;
import com.broton.enote.entity.Stage;

@Repository
@Transactional
public interface StageRepository extends JpaRepository<Stage, BigInteger> {

	public List<StageGroupDto> getStageGroup(EmployeeIdBO idBo);
	
	public List<StageListDto> getStageGroupList(CustomerIdNameBO idNameBo);
	
	public StageDto getStageById(StageIdBO idBo);
}
