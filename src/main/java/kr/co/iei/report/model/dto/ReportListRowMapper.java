package kr.co.iei.report.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReportListRowMapper implements RowMapper<Report>{
	//리스트에 쓰일 것만 받아올 것임
	@Override
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
		Report r = new Report();
		r.setReportNo(rs.getInt("report_no"));
		r.setReportContent(rs.getString("report_content"));
		r.setReportBoardType(rs.getInt("report_board_type"));
		r.setReportBoardNo(rs.getInt("report_board_no"));
		r.setReportReviewNo(rs.getInt("report_review_no"));
		r.setReportBoardCommentNo(rs.getInt("report_board_comment_no"));
		r.setRespondentId(rs.getString("respondent_id"));
		r.setRespondentNo(rs.getInt("respondent_no"));
		r.setRespondentContent(rs.getString("respondent_content"));
		r.setReportType(rs.getString("report_type"));
		
		if(r.getReportBoardType()==2) {
			r.setRespondentContent("맛집 게시글 내용은 미리보기 할 수 없습니다.");
		}else {
			if(r.getRespondentContent().length()>24) {
				r.setRespondentContent(r.getRespondentContent().substring(0, 24)+"...");				
			}
		}
		if(r.getReportContent().length()>24) {
			r.setReportContent(r.getReportContent().substring(0, 24)+"...");
		}
		
		return r;
	}
	
}
