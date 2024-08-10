package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BestReviewRowMapper implements RowMapper<BestReview>{

	@Override
	public BestReview mapRow(ResultSet rs, int rowNum) throws SQLException {
		BestReview br = new BestReview();
		br.setMemberName(rs.getString("member_name"));
		br.setRestrImg1(rs.getString("restr_img1"));
		br.setRestrNo(rs.getInt("restr_no"));
		br.setReviewImg1(rs.getString("review_img1"));
		br.setReviewStar(rs.getDouble("review_star"));
		br.setRestrName(rs.getString("restr_name"));
		String content = rs.getString("review_content");
		if(content.length()>108) {
			br.setReviewContent(content.substring(0, 108)+"...");
		}else {
			br.setReviewContent(content);
		}
		return br;
	}

}
