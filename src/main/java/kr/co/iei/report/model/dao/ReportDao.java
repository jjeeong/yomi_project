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
				"from report r where report_check=0";
		List list = jdbc.query(query, reportListRowMapper);
		return list;
	}

	public Report selectOneReport(int reportNo) {
		String query = "select report_board_type, report_content, report_board_no, report_review_no, report_board_comment_no,(select member_id from member_tbl where member_no = r.reporter_no)as reporter_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_no= ?";
		Object[] params = {reportNo};
		List<Report> list = jdbc.query(query, reportRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		System.out.println(list.get(0));
		return list.get(0);
	}

	public int updateReportCheck(int reportNo) {
		String query = "update report set report_check = 1 where report_no = ?";
		Object[] params = {reportNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteReport(int reportNo) {
		String query = "delete from report where report_no = ?";
		Object[] params = {reportNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertBoardReport(Report r) {
		String query = "insert into report values(report_seq.nextval, ?, ?,?,?, null,null,?,0,?)";
		Object[] params = {r.getReportType(), r.getReportContent(), r.getReportBoardType(), r.getReportBoardNo(), r.getReporterNo(), r.getRespondentNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertBoardCommentReport(Report r) {
		String query = "insert into report values(report_seq.nextval, ?, ?,?,null, null,?,?,0,?)";
		Object[] params = {r.getReportType(), r.getReportContent(), r.getReportBoardType(), r.getReportBoardCommentNo(), r.getReporterNo(), r.getRespondentNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public List searchByReportType(String reportTypeString) {
		String query = "select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_type=?";
		Object[] params = {reportTypeString};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public List searchByReportTypeETC() {
		String query = "select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_type not in ('스팸 홍보/도배', '음란물','불법 정보 포함', '불쾌한 내용 포함', '잘못된 정보 포함')";
		List list = jdbc.query(query, reportListRowMapper);
		return list;
	}

	public List searchByBoardType(int reportBoardType) {
		String query = "select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_board_type = ?";
		Object[] params = {reportBoardType};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public List searchById(String respondentId) {
		String query = "select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and r.respondent_no=(select member_no from member_tbl where member_id = ?)";
		Object[]params = {respondentId};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}
}
