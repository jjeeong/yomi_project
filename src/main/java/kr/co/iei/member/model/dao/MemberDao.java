package kr.co.iei.member.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.member.model.dto.MemberRowMapper;

@Repository
public class MemberDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private MemberRowMapper memberRowMapper;
	public Member selelctOneMember(Member m) {
		String query = "select * from member_tbl where member_id=? and member_pw=?";
		Object[] params = {m.getMemberId(), m.getMemberPw()};
		List list = jdbc.query(query,memberRowMapper,params);
		if(list.isEmpty()) {
			return null;			
		}else {
			return (Member)list.get(0);	
		}
	}


	public int insertMember(Member m) {
		String query = "insert into member_tbl values(member_seq.nextval,?,?,?,?,?,?,null,to_char(sysdate,'yyyy-mm-dd'),?,?,default)";
		Object[] params = {m.getMemberId(),m.getMemberPw(),m.getMemberName(),m.getMemberAddr(),m.getMemberPhone(),m.getMemberEmail(),m.getMemberBirthDate(),m.getMemberGender()};
		int result = jdbc.update(query,params);
		return result;
	}


	public Member selectOneMember(String checkId) {
		String query = "select * from member_tbl where member_id=?";
		Object[] params = {checkId};
		List list = jdbc.query(query, memberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}
	
	public Member selectOneMemberId(String memberId) {
		String query = "select * from member_tbl where member_id = ?";
		Object[] params = {memberId};
		List list = jdbc.query(query, memberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}

	public Member selectOneMemberEmail(String memberEmail) {
		String query = "select * from member_tbl where member_email = ?";
		Object[] params = {memberEmail};
		List list = jdbc.query(query, memberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
		
	}
	public List<Member> selectAllMember() {
		String query = "select * from member_tbl order by 1";
		List list = jdbc.query(query, memberRowMapper);
		return list;
		
	}


	public Member findId(Member m) {
		String query ="select * from member_tbl where member_name = ? and member_email = ?";
		Object[] params = {m.getMemberName(),m.getMemberEmail()};
		List list = jdbc.query(query, memberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}


	public Member findName(Member m) {
		String query ="select * from member_tbl where member_name = ? and member_email = ? and member_id = ?" ;
		Object[] params = {m.getMemberName(),m.getMemberEmail(),m.getMemberId()};
		List list = jdbc.query(query, memberRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Member)list.get(0);
		}
	}


	public int updatePw(Member m) {
		String query = "update member_tbl set member_pw = ? where member_name=? and member_id=? and member_email =? ";
		Object[] params = {m.getMemberPw(),m.getMemberName(),m.getMemberId(),m.getMemberEmail()};
		int result = jdbc.update(query,params);
		
		return result;
	}

		
		
	}






	
