package kr.co.iei.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import kr.co.iei.member.model.dao.MemberDao;
import kr.co.iei.member.model.dto.Member;

@Service
public class MemberService {
	@Autowired
	private MemberDao memberDao;

	public Member selectOneMember(Member m) {
		Member member = memberDao.selelctOneMember(m);
			
		return member;
	}

	public int insertMember(Member m) {
		int result = memberDao.insertMember(m);
		return result;
	}
	public Member selectOneMember(String checkId) {
		Member member = memberDao.selectOneMember(checkId);
		return member;
	}

	public List selectAllMember() {
		List list = memberDao.selectAllMember();
		return list;
	}
		

	public Member selectOneMemberId(String memberId) {
		Member member = memberDao.selectOneMemberId(memberId);
		
		return member;
	}

	public Member selectOneMemberEmail(String memberEmail) {
		Member member = memberDao.selectOneMemberEmail(memberEmail);
		return member;
	}
	
}

	
				
	
	

