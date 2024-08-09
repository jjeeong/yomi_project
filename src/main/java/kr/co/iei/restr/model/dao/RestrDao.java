package kr.co.iei.restr.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kr.co.iei.restr.model.dto.BestRestrRowMapper;
import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.dto.RestaurantRowMapper;

import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.RestrMenuRowMapper;
import kr.co.iei.restr.model.dto.RestrTagRowMapper;
import kr.co.iei.restr.model.dto.Review;
import kr.co.iei.restr.model.dto.ReviewImg;
import kr.co.iei.restr.model.dto.ReviewImgRowMapper;
import kr.co.iei.restr.model.dto.ReviewRowMapper;
import kr.co.iei.restr.model.dto.ReviewTagCountRowMapper;
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
	
	@Autowired
	private ReviewTagCountRowMapper reviewTagCountRowMapper;

	@Autowired
	private BestRestrRowMapper bestRestrRowMapper;

	public Restaurant selectOneRestr(int restrNo) {
		String query = "select * from restaurant where restr_no = ?";
		Object[] params = { restrNo };
		List list = jdbc.query(query, restaurantRowMapper, params);
		if (list.isEmpty()) {
			return null;
		}
		return (Restaurant) list.get(0);
	}

	//------ 좋아요
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
	
	public int selectIsLike(int restrNo, int memberNo) {
		String query = "select count(*) from RESTAURANT_LIKE where restr_no=? and member_no=?";
		Object[] params = {restrNo, memberNo};
		int isLike = jdbc.queryForObject(query, Integer.class, params);
		return isLike;
	}
	//-------
	

	//------북마크
	public int insertRestrBookmark(int restrNo, int memberNo) {
		String query = "insert into RESTAURANT_FAVORITES values(?,?)";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteRestrBookmark(int restrNo, int memberNo) {
		String query = "delete from RESTAURANT_FAVORITES where restr_no = ? and member_no = ?";
		Object[] params = { restrNo, memberNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectRestrBookmarkCount(int restrNo) {
		String query = "select count(*) from RESTAURANT_FAVORITES where restr_no = ?";
		Object[] params = { restrNo };
		int bookmarkCount = jdbc.queryForObject(query, Integer.class, params);
		return bookmarkCount;
	}
	
	public int selectIsBookmark(int restrNo, int memberNo) {
		String query = "select count(*) from RESTAURANT_FAVORITES where restr_no=? and member_no=?";
		Object[] params = {restrNo, memberNo};
		int isBookmark = jdbc.queryForObject(query, Integer.class, params);
		return isBookmark;
	}
	
	//------리뷰 좋아요
		public int insertReviewLike(int reviewNo, int memberNo) {
			String query = "insert into review_like values(?,?)";
			Object[] params = { reviewNo, memberNo };
			int result = jdbc.update(query, params);
			return result;
		}

		public int deleteReviewLike(int reviewNo, int memberNo) {
			String query = "delete from review_like where review_no = ? and member_no = ?";
			Object[] params = { reviewNo, memberNo };
			int result = jdbc.update(query, params);
			return result;
		}

		public int selectReviewLike(int reviewNo) {
			String query = "select count(*) from review_like where review_no = ?";
			Object[] params = { reviewNo };
			int reviewLikeCount = jdbc.queryForObject(query, Integer.class, params);
			return reviewLikeCount;
		}
		
		public int selectIsReviewLike(int reviewNo, int memberNo) {
			String query = "select count(*) from review_like where review_no=? and member_no=?";
			Object[] params = {reviewNo, memberNo};
			int isReviewLike = jdbc.queryForObject(query, Integer.class, params);
			return isReviewLike;
		}

	//------
	public List selectRestrList(int start, int end) {
		String query = "select * from (select rownum as rnum, r.* from (select * from restaurant order by restr_no desc)r) where rnum between ? and ?";
		Object[] params = { start, end };
		List list = jdbc.query(query, restaurantRowMapper, params);
		return list;
	}
	
	public List selectRestrListStar(int start, int end) {
		String query = "SELECT *\r\n" + 
				"FROM (\r\n" + 
				"    SELECT \r\n" + 
				"        rownum AS rnum, \r\n" + 
				"        r.*\r\n" + 
				"    FROM (\r\n" + 
				"        SELECT \r\n" + 
				"            restr.* \r\n" + 
				"        FROM \r\n" + 
				"            restaurant restr\r\n" + 
				"        LEFT JOIN \r\n" + 
				"            review rev ON restr.restr_no = rev.restr_no\r\n" + 
				"        GROUP BY \r\n" + 
				"            restr.restr_no,\r\n" + 
				"            restr.restr_addr1,\r\n" + 
				"            restr.restr_addr2,\r\n" + 
				"            restr.restr_content,\r\n" + 
				"            restr.restr_img1,\r\n" + 
				"            restr.restr_img2,\r\n" + 
				"            restr.restr_mapx,\r\n" + 
				"            restr.restr_mapy,\r\n" + 
				"            restr.restr_name,\r\n" + 
				"            restr.restr_tel\r\n" + 
				"        ORDER BY \r\n" + 
				"            ROUND(AVG(rev.review_star), 1) DESC,\r\n" + 
				"            restr.restr_no DESC\r\n" + 
				"    ) r\r\n" + 
				") \r\n" + 
				"WHERE rnum BETWEEN ? AND ?";
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
		String query = "select * from restr_menu where restr_no = ? order by 1";
		Object[] params = { restrNo };
		List list = jdbc.query(query, restrMenuRowMapper, params);
		return list;
	}

	public int writeReview(Review review) {
		String query = "insert into review values(?, ?, ?, TO_CHAR(SYSDATE, 'yyyy-mm-dd'), ?, ?)";
		Object[] params = { review.getReviewNo() ,review.getReviewStar(), review.getReviewContent(), review.getMemberNo(),
				review.getRestrNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertWrite(Restaurant r) {
		String query = "insert into restaurant values (restr_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(),
				r.getRestrTel(), r.getRestrContent(), r.getRestrImg1(), r.getRestrImg2() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int getRecentRestrNo() {
		String query = "select max(restr_no) from restaurant";
		int restrNo = jdbc.queryForObject(query, Integer.class);
		return restrNo;
	}

	public int insertRestrMenu(RestrMenu menu, int restrNo) {
		String query = "insert into restr_menu values(restr_menu_seq.nextval, ?, ?, ?)";
		Object[] params = { restrNo, menu.getRestrMenuName(), menu.getRestrMenuPrice() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int insertRestrTag(String tagName, int restrNo) {
		String query = "insert into restr_tag values (restr_tag_seq.nextval, ?, ?)";
		Object[] params = { tagName, restrNo };
		int result = jdbc.update(query, params);
		return result;
	}

	public int reviewCount(int restrNo) {
		String query = "select count(*) from review where restr_no = ?";
		Object[] params = { restrNo };
		int reviewCount = jdbc.queryForObject(query, Integer.class, params);
		return reviewCount;
	}

	public List selectReviewList(int start, int end, int restrNo) {
		String query = "select * from (select rownum as rnum, review.* from (select * from review where restr_no = ? order by review_no desc)review) left join member_tbl using (member_no) where rnum between ? and ?";
		Object[] params = { restrNo, start, end };
		List reviewList = jdbc.query(query, reviewRowMapper, params);
		return reviewList;
	}

	public List selectRestrTag(int restrNo) {
		String query = "select * from restr_tag where restr_no = ? order by 1";
		Object[] params = { restrNo };
		List tagList = jdbc.query(query, restrTagRowMapper, params);
		return tagList;
	}


	public int deleteMenu(int restrMenuNo) {
		String query = "delete from restr_menu where restr_menu_no =?";
		Object[] params = {restrMenuNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public int selectOneReview() {
		//String query = "select max(review_no) from review";
		String query = "select REVIEW_SEQ.nextval from dual";
		Integer reviewNo = jdbc.queryForObject(query, Integer.class);
		return reviewNo;
	}

	public int insertKeyword(int reviewNo, String keyword) {
		String query = "insert into review_tag values(review_tag_seq.nextval, ?, ?)";
		Object[] params = {keyword, reviewNo};
		int result = jdbc.update(query, params);
		return result;
	}


	public int deleteTag(int restrTagNo) {
		String query = "delete from restr_tag where restr_tag_no = ?";
		Object[] params = {restrTagNo};
		int result = jdbc.update(query, params);
		return result;
	}

	public List selectReviewTagList(int reviewNo) {
		String query = "select review_no, review_tag_name, restr_no, review_tag_no from review_tag join review using (review_no) where review_no = ?";
		Object[] params = { reviewNo };
		List list = jdbc.query(query, reviewTagRowMapper, params);
		return list;
	}

	public int insertReviewImg(ReviewImg reviewImg) {
		String query = "insert into review_img values(review_img_seq.nextval,?,?,?)";
		Object[] params = {reviewImg.getReviewFilename(), reviewImg.getReviewFilePath(), reviewImg.getReviewNo()};
		int result = jdbc.update(query, params);
		return result;
	}


	public int updateRestr(Restaurant r) {
		String query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=? where restr_no=?";
		Object[] params = { r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(),
				r.getRestrTel(), r.getRestrContent(), r.getRestrNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int updateRestrWithOne(Restaurant r, int updateImgCount) {
		int result = 0;
		String query = "";
		Object[] params = { r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(),
				r.getRestrTel(), r.getRestrContent(), null, r.getRestrNo() };
		switch (updateImgCount) {
			case 1:
				query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img1=? where restr_no=?";
				params[7] = r.getRestrImg1();
				break;
			case 2:
				query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img2=? where restr_no=?";
				params[7] = r.getRestrImg2();
				break;
		}
		result = jdbc.update(query, params);
		return result;
	}

	public int updateRestrWithAll(Restaurant r) {
		String query = "update restaurant set restr_name=?, restr_addr1=?, restr_addr2=?, restr_mapx=?, restr_mapy=?, restr_tel=?, restr_content=?, restr_img1=?, restr_img2=? where restr_no=?";
		Object[] params = { r.getRestrName(), r.getRestrAddr1(), r.getRestrAddr2(), r.getRestrMapx(), r.getRestrMapy(),
				r.getRestrTel(), r.getRestrContent(), r.getRestrImg1(), r.getRestrImg2(), r.getRestrNo() };
		int result = jdbc.update(query, params);
		return result;
	}

	public int deleteRestr(int restrNo) {
		String query = "delete from restaurant where restr_no = ?";
		Object[] params = { restrNo };
		int result = jdbc.update(query, params);
		return result;
	}


	public List selectReviewImgList(int reviewNo) {
		String query = "select review_no, review_img_no, review_filename, review_filepath from review_img join review using (review_no) where review_no = ?";
		Object[] params = { reviewNo };
		List list = jdbc.query(query, reviewImgRowMapper, params);
		return list;
	}

	public Double RestrStarAvg(int restrNo) {
		String query = "select round(avg(review_star),1) from review where restr_no = ?";
		Object[] params = {restrNo};
		Double starAddr = jdbc.queryForObject(query , Double.class, params);
		return starAddr;
	}

	public List selectBest() {//별점 베스트 12개 뽑기.. 정렬이 된지는 알수 없음.. 
		String query = "select restr_name, restr_no, restr_img1,(select round(avg(review_star),1) from review r where r.restr_no=restr.restr_no)review_star from restaurant restr \r\n" + 
				"where restr_no in (\r\n" + 
				"select restr_no from \r\n" + 
				"(select rownum as rnum, r.* from \r\n" + 
				"(select avg(review_star), restr_no from review group by restr_no order by 1 desc)r)r2 where rnum<13)";
		List list = jdbc.query(query, bestRestrRowMapper);
		return list;
	}

	public List tagCountList(int restrNo) {
		String query = "select count(review_tag_name) as tag_count, review_tag_name from review_tag join review using(review_no) where restr_no = ? GROUP BY review_tag_name";
		Object[] params = {restrNo};
		List tagCountList = jdbc.query(query, reviewTagCountRowMapper, restrNo);
		return tagCountList;
	}

	//검색결과
	public List restrSearch(String searchKeyword) {
		String query = "SELECT restr_no, restr_addr1, restr_addr2, restr_content, restr_img1, restr_img2, restr_mapx, restr_mapy, restr_name, restr_tel\r\n" + 
				"FROM RESTAURANT\r\n" + 
				"left JOIN restr_tag USING (restr_no)\r\n" + 
				"left JOIN restr_menu USING (restr_no)\r\n" + 
				"WHERE RESTR_TAG_NAME LIKE ?\r\n" + 
				"OR RESTR_MENU_NAME LIKE ?\r\n" + 
				"OR RESTR_NAME LIKE ?\r\n" + 
				"GROUP BY restr_no, restr_addr1, restr_addr2, restr_content, restr_img1,\r\n" + 
				"restr_img2, restr_mapx, restr_mapy, restr_name, restr_tel";
		
		Object[] params = {"%"+searchKeyword +"%","%"+searchKeyword+"%","%"+searchKeyword+"%"}; 
		List<Restaurant> list = jdbc.query(query, restaurantRowMapper, params); 
		return list;
	}

	public List restrSearchStar(String searchKeyword) {
	    String query = "SELECT \r\n" + 
	    		"    restr.restr_no, \r\n" + 
	    		"    restr.restr_addr1, \r\n" + 
	    		"    restr.restr_addr2, \r\n" + 
	    		"    restr.restr_content, \r\n" + 
	    		"    restr.restr_img1, \r\n" + 
	    		"    restr.restr_img2, \r\n" + 
	    		"    restr.restr_mapx, \r\n" + 
	    		"    restr.restr_mapy, \r\n" + 
	    		"    restr.restr_name, \r\n" + 
	    		"    restr.restr_tel\r\n" + 
	    		"FROM \r\n" + 
	    		"    restaurant restr\r\n" + 
	    		"LEFT JOIN \r\n" + 
	    		"    review rev ON restr.restr_no = rev.restr_no\r\n" + 
	    		"LEFT JOIN \r\n" + 
	    		"    restr_tag rt ON restr.restr_no = rt.restr_no\r\n" + 
	    		"LEFT JOIN \r\n" + 
	    		"    restr_menu rm ON restr.restr_no = rm.restr_no\r\n" + 
	    		"WHERE \r\n" + 
	    		"    rt.restr_tag_name LIKE ? \r\n" + 
	    		"    OR rm.restr_menu_name LIKE ? \r\n" + 
	    		"    OR restr.restr_name LIKE ?\r\n" + 
	    		"GROUP BY \r\n" + 
	    		"    restr.restr_no, \r\n" + 
	    		"    restr.restr_addr1, \r\n" + 
	    		"    restr.restr_addr2, \r\n" + 
	    		"    restr.restr_content, \r\n" + 
	    		"    restr.restr_img1, \r\n" + 
	    		"    restr.restr_img2, \r\n" + 
	    		"    restr.restr_mapx, \r\n" + 
	    		"    restr.restr_mapy, \r\n" + 
	    		"    restr.restr_name, \r\n" + 
	    		"    restr.restr_tel\r\n" + 
	    		"ORDER BY \r\n" + 
	    		"    ROUND(AVG(rev.review_star), 1) DESC, \r\n" + 
	    		"    restr.restr_no DESC";

	    Object[] params = {"%" + searchKeyword + "%", "%" + searchKeyword + "%", "%" + searchKeyword + "%"};
	    List list = jdbc.query(query, restaurantRowMapper, params);
	    return list;
	}

}
