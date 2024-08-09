package kr.co.iei.report.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReportRowMapper implements RowMapper<Report>{

	@Override
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
		Report r = new Report();
		r.setReportBoardCommentNo(rs.getInt("report_board_comment_no"));
		r.setReportBoardNo(rs.getInt("report_board_no"));
		r.setReportContent(rs.getString("report_content"));
		r.setReportReviewNo(rs.getInt("report_review_no"));
		r.setReporterId(rs.getString("reporter_id"));
		r.setRespondentContent(rs.getString("respondent_content"));
		r.setReportBoardType(rs.getInt("report_board_type"));
		return r;
	}

}
