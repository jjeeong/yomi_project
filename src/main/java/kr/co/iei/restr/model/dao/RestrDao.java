package kr.co.iei.restr.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.dto.RestaurantRowMapper;

import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.RestrMenuRowMapper;
import kr.co.iei.restr.model.dto.RestrTagRowMapper;
import kr.co.iei.restr.model.dto.Review;
import kr.co.iei.restr.model.dto.ReviewImg;
import kr.co.iei.restr.model.dto.ReviewImgRowMapper;
import kr.co.iei.restr.model.dto.ReviewRowMapper;
import kr.co.iei.restr.model.dto.ReviewTagRowMapper;

@Repository
public class RestrDao {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private RestaurantRowMapper restaurantRowMapper;

	@Autowired
	private RestrMenuRowMapper restrMenuRowMapper;
	
	@Autowired
	private ReviewRowMapper reviewRowMapper;
	
	@Autowired
	private RestrTagRowMapper restrTagRowMapper;
	
	@Autowired
	private ReviewTagRowMapper reviewTagRowMapper;
	
	@Autowired
	private ReviewImgRowMapper reviewImgRowMapper;

	public Restaurant selectOneRestr(int restrNo) {
		String query = "select * from restaurant where restr_no = ?";
		Object[] params = { restrNo };
		List list = jdbc.query(query, restaurantRowMapper, params);
		if (list.isEmpty()) {
			return null;
		}
		return (Restaurant) list.get(0);
	}

	public int insertRestrLike(int restrNo, int memberNo) {
		String query = "insert into restaurant_like values(?,?)";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteRestrLike(int restrNo, int memberNo) {
		String query = "delete from restaurant_like where restr_no = ? and member_no = ?";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectRestrLikeCount(int restrNo) {
		String query = "select count(*) from restaurant_like where restr_no = ?";
		Object[] params = { restrNo };
		int likeCount = jdbc.queryForObject(query, Integer.class, params);
		return likeCount;
	}

	public List selectRestrList(int start, int end) {
		String query = "select * from (select rownum as rnum, r.* from (select * from restaurant order by restr_no desc)r) where rnum between ? and ?";
		Object[] params = { start, end };
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
		List list = jdbc.query(query, restrMenuRowMapper, params);
		return list;
	}

	public int writeReview(Review review) {
		String query = "insert into review values(review_seq.nextval, ?, ?, TO_CHAR(SYSDATE, 'yyyy-mm-dd'), ?, ?)";
		Object[] params = { review.getReviewStar(), review.getReviewContent(), review.getMemberNo(), review.getRestrNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertWrite(Restaurant r) {
		String query = "insert into restaurant values (restr_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = {r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(), r.getRestrTel(), r.getRestrContent(), r.getRestrImg1(), r.getRestrImg2()};
		int result = jdbc.update(query, params);
		return result;
	}

	public int getRecentRestrNo() {
		String query ="select max(restr_no) from restaurant";
		int restrNo = jdbc.queryForObject(query, Integer.class);
		return restrNo;
	}

	public int insertRestrMenu(RestrMenu menu, int restrNo) {
		String query ="insert into restr_menu values(restr_menu_seq.nextval, ?, ?, ?)";
		Object[] params= {restrNo, menu.getRestrMenuName(), menu.getRestrMenuPrice()};
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertRestrTag(String tagName, int restrNo) {
		String query = "insert into restr_tag values (restr_tag_seq.nextval, ?, ?)";
		Object[] params = {tagName, restrNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int reviewCount(int restrNo) {
		String query = "select count(*) from review where restr_no = ?";
		Object[] params = {restrNo};
		int reviewCount = jdbc.queryForObject(query, Integer.class ,params);
		return reviewCount;
	}

	public List selectReviewList(int start, int end, int restrNo) {
		String query = "select * from (select rownum as rnum, review.* from (select * from review where restr_no = ? order by review_no desc)review) join member_tbl using (member_no) where rnum between ? and ?";
		Object[] params = {restrNo, start, end};
		List reviewList = jdbc.query(query, reviewRowMapper, params);
		return reviewList;
	}

	public List selectRestrTag(int restrNo) {
		String query = "select * from restr_tag where restr_no = ?";
		Object[] params = {restrNo};
		List tagList = jdbc.query(query, restrTagRowMapper, params);
		return tagList;
	}

	public int selectOneReview(int restrNo) {
	    String query = "select review_no from (select rownum as rnum, review.* from (select * from review where restr_no = ? order by review_no desc) review) where rnum = 1";
	    Object[] params = {restrNo};
	    Integer reviewNo = jdbc.queryForObject(query, Integer.class, params);
	    return reviewNo;
	}

	public int insertKeyword(int reviewNo, String keyword) {
		String query = "insert into review_tag values(review_tag_seq.nextval, ?, ?)";
		Object[] params = {keyword, reviewNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public List selectReviewTagList(int reviewNo) {
		String query = "select review_no, review_tag_name, restr_no, review_tag_no from review_tag join review using (review_no) where review_no = ?";
		Object[] params = {reviewNo};
		List list = jdbc.query(query , reviewTagRowMapper, params); 
		return list;
	}

	public int insertReviewImg(ReviewImg reviewImg) {
		String query = "insert into review_img values(review_img_seq.nextval,?,?,?)";
		Object[] params = {reviewImg.getReviewFilename(), reviewImg.getReviewFilePath(), reviewImg.getReviewNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public List selectReviewImgList(int reviewNo) {
		String query = "select review_no, review_img_no, review_filename, review_filepath from review_img join review using (review_no) where review_no = ?";
		Object[] params = {reviewNo};
		List list = jdbc.query(query , reviewImgRowMapper, params); 
		return list;
	}

	public Double RestrStarAvg(int restrNo) {
		String query = "select round(avg(review_star),1) from review where restr_no = ?";
		Object[] params = {restrNo};
		Double starAddr = jdbc.queryForObject(query , Double.class, params);
		return starAddr;
	}

}
