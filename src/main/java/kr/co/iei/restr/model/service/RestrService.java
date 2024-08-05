package kr.co.iei.restr.model.service;

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

import kr.co.iei.restr.model.dao.RestrDao;
import kr.co.iei.restr.model.dto.Restaurant;
<<<<<<< Updated upstream
=======
import kr.co.iei.restr.model.dto.RestrMenu;
import kr.co.iei.restr.model.dto.Review;
>>>>>>> Stashed changes

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
		if(isLike == 0) {
			//현재 좋아요를 누르지 않은 상태에서 클릭 -> 좋아요 -> insert
			result = restrDao.insertNoticeRestrLike(restrNo, memberNo);
		} else if (isLike == 1) {
			//현재 좋아요를 누른 상태에서 클릭 -> 좋아요 취소 -> delete
			result = restrDao.deleteNoticeRestrLike(restrNo, memberNo);
		}
		if(result>0) {
			//좋아요,좋아요 취소 로직을 수행하고나면 현재 좋아요 갯수를 조회해서 리턴
			int likeCount = restrDao.selectNoticeRestrLikeCount(restrNo);
			return likeCount;
		} else {
			return -1;
		}
	}


	public List selectRestrList() {
		List list = restrDao.selectRestrList();
		return list;
	}

<<<<<<< Updated upstream
=======

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
		if(result>0) {
			int restrNo = restrDao.getRecentNo();
			for(RestrMenu menu : menuList) {
				result += restrDao.insertRestrMenu(menu, restrNo);
			}
			for(int i=0; i<tagName.length; i++) {
				result += restrDao.insertRestrTag(tagName[i], restrNo);
			}
			if(result == 1+menuList.size()+tagName.length) {
				return result;
			}
		}
		return 0;
	}

>>>>>>> Stashed changes
}
