package kr.co.iei.inquery.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class InqueryCommentRowMapper implements RowMapper<InqueryComment> {

	@Override
	public InqueryComment mapRow(ResultSet rs, int rowNum) throws SQLException {
		InqueryComment comment = new InqueryComment();
		comment.setInqueryCommentContent(rs.getString("inquery_comment_content"));
		comment.setInqueryCommentDate(rs.getString("inquery_comment_date"));
		comment.setInqueryCommentNo(rs.getInt("inquery_comment_no"));
		comment.setInqueryCommentRef(rs.getInt("inquery_comment_ref"));
		comment.setInqueryCommentWriter(rs.getString("inquery_comment_writer"));
		comment.setInqueryRef(rs.getInt("inquery_ref"));
		return comment;
	}
	
}
