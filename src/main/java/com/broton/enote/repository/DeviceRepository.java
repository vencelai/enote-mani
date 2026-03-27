package com.broton.enote.repository;

import java.math.BigInteger;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.broton.enote.bo.QueryDeviceStatusListBO;
import com.broton.enote.dto.DeviceResultListDto;
import com.broton.enote.entity.Device;

@Repository
@Transactional
public interface DeviceRepository extends JpaRepository<Device, BigInteger> {

	/** 取得設備資料列表
	 * @return
	 */
	Page<DeviceResultListDto> getDeviceStatusList(QueryDeviceStatusListBO qryBo, Pageable pageable);

	/** 由設備 ID 取得流水號
	 * @param deviceId
	 * @return
	 */
	@Query(value = "Select id From device Where device_id = :deviceId ", nativeQuery = true)
	BigInteger getDeviceNo(@Param("deviceId") String deviceId);
	
	/** 由設備 ID 取得所屬據點
	 * @param deviceId
	 * @return
	 */
	@Query(value = "Select branch_id From device Where device_id = :deviceId ", nativeQuery = true)
	BigInteger getDeviceBranch(@Param("deviceId") String deviceId);
}
