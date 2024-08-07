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
		String query = "insert into notice_file values(board_file_seq.nextval,?,?,?)";
		Object[] params = {boardFile.getBoardNo(),boardFile.getFileName(),boardFile.getFilePath()};
		int result = jdbc.update(query,params);
		return result;
	}

	public Board selectOneBoard(int boardNo) {
		String query = "select * from board where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query,boardRowMapper,params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Board)list.get(0);
		}
	}

	public int updateReadCount(int boardNo) {
		String query = "update board set read_count = read_count+1 where board_no=?";
		Object[] params = {boardNo};
		int result = jdbc.update(query,params);
		return result;
	}

	public List selectBoardFile(int boardNo) {
		String query = "select & from board_file where board_no=?";
		Object[] params = {boardNo};
		List list = jdbc.query(query,boardFileRowMapper,params);
		return list;
	}

	public List<BoardComment> selectCommentList(int boardNo, int memberNo) {
	    String query = "SELECT \r\n" + 
	                   "    bc.*, \r\n" + 
	                   "    (SELECT COUNT(*) FROM board_comment_like WHERE board_comment_no=bc.board_comment_no) AS like_count, \r\n" + 
	                   "    (SELECT COUNT(*) FROM board_comment_like WHERE board_comment_no=bc.board_comment_no AND member_no=?) AS is_like \r\n" + 
	                   "FROM board_comment bc \r\n" + 
	                   "WHERE board_ref=? AND board_comment_ref IS NULL ORDER BY 1";
	    
	    Object[] params = {memberNo, boardNo};
	    List<BoardComment> list = jdbc.query(query, boardCommentRowMapper , params);
	    return list;
	}

	

	
	

}
