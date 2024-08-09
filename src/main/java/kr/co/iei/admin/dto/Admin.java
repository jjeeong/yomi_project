package kr.co.iei.admin.dto;

import kr.co.iei.member.model.dto.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
	private int    memberNo;
	private String memberId;
	private String memberPw;
	private String memberName;
	private String memberAddr;
	private String memberPhone;
	private String memberGender;
	private int    memberGrade;
	private String memberEmail;
	private String memberPhoto;
	private String memberRegDate;
	private int memberBirthDate;
	private int    memberReport;

}
