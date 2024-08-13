package kr.co.iei.board.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.board.model.dto.Board;
import kr.co.iei.board.model.dto.BoardComment;
import kr.co.iei.board.model.dto.BoardCommentRowMapper;
import kr.co.iei.board.model.dto.BoardFile;
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
	@Autowired
	private BoardCommentRowMapper boardCommentRowMapper;
	
	public List selectBoardList(int start, int end) {
		String query = 
	"select * from(select rownum as rnum, n.* from (select * from board order by 1 desc)n)where rnum between ? and ?";
		Object[] params = {start,end};
		List list = jdbc.query(query, boardRowMapper,params);
		return list;
	}
	
	public int selectBoardToTalCount() {
		String query= "select count(*) from board";
		int totalCount = jdbc.queryForObject(query, Integer.class);
		return totalCount;
	}

	public int insertBoard(Board b) {
		String query = "insert into board values(board_seq.nextval,?,?,?,?,to_char(sysdate,'yyyy-mm-dd'),0,?,?)";
		Object[] params = {b.getBoardTitle(),b.getBoardAddr(),b.getBoardContent(),b.getThumbNailImg(),b.getBoardWriter(),b.getBoardStoreName()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectBoardNo() {
		String query = "select max(board_no) from board";
		int boardNo = jdbc.queryForObject(query,Integer.class);
		return boardNo;
	}

	public int insertBoardFile(BoardFile boardFile) {
		String query = "insert into board_file values(board_file_seq.nextval,?,?,?)";
		Object[] params = {boardFile.getBoardNo(),boardFile.getFileName(),boardFile.getFilePath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public Board selectOneBoard(int boardNo) {
		String query = "select * from board where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query, boardRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Board)list.get(0);
		}
	}

	public int updateReadCount(int boardNo) {
		String query = "update board set board_readcount = board_readcount+1 where board_no=?";
		Object[] params = {boardNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectBoardFile(int boardNo) {
		String query ="select * from board_file where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query,boardFileRowMapper,params);
		return list;
	}

	public List<BoardComment> selectCommentList(int boardNo, int memberNo) {
		String query= "select \r\n" + 
				"    bc.*,\r\n" + 
				"    (select count(*) from board_comment_like where comment_no = bc.comment_no) as like_count,\r\n" + 
				"    (select count(*) from board_comment_like where comment_no = bc.comment_no and member_no=?)as is_like\r\n" + 
				"from board_comment bc\r\n" + 
				"where comment_board_no=? and comment_ref_no is null order by 1";
		
			Object[] params = {memberNo, boardNo};
			List list = jdbc.query(query,boardCommentRowMapper,params);
			return list;
	}

	public List selectRecommentList(int boardNo, int memberNo) {
		String query = "select \r\n" + 
				"    bc.*,\r\n" + 
				"    (select count(*) from board_comment_like where comment_no = bc.comment_no) as like_count,\r\n" + 
				"    (select count(*) from board_comment_like where comment_no = bc.comment_no and member_no=?)as is_like\r\n" + 
				"from board_comment bc\r\n" + 
				"where comment_board_no=? and comment_ref_no is null order by 1";
		Object[] params = {memberNo, boardNo};
		List list = jdbc.query(query, boardCommentRowMapper,params);
		return list;
	}

	public int deleteBoard(int boardNo) {
		String query = "delete from board where board_no=?";
		Object[] params = {boardNo};
		int result = jdbc.update(query,params);
		return result;
	}


	public int updateBoard(Board b) {
		String query = "update board set thumnail_img=?, board_store_name=?,board_title=?,board_addr=?, board_content=? where board_no=?";
		Object[] params = {b.getThumbNailImg(),b.getBoardStoreName(),b.getBoardTitle(),b.getBoardAddr(),b.getBoardContent(),b.getBoardNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public BoardFile selectOneBoardFile(int fileNo) {
		String query = "select * from board_file where file_no=?";
		Object[] params = {fileNo};
		List list = jdbc.query(query, boardFileRowMapper,params);
		return (BoardFile)list.get(0);
	}

	public int deleteBoardFile(int fileNo) {
		String query = "delete from board_file where file_no=?";
		Object[] params= {fileNo};
		int result = jdbc.update(query,params);				
		return result;
	}

	public int insertComment(BoardComment bc) {
		String query = "insert into board_comment values(board_comment_seq.nextval,?,?,to_char(sysdate,'yyyy-mm-dd'),?,null)";
		String boardCommentRef = bc.getCommentRefNo()==0 ? null : String.valueOf(bc.getCommentRefNo());
		Object[] params = {bc.getCommentWriter(),bc.getCommentContent(),bc.getCommentBoardNo()};
		int result = jdbc.update(query,params);
		
		return result;
	}

	public int updateComment(BoardComment bc) {
		String query = "update board_comment set comment_content=? where comment_no=?";
		Object[] params = {bc.getCommentContent(),bc.getCommentNo()};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteComment(BoardComment bc) {
		String query ="delete from board_comment where comment_no=?";
		Object[] params = {bc.getCommentNo()};
		int result =jdbc.update(query,params);
		return result;
	}

	public int insertBoardCommentLike(int commentNo, int memberNo) {
		String query ="insert into board_comment_like values(?,?)"; 
		Object[] params = {commentNo,memberNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int deleteBoardCommentLike(int commentNo, int memberNo) {
		String query = "delete from board_comment_like where comment_no=? and member_no=?";
		Object[] params = {commentNo,memberNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public int selectBoardCommentLikeCount(int commentNo) {
		String query = "select count(*) from board_comment_like where comment_no=?";
		Object[] params = {commentNo};
		int likeCount = jdbc.queryForObject(query,Integer.class, params);
		return likeCount;
	}
	
	

	public List selectBoardSearch(String search) {
		String query = "select * from board where board_title like ? or board_content like ?";
		Object[] params = {"%"+search+"%","%"+search+"%"};
		List list = jdbc.query(query, boardRowMapper, params);
		return list;
	}

}
