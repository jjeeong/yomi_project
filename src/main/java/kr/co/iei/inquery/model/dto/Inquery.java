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
	private int inqueryOpen;
	private String inqueryRegDate;
}
