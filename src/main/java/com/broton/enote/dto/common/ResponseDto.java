/**
 * 
 */
package com.broton.enote.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title 共通使用回傳
 * @desction 共通使用回傳
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {

	private String code;
	private String msg;
	private T data;
}
