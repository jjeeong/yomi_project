package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RecentRestrRowMapper implements RowMapper<Restaurant>{

	@Override
	public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
		Restaurant r = new Restaurant();
		r.setRestrNo(rs.getInt("restr_no"));
		r.setRestrImg1(rs.getString("restr_img1"));
		r.setRestrName(rs.getString("restr_name"));
		return r;
	}
	
}
