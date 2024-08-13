package kr.co.iei.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardComment {
	private int commentNo;
	private String commentWriter;
	private String	commentContent;
	private String commentRegDate;
	private int commentBoardNo;
	private int commentRefNo;
	private int likeCount;
	private int isLike;
}
