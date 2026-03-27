package com.broton.enote.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class DataTables<T> implements Serializable {

	private static final long serialVersionUID = 9142779692281565977L;

	// 與datatales 加載的“dataSrc"對應
	private List<T> datas;
	// 過濾後的記錄數
	private int recordsFiltered;
	// 數據庫裏總共記錄數
	private int recordsTotal;
	// 請求次數計數器
	private int draw;

	private Boolean success;


}
