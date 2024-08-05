package kr.co.iei.inquery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Inquery {
	private int inqueryNo;
	private String inqueryWriter;
	private String inqueryTitle;
	private String inuqueryContent;
	private int inqueryReadCount;
	private int inqueryOpen; // 공개 : 1 / 비공개 : 0 (기본:0)
	private String inqueryRegDate;
}
