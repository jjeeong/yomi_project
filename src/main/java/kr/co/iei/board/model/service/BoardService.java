package kr.co.iei.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.board.model.dao.BoardDao;
import kr.co.iei.board.model.dto.Board;
import kr.co.iei.board.model.dto.BoardFile;

@Service
public class BoardService {
	
	@Autowired
	private BoardDao boardDao;

	public List selectBoardList() {
		List list =  boardDao.selectBoardList();
		return list;
	}

	public int insertBoard(Board b) {
		int result = boardDao.insertBoard(b);
		
		return result;
	}
	

	}

