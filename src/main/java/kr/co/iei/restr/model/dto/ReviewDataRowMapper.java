package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewDataRowMapper implements RowMapper<Review>{
	@Override
	public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
		Review r = new Review();
		r.setRestrNo(rs.getInt("restr_no"));
		r.setReviewStar(rs.getDouble("review_star"));
		r.setReviewContent(rs.getString("review_content"));
		r.setReviewRegDate(rs.getString("review_reg_date"));
		r.setReviewNo(rs.getInt("review_no"));
		r.setMemberName(rs.getString("member_name"));
		r.setRestrAddr1(rs.getString("restr_addr1"));
		r.setRestrImg1(rs.getString("restr_img1"));
		r.setRestrName(rs.getString("restr_name"));
		return r;
	}
}
