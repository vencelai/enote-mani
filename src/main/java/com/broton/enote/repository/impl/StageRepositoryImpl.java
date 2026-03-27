package com.broton.enote.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.util.StringUtils;
import com.broton.enote.bo.CustomerIdNameBO;
import com.broton.enote.bo.EmployeeIdBO;
import com.broton.enote.bo.StageIdBO;
import com.broton.enote.dto.StageDto;
import com.broton.enote.dto.StageGroupDto;
import com.broton.enote.dto.StageListDto;

public class StageRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public List<StageGroupDto> getStageGroup(EmployeeIdBO idBo) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.customer_id as customerId, ");
		dataSql.append("  a.customer_name as customerName ");
		dataSql.append("From stage a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (StringUtils.hasText(idBo.getEmployeeId())) {
			whereSql.append(" and a.employee_id = :employeeId ");
		}	
		whereSql.append("Group by a.customer_id, a.customer_name ");
		whereSql.append("Order By a.create_date ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());
		
		// 設定引數
		if (StringUtils.hasText(idBo.getEmployeeId())) {
			dataQuery.setParameter("employeeId", idBo.getEmployeeId());
		}

		List<StageGroupDto> output = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(StageGroupDto.class)).getResultList();
		
		return output;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
	public List<StageListDto> getStageGroupList(CustomerIdNameBO idNameBo) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.id as stageId, ");
		dataSql.append("  b.form_name as formName, ");
		dataSql.append("  a.customer_name as customerName, ");
		dataSql.append("  a.employee_name as createEmployeeName, ");
		dataSql.append("  a.doctor_id as doctorId, ");
		dataSql.append("  '' as doctorName, ");
		dataSql.append("  a.create_date as createDate, ");
		dataSql.append("  a.stage_date as stageDate ");
		dataSql.append("From stage a ");
		dataSql.append("  Left Join form b on (b.id = a.form_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (StringUtils.hasText(idNameBo.getCustomerId())) {
			whereSql.append(" and a.customer_id = :customerId ");
		}	
		if (StringUtils.hasText(idNameBo.getCustomerName())) {
			whereSql.append(" and a.customer_name = :customerName ");
		}
		//whereSql.append("Group by a.customer_id, a.customer_name ");
		whereSql.append("Order By a.create_date ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());
		
		// 設定引數
		if (StringUtils.hasText(idNameBo.getCustomerId())) {
			dataQuery.setParameter("customerId", idNameBo.getCustomerId());
		}
		if (StringUtils.hasText(idNameBo.getCustomerName())) {
			dataQuery.setParameter("customerName", idNameBo.getCustomerName());
		}

		List<StageListDto> output = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(StageListDto.class)).getResultList();
		
		return output;
	}
		
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public StageDto getStageById(StageIdBO idBo) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.id as stageId, ");
		dataSql.append("  a.form_id as formId, ");
		dataSql.append("  a.sign_track as signTrack, ");
		dataSql.append("  a.customer_id as customerId, ");
		dataSql.append("  a.customer_name as customerName, ");
		dataSql.append("  '' as customerTel, ");
		dataSql.append("  '' as customerBirthday, ");
		dataSql.append("  a.doctor_id as doctorId, ");
		dataSql.append("  '' as doctorName, ");
		dataSql.append("  a.employee_id as createEmployeeId, ");
		dataSql.append("  a.employee_name as createEmployeeName ");
		dataSql.append("From stage a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (null != idBo.getId()) {
			whereSql.append(" and a.id = :id ");
		}	
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());
		
		// 設定引數
		if (null != idBo.getId()) {
			dataQuery.setParameter("id", idBo.getId());
		}

		StageDto output = (StageDto) dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(StageDto.class)).getSingleResult();
		
		return output;
	}
}
