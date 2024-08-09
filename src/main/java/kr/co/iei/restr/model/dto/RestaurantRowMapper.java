package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantRowMapper implements RowMapper<Restaurant>{

	@Override
	public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
		Restaurant r = new Restaurant();
		r.setRestrAddr1(rs.getString("restr_addr1"));
		r.setRestrAddr2(rs.getString("restr_addr2"));
		r.setRestrContent(rs.getString("restr_content"));
		r.setRestrImg1(rs.getString("restr_img1"));
		r.setRestrImg2(rs.getString("restr_img2"));
		r.setRestrMapx(rs.getString("restr_mapx"));
		r.setRestrMapy(rs.getString("restr_mapy"));
		r.setRestrName(rs.getString("restr_name"));
		r.setRestrNo(rs.getInt("restr_no"));
		r.setRestrTel(rs.getString("restr_tel"));
		return r;
	}
}
