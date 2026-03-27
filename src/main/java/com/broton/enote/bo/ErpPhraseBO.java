package com.broton.enote.bo;

import lombok.Data;

@Data
public class ErpPhraseBO {
	
	private String phraseType; // PdfFileKind = PDF同意書分類代碼與名稱
	
	private String token;
}
