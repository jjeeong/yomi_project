package kr.co.iei.report.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.report.model.dto.Report;
import kr.co.iei.report.model.dto.ReportRowMapper;
import kr.co.iei.report.model.dto.ReportListRowMapper;

@Repository
public class ReportDao {
	@Autowired
	private ReportRowMapper reportRowMapper;
	@Autowired
	private ReportListRowMapper reportListRowMapper;
	@Autowired
	private JdbcTemplate jdbc;

	public int insertReviewReport(Report r) {
		String query = "insert into report values(report_seq.nextval, ?, ?,?,null, ?,null,?,0,?)";
		Object[] params = {r.getReportType(), r.getReportContent(), r.getReportBoardType(), r.getReportReviewNo(), r.getReporterNo(), r.getRespondentNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	
	public List selectUncheckReport() {
		String query = "select report_type, report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r";
		List list = jdbc.query(query, reportListRowMapper);
		return list;
	}
}
