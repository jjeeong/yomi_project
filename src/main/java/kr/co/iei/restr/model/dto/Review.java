package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Review {
	private int reviewNo;
	private int reviewStar;
	private String reviewContent;
	private String reviewRegDate;
	private int memberNo;
	private int restrNo;
}
