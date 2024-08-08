package kr.co.iei.board.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Board {
	private int boardNo;
	private String boardWriter;
	private String boardTitle;
	private String boardAddr;
	private String boardContent;
	private String thumbNailImg;
	private int readCount;
	private String boardRegdate;
	private String boardStoreName;
	private List<BoardFile> fileList;
	private List<BoardComment> commentList;
	private List<BoardComment> reCommentList;
}
