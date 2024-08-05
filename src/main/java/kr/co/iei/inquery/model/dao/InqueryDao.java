package kr.co.iei.inquery.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.inquery.model.dto.Inquery;
import kr.co.iei.inquery.model.dto.InqueryFile;
import kr.co.iei.inquery.model.dto.InqueryRowMapper;

@Repository
public class InqueryDao {
	@Autowired
	private JdbcTemplate jdbc;
	@Autowired
	private InqueryRowMapper inqueryRowMapper;
	public List selectInqueryList(int start, int end) {
		String query = "select * from (select rownum as rnum, n.* from(select * from inquery order by 1 desc)n) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, inqueryRowMapper, params);
				System.out.println("dao"+list);
				System.out.println("start"+start);
				System.out.println("end"+end);
		return list;
	}
	public int selectInqueryTotalCount() {
		String query = "select count(*) from Inquery";
		int totalCount = jdbc.queryForObject(query, Integer.class); //query문 실행해서 Integer.class로 바로 꺼내기
		return totalCount;
	}
	public int insertInquery(Inquery inq) {
		String query = "insert into inquery values(inquery_seq.nextval,?,?,?,0,0,to_char(sysdate,'yyyy-mm-dd'))";
		Object[] params = {inq.getInqueryWriter(), inq.getInqueryTitle(), inq.getInuqueryContent()};
		int result = jdbc.update(query,params);
		return result;
	}
	public int selectInqueryNo() {
		String query = "select max(inquery_no) from inquery";
		int inqueryNo = jdbc.queryForObject(query, Integer.class);
		return inqueryNo;
	}
	public int insertInqueryFile(InqueryFile inqueryFile) {
		String query = "insert into inquery_file values(inquery_seq.nextval,?,?,?)";
		Object[] params = {inqueryFile.getInqueryNo(), inqueryFile.getFilename(),inqueryFile.getFilepath()};
		int result = jdbc.update(query, params);
		return result;
	}
}






















