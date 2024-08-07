package kr.co.iei.board.model.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class BoardCommentRowMapper implements RowMapper<BoardComment> {

	@Override
	public BoardComment mapRow(ResultSet rs, int rowNum) throws SQLException {
		BoardComment comment = new BoardComment();
		comment.setCommentBoardNo(rs.getInt("comment_board_no"));
		comment.setCommentContent(rs.getString("comment_content"));
		comment.setCommentNo(rs.getInt("comment_no"));
		comment.setCommentRefNo(rs.getInt("comment_ref_no"));
		comment.setCommentRegDate(rs.getString("comment_reg_Date"));
		comment.setCommentWriter(rs.getString("comment_writer"));
		
		return comment;
	}

}
