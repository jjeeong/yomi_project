package kr.co.iei.restr.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class RestrTagRowMapper implements RowMapper<RestrTag>{

	@Override
	public RestrTag mapRow(ResultSet rs, int rowNum) throws SQLException {
		RestrTag rt = new RestrTag();
		rt.setRestrNo(rs.getInt("restr_no"));
		rt.setRestrTagName(rs.getString("restr_tag_name"));
		rt.setRestrTagNo(rs.getInt("restr_tag_no"));
		return rt;
	}

}
