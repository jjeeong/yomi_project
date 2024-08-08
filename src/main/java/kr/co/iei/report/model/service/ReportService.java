package kr.co.iei.report.model.service;

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
}
