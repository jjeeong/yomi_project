package kr.co.iei.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.board.model.dao.BoardDao;
import kr.co.iei.board.model.dto.Board;
import kr.co.iei.board.model.dto.BoardComment;
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

	public Board selectOneBoard(int boardNo, String check, int memberNo) {
		Board b = boardDao.selectOneBoard(boardNo);
		if(b != null) {
			//조회수 증가하기
			if(check == null) {
				int result = boardDao.updateReadCount(boardNo);
			}
			List list = boardDao.selectBoardFile(boardNo);
			//댓글 조회 - 기본댓글만
			List<BoardComment> commentList = boardDao.selectCommentList(boardNo,memberNo);
		}
		return null;
	}
	

	}

