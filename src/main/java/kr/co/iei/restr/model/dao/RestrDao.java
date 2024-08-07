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
import kr.co.iei.restr.model.dto.ReviewRowMapper;

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

	public Restaurant selectOneRestr(int restrNo) {
		String query = "select * from restaurant where restr_no = ?";
		Object[] params = { restrNo };
		List list = jdbc.query(query, restaurantRowMapper, params);
		if (list.isEmpty()) {
			return null;
		}
		return (Restaurant) list.get(0);
	}

	public int insertNoticeRestrLike(int restrNo, int memberNo) {
		String query = "insert into restaurant_like values(?,?)";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteNoticeRestrLike(int restrNo, int memberNo) {
		String query = "delete from restaurant_like where restr_no = ? and member_no = ?";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectNoticeRestrLikeCount(int restrNo) {
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

	public int deleteMenu(int restrMenuNo) {
		String query = "delete from restr_menu where restr_menu_no =?";
		Object[] params = {restrMenuNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteTag(int restrTagNo) {
		String query = "delete from restr_tag where restr_tag_no = ?";
		Object[] params = {restrTagNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int updateRestr(Restaurant r) {
		String query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=? where restr_no=?";
		Object[] params= {r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(), r.getRestrTel(), r.getRestrContent(), r.getRestrNo()};
		int result = jdbc.update(query, params);
		return result;
	}

	public int updateRestrWithOne(Restaurant r, int updateImgCount) {
		int result=0;
		String query="";
		Object[] params= {r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(), r.getRestrTel(), r.getRestrContent(), null,r.getRestrNo()};
		switch(updateImgCount) {
		case 1:
			query="update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img1=? where restr_no=?";
			params[7] = r.getRestrImg1(); 
			break;
		case 2:
			query="update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img2=? where restr_no=?";
			params[7] = r.getRestrImg2(); 
			break;
		}
		result = jdbc.update(query, params);
		return result;
	}

	public int updateRestrWithAll(Restaurant r) {
		String query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img1=?, restr_img2=? where restr_no=?";
		Object[] params= {r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(), r.getRestrTel(), r.getRestrContent(), r.getRestrImg1(), r.getRestrImg2(), r.getRestrNo()};
		int result = jdbc.update(query, params);
		return result;
	}


}
