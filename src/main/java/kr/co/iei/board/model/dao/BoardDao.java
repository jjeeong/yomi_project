package kr.co.iei.board.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.board.model.dto.BoardFileRowMapper;
import kr.co.iei.board.model.dto.BoardRowMapper;

@Repository
public class BoardDao {
	
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private BoardRowMapper boardRowMapper;
	@Autowired
	private BoardFileRowMapper boardFileRowMapper;
	
	public List selectBoardList() {
		String query="select * from board";
		List list = jdbc.query(query, boardRowMapper);
		return list;
	}
	

}
