package kr.co.iei.report.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.iei.report.model.dao.ReportDao;
import kr.co.iei.report.model.dto.Report;

@Service
public class ReportService {
	@Autowired
	private ReportDao reportDao;

	public int insertReviewReport(Report r) {
		int result = reportDao.insertReviewReport(r);
		return result;
	}
}
