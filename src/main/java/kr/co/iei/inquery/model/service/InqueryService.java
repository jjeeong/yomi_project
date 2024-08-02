package kr.co.iei.inquery.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.inquery.model.dao.InqueryDao;
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
			pageNavi += "<a href='/notice/list?reqPage=" + (pageNo - 1) + "'>";
			pageNavi += "<span>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		
		
		
		
		for (int i = 0; i < pageNaviSize; i++) {
			pageNavi += "<li>";
			if (pageNo == reqPage) {
				pageNavi += "<a class='page-item active-page' href='/notice/list?reqPage=" + pageNo + "'>";
				//pageNavi += "<a href='/inquery/list?reqPage=" + pageNo + "'>";
			} else {
				pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + pageNo + "'>";
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
			pageNavi += "<a class='page-item' href='/notice/list?reqPage=" + pageNo + "'>";
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
}




























