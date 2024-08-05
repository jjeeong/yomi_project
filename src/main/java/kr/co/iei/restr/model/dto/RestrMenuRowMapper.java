package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RestrMenuRowMapper implements RowMapper<RestrMenu> {
	
	@Override
	public RestrMenu mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestrMenu rm = new RestrMenu();
		rm.setRestrMenuName(rs.getString("restr_menu_name"));
		rm.setRestrMenuNo(rs.getInt("restr_menu_no"));
		rm.setRestrMenuPrice(rs.getInt("restr_menu_price"));
		rm.setRestrNo(rs.getInt("restr_no"));
		return rm;
	}

}
