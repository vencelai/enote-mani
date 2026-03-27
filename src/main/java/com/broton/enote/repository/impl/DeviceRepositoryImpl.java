package com.broton.enote.repository.impl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.broton.enote.bo.QueryDeviceStatusListBO;
import com.broton.enote.dto.DeviceResultListDto;

public class DeviceRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public Page<DeviceResultListDto> getDeviceStatusList(QueryDeviceStatusListBO qryBo, Pageable pageable) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id as id,");
		dataSql.append("a.branch_id as branchId, ");
		dataSql.append("b.branch_name as branchName, ");
		dataSql.append("a.device_id as deviceId, ");
		dataSql.append("a.last_update_time as lastUpdateTime, ");
		dataSql.append("concat('v', a.version) as version, ");
		dataSql.append("concat(a.battery, '%') as battery, ");
		dataSql.append("a.ip as ip, ");
		dataSql.append("a.last_login_user as lastLoginUser, ");
		dataSql.append("a.last_login_user_name as lastLoginUserName ");
		dataSql.append("From device a ");
		dataSql.append("  Left Join branch b on (b.id = a.branch_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (null != qryBo.getDate()) {
			whereSql.append(" And a.last_update_time between :startDate and :endDate ");
		}
		if (null != qryBo.getEmployeeId()) {
			whereSql.append(" And a.last_login_user = :lastLoginUser ");
		}
		if (null != qryBo.getBranchId()) {
			whereSql.append(" And a.branch_id = :branchId ");
		}
		if (null != qryBo.getDeviceId()) {
			whereSql.append(" And a.device_id = :deviceId ");
		}
		
		if (null != qryBo.getOrderBy() && null != qryBo.getSort()) {
			String sortBy = qryBo.getSort();
			switch(qryBo.getOrderBy()) {
				case "deviceId":
					whereSql.append("Order By a.device_id " + sortBy);
					break;
				case "lastUpdateTime":
					whereSql.append("Order By a.last_update_time " + sortBy);
					break;
				case "version":
					whereSql.append("Order By a.version " + sortBy);
					break;
				default :
					whereSql.append("Order By a.last_update_time Desc ");
			}
		} else {
			whereSql.append("Order By a.last_update_time Desc ");
		}

		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (null != qryBo.getDate()) {
			dataQuery.setParameter("startDate", qryBo.getDate() + " 00:00:01");
			dataQuery.setParameter("endDate", qryBo.getDate() + " 23:59:59");
		}
		if (null != qryBo.getEmployeeId()) {
			dataQuery.setParameter("lastLoginUser", qryBo.getEmployeeId());
		}
		if (null != qryBo.getBranchId()) {
			dataQuery.setParameter("branchId", qryBo.getBranchId());
		}
		if (null != qryBo.getDeviceId()) {
			dataQuery.setParameter("deviceId", qryBo.getDeviceId());
		}

		// 設定分頁
		dataQuery.setFirstResult((int) pageable.getOffset());
		dataQuery.setMaxResults(pageable.getPageSize());

		String countSql = "Select Count(*) From (" + dataSql + ") t";
		Query countQuery = (Query) entityManager.createNativeQuery(countSql);
		// 設定引數
		if (null != qryBo.getDate()) {
			countQuery.setParameter("startDate", qryBo.getDate() + " 00:00:01");
			countQuery.setParameter("endDate", qryBo.getDate() + " 23:59:59");
		}
		if (null != qryBo.getEmployeeId()) {
			countQuery.setParameter("lastLoginUser", qryBo.getEmployeeId());
		}
		if (null != qryBo.getBranchId()) {
			countQuery.setParameter("branchId", qryBo.getBranchId());
		}
		if (null != qryBo.getDeviceId()) {
			countQuery.setParameter("deviceId", qryBo.getDeviceId());
		}
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		Long total = count.longValue();

		List<DeviceResultListDto> listDeviceResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(DeviceResultListDto.class)).getResultList();
		
		return new PageImpl<>(listDeviceResultListDto, pageable, total);
	}
	
}
