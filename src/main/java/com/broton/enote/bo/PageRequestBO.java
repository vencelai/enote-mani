package com.broton.enote.bo;

import lombok.Data;

@Data
public class PageRequestBO {

	private String search;
	private Integer start;
	private Integer length;
	private String sort;
}
