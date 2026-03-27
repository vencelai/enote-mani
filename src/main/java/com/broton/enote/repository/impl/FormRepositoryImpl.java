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
import com.broton.enote.dto.FormListResultDto;

public class FormRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public Page<FormListResultDto> getFormList(String formTypeId, Pageable pageable) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id, ");
		dataSql.append("a.form_name as formName, ");
		dataSql.append("a.form_type_id as formTypeId, ");
		dataSql.append("'' as formTypeName, ");
		//dataSql.append("b.code as formTypeCode, ");
		//dataSql.append("c.id as typeId, ");
		//dataSql.append("c.type_name as typeName, ");
		dataSql.append("concat('v',a.version) as version, ");
		dataSql.append("a.create_date as createDate, ");
		dataSql.append("a.create_user as createUser, ");
		dataSql.append("a.edit_date as editDate, ");
		dataSql.append("a.edit_user as editUser, ");
		dataSql.append("a.show_flag as showFlag, ");
		dataSql.append("a.doctor_sign as doctorSign ");
		dataSql.append("From form a ");
		//dataSql.append("  Left Join form_type b on (b.id = a.form_type_id)");
		//dataSql.append("  Left Join form_type c on (c.id = a.form_type_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (null != formTypeId) {
			whereSql.append(" And a.form_type_id = :typeId ");
		}
		whereSql.append("Order By a.create_date Desc ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (null != formTypeId) {
			dataQuery.setParameter("typeId", formTypeId);
		}

		// 設定分頁
		dataQuery.setFirstResult((int) pageable.getOffset());
		dataQuery.setMaxResults(pageable.getPageSize());

		String countSql = "Select Count(*) From (" + dataSql + ") t";
		Query countQuery = (Query) entityManager.createNativeQuery(countSql);
		// 設定引數
		if (null != formTypeId) {
			countQuery.setParameter("typeId", formTypeId);
		}
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		Long total = count.longValue();

		List<FormListResultDto> listFormResultDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(FormListResultDto.class)).getResultList();
		
		return new PageImpl<>(listFormResultDto, pageable, total);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<FormListResultDto> getFormListAll(String formTypeId) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id, ");
		dataSql.append("a.form_name as formName, ");
		dataSql.append("a.form_type_id as formTypeId, ");
		dataSql.append("'' as formTypeName, ");
		//dataSql.append("b.code as formTypeCode, ");
		dataSql.append("concat('v',a.version) as version, ");
		dataSql.append("a.create_date as createDate, ");
		dataSql.append("a.create_user as createUser, ");
		dataSql.append("a.show_flag as showFlag, ");
		dataSql.append("a.doctor_sign as doctorSign ");
		dataSql.append("From form a ");
		//dataSql.append("  Left Join form_type b on (b.id = a.form_type_id)");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) And (a.show_flag = 1) ");
		if (null != formTypeId) {
			whereSql.append(" And a.form_type_id = :typeId ");
		}
		whereSql.append("Order By a.create_date Desc ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (null != formTypeId) {
			dataQuery.setParameter("typeId", formTypeId);
		}

		List<FormListResultDto> listFormListResultDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(FormListResultDto.class)).getResultList();
		
		return listFormListResultDto;
	}
	
}
