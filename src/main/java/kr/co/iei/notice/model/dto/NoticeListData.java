package kr.co.iei.notice.model.dto;

import java.util.List;

import kr.co.iei.inquery.model.dto.InqueryListData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeListData {
	private List list ; 
	private String pageNavi;
}
