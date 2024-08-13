package kr.co.iei.notice.model.dto;

import kr.co.iei.inquery.model.dto.InqueryFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeFile {
	private int fileNo;
	private int NoticeNo;
	private String fileName;
	private String filePath;
}
