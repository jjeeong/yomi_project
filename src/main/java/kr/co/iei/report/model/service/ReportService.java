package kr.co.iei.report.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.report.model.dao.ReportDao;
import kr.co.iei.report.model.dto.RP;
import kr.co.iei.report.model.dto.Report;

@Service
public class ReportService {
	@Autowired
	private ReportDao reportDao;
	
	@Transactional
	public int insertReviewReport(Report r) {
		int result = reportDao.insertReviewReport(r);
		return result;
	}


	public Report selectOneReport(int reportNo) {
		Report r = reportDao.selectOneReport(reportNo);
		return r;
	}
	@Transactional
	public int updateReportCheck(int[] reportNo) {
		int result =0;
		for(int i=0; i<reportNo.length;i++) {
			result += reportDao.updateReportCheck(reportNo[i]);
		}
		if(result == reportNo.length) {
			return result;
		}
		return 0;
	}
	@Transactional
	public int deleteReport(int[] reportNo) {
		int result =0;
		for(int i=0; i<reportNo.length;i++) {
			result += reportDao.deleteReport(reportNo[i]);
		}
		if(result == reportNo.length) {
			return result;
		}
		return 0;
	}
	
	@Transactional
	public int insertBoardReport(Report r) {
		int result = reportDao.insertBoardReport(r);
		return result;
	}
	@Transactional
	public int insertBoardCommentReport(Report r) {
		int result = reportDao.insertBoardCommentReport(r);
		return result;
	}

	public RP searchByReportType(String reportTypeString, int reqPage, int reportType) {
		RP r = new RP();
		int numPerPage = 10;
		int start = (reqPage-1)*numPerPage+1;
		int end = reqPage*numPerPage;
		
		List list = reportDao.searchByReportType(reportTypeString, start, end);
		int totalCount = reportDao.countReportByReportType(reportTypeString);
		int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
		String href = "/report/searchByReportType?reportType="+reportType+"&reqPage=";
		String pageNavi = getPageNav(totalPage, reqPage, href);
		r.setList(list);
		r.setPagiNavi(pageNavi);
		return r;
	}

	public RP searchByReportTypeETC(int reqPage) {
		int numPerPage = 10;
		int start = (reqPage-1)*numPerPage+1;
		int end = reqPage*numPerPage;
		List list = reportDao.searchByReportTypeETC(start, end);
		int totalCount = reportDao.countReportByReportTypeETC();
		int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
		String href = "/report/searchByReportType?reportType="+6+"&reqPage=";
		String pageNavi = getPageNav(totalPage, reqPage, href);
		RP r = new RP(list, pageNavi);
		return r;
	}

	public RP searchByBoardType(int reportBoardType, int reqPage) {
		int numPerPage = 10;
		int start = (reqPage-1)*numPerPage+1;
		int end = reqPage*numPerPage;
		List list = reportDao.searchByBoardType(reportBoardType, start, end);
		int totalCount = reportDao.countReportByBoardType(reportBoardType);
		int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
		String href = "/report/searchByBoardType?reportBoardType="+reportBoardType+"&reqPage=";
		String pageNavi = getPageNav(totalPage, reqPage, href);
		RP r = new RP(list, pageNavi);
		return r;
	}

	public RP searchById(String respondentId, int reqPage) {
		int numPerPage = 10;
		int start = (reqPage-1)*numPerPage+1;
		int end = reqPage*numPerPage;
		List list = reportDao.searchById(respondentId, start, end);
		int totalCount = reportDao.countReportById(respondentId);
		int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
		String href = "/report/searchById?repondentId="+respondentId+"&reqPage=";
		String pageNavi = getPageNav(totalPage, reqPage, href);
		RP r = new RP(list, pageNavi);
		return r;
	}

	public RP selectUncheckReport(int reqPage) {
		RP r = new RP();
		int numPerPage = 10;
		int start = (reqPage-1)*numPerPage +1;
		int end = reqPage*numPerPage;
		
		List list = reportDao.selectUncheckReport(start, end);
		int totalCount = reportDao.countReport();
		int totalPage = (int)Math.ceil((double)totalCount/numPerPage);
		String href = "/report/checkReport?reqPage=";
		String pageNavi = getPageNav(totalPage, reqPage, href);
		r.setList(list);
		r.setPagiNavi(pageNavi);
		return r;
	}
	
	public String getPageNav(int totalPage, int reqPage, String href) {
		System.out.println(totalPage);
		int start = reqPage-2;
		int pageNavSize = 5;
		String pageNavi = "<ul class='pagination pagination-sm'>";
		pageNavi += "<li class=\"page-item\">\r\n" + 
				"			      <a class=\"page-link chevron\" href=\""+href+"1\" aria-label=\"Previous\">\r\n" + 
				"			        <span aria-hidden=\"true\">&laquo;</span>\r\n" + 
				"			      </a>\r\n" + 
				"			    </li>";
		if(totalPage>start) {
			while(start<=0) {
				start++;
			}
			while((start+pageNavSize-1)>totalPage) {
				start--;
			}
		}else {
			start = 1;
		}
		for(int i=0; i<pageNavSize; i++) {
			int pageNo = start+i;
			System.out.println(pageNo);
			if(pageNo > 0 && pageNo <=totalPage) {
				if(pageNo == reqPage) {
					pageNavi +="<li class=\"page-item current\"><a class=\"page-link\" href=\""+href+reqPage+"\">"+reqPage+"</a></li>";
				}else {
					pageNavi +="<li class=\"page-item\"><a class=\"page-link\" href=\""+href+pageNo+"\">"+pageNo+"</a></li>";
				}
			}else if(pageNo>totalPage) {
				break;
			}//elseif
		}//for
		pageNavi +="<li class=\"page-item\">\r\n" + 
				"			      <a class=\"page-link chevron\" href=\""+href+totalPage+"\" aria-label=\"Next\">\r\n" + 
				"			        <span aria-hidden=\"true\">&raquo;</span>\r\n" + 
				"			      </a>\r\n" + 
				"			    </li>\r\n" + 
				"			  </ul>";
		return pageNavi;
	}


	public int checkDuplication(Report r) {
		int count = reportDao.checkDuplication(r);
		return count;
	}
	

		
	
}
