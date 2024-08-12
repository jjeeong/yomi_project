package kr.co.iei.inquery.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryComment;
import kr.co.iei.inquery.model.dto.InqueryCommentRowMapper;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.inquery.model.dto.InqueryFileRowMapper;
import kr.co.iei.inquery.model.dto.InqueryRowMapper;
import kr.co.iei.member.model.dto.Member;

@Repository
public class InqueryDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private InqueryRowMapper inqueryRowMapper;
	@Autowired
	private InqueryFileRowMapper inqueryFileRowMapper;
	@Autowired
	private InqueryCommentRowMapper inqueryCommentRowMapper;
	
	
	
	
	
	
	
	public List selectInqueryList(int start, int end) {
		String query = "select * from (select rownum as rnum, n.* from(select * from inquery where inquery_open = 1 order by 1 desc)n) where rnum between ? and ?";
//		String query = "select * from (select rownum as rnum, n.* from(select * from inquery order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, inqueryRowMapper, params);
		return list;
	}
	public int selectInqueryTotalCount() {
		String query = "select count(*) from Inquery where inquery_open=1";
//		String query = "select count(*) from Inquery";
		int totalCount = jdbc.queryForObject(query, Integer.class); //query문 실행해서 Integer.class로 바로 꺼내기
		return totalCount;
	}
	
	
//	open=1 || (open=0 && memberId)
	
	public List selectInqueryList(int start, int end, Member member) {
//		String query = "select * from (select rownum as rnum, n.* from(select * from inquery where inquery_open = 1 order by 1 desc)n) where rnum between ? and ?";
		
		String query = "select * from (select rownum as rnum, n.* from(select * from inquery where inquery_open=1 or (inquery_open=0 and inquery_writer = ?) order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {member.getMemberId(), start, end};
		List list = jdbc.query(query, inqueryRowMapper, params);
		return list;
	}
	
	public int selectInqueryTotalCount(Member member) {
		String query = "select count(*) from Inquery where inquery_open=1 or (inquery_open=0 and inquery_writer = ?)";
//		String query = "select count(*) from Inquery";
		Object[] params = {member.getMemberId()};
		int totalCount = jdbc.queryForObject(query, Integer.class, params); //query문 실행해서 Integer.class로 바로 꺼내기
		return totalCount;
	}
	
	
	
	
	
	
	
	
	
	
	public int insertInquery(Inquery inq, int open) {
		String query = "insert into inquery values(inquery_seq.nextval,?,?,?,?,0,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {inq.getInqueryWriter(), inq.getInqueryTitle(), inq.getInqueryContent(), open};		
		int result = jdbc.update(query,params);
		return result;
	}
	public int selectInqueryNo() {
		String query = "select max(inquery_no) from inquery";
		int inqueryNo = jdbc.queryForObject(query, Integer.class);
		return inqueryNo;
	}
	public int insertInqueryFile(InqueryFile inqueryFile) {
		String query = "insert into inquery_file values(inquery_file_seq.nextval,?,?,?)";
		Object[] params = {inqueryFile.getInqueryNo(), inqueryFile.getFilename(),inqueryFile.getFilepath()};
		int result = jdbc.update(query, params);
		return result;
	}
	public Inquery selectOneInquery(int inqueryNo) {
		String query = "select * from inquery where inquery_no = ?";
		Object[] params = {inqueryNo};
		List list = jdbc.query(query, inqueryRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}		
		return (Inquery)list.get(0); 
	}
	public int updateReadCount(int inqueryNo) {
		String query = "update inquery set inquery_read_count = inquery_read_count+1 where inquery_no = ?";
		Object[] params = {inqueryNo};
		int result = jdbc.update(query,params);
		return result;
	}
	public List selectInqueryFile(int inqueryNo) {
		String query = "select * from inquery_file where inquery_no = ?";
		Object[] params = {inqueryNo};
		List list = jdbc.query(query, inqueryFileRowMapper, params);
		return list;
	}
	
	
	
	
	public List<InqueryComment> selectCommentList(int inqueryNo,int memberNo) {
		String query="select * from inquery_comment where inquery_ref=? and inquery_comment_ref is null order by 1";
		Object[] params = {inqueryNo};
		List list = jdbc.query(query, inqueryCommentRowMapper, params);
		System.out.println("list:"+list);
		System.out.println(inqueryNo);
		System.out.println(memberNo);
		return list;
	}
	
	public List selectReCommentList(int inqueryNo, int memberNo) {
		String query="select * from inquery_comment where inquery_ref=? and inquery_comment_ref is not null order by 1";
		Object[] params = {inqueryNo};
		List list = jdbc.query(query, inqueryCommentRowMapper, params);
		return list;
	}
	
	public int deleteInquery(int inqueryNo) {
		String query = "delete from inquery where inquery_no=?";
		Object[] params = {inqueryNo};
		int result = jdbc.update(query, params);
		return result;
	}
	public int updateInquery(Inquery inq, String open) {
		String query = "update inquery set inquery_title=?, inquery_content=?, inquery_open=?,inquery_read_count = inquery_read_count -1 where inquery_no=?";
		Object[] params = {inq.getInqueryTitle(), inq.getInqueryContent(), open, inq.getInqueryNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	public InqueryFile selectOneInqueryFile(int fileNo) {
		String query = "select * from inquery_file where file_no=?";
		Object[] params = {fileNo};
		List list = jdbc.query(query, inqueryFileRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (InqueryFile)list.get(0);
	}
	public int deleteInqueryFile(int fileNo) {
		String query = "delete from inquery_file where file_no=?";
		Object[] params = {fileNo};
		int result = jdbc.update(query, params);
		return result;
	}
	public int insertComment(InqueryComment ic) {
		String query = "insert into inquery_comment values(inquery_comment_seq.nextval,?,?,to_char(sysdate,'yyyy-mm-dd'),?,?)";
		String inqueryCommentRef = ic.getInqueryCommentRef() == 0 ? null : String.valueOf(ic.getInqueryCommentRef());
		Object[] params = {ic.getInqueryCommentWriter(),ic.getInqueryCommentContent(),ic.getInqueryRef(),inqueryCommentRef};
//		Object[] params = {ic.getInqueryCommentWriter(),ic.getInqueryCommentContent(),ic.getInqueryRef(),ic.getInqueryCommentRef()};
		int result = jdbc.update(query,params);
		return result;
	}
	
	public int updateComment(InqueryComment ic) {
		String query = "update inquery_comment set inquery_comment_content=? where inquery_comment_no=?";
		Object[] params = {ic.getInqueryCommentContent(), ic.getInqueryCommentNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	public int deleteComment(InqueryComment ic) {
		String query = "delete from inquery_comment where inquery_comment_no=?";
		Object[] params = {ic.getInqueryCommentNo()};
		int result = jdbc.update(query,params);
		return result;
	}
	
}







































