package kr.co.iei.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

		

	public Member selectOneMemberId(String memberId) {
		Member member = memberDao.selectOneMemberId(memberId);
		
		return member;
	}

	public Member selectOneMemberEmail(String memberEmail) {
		Member member = memberDao.selectOneMemberEmail(memberEmail);
		return member;
	}


	public Member findId(Member m) {
		Member member = memberDao.findId(m);
		
		return member;
	}

	public Member findName(Member m) {
		Member member = memberDao.findName(m);
		
		return member;
	}
	
	@Transactional
	public int updatePw(Member m) {
		int result = memberDao.updatePw(m);
		
		return result;
	}
	
}

	
				
	
	

