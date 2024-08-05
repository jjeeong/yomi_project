package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewRowMapper implements RowMapper<Review> {
	
	@Override
	public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
		Review r = new Review();
		r.setMemberNo(rs.getInt("member_no"));
		r.setRestrNo(rs.getInt("restr_no"));
		r.setReviewContent(rs.getString("review_content"));
		r.setReviewNo(rs.getInt("review_no"));
		r.setReviewRegDate(rs.getString("review_reg_date"));
		r.setReviewStar(rs.getDouble("review_star"));
		return r;
	}
}
