package com.broton.enote.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.entity.Branch;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.BranchRepository;
import com.broton.enote.service.BranchService;
import com.broton.enote.bo.BranchBO;
import com.broton.enote.bo.UpdateBranchBO;
import com.broton.enote.dto.BranchListResultDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BranchServiceImpl implements BranchService {

	@Autowired
	private BranchRepository branchRepository;
	
	@Override
	public List<BranchListResultDto> getBranchList() {
		List<BranchListResultDto> output = new ArrayList<BranchListResultDto>();
		try {
			output = branchRepository.getBranchList();
			log.info("取得據點列表成功:{}筆", output.size());
		} catch (Exception e) {
			log.error("取得據點列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5961.getCode(), ResultCode.ERR_5961.getDesc());
		}
		return output;
	}
	
	@Override
	public void addBranch(BranchBO addBo) {
		try {
			int cnt = branchRepository.getBranchNameCount(addBo.getBranchName());
			if (0 == cnt) {
				Branch branch = new Branch();
				branch.setBranchName(addBo.getBranchName());
				if (StringUtils.hasText(addBo.getErpId())) {
					branch.setErp_id(addBo.getErpId());
				}				
				if (StringUtils.hasText(addBo.getNetwork())) {
					branch.setNetwork(addBo.getNetwork());
				}
				branchRepository.save(branch);
			} else {
				log.error("新增據點錯誤,據點名稱重複:{}", addBo.getBranchName());
				throw new ErrorCodeException(ResultCode.ERR_5962.getCode(), ResultCode.ERR_5962.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("新增據點錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5963.getCode(), ResultCode.ERR_5963.getDesc());
		}
	}
	
	@Override
	public void updateBranch(UpdateBranchBO updBo) {
		try {
			if (branchRepository.existsById(updBo.getBranchId())) {
				Branch branch = branchRepository.findById(updBo.getBranchId()).orElse(null);
				BigInteger nameId = branchRepository.getBranchIdByName(updBo.getBranchName());
				if (nameId == null || nameId == branch.getId()) {
					branch.setBranchName(updBo.getBranchName());
					if (StringUtils.hasText(updBo.getErpId())) {
						branch.setErp_id(updBo.getErpId());
					}				
					if (StringUtils.hasText(updBo.getNetwork())) {
						branch.setNetwork(updBo.getNetwork());
					}
					branchRepository.save(branch);
				} else {
					log.error("修改據點錯誤,據點名稱重複:{}", updBo.getBranchName());
					throw new ErrorCodeException(ResultCode.ERR_5964.getCode(), ResultCode.ERR_5964.getDesc());
				}
			} else {
				log.error("修改據點錯誤,據點資料不存在:{}", updBo.getBranchId());
				throw new ErrorCodeException(ResultCode.ERR_5965.getCode(), ResultCode.ERR_5965.getDesc());
			}
		} catch (IllegalArgumentException e) {
			log.error("修改據點錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5966.getCode(), ResultCode.ERR_5966.getDesc());
		}
	}

}
