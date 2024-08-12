package kr.co.iei.inquery.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.inquery.model.dao.InqueryDao;
import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryComment;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.inquery.model.dto.InqueryListData;


@Service
public class InqueryService {
	@Autowired
	private InqueryDao inqueryDao;
		
	public InqueryListData selectInqueryList(int reqPage) { //reqPage : 사용자가 요청한 페이지 번호
		//numPerPage : 한 페이지당 보여줄 게시물의 수
		int numPerPage = 10;
		//사용자가 요청한 페이지에 따른 게시물 시작번호(start)/끝번호(end) 계산
		// reqPage == 1 -> start = 1 / end = 10
		// reqPage == 2 -> start = 11 / end = 20
		// reqPage == 3 -> start = 21 / end = 30 ...
		int end = reqPage * numPerPage;
		int start = end - numPerPage + 1;
		// start : 1(10-10+1), 11(20-10+1), 21(30-10+1), ... / end : : 10(1*10), 20(2*10), 30(3*10), ...
		List list = inqueryDao.selectInqueryList(start, end);
		//페이지 네비게이션
		int totalCount = inqueryDao.selectInqueryTotalCount();
		//totalPage : 전체 페이지 수
		int totalPage = 0;
		if(totalCount % numPerPage == 0) {
			totalPage = totalCount / numPerPage;			
		}else {
			totalPage = totalCount / numPerPage + 1;
		}
		//pageNaviSize : 페이지네비사이즈
		int pageNaviSize = 5;
		
		//페이지네비 시작번호
		// reqPage 1 ~ 5 : 1 2 3 4 5
		// reqPage 6 ~ 10 : 6 7 8 9 10
		// reqPage 11 ~ 15 : 11 12 13 14 15 ...
		
		int pageNo = ((reqPage - 1) / pageNaviSize) * pageNaviSize + 1;
		// html 코드 작성(페이지네비게이션 작성)
		String pageNavi = "<ul class='pagination circle-style'>";
		//String pageNavi = "<ul>";
		// 이전버튼(1페이지로 시작안하면)
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/inquery/list?reqPage=" + (pageNo - 1) + "'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		
		
		
		
		for (int i = 0; i < pageNaviSize; i++) {
			pageNavi += "<li>";
			if (pageNo == reqPage) {
				pageNavi += "<a class='page-item active-page' href='/inquery/list?reqPage=" + pageNo + "'>";
				//pageNavi += "<a href='/inquery/list?reqPage=" + pageNo + "'>";
			} else {
				pageNavi += "<a class='page-item' href='/inquery/list?reqPage=" + pageNo + "'>";
			}
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			// 페이지를 만들다가 최종페이지를 출력했으면 더 이상 반복하지 않고 종료
			if (pageNo > totalPage) {
				break;
			}
		}
		// 다음버튼(최종페이지를 출력하지 않았으면)
		if (pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item' href='/inquery/list?reqPage=" + pageNo + "'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			
			
			pageNavi += "</a></li>";
		}

		pageNavi += "</ul>";
		
		// 컨트롤러로 되돌려줄 데이터가 공지사항 목록, 만든 페이지 네비게이션
		// -> java의 메소드는 1개 자료형만 리턴 기능 -> List,String(2개)를 되돌려줘야함
		// -> 되돌려주고 싶은 데이터 2개를 저장할 수 있는 데이터를 생성해서 객체로 묶어서 하나로 리턴
		InqueryListData Ild = new InqueryListData(list, pageNavi);
		
		return Ild;
	}

	@Transactional
	public int insertInquery(Inquery inq, List<InqueryFile> fileList) {
		int result = inqueryDao.insertInquery(inq);
		if(result > 0) {
			int inqueryNo = inqueryDao.selectInqueryNo();
			for(InqueryFile inqueryFile : fileList) {
				inqueryFile.setInqueryNo(inqueryNo);
				result += inqueryDao.insertInqueryFile(inqueryFile);
			}
		}				
		return result;
	}
	
	@Transactional
	public Inquery selectOneInquery(int inqueryNo, String check, int memberNo) {
		//문의글 조회
		Inquery inq = inqueryDao.selectOneInquery(inqueryNo);
		if(inq != null) {
			//조회수 증가
			if(check == null) {
				int result = inqueryDao.updateReadCount(inqueryNo);
			}
			//해당게시글의 첨부파일을 조회
			List fileList = inqueryDao.selectInqueryFile(inqueryNo);
			inq.setFileList(fileList);
			//댓글조회(문의사항 상세보기 할때 해당 문의사항의 댓글을 같이 조회) - 기본댓글만
			List<InqueryComment> commentList = inqueryDao.selectCommentList(inqueryNo,memberNo);
			System.out.println("comlist:"+commentList);
		inq.setCommentList(commentList);
		//댓글 조회 - 대댓글만 조회
		List reCommentList = inqueryDao.selectReCommentList(inqueryNo,memberNo);
		inq.setReCommentList(reCommentList); 
		}
		return inq;
	}

	@Transactional
	public List<InqueryFile> deleteInquery(int inqueryNo) {
		//1.InqueryFile에서 해당 문의사항의 첨부파일 조회
		List list = inqueryDao.selectInqueryFile(inqueryNo);
		//2. Inquery테이블에서  문의사항 삭제(외래키 옵션으로 Inquery에서 삭제되면 Inquery_file은 자동삭제)
		int result = inqueryDao.deleteInquery(inqueryNo);
		if(result > 0) {
			return list;
		}
		return null;
	}

	public Inquery getOneInquery(int inqueryNo) {
		Inquery inq = inqueryDao.selectOneInquery(inqueryNo);
		List list = inqueryDao.selectInqueryFile(inqueryNo);
		inq.setFileList(list); 
		return inq;
	}

	@Transactional
	public List<InqueryFile> updateInquery(Inquery inq, List<InqueryFile> fileList, int[] delFileNo) {
		List<InqueryFile> delFileList = new ArrayList<InqueryFile>();
		//inquery업데이트, inquery_file insert(추가한 파일이 있을때만), inquery_file delete(삭제한 파일이 있을때만)
		int result = inqueryDao.updateInquery(inq);
		if(result>0) {
			//추가한 파일이 있는 경우 추가파일 insert
			for(InqueryFile inqueryFile : fileList) {
				result += inqueryDao.insertInqueryFile(inqueryFile);
			}
			//삭제한 파일이 있는 경우 (파일을 DB에서 조회한 후(실제 폴더에서도 지우기위함) -> 삭제)
			if(delFileNo != null) {
				for(int fileNo : delFileNo) {
					InqueryFile inqueryFile = inqueryDao.selectOneInqueryFile(fileNo);
					delFileList.add(inqueryFile);
					result += inqueryDao.deleteInqueryFile(fileNo);
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

	@Transactional
	public int insertComment(InqueryComment ic) {
		int result = inqueryDao.insertComment(ic);
		return result;
	}

	@Transactional
	public int updateComment(InqueryComment ic) {
		int result = inqueryDao.updateComment(ic);
		return result;
	}
	
	@Transactional
	public int deleteComment(InqueryComment ic) {
		int result = inqueryDao.deleteComment(ic);
		return result;
	}
	
	
	
	
	
}




































