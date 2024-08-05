package kr.co.iei.inquery.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class InqueryFileRowMapper implements RowMapper<InqueryFile>{

	@Override
	public InqueryFile mapRow(ResultSet rs, int rowNum) throws SQLException {
		InqueryFile inqueryFile = new InqueryFile();
		inqueryFile.setFilename(rs.getString("filename"));
		inqueryFile.setFileNo(rs.getInt("file_no"));
		inqueryFile.setFilepath(rs.getString("filepath"));
		inqueryFile.setInqueryNo(rs.getInt("notice_no"));	
		return inqueryFile;
	}

	
}
