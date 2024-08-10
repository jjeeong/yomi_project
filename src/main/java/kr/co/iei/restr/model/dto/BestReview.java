package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestReview {
	//private int likeCount; 조건 찾을때만 쓰이고 실제로 보일 필요는 없으므로 빼겠음
	//마찬가지로 reviewNo가 쓰이지 않으므로 빼겠음
	private double reviewStar;
	private int restrNo;
	private String memberName;
	private String restrImg1;
	private String reviewImg1;
	private boolean reviewImg1Exist;
	private String restrName;
	private String reviewContent;
}
