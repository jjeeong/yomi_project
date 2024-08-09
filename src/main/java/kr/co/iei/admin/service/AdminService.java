package kr.co.iei.admin.service;

import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.admin.dao.AdminDao;
import kr.co.iei.admin.dto.AdminListData;
import kr.co.iei.member.model.dto.Member;



@Service
public class AdminService {
	@Autowired
	private AdminDao adminDao;


	public  AdminListData selectAdminList(int reqPage) {
		//reqPage : 사용자가 요청한 페이지 번호
		//한 페이지당 보여줄 게시물의 수(지정) -> 10개 
		int numPerPage = 10;
		//사용자가 요청한 페이지에따른 게시물 시작번호/끝번호 계산
		//reqPage == 1 -> start = 1 / end =10
		//reqPage == 2 -> start = 11 / end =20
		//reqPage == 3 -> start = 21 / end =30
		//reqPage == 4 -> start = 31 / end =40
		int end = reqPage * numPerPage;
		int start = end - numPerPage + 1;	
		//요청페이지에 필요한 공지사항 목록을 조회
		List adminPage = adminDao.selectAdminList(start,end);
		
		//페이지네비게이션(사용자가 클릭해서 다른 페이지를 요청할 수 있도록 하는 요소) 제작
		//페이지네비게이션을 서비스에서 만드는이유 -> 총 게시물수를 조회해와야 가능하기때문
		//select count(*) from notice
		int totalCount = adminDao.selectAdminTotalCount();
		//전체 페이지 수 계산 
		int totalPage = 0;
		if(totalCount%numPerPage == 0) {
			totalPage = totalCount/numPerPage;
		}else {
			totalPage = totalCount/numPerPage+1;
		}
		System.out.println("totalPage : "+totalPage);
				
		//페이지네비 사이즈 지정
		int pageNaviSize = 5;
		
		//페이지네비 시작번호를 지정
		//reqPage 1 ~ 5 : 1 2 3 4 5
		//reqPage 6 ~ 10 : 6 7 8 9 10
		//reqPage 11 ~ 15 : 11 12 13 14 15
						
		int pageNo = (reqPage-1)/pageNaviSize*pageNaviSize + 1;
		
		//html코드작성(페이지네비게이션 작성)
		String pageNavi = "<ul class='pagination circle-style'>";
		
		//이전버튼(1페이지로 시작하지 않으면)
		if(pageNo != 1) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item'  href='/admin/adminPage?reqPage="+(pageNo-1)+"'>";
			pageNavi += "<span class='material-icons'>chevron_left</span>";
			pageNavi += "</a></li>";
		}
		
		for(int i=0;i<pageNaviSize;i++) {
			pageNavi += "<li>";
			if(pageNo == reqPage) {
				pageNavi += "<a class='page-item active-page' href='/admin/adminPage?reqPage="+pageNo+"'>";
			}else {
				pageNavi += "<a class='page-item' href='/admin/adminPage?reqPage="+pageNo+"'>";
			}		
			
			pageNavi += pageNo;
			pageNavi += "</a></li>";
			pageNo++;
			//페이지를 만들다가 최종페이지를 출력했으면 더이상 반복하지 않고 종료
			if(pageNo > totalPage) {
				break;
			}
		}	
		//다음버튼(최종페이지를 출력하지 않았으면)
		if(pageNo <= totalPage) {
			pageNavi += "<li>";
			pageNavi += "<a class='page-item'  href='/admin/adminPage?reqPage="+pageNo+"'>";
			pageNavi += "<span class='material-icons'>chevron_right</span>";
			pageNavi += "</a></li>";
		}
		pageNavi += "</ul>";
		
		//컨트롤러로 되돌려줄 데이터가 공지사항 목록, 만든 페이지 네비게이션 
		//-> java의 메소드는 1개의 자료형만 리턴 가능 -> 2개를 되돌려줘야함 List, String
		//-> 되돌려주고싶은 데이터 2개를 저장할 수 있는 객체를 생성해서 객체로 묶어서 하나로 리턴
		AdminListData ald = new AdminListData(adminPage, pageNavi);
						
		return ald;
	}
	@Transactional
	public int changeGrade(Member m) {
		int result = adminDao.changeGrade(m);
		return result;
	}
	@Transactional
	public boolean checkedChangeGrade(String no, String grade) {
		StringTokenizer sT1 = new StringTokenizer(no,"/");
		StringTokenizer sT2 = new StringTokenizer(grade,"/");
		boolean result = true;
		while(sT1.hasMoreTokens()) {
			int memberNo = Integer.parseInt(sT1.nextToken());
			int memberGrade = Integer.parseInt(sT2.nextToken());
			Member m = new Member();
			m.setMemberNo(memberNo);
			m.setMemberGrade(memberGrade);
			int intResult = adminDao.changeGrade(m);
			if(intResult == 0) {
				result = false;
				break;
			}
		}		
		return result;
	}
	public List selectAllMember() {
		List blackPage = adminDao.blackMember();		
		return blackPage;
	}
	@Transactional
	public int deleteMember(int memberNo) {
		int result = adminDao.deleteMember(memberNo);
		return result;
	}
		
	 @Transactional
	public int updateMember(Member m) {
		int result = adminDao.updateMember(m);
		return result;
	}
	
	 public List postingMember(String memberId) {
		 List result = adminDao.postingMember(memberId);
		 return result;
	 }
	public List reviewsMember(int memberNo) {
		List result = adminDao.reviewsMember(memberNo);
		return result;
	}
	 
}
