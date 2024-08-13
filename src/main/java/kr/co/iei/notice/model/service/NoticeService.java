package kr.co.iei.notice.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryComment;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.notice.model.dao.NoticeDao;
import kr.co.iei.notice.model.dto.Notice;
import kr.co.iei.notice.model.dto.NoticeFile;
import kr.co.iei.notice.model.dto.NoticeListData;

@Service
public class NoticeService {
	@Autowired
	private NoticeDao noticeDao;

	public NoticeListData selectNoticeList(int reqPage) {
		int numPerPage = 10;
		
		int end = reqPage * numPerPage;
		int start = end - numPerPage +1;
		List list = noticeDao.selectNoticeList(start, end);
		
		int totalCount = noticeDao.selectNoticeTotalCount();
		
		int totalPage = 0;
		if(totalCount % numPerPage == 0) {
			totalPage = totalCount / numPerPage;
		}else {
			totalPage = totalCount / numPerPage + 1;
		}
		
		int pageNaviSize = 5;
		
		int pageNo = ((reqPage -1) / pageNaviSize) * pageNaviSize + 1;
		
		String pageNavi = "<ul class='pagination circle-style'>";
		
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + (pageNo - 1) + "'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i = 0; i<pageNaviSize; i++) {
			pageNavi +="<li>";
			if(pageNo == reqPage) {
				pageNavi += "<a class='page-item active-page' href='/notice/list?reqPage=" + pageNo + "'>";
			}else {
				pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + pageNo + "'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			if(pageNo > totalPage) {
				break;
			}
		}
		if(pageNo <+ totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + pageNo + "'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			
			
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
		
		
		NoticeListData nld = new NoticeListData(list, pageNavi);
		return nld;
	}
	@Transactional
	public int insertnotice(Notice n, List<NoticeFile> fileList) {
		int result = noticeDao.insertNotice(n);
		System.out.println(n.getMemberNo());
		if(result >0) {
			int noticeNo = noticeDao.selectNoticeNo();
			for(NoticeFile noticeFile : fileList) {
				noticeFile.setNoticeNo(noticeNo);
				result += noticeDao.insertNoticeFile(noticeFile);
			}
		}
		
		return result;
	}

	public Notice selectOneNotice(int noticeNo, String check, int memberNo) {
		Notice n = noticeDao.selectOneNotice(noticeNo);
		if(n != null) {
			//조회수 증가
			if(check == null) {
				int result = noticeDao.updateReadCount(noticeNo);
			}
			//해당게시글의 첨부파일을 조회
			List fileList = noticeDao.selectNoticeFile(noticeNo);
			System.out.println(fileList);
			n.setFileList(fileList);
			
		}
		return n;
	}
	@Transactional
	public List<NoticeFile> deleteNotice(int noticeNo) {
		List list = noticeDao.selectNoticeFile(noticeNo);
		int result = noticeDao.deleteNotice(noticeNo);
		if(result > 0) {
			return list;
		}
		return null;
	}
	public Notice getOneNotice(int noticeNo) {
		Notice n = noticeDao.selectOneNotice(noticeNo);
		List list = noticeDao.selectNoticeFile(noticeNo);
		n.setFileList(list);
		return n;
	}
	public List<NoticeFile> updateNotice(Notice n, List<NoticeFile> fileList, int[] delFileNo) {
		List<NoticeFile> delFileList = new ArrayList<NoticeFile>();
		//inquery업데이트, inquery_file insert(추가한 파일이 있을때만), inquery_file delete(삭제한 파일이 있을때만)
		int result = noticeDao.updateNotice(n);
		if(result>0) {
			//추가한 파일이 있는 경우 추가파일 insert
			for(NoticeFile noticeFile : fileList) {
				result += noticeDao.insertNoticeFile(noticeFile);
			}
			//삭제한 파일이 있는 경우 (파일을 DB에서 조회한 후(실제 폴더에서도 지우기위함) -> 삭제)
			if(delFileNo != null) {
				for(int fileNo : delFileNo) {
					NoticeFile noticeFile = noticeDao.selectOneNoticeFile(fileNo);
					delFileList.add(noticeFile);
					result += noticeDao.deleteNoticeFile(fileNo);
				}
			}
		}
		int updateTotal = delFileNo == null ? fileList.size() + 1 : fileList.size() + 1 + delFileNo.length;
		if(updateTotal == result) {
			return delFileList;
		}else {
			return null;			
		}
	}
}