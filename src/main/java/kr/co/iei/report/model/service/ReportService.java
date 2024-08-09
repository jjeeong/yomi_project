package kr.co.iei.report.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.iei.report.model.dao.ReportDao;
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

	public List selectUncheckReport() {
		List list = reportDao.selectUncheckReport();
		return list;
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

	public List searchByReportType(String reportTypeString) {
		List list = reportDao.searchByReportType(reportTypeString);
		return list;
	}

	public List searchByReportTypeETC() {
		List list = reportDao.searchByReportTypeETC();
		return list;
	}

	public List searchByBoardType(int reportBoardType) {
		List list = reportDao.searchByBoardType(reportBoardType);
		return list;
	}

	public List searchById(String respondentId) {
		List list = reportDao.searchById(respondentId);
		return list;
	}
}
