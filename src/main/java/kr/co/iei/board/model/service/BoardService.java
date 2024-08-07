package kr.co.iei.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.board.model.dao.BoardDao;
import kr.co.iei.board.model.dto.Board;
import kr.co.iei.board.model.dto.BoardComment;
import kr.co.iei.board.model.dto.BoardFile;
import kr.co.iei.board.model.dto.BoardListData;

@Service
public class BoardService {
	
	@Autowired
	private BoardDao boardDao;

	public BoardListData selectBoardList(int reqPage) {
		int numPerPage=6;
		
		int end = reqPage * numPerPage; 
		int start = end - numPerPage + 1;
		
		List list = boardDao.selectBoardList(start,end);
		int totalCount = boardDao.selectBoardToTalCount();
		
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage + 1;
		}
		//페이지네비 사이즈 지정
		int pageNaviSize = 2;
		
		int pageNo = ((reqPage-1)/pageNaviSize)*pageNaviSize+1;
		
		//페이지네비게이션 작성

		String pageNavi = "<ul>";
		
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi +="<a class ='page-item' href='/board/list?reqPage="+(pageNo-1)+"'>";	
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0; i<pageNaviSize;i++) {
			pageNavi += "<li>";
			if(pageNo == reqPage) {
				pageNavi +="<a class ='page-item active-page' href='/board/list?reqPage="+pageNo+"'>";				
			}else {
				pageNavi +="<a class ='page-item' href='/board/list?reqPage="+pageNo+"'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
			if(pageNo > totalPage) {
				break;
			}		
		}
		
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi +="<a class ='page-item' href='/board/list?reqPage="+pageNo+"'>";	
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		
		pageNavi += "</ul>";
		BoardListData bld = new BoardListData(list,pageNavi);
		return bld;
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

