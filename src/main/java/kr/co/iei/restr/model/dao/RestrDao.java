package kr.co.iei.restr.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.dto.RestaurantRowMapper;
<<<<<<< Updated upstream
=======
import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.RestrMenuRowMapper;
import kr.co.iei.restr.model.dto.Review;
>>>>>>> Stashed changes

@Repository
public class RestrDao {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private RestaurantRowMapper restaurantRowMapper;

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

	public List selectRestrList() {
		String query = "select * from restaurant";
		List list = jdbc.query(query, restaurantRowMapper);
		return list;
	}
<<<<<<< Updated upstream
=======

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

	public int writeReview(Review review) {
		String query = "insert into review values(review_seq.nextval, ?, ?, TO_CHAR(SYSDATE, 'yyyy-mm-dd'), ?, ?)";
		Object[] params = {review.getReviewStar(), review.getReviewContent(), review.getMemberNo(), review.getRestrNo()};
		int result = jdbc.update(query, params);
		return result;
	}
	//이제 만들어야 할 것들-->수진
	public int insertWrite(Restaurant r) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRecentNo() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertRestrMenu(RestrMenu menu, int restrNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int insertRestrTag(String string, int restrNo) {
		// TODO Auto-generated method stub
		return 0;
	}
	//--여기까지
>>>>>>> Stashed changes
}
