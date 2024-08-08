package kr.co.iei.report.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReportRowMapper2 implements RowMapper<Report>{

	@Override
	public Report mapRow(ResultSet rs, int rowNum) throws SQLException {
		Report r = new Report();
		r.setReportBoardCommentNo(rs.getInt("board_comment_no"));
		r.setReportBoardNo(rs.getInt("report_board_no"));
		r.setReportBoardType(rs.getInt("report_board_type"));
		r.setReportCheck(rs.getInt("report_check"));
		r.setReportContent(rs.getString("report_content"));
		r.setReporterNo(rs.getInt("reporter_no"));
		r.setReportReviewNo(rs.getInt("reportReviewNo"));
		r.setReportType(rs.getString("reportType"));
		r.setRespondentNo(rs.getInt("respondent_no"));
		r.setReportNo(rs.getInt("report_no"));
		try {
			r.setRespondentId(rs.getString("respondent_id"));
			r.setReporterId(rs.getString("reporter_Id"));			
		} catch (SQLException e) {
			
		}
		return r;
	}

}
