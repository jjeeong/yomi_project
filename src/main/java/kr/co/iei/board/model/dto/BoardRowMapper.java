package kr.co.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

//Dao에서 @AutoWired 쓰기 위해서는 Component필요해서 적어놉니다!!
@Component
public class BoardRowMapper implements RowMapper<Board>{

	@Override
	public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
		Board b = new Board();
		b.setBoardAddr(rs.getString("board_addr"));
		b.setBoardContent(rs.getString("board_content"));
		b.setBoardNo(rs.getInt("board_no"));
		b.setBoardRegdate(rs.getString("board_regdate"));
		b.setBoardTitle(rs.getString("board_title"));
		b.setReadCount(rs.getInt("board_readCount"));
		b.setThumbNailImg(rs.getString("thumnail_img"));
		b.setBoardStoreName(rs.getNString("board_store_name"));
		return b;
	}

}
