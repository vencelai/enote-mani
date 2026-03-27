package com.broton.enote.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import com.broton.enote.dto.FormTypeResultListDto;

public class FormTypeRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<FormTypeResultListDto> getFormTypeList() {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id as id,");
		dataSql.append("a.code as code, ");
		dataSql.append("a.name as name ");
		dataSql.append("From form_type a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		whereSql.append("Order By a.id ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數

		List<FormTypeResultListDto> listFormNameResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(FormTypeResultListDto.class)).getResultList();
		
		return listFormNameResultListDto;
	}
	
}
