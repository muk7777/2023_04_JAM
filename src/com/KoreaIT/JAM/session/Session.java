package com.KoreaIT.JAM.session;

import com.KoreaIT.JAM.dto.Member;

public class Session {
	public static int loginedMemberId;
	public static Member loginedMember;
	
	static {
		loginedMemberId = -1;
		loginedMember = null;
	}

	public static void login(Member member) {
		Session.loginedMemberId = member.id;		
		Session.loginedMember = member;
	}
	public static void logout() {
		Session.loginedMemberId = -1;		
		Session.loginedMember = null;
	}
	public static boolean isLogined() {
		return loginedMemberId != -1;
	}
}
