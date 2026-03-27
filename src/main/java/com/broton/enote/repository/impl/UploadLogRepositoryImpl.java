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
import com.broton.enote.bo.QueryUploadLogListBO;
import com.broton.enote.dto.UploadLogResultListDto;

public class UploadLogRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public Page<UploadLogResultListDto> getUploadLogList(QueryUploadLogListBO qryBo, Pageable pageable) {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.id as id, ");
		dataSql.append("  a.customer_id as customerId, ");
		dataSql.append("  a.customer_name as customerName, ");
		dataSql.append("  a.customer_phone as customerPhone, ");
		dataSql.append("  a.file_name as fileName, ");
		dataSql.append("  a.file_type_id as fileTypeId, ");
		dataSql.append("  a.doctor_id as doctorId, ");
		dataSql.append("  a.doctor_name as doctorName, ");
		dataSql.append("  a.status as status, ");
		dataSql.append("  case ");
		dataSql.append("    when a.status = 0 then '失敗' ");
		dataSql.append("    when a.status = 1 then '成功' ");
		dataSql.append("  end as statusName, ");
		dataSql.append("  a.fail_count as failCount, ");
		dataSql.append("  a.create_date as createDate, ");
		dataSql.append("  a.create_user as createUser, ");
		dataSql.append("  a.branch_id as branchId, ");
		dataSql.append("  b.branch_name as branchName ");
		dataSql.append("From upload_log a ");
		dataSql.append("  Left Join branch b on (b.id = a.branch_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		if (null != qryBo.getDate()) {
			whereSql.append(" And a.create_date between :startDate and :endDate ");
		}
		if (null != qryBo.getCustomerIdOrPhone()) {
			whereSql.append(" And (a.customer_id like :customerId or a.customer_phone like :customerPhone or a.customer_name like :customerName) ");
		}
		if (null != qryBo.getDoctorId()) {
			whereSql.append(" And a.doctor_id = :doctorId ");
		}
		if (null != qryBo.getFormTypeId()) {
			whereSql.append(" And a.file_type_id = :fileTypeId ");
		}
		if (null != qryBo.getUploadStatus()) {
			whereSql.append(" And a.status = :status ");
		}
		if (null != qryBo.getBranchId()) {
			whereSql.append(" And a.branch_id = :branchId ");
		}
		whereSql.append("Order By a.create_date Desc ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		// 設定引數
		if (null != qryBo.getDate()) {
			dataQuery.setParameter("startDate", qryBo.getDate() + " 00:00:01");
			dataQuery.setParameter("endDate", qryBo.getDate() + " 23:59:59");
		}
		if (null != qryBo.getCustomerIdOrPhone()) {
			dataQuery.setParameter("customerId", "%" + qryBo.getCustomerIdOrPhone() + "%");
			dataQuery.setParameter("customerPhone", "%" + qryBo.getCustomerIdOrPhone() + "%");
			dataQuery.setParameter("customerName", "%" + qryBo.getCustomerIdOrPhone() + "%");
		}
		if (null != qryBo.getDoctorId()) {
			dataQuery.setParameter("doctorId", qryBo.getDoctorId());
		}
		if (null != qryBo.getFormTypeId()) {
			dataQuery.setParameter("fileTypeId", qryBo.getFormTypeId());
		}
		if (null != qryBo.getUploadStatus()) {
			dataQuery.setParameter("status", qryBo.getUploadStatus());
		}
		if (null != qryBo.getBranchId()) {
			dataQuery.setParameter("branchId", qryBo.getBranchId());
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
		if (null != qryBo.getCustomerIdOrPhone()) {
			countQuery.setParameter("customerId", "%" + qryBo.getCustomerIdOrPhone() + "%");
			countQuery.setParameter("customerPhone", "%" + qryBo.getCustomerIdOrPhone() + "%");
			countQuery.setParameter("customerName", "%" + qryBo.getCustomerIdOrPhone() + "%");
		}
		if (null != qryBo.getDoctorId()) {
			countQuery.setParameter("doctorId", qryBo.getDoctorId());
		}
		if (null != qryBo.getFormTypeId()) {
			countQuery.setParameter("fileTypeId", qryBo.getFormTypeId());
		}
		if (null != qryBo.getUploadStatus()) {
			countQuery.setParameter("status", qryBo.getUploadStatus());
		}
		if (null != qryBo.getBranchId()) {
			countQuery.setParameter("branchId", qryBo.getBranchId());
		}
		BigInteger count = (BigInteger) countQuery.getSingleResult();
		Long total = count.longValue();

		List<UploadLogResultListDto> listUploadLogResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(UploadLogResultListDto.class)).getResultList();
		
		return new PageImpl<>(listUploadLogResultListDto, pageable, total);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<UploadLogResultListDto> getUploadLogFailList() {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.id as id, ");
		dataSql.append("  a.customer_id as customerId, ");
		dataSql.append("  a.customer_name as customerName, ");
		dataSql.append("  a.customer_phone as customerPhone, ");
		dataSql.append("  a.file_name as fileName, ");
		dataSql.append("  a.file_type_id as fileTypeId, ");
		dataSql.append("  a.doctor_id as doctorId, ");
		dataSql.append("  a.doctor_name as doctorName, ");
		dataSql.append("  a.status as status, ");
		dataSql.append("  case ");
		dataSql.append("    when a.status = 0 then '失敗' ");
		dataSql.append("    when a.status = 1 then '成功' ");
		dataSql.append("  end as statusName, ");
		dataSql.append("  a.fail_count as failCount, ");
		dataSql.append("  a.create_date as createDate, ");
		dataSql.append("  a.create_user as createUser, ");
		dataSql.append("  a.branch_id as branchId, ");
		dataSql.append("  b.branch_name as branchName ");
		dataSql.append("From upload_log a ");
		dataSql.append("  Left Join branch b on (b.id = a.branch_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		whereSql.append(" And a.status = 0 And a.fail_count < 3 ");
		whereSql.append("Order By a.create_date ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		List<UploadLogResultListDto> listUploadLogResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(UploadLogResultListDto.class)).getResultList();
		
		return listUploadLogResultListDto;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public List<UploadLogResultListDto> getUploadLogFailThirdList() {
		StringBuilder dataSql = new StringBuilder();
		dataSql.append("Select ");
		dataSql.append("  a.id as id, ");
		dataSql.append("  a.customer_id as customerId, ");
		dataSql.append("  a.customer_name as customerName, ");
		dataSql.append("  a.customer_phone as customerPhone, ");
		dataSql.append("  a.file_name as fileName, ");
		dataSql.append("  a.file_type_id as fileTypeId, ");
		dataSql.append("  a.doctor_id as doctorId, ");
		dataSql.append("  a.doctor_name as doctorName, ");
		dataSql.append("  a.status as status, ");
		dataSql.append("  case ");
		dataSql.append("    when a.status = 0 then '失敗' ");
		dataSql.append("    when a.status = 1 then '成功' ");
		dataSql.append("  end as statusName, ");
		dataSql.append("  a.fail_count as failCount, ");
		dataSql.append("  a.create_date as createDate, ");
		dataSql.append("  a.create_user as createUser, ");
		dataSql.append("  a.branch_id as branchId, ");
		dataSql.append("  b.branch_name as branchName ");
		dataSql.append("From upload_log a ");
		dataSql.append("  Left Join branch b on (b.id = a.branch_id) ");

		StringBuilder whereSql = new StringBuilder("Where (1 = 1) ");
		whereSql.append(" And a.status = 0 And a.fail_count >= 3 ");
		whereSql.append("Order By a.create_date ");
		// 組裝sql語句
		dataSql.append(whereSql);

		// 建立本地sql查詢例項
		Query dataQuery = (Query) entityManager.createNativeQuery(dataSql.toString());

		List<UploadLogResultListDto> listUploadLogResultListDto = dataQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(UploadLogResultListDto.class)).getResultList();
		
		return listUploadLogResultListDto;
	}
	
}
