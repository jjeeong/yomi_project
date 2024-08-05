package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestrMenu {
	private int restrMenuNo;
	private int restrNo;
	private String restrMenuName;
	private int restrMenuPrice;
}