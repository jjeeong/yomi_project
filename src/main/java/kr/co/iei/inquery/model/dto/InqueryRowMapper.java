package kr.co.iei.inquery.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class InqueryRowMapper implements RowMapper<Inquery> {

	@Override
	public Inquery mapRow(ResultSet rs, int rowNum) throws SQLException {

		Inquery inq = new Inquery();
		inq.setInqueryNo(rs.getInt("inquery_no"));
		inq.setInqueryTitle(rs.getString("inquery_title"));
		inq.setInqueryWriter(rs.getString("inquery_writer"));
		inq.setInqueryOpen(rs.getInt("inquery_open"));
		inq.setInqueryReadCount(rs.getInt("inquery_read_count"));
		inq.setInuqueryContent(rs.getString("inquery_content"));
		inq.setInqueryRegDate(rs.getString("Inquery_reg_date"));				
		return inq;
	}

	
}
