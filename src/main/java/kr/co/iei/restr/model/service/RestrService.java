package kr.co.iei.restr.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import kr.co.iei.member.model.dto.Member;
import kr.co.iei.restr.model.dao.RestrDao;
import kr.co.iei.restr.model.dto.Restaurant;

import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.Review;

@Service
public class RestrService {

	@Autowired
	private RestrDao restrDao;

	public Restaurant selectOneRestr(int restrNo) {
		Restaurant r = restrDao.selectOneRestr(restrNo);
		return r;
	}

	@Value("${naver.client-id}")
	private String clientId;

	@Value("${naver.client-secret}")
	private String clientSecret;

	private static final String API_URL = "https://openapi.naver.com/v1/search/blog.json?query=";

	public String searchBlog(String query) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-Naver-Client-Id", clientId);
		headers.set("X-Naver-Client-Secret", clientSecret);

		HttpEntity<String> entity = new HttpEntity<>(headers);

		String url = API_URL + query;
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

		return response.getBody();
	}

	@Transactional
	public int likePush(int restrNo, int isLike, int memberNo) {
		int result = 0;
		if (isLike == 0) {
			// 현재 좋아요를 누르지 않은 상태에서 클릭 -> 좋아요 -> insert
			result = restrDao.insertNoticeRestrLike(restrNo, memberNo);
		} else if (isLike == 1) {
			// 현재 좋아요를 누른 상태에서 클릭 -> 좋아요 취소 -> delete
			result = restrDao.deleteNoticeRestrLike(restrNo, memberNo);
		}
		if (result > 0) {
			// 좋아요,좋아요 취소 로직을 수행하고나면 현재 좋아요 갯수를 조회해서 리턴
			int likeCount = restrDao.selectNoticeRestrLikeCount(restrNo);
			return likeCount;
		} else {
			return -1;
		}
	}

	public List selectRestrList(int start, int amount) {
		int end = start + amount - 1;
		List list = restrDao.selectRestrList(start, end);
		return list;
	}

	public int selectRestrTotalCount() {
		int restrTotalCount = restrDao.selectRestrTotalCount();
		return restrTotalCount;
	}

	public List selectRestrMenu(int restrNo) {
		List list = restrDao.selectRestrMenu(restrNo);
		return list;
	}

	public int writeReview(Review review) {
		int result = restrDao.writeReview(review);
		return result;
	}

	@Transactional
	public int insertRestr(Restaurant r, List<RestrMenu> menuList, String[] tagName) {
		int result = restrDao.insertWrite(r);
		if (result > 0) {
			int restrNo = restrDao.getRecentRestrNo();
			for (RestrMenu menu : menuList) {
				result += restrDao.insertRestrMenu(menu, restrNo);
			}
			for (int i = 0; i < tagName.length; i++) {
				result += restrDao.insertRestrTag(tagName[i], restrNo);
			}
			if (result == 1 + menuList.size() + tagName.length) {
				return result;
			}
		}
		return 0;
	}

	public int reviewCount(int restrNo) {
		int reviewCount = restrDao.reviewCount(restrNo);
		return reviewCount;
	}

	public List selectReviewList(int start, int amount, int restrNo) {
		int end = start + amount - 1;
		List reviewList = restrDao.selectReviewList(start, end, restrNo);
		return reviewList;
	}

	public List selectRestrTag(int restrNo) {
		List tagList = restrDao.selectRestrTag(restrNo);
		return tagList;
	}

	public int getRecentRestrNo() {
		int restrNo = restrDao.getRecentRestrNo();
		return restrNo;
	}

	public Restaurant selectOneRestrWith(int restrNo) {
		Restaurant r = restrDao.selectOneRestr(restrNo);
		if(r!=null) {
			List tagList = restrDao.selectRestrTag(restrNo);
			List menuList = restrDao.selectRestrMenu(restrNo);
			r.setRestrMenu(menuList);
			r.setRestrTag(tagList);
			return r;
		}
		return null;
	}
	@Transactional
	public int updateRestr(Restaurant r, List<RestrMenu> menuList, String[] tagName, int[] delMenuNo, int[] delTagNo,
			int updateImgCount) {
		//1. restaurant 테이블을 업데이트 한다
		int result=0;
		int delCount=0;
		int insertCount=0;
		switch(updateImgCount) {
		case 0:
			result = restrDao.updateRestr(r);
			break;
		case 1: case 2:
			result = restrDao.updateRestrWithOne(r, updateImgCount);
			break;
		case 3:
			result = restrDao.updateRestrWithAll(r);
			break;
		}
		//2. menu, tag 삭제할 것들을 삭제한다
		if(delMenuNo!=null) {
			for(int i=0; i<delMenuNo.length; i++) {
				result+=restrDao.deleteMenu(delMenuNo[i]);
			}
			delCount+=delMenuNo.length;
		}
		if(delTagNo!=null) {
			for(int i=0; i<delTagNo.length; i++) {
				result+=restrDao.deleteTag(delTagNo[i]);
			}
			delCount+=delTagNo.length;
		}
		//3. menu, tag 추가할것들을 추가한다
		for(RestrMenu menu : menuList) {
			result += restrDao.insertRestrMenu(menu, r.getRestrNo());
		}
		insertCount+=menuList.size();
		if(tagName!=null) {
			for(int i=0; i<tagName.length;i++) {
				result += restrDao.insertRestrTag(tagName[i], r.getRestrNo());
			}
			insertCount+=tagName.length;
		}
		//4. int result가 괜찮은지 확인하는 if문을 작성, 맞으면 그 값을, 아니면 0을 반환한다.
		if(result == 1 + delCount + insertCount) {
			return result;
		}
		return 0;
	}

	public List<String> deleteRestr(int restrNo) {
		List<String>delFilepath = new ArrayList<String>();
		Restaurant r = selectOneRestr(restrNo);
		if(r!=null) {
			delFilepath.add(r.getRestrImg1());
			delFilepath.add(r.getRestrImg2());
			int result = restrDao.deleteRestr(restrNo);
			if(result>0) {
				return delFilepath;
			}
		}
		return null;
	}

}
