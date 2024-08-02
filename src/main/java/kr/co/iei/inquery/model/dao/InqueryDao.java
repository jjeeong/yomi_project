package kr.co.iei.inquery.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
				
		return list;
	}
	public int selectInqueryTotalCount() {
		String query = "select count(*) from Inquery";
		int totalCount = jdbc.queryForObject(query, Integer.class); //query문 실행해서 Integer.class로 바로 꺼내기
		return totalCount;
	}
	
}























