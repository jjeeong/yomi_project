package kr.co.iei.notice.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
@Component
public class NoticeRowMapper implements RowMapper<Notice>{

	@Override
	public Notice mapRow(ResultSet rs, int rowNum) throws SQLException {
		Notice n = new Notice();
		n.setMemberNo(rs.getInt("member_no"));
		n.setNoticeContent(rs.getString("notice_content"));
		n.setNoticeNo(rs.getInt("notice_no"));
		n.setNoticeRegDate(rs.getString("notice_reg_date"));
		n.setNoticeTitle(rs.getString("notice_title"));
		n.setNoticeReadCount(rs.getInt("notice_read_count"));
		n.setMemberName(rs.getString("member_name"));
		return n;
	}

}
