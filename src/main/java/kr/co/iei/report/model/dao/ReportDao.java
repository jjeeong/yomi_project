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
	
	public List selectUncheckReport(int start, int end) {
		String query = "select * from (select rownum rn, a.* from (select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0)a)ar where rn between ? and ? order by 1";
		Object[] params = {start, end};
		List list = jdbc.query(query, reportListRowMapper, params);
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

	public List searchByReportType(String reportTypeString, int start, int end) {
		String query = "select * from (select rownum rnum, rp_tbl.* from (select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_type=?) rp_tbl) where rnum between ? and ?";
		Object[] params = {reportTypeString, start, end};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public List searchByReportTypeETC(int start, int end) {
		String query = "select * from (select rownum as rnum, rp.* from (select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_type not in ('스팸 홍보/도배', '음란물','불법 정보 포함', '불쾌한 내용 포함', '잘못된 정보 포함'))rp) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public List searchByBoardType(int reportBoardType, int start, int end) {
		String query = "select * from (select rownum rnum, rp.* from (select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and report_board_type = ?)rp) where rnum between ? and ?";
		Object[] params = {reportBoardType, start, end};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public List searchById(String respondentId, int start, int end) {
		String query = "select * from (select rownum rnum, rp.* from (select report_type,report_no, report_content, report_board_type, report_board_no, report_review_no, report_board_comment_no,respondent_no,(select member_id from member_tbl where member_no = r.respondent_no)as respondent_id, \r\n" + 
				"decode(report_board_type, 1, (select review_content from review where review_no = r.report_review_no),\r\n" + 
				"                                2,(select board_content from board where board_no = r.report_board_no), \r\n" + 
				"                                3,(select comment_content from board_comment where comment_no = r.report_board_comment_no))as respondent_content \r\n" + 
				"from report r where report_check=0 and r.respondent_no=(select member_no from member_tbl where member_id = ?))rp) where rnum between ? and ?";
		Object[]params = {respondentId, start, end};
		List list = jdbc.query(query, reportListRowMapper, params);
		return list;
	}

	public int countReport() {
		String query = "select count(*) from report where report_check=0";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int countReportByReportType(String reportTypeString) {
		String query = "select count(*) from report where report_type = ?";
		Object[] params = {reportTypeString};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int countReportByReportTypeETC() {
		String query = "select count(*) from report where report_type not in ('스팸 홍보/도배', '음란물','불법 정보 포함', '불쾌한 내용 포함', '잘못된 정보 포함')";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int countReportByBoardType(int reportBoardType) {
		String query = "select count(*) from report where report_board_type = ?";
		Object [] params = {reportBoardType};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int countReportById(String respondentId) {
		String query = "select count(*) from report where respondent_no = (select member_no from member_tbl where member_id = ?)";
		Object[] params = {respondentId};
		int totalCount = jdbc.queryForObject(query, Integer.class, params);
		return totalCount;
	}

	public int checkDuplication(Report r) {
		String query="";
		Object[] params = new Object[2];
		params[0] = r.getReporterNo();
		switch(r.getReportBoardType()) {
		case 1:
			query = "select count(*) from report where reporter_no=? and report_review_no=?";
			params[1]=r.getReportReviewNo();
			break;
		case 2:
			query = "select count(*) from report where reporter_no=? and report_board_no=?";
			params[1]=r.getReportBoardNo();
			break;
		case 3:
			query = "select count(*) from report where reporter_no=? and report_board_comment_no=?";
			params[1]=r.getReportBoardCommentNo();
			break;
		}//switch
		int count = jdbc.queryForObject(query, Integer.class, params);
		System.out.println(count);
		if(count==0) {
			return 0;
		}
		return -2;
	}
}
