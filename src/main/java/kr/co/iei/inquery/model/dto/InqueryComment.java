package kr.co.iei.inquery.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InqueryComment {
	private int inqueryCommentNo;
	private String inqueryCommentWriter;
	private String inqueryCommentContent;
	private String inqueryCommentDate;
	private int inqueryRef;
	private int inqueryCommentRef;
	
	
}
