package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewTagRowMapper implements RowMapper<ReviewTag>{
	
	@Override
	public ReviewTag mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReviewTag reviewTag = new ReviewTag();
		reviewTag.setReviewTagName(rs.getString("review_tag_name"));
		reviewTag.setReviewNo(rs.getInt("review_no"));
		reviewTag.setReviewTagNo(rs.getInt("review_tag_no"));
		reviewTag.setRestrNo(rs.getInt("restr_no"));
		return reviewTag;
	}
}
