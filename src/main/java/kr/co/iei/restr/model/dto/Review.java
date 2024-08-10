package kr.co.iei.restr.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Review {
	private int reviewNo;
	private Double reviewStar;
	private String reviewContent;
	private String reviewRegDate;
	private int memberNo;
	private int restrNo;
	private String memberName;
	private List<ReviewTag> reviewTag;
	private List<ReviewImg> reviewImg;
	private int isReviewLike;
	private int reviewLikeCount;
	private String restrName;
	private String restrImg1;
	private String restrAddr1;
}
