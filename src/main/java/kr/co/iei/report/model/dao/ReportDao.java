package kr.co.iei.report.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.report.model.dto.Report;
import kr.co.iei.report.model.dto.ReportRowMapper;

@Repository
public class ReportDao {
	@Autowired
	private ReportRowMapper reportRowMapper;
	@Autowired
	private JdbcTemplate jdbc;
	public int insertReviewReport(Report r) {
		String query = "insert into report values(report_seq.nextval, ?, ?,?,null, ?,null,?,0,?)";
		Object[] params = {r.getReportType(), r.getReportContent(), r.getReportBoardType(), r.getReportReviewNo(), r.getReporterNo(), r.getRespondentNo()};
		int result = jdbc.update(query, params);
		return result;
	}
}
