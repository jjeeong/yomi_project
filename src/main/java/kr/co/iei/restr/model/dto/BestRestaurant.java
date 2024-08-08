package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BestRestaurant {//메인에 띄울 맛집 리스트(가지고 올 정보가 적으므로 따로 클래스를 팠음)
	private String restrName;	//상호명 띄워야하니까
	private int restrNo;	//상세보기로 이동하기 위해서 필요
	private double reviewStar; //상호명 위에 띄워볼 별점
	private String restrImg1; //위에 띄울 이미지
}
