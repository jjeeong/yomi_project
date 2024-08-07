package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewTag {
	private int reviewTagNo;
	private String reviewTagName;
	private int reviewNo;
	private int restrNo;
}
