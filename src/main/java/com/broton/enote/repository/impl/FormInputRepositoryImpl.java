package com.broton.enote.repository.impl;

import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import com.broton.enote.dto.FormInputListResultDto;

public class FormInputRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<FormInputListResultDto> getFormInputList(BigInteger formId) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id as id,");
		dataSql.append("a.page_no as pageNo,");
		dataSql.append("a.input_type as inputType, ");
		dataSql.append("case ");
		dataSql.append("  when a.input_type = 1 then '簽名' ");
		dataSql.append("  when a.input_type = 2 then '印章' ");		
		dataSql.append("  when a.input_type = 3 then '輸入文字' ");
		dataSql.append("  when a.input_type = 4 then '單選框' ");
		dataSql.append("  when a.input_type = 5 then '單選框(有輸入文字)' ");
		dataSql.append("  when a.input_type = 6 then '核取框' ");
		dataSql.append("  when a.input_type = 7 then '核取框(有輸入文字)' ");
		dataSql.append("  when a.input_type = 8 then '文字輸入框' ");
		dataSql.append("end as inputTypeName, ");		
		dataSql.append("a.position as positionStr, ");
		dataSql.append("a.memo as memo, ");
		dataSql.append("a.required, ");
		dataSql.append("a.return_field as returnField, ");
		dataSql.append("a.relation_position as relationPositionStr ");
		dataSql.append("From form_input a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (null != formId) {
			whereSql.append(" And a.form_id = :formId ");
		}
		whereSql.append("Order By a.page_no,a.id ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (null != formId) {
			dataQuery.setParameter("formId", formId);
		}

		List<FormInputListResultDto> listFormInputResultDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(FormInputListResultDto.class)).getResultList();
		
		return listFormInputResultDto;
	}
	
}
