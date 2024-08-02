package kr.co.iei.member.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberRowMapper implements RowMapper<Member> {

	@Override
	public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
		Member m = new Member();
		m.setMemberAddr(rs.getString("member_addr"));
		m.setMemberEmail(rs.getString("member_email"));
		m.setMemberGender(rs.getString("member_gender"));
		m.setMemberGrade(rs.getInt("member_grade"));
		m.setMemberId(rs.getString("member_id"));
		m.setMemberName(rs.getString("member_name"));
		m.setMemberNo(rs.getInt("member_no"));
		m.setMemberPhone(rs.getString("member_phone"));
		m.setMemberPhoto(rs.getString("member_photo"));
		m.setMemberPw(rs.getString("member_pw"));
		m.setMemberRegDate(rs.getString("member_reg_date"));
		
		return m;
	}

}
