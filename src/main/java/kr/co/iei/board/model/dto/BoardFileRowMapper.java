package kr.co.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

//마찬가지로 적어놓을게요~~
@Component
public class BoardFileRowMapper implements RowMapper<BoardFile>{

	@Override
	public BoardFile mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardFile boardFile = new BoardFile();
		boardFile.setBoardNo(rs.getInt("board_no"));
		boardFile.setFileName(rs.getString("file_name"));
		boardFile.setFileNo(rs.getInt("file_no"));
		boardFile.setFilePath(rs.getString("file_path"));
		
		return boardFile;
	}

}
