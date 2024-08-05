package kr.co.iei.restr.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Restaurant {
	private int restrNo;
	private String restrName;
	private String restrAddr1;
	private String restrAddr2;
	private String restrMapx;
	private String restrMapy;
	private String restrTel;
	private String restrContent;
	private String restrImg1;
	private String restrImg2;
	private List<RestrMenu> restrMenu;
	private Double star;
	private int reviewCount;
}
