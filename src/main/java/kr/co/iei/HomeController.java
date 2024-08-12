package kr.co.iei;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.iei.board.model.service.BoardService;
import kr.co.iei.restr.model.dto.Restaurant;
import kr.co.iei.restr.model.dto.RestrReviewData;
import kr.co.iei.restr.model.dto.Review;
import kr.co.iei.restr.model.service.RestrService;
import kr.co.iei.util.CookieUtils;

@Controller
public class HomeController {
	@Autowired
	private RestrService restrService;
	@Autowired
	private CookieUtils cookieUtils;
	@Autowired
	private BoardService boardService;
	
		@GetMapping(value="/")
		public String main(Model model, HttpServletRequest request) {
			List restrList = restrService.selectBest();
			List reviewList = restrService.selectBestReview();
			model.addAttribute("restrList", restrList);
			model.addAttribute("reviewList", reviewList);
			return "index";
		}

		@GetMapping(value = "/searchList")
		public String searchList(String search, Model model) {
			List<Restaurant> restrSearchList = restrService.restrSearch(search, "star", 1, 12);

			List<RestrReviewData> restrReviewData = new ArrayList<>();
			for (Restaurant restaurant : restrSearchList) {
				Double star = restrService.RestrStarAvg(restaurant.getRestrNo());
				restaurant.setStar(star);
				
			    List<Review> restaurantReviews = restrService.reviewSearch(restaurant.getRestrNo());

			    RestrReviewData data = new RestrReviewData();
			    data.setRestaurant(restaurant);
			    data.setReviewList(restaurantReviews);

			    restrReviewData.add(data);
			}
			
			List boardList = boardService.boardSearch(search);
			
			model.addAttribute("restrSearchList", restrSearchList);
			model.addAttribute("restrReviewData", restrReviewData);
			model.addAttribute("boardList", boardList);
			model.addAttribute("search", search);
			
			return "restaurant/searchList";
		}
}
