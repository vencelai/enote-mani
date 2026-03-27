package com.broton.enote.service;

import java.util.List;
import com.broton.enote.bo.BranchBO;
import com.broton.enote.bo.UpdateBranchBO;
import com.broton.enote.dto.BranchListResultDto;

/**
 * 據點 Service
 * 
 */
public interface BranchService {
	
	/** 取得據點列表
	 * @return
	 */
	public List<BranchListResultDto> getBranchList();
	
	/** 新增據點
	 * @param addBo
	 */
	public void addBranch(BranchBO addBo);
	
	/** 修改據點
	 * @param updBo
	 */
	public void updateBranch(UpdateBranchBO updBo);

}
