package kr.co.iei.board.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		int numPerPage = 6;

		int end = reqPage * numPerPage;
		int start = end - numPerPage + 1;

		List list = boardDao.selectBoardList(start, end);
		int totalCount = boardDao.selectBoardToTalCount();

		int totalPage = 0;
		if (totalCount % numPerPage == 0) {
			totalPage = totalCount / numPerPage;
		} else {
			totalPage = totalCount / numPerPage + 1;
		}
		// 페이지네비 사이즈 지정
		int pageNaviSize = 2;

		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;

		// 페이지네비게이션 작성

		String pageNavi = "<ul>";

		if (pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item' href='/board/list?reqPage=" + (pageNo - 1) + "'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}

		for (int i = 0; i < pageNaviSize; i++) {
			pageNavi += "<li>";
			if (pageNo == reqPage) {
				pageNavi += "<a class ='page-item active-page' href='/board/list?reqPage=" + pageNo + "'>";
			} else {
				pageNavi += "<a class ='page-item' href='/board/list?reqPage=" + pageNo + "'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			// 페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
			if (pageNo > totalPage) {
				break;
			}
		}

		if (pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class ='page-item' href='/board/list?reqPage=" + pageNo + "'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}

		pageNavi += "</ul>";
		BoardListData bld = new BoardListData(list, pageNavi);
		return bld;
	}

	public int insertBoard(Board b) {
		int result = boardDao.insertBoard(b);

		return result;
	}
	
	@Transactional
	public Board selectOneBoard(int boardNo, String check, int memberNo) {
		Board b = boardDao.selectOneBoard(boardNo);
		if (b != null) {
			if (check == null) {
				int result = boardDao.updateReadCount(boardNo);
			}
			List fileList = boardDao.selectBoardFile(boardNo);
			b.setFileList(fileList);
			
			List<BoardComment> commentList=boardDao.selectCommentList(boardNo,memberNo);
			
			b.setCommentList(commentList);
			List reCommentList = boardDao.selectRecommentList(boardNo,memberNo);
			b.setReCommentList(reCommentList);
		}
		return b;
	}

	public List<BoardFile> deleteBoard(int boardNo) {
		List list = boardDao.selectBoardFile(boardNo);
		
		int result = boardDao.deleteBoard(boardNo);
		if(result > 0) {
			return list;
		}
		return null;
	}

	public Board getOneBoard(int boardNo) {
		Board b = boardDao.selectOneBoard(boardNo);
		List list = boardDao.selectBoardFile(boardNo);
		b.setFileList(list);
		return b;
	}
	
	@Transactional
	public List<BoardFile> updateBoard(Board b, List<BoardFile> fileList, int[] delFileNo) {
		List<BoardFile> delFileList = new ArrayList<BoardFile>();
		
		int result = boardDao.updateBoard(b);
		if(result > 0) {
			for(BoardFile boardFile : fileList) {
				result += boardDao.insertBoardFile(boardFile);
			}
			
			if(delFileNo != null) {
				
				for(int fileNo : delFileNo) {
					BoardFile boardFile = boardDao.selectOneBoardFile(fileNo);
					delFileList.add(boardFile);
					result += boardDao.deleteBoardFile(fileNo);
				}
			}
		}
		int updateTotal = delFileNo == null?fileList.size()+1:fileList.size()+1+delFileNo.length;
		if(updateTotal == result) {
				return delFileList;
		}else {
			return null;			
		}
	}
	@Transactional
	public int insertComment(BoardComment bc) {
		int result = boardDao.insertComment(bc);
		return result;
	}
	@Transactional
	public int updateComment(BoardComment bc) {
		int result = boardDao.updateComment(bc);
		return result;
	}
	@Transactional
	public int deleteComment(BoardComment bc) {
		int result = boardDao.deleteComment(bc);
		return result;
	}
	@Transactional
	public int likePush(int commentNo, int isLike, int memberNo) {
		int result = 0;
		if(isLike == 0) {
			result = boardDao.insertBoardCommentLike(commentNo, memberNo);	
		}else if(isLike == 1 ){
			result = boardDao.deleteBoardCommentLike(commentNo,memberNo);		
		}if(result >  0) {
			int likeCount = boardDao.selectBoardCommentLikeCount(commentNo);		
			return likeCount;
		}else {
			return -1;		
		}
	}
}

