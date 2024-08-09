package kr.co.iei.admin.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

import kr.co.iei.admin.dto.AdminRowMapper;
import kr.co.iei.admin.service.AdminService;
import kr.co.iei.board.model.dto.BoardRowMapper;
import kr.co.iei.member.model.dto.Member;
import kr.co.iei.restr.model.dto.ReviewRowMapper;



@Repository
public class AdminDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private AdminRowMapper adminRowMapper;
	@Autowired
	private BoardRowMapper boardRowMapper;
	@Autowired
	private ReviewRowMapper reviewRowMapper;
	
	public List selectAdminList(int start,int end) {
		String query = "select * from (select  rownum as rnum, n.* from (select * from member_tbl order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start,end};
		List list = jdbc.query(query, adminRowMapper , params);
		return list;
	}
	public int selectAdminTotalCount() {
		String query = "select count(*) from member_tbl";
		//조회결과가 단일값(1행 1열)인경우에만 사용할 수 있는 메소드
		//쿼리문 수행결과를 두번째 매개변수타입으로 변환해서 바로 리턴
		int totalCount = jdbc.queryForObject(query, Integer.class);		
		return totalCount;
	}
	public int changeGrade(Member m) {
		String query = "update member_tbl set member_grade=? where member_no=?";
		Object[] params = {m.getMemberGrade(), m.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;	
	}
	
	public  List blackMember() {
		String query = "select * from member_tbl where member_grade=3";
		List list = jdbc.query(query, adminRowMapper);
		return list;
	}
	public int deleteMember(int memberNo) {
		String query = "delete from member_tbl where member_no=?";
		Object[] params = {memberNo};
		int result = jdbc.update(query,params);
		return result;
	}
	
	public int updateMember(Member m) {
		String query = "update member_tbl set member_pw=?,member_phone=?,member_addr=? where member_no=?";
		Object[] params = {m.getMemberPw(), m.getMemberPhone(),m.getMemberAddr(),m.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;		
	}
	
	public List postingMember(String memberId ) {
		String query = "select * from board where board_writer=?";
		Object[] params = {memberId};
		List list = jdbc.query(query,boardRowMapper,params);
		return list;				
	}
		
	public List reviewsMember(int memberNo) {
		String query = "select * from reviews where member_no=?";
		Object[] params = {memberNo};
		List list = jdbc.query(query,reviewRowMapper,params);
		return list;
	}
	
	
		
}