package kr.co.iei.inquery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InqueryFile {
	private int fileNo;
	private int inqueryNo;
	private String filename;
	private String filepath;
}
