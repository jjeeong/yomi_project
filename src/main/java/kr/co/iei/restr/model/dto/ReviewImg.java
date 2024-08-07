package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewImg {
	private int reviewImgNo;
	private String reviewFilename;
	private String reviewFilePath;
	private int reviewNo;
}
