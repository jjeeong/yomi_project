package kr.co.iei.restr.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.dto.RestaurantRowMapper;
import kr.co.iei.restr.model.dto.RestrMenuRowMapper;
import kr.co.iei.restr.model.dto.Review;

@Repository
public class RestrDao {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private RestaurantRowMapper restaurantRowMapper;
	
	@Autowired
	private RestrMenuRowMapper restrMenuRowMapper;

	public Restaurant selectOneRestr(int restrNo) {
		String query = "select * from restaurant where restr_no = ?";
		Object[] params = {restrNo};
		List list = jdbc.query(query, restaurantRowMapper, params);
		if(list.isEmpty()) {
			return null;			
		}
		return (Restaurant)list.get(0);
	}

	public int insertNoticeRestrLike(int restrNo, int memberNo) {
		String query = "insert into restr_like values(?,?)";
		Object[] params = {restrNo, memberNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteNoticeRestrLike(int restrNo, int memberNo) {
		String query = "delete from restr_like where restr_no = ? and member_no = ?";
		Object[] params = {restrNo, memberNo};
		int result = jdbc.update(query, params);
		return result;
	}
	public int selectNoticeRestrLikeCount(int restrNo) {
		String query = "select count(*) from restr_like where restr_no = ?";
		Object[] params = {restrNo};
		int likeCount = jdbc.queryForObject(query, Integer.class , params);
		return likeCount;
	}

	public List selectRestrList(int start, int end) {
		String query = "select * from (select rownum as rnum, r.* from (select * from restaurant order by restr_no desc)r) where rnum between ? and ?";
		Object[] params = {start, end};
		List list = jdbc.query(query, restaurantRowMapper, params);
		return list;
	}

	public int selectRestrTotalCount() {
		String query = "select count(*) from restaurant";
		int restrTotalCount = jdbc.queryForObject(query, Integer.class);
		return restrTotalCount;
	}

	public List selectRestrMenu(int restrNo) {
		String query = "select * from restr_menu where restr_no = ?";
		Object[] params = {restrNo};
		List list = jdbc.query(query, restrMenuRowMapper ,params);
		return list;
	}

	public int writeReview(int memberNo, Review review, int restrNo) {
		String query = "insert into review values(review_seq.nextval, ?, ?, ?, to_char(sysdate, 'yyyy-mm-dd')), ?, ?)";
		Object[] params = {review.getReviewStar(), review.getReviewContent(), memberNo, restrNo};
		int result = jdbc.update(query, params);
		return result;
	}
}
