package kr.co.iei.restr.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestrTag {
	private int restrTagNo;
	private String restrTagName;
	private int restrNo;
}
