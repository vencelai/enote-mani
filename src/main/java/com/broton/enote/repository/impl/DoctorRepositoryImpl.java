package com.broton.enote.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import com.broton.enote.dto.DoctorResultListDto;

public class DoctorRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<DoctorResultListDto> getDoctorList() {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id as id,");
		dataSql.append("a.doctor_id_no as doctorIdNo, ");
		dataSql.append("a.doctor_name as doctorName, ");
		dataSql.append("a.medical_number as medicalNumber ");
		dataSql.append("From doctor a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		whereSql.append("Order By a.doctor_name ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數

		List<DoctorResultListDto> listDoctorResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(DoctorResultListDto.class)).getResultList();
		
		return listDoctorResultListDto;
	}
	
}
