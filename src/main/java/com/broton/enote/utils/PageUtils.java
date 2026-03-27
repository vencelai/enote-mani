package com.broton.enote.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.broton.enote.bo.PageRequestBO;

public class PageUtils {

	private static final int CONSTANT_START = 0;
	private static final int CONSTANT_LENGTH = 10;

	private PageUtils() {
	}

	public static Pageable getPageCondition(PageRequestBO pageRequest) {
		if (isAllNull(pageRequest))
			return Pageable.unpaged();
		int offset = pageRequest.getStart() == null ? CONSTANT_START : pageRequest.getStart();
		int length = pageRequest.getLength() == null ? CONSTANT_LENGTH : pageRequest.getLength();
		int page = 0;
		try {
			page = offset / length;
		} catch (ArithmeticException e) {
			page = 0;
		}
		Sort sort = pageRequest.getSort() == null ? Sort.unsorted() : Sort.by(pageRequest.getSort());
		return PageRequest.of(page, length, sort);
	}

	private static boolean isAllNull(PageRequestBO pageRequest) {
		if (pageRequest.getStart() == null && pageRequest.getLength() == null && pageRequest.getSort() == null) {
			return true;
		}
		return false;
	}

	public static Pageable getPageByParameter(Integer start, Integer length, String sort) {
		PageRequestBO pageRequest = new PageRequestBO();
		pageRequest.setStart(start);
		pageRequest.setSort(sort);
		pageRequest.setLength(length);
		return getPageCondition(pageRequest);
	}
}
