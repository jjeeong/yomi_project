package kr.co.iei.notice.model.dao;

import java.util.List;	

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.notice.model.dto.Notice;
import kr.co.iei.notice.model.dto.NoticeFile;
import kr.co.iei.notice.model.dto.NoticeFileRowMapper;
import kr.co.iei.notice.model.dto.NoticeRowMapper;

@Repository
public class NoticeDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private NoticeRowMapper noticeRowMapper;
	@Autowired
	private NoticeFileRowMapper noticeFileRowMapper;
	public List selectNoticeList(int start, int end) {
		String query = "select * from (select rownum as rnum, n.* from(select *\r\n" + 
				"from notice\r\n" + 
				"left join member_tbl using (member_no)order by 2 desc)n) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, noticeRowMapper, params);
		return list;
	}
	public int selectNoticeTotalCount() {
		String query = "select count(*) from notice";
		int totalCount = jdbc.queryForObject(query, Integer.class); //
		
		return totalCount;
	}
	public int insertNotice(Notice n) {
		String query = "insert into notice values(notice_seq.nextval,?,?,?,to_char(sysdate,'yyyy-mm-dd'),0)";
		Object[] params = {n.getNoticeTitle(), n.getNoticeContent(), n.getMemberNo()};
		int result = jdbc.update(query,params);
		return result;
	}
	public int selectNoticeNo() {
		String query = "select max(notice_no) from notice";
		int noticeNo = jdbc.queryForObject(query, Integer.class);
		return noticeNo;
	}
	public int insertNoticeFile(NoticeFile noticeFile) {
		String query = "insert into notice_file values(notice_file_seq.nextval,?,?,?)";
		Object[] params = {noticeFile.getNoticeNo(),noticeFile.getFileName(),noticeFile.getFilePath()};
		int result = jdbc.update(query, params);
		return result;
	}
	public Notice selectOneNotice(int noticeNo) {
		String query ="select * from notice left join member_tbl using (member_no) where notice_no= ?";
		Object[] params = {noticeNo};
		List list = jdbc.query(query, noticeRowMapper , params);
		if(list.isEmpty()) {
			return null;
		}else {
			return (Notice)list.get(0);
		}
	}
	public int updateReadCount(int noticeNo) {
		String query = "update notice set notice_read_count = notice_read_count + 1 where notice_no = ?";
		Object[] params = {noticeNo};
		int result = jdbc.update(query,params);
		System.out.println("result:"+result);
		return result;
	}
	public List selectNoticeFile(int noticeNo) {
		String query = "select * from notice_file where notice_no=?";
		Object[] params = {noticeNo};
		List list = jdbc.query(query, noticeFileRowMapper, params);
		return list;
	}
	public int deleteNotice(int noticeNo) {
		String query = "delete from notice where notice_no = ?";
		Object[] params = {noticeNo};
		int result = jdbc.update(query,params);
		return result;
	}
	public int updateNotice(Notice n) {
		String query = "update notice set notice_title=?, notice_content=?,notice_read_count = notice_read_count -1 where notice_no=?";
		Object[] params = {n.getNoticeTitle(), n.getNoticeContent(), n.getNoticeNo()};
		int result = jdbc.update(query,params);
		return result;
	}
	public NoticeFile selectOneNoticeFile(int fileNo) {
		String query = "select * from notice_file where file_no = ?";
		Object[] params = {fileNo};
		List list = jdbc.query(query, noticeFileRowMapper, params);
		if(list.isEmpty()) {
			return null;
		}
		return (NoticeFile)list.get(0);
	}
	public int deleteNoticeFile(int fileNo) {
		String query = "delete from notice_file where file_no=?";
		Object[] params = {fileNo};
		int result = jdbc.update(query, params);
		return result;
	}
	
}


