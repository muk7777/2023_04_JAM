package com.KoreaIT.JAM.service;

import java.sql.Connection;

import com.KoreaIT.JAM.dao.MemberDao;

public class MemberService {
	
	private MemberDao memberDao;
	
	public MemberService(Connection conn) {
		this.memberDao = new MemberDao(conn);
	}

	public void add(String loginId, String loginPw, String name) {
		memberDao.add(loginId, loginPw, name);
	}

	public boolean isLoginIdDup(String loginId) {
		return memberDao.isLoginIdDup(loginId);
	}

}
