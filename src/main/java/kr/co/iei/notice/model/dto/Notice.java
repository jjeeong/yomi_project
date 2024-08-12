package kr.co.iei.notice.model.dto;

import java.util.List;	

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Notice {
	private int noticeNo;
	private String noticeTitle;
	private String noticeContent;
	private int memberNo;
	private String noticeRegDate;
	private String memberName;
	private int noticeReadCount;
	private List<NoticeFile> fileList;
}
