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
import org.springframework.util.StringUtils;
import com.broton.enote.dto.ManagerResultDto;

public class ManagerRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public Page<ManagerResultDto> getManagerList(String userId, String userName, Pageable pageable) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select id, ");
		//dataSql.append("  CAST(AES_DECRYPT(UNHEX(user_id),'btencdec')as char(1024)) as userId, ");
		//dataSql.append("  CAST(AES_DECRYPT(UNHEX(user_name),'btencdec')as char(1024)) as userName, ");
		dataSql.append("  user_id as userId, ");
		dataSql.append("  user_name as userName, ");
		dataSql.append("  active, ");
		dataSql.append("  case ");
		dataSql.append("    when active = 1 then '啟用' ");
		dataSql.append("    when active = 0 then '停用' ");
		dataSql.append("  end as activeName, ");
		dataSql.append("  create_date as createDate, ");
		dataSql.append("  create_user as createUser, ");
		dataSql.append("  edit_date as editDate, ");
		dataSql.append("  edit_user as editUser ");
		dataSql.append("From manager ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (StringUtils.hasText(userId)) {
			//whereSql.append(" And AES_DECRYPT(UNHEX(user_id), 'btencdec') = :userId ");
			whereSql.append(" And user_id = :userId ");
		}
		if (StringUtils.hasText(userName)) {
			//whereSql.append(" And AES_DECRYPT(UNHEX(user_name), 'btencdec') Like CONCAT('%', :userName, '%') ");
			whereSql.append(" And user_name Like CONCAT('%', :userName, '%') ");
		}
		whereSql.append("Order By create_date Desc ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (StringUtils.hasText(userId)) {
			dataQuery.setParameter("userId", userId);
		}
		if (StringUtils.hasText(userName)) {
			dataQuery.setParameter("userName", userName);
		}

		// 設定分頁
		dataQuery.setFirstResult((int) pageable.getOffset());
		dataQuery.setMaxResults(pageable.getPageSize());

		String countSql = "Select Count(*) From (" + dataSql + ") t";
		Query countQuery = (Query) entityManager.createNativeQuery(countSql);
		// 設定引數
		if (StringUtils.hasText(userId)) {
			countQuery.setParameter("userId", userId);
		}
		if (StringUtils.hasText(userName)) {
			countQuery.setParameter("userName", userName);
		}
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		Long total = count.longValue();

		List<ManagerResultDto> listManagerResultDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(ManagerResultDto.class)).getResultList();
		
		return new PageImpl<>(listManagerResultDto, pageable, total);
	}
	
}
