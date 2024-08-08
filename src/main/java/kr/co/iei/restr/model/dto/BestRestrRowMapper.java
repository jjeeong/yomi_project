package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BestRestrRowMapper implements RowMapper<BestRestaurant>{

	@Override
	public BestRestaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
		BestRestaurant bs = new BestRestaurant();
		bs.setRestrName(rs.getString("restr_name"));
		bs.setRestrNo(rs.getInt("restr_no"));
		bs.setReviewStar(rs.getDouble("review_star"));
		bs.setRestrImg1(rs.getString("restr_img1"));
		return bs;
	}

}
