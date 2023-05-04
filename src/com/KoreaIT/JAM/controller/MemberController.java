package com.KoreaIT.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.JAM.service.MemberService;

public class MemberController extends Controller {
	
	private Scanner sc;
	private MemberService memberService;
	
	public MemberController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.memberService = new MemberService(conn);
	}	
	
	public void doJoin() {
		System.out.println("== 회원 가입 ==");
		
		String loginId;
		String loginPw;
		String name;
		
		while (true) {
			System.out.printf("로그인 아이디 :");
			loginId = sc.nextLine().trim();
			
			if (loginId.length() == 0) {
				System.out.println("아이디를 입력해주세요.");
				continue;
			}
			
			boolean isLoginIdDup =  memberService.isLoginIdDup(loginId);
			
			if (isLoginIdDup) {
				System.out.printf("%s는 이미 있는 아이디입니다.\n", loginId);
				continue;
			}
			break;
		}
		
		while (true) {
			System.out.printf("로그인 비밀번호 :");
			loginPw = sc.nextLine().trim();
			
			if (loginPw.length() == 0) {
				System.out.println("비밀번호를 입력해주세요.");
				continue;
			}
			
			System.out.printf("로그인 비밀번호 확인 :");
			String loginPwCheck = sc.nextLine().trim();
			
			if (loginPw.equals(loginPwCheck) == false) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				continue;
			}
			break;
		}
		
		while (true) {
			System.out.printf("이름 :");
			name = sc.nextLine().trim();
			
			if (name.length() == 0) {
				System.out.println("이름을 입력해주세요.");
				continue;
			}
			break;
		}
		
		memberService.add(loginId, loginPw, name);
		
		System.out.printf("%s님 환영합니다~\n", name);
	}
}