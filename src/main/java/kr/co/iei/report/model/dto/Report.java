package kr.co.iei.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Report {
	private int reportNo;
	private String reportType;
	private String reportContent;
	private int reportBoardType;
	private int reportBoardNo;
	private int reportReviewNo;
	private int reportBoardCommentNo;
	private int reporterNo;			//신고자 멤버 번호
	private String reporterId;		//신고자 멤버 아이디
	private int respondentNo;		//신고당한 멤버 번호
	private String respondentId;	//신고당한 멤버 아이디
	private int reportCheck;
	private String respondentContent; //신고게시글 내용
}
