package kr.co.iei.report.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RP {
	private List<Report>list;
	private String pagiNavi;
}
