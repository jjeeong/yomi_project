package kr.co.iei.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.board.model.dao.BoardDao;

@Service
public class BoardService {
	
	@Autowired
	private BoardDao boardDao;

	public List selectBoardList() {
		List list =  boardDao.selectBoardList();
		return list;
	}


	}

