package kr.co.iei.etc;

import java.util.List;

import kr.co.iei.board.model.dto.Board;
import kr.co.iei.restr.model.dto.BestRestaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MainList {
	private List<BestRestaurant> bestRestrList;
	private List<Board> bestBoardList;
	//쿠키 값도 아마 받아야 할듯?=>쿠키 클래스도 따로 만들어야 할거 같으이
}
