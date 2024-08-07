package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewImgRowMapper implements RowMapper<ReviewImg>{
	@Override
	public ReviewImg mapRow(ResultSet rs, int rowNum) throws SQLException {
		ReviewImg reviewImg = new ReviewImg();
		reviewImg.setReviewFilename(rs.getString("review_filename"));
		reviewImg.setReviewFilePath(rs.getString("review_filepath"));
		reviewImg.setReviewImgNo(rs.getInt("review_img_no"));
		reviewImg.setReviewNo(rs.getInt("review_no"));
		return reviewImg;
	}
}
