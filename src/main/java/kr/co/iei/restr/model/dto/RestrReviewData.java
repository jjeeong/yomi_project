package kr.co.iei.restr.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RestrReviewData {
	Restaurant restaurant;
	List reviewList;
}
