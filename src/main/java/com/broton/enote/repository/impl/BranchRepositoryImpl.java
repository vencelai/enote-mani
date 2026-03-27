package com.broton.enote.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import com.broton.enote.dto.BranchListResultDto;

public class BranchRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<BranchListResultDto> getBranchList() {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("a.id as id, ");
		dataSql.append("a.branch_name as branchName, ");
		dataSql.append("a.erp_id as erpId, ");
		dataSql.append("a.network as network ");
		dataSql.append("From branch a ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		whereSql.append("Order By a.id ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數

		List<BranchListResultDto> listBranchListResultDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(BranchListResultDto.class)).getResultList();
		
		return listBranchListResultDto;
	}
	
}
