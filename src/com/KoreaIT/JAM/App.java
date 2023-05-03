package com.KoreaIT.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.JAM.dto.Article;
import com.KoreaIT.JAM.dto.Member;
import com.KoreaIT.JAM.util.DBUtil;
import com.KoreaIT.JAM.util.SecSql;

public class App {

	public void run() {
		System.out.println("== 프로그램 시작 ==");

		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1:3306/jdbc_article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			while (true) {
				System.out.printf("명령어) ");
				String cmd = sc.nextLine().trim();
	
				if (cmd.equals("exit")) {
					System.out.println("== 프로그램 끝 ==");
					break;
				}
				
	
				if (cmd.equals("member join")) {
					System.out.println("== 회원 가입 ==");
					
					String loginId;
					String loginPw;
					
					while (true) {
						System.out.printf("로그인 아이디 :");
						loginId = sc.nextLine().trim();
						boolean loginIdDupCheck = false;
						
						SecSql sql = new SecSql();
						sql.append("SELECT *");
						sql.append("FROM `member`");
						
						List<Member> members = new ArrayList<>();
						
						List<Map<String, Object>> memberListMap = DBUtil.selectRows(conn, sql);
						
						for (Map<String, Object> memberMap : memberListMap) {
							members.add(new Member(memberMap));
						}
						
						for (Member member : members) {
							if (member.loginId.equals(loginId)) {
								loginIdDupCheck = true;
							}
						}
						
						if (loginIdDupCheck) {
							System.out.printf("%s는 이미 있는 아이디입니다.\n", loginId);
							continue;
						}
						break;
					}
					
					while (true) {
						System.out.printf("로그인 비밀번호 :");
						loginPw = sc.nextLine().trim();
						System.out.printf("로그인 비밀번호 확인 :");
						String loginPwCheck = sc.nextLine().trim();
						
						if (loginPw.equals(loginPwCheck) == false) {
							System.out.println("비밀번호가 일치하지 않습니다.");
							continue;
						}
						break;
					}
					
					System.out.printf("이름 :");
					String name = sc.nextLine().trim();
					
					SecSql sql = new SecSql();
					sql.append("INSERT INTO `member`");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", loginId = ?", loginId);
					sql.append(", loginPw = ?", loginPw);
					sql.append(", name = ?", name);
					
					DBUtil.insert(conn, sql);
					
					System.out.printf("%s님 환영합니다~\n", name);
					
				} else if (cmd.equals("article write")) {

					System.out.println("== 게시물 작성 ==");
					System.out.printf("제목 :");
					String title = sc.nextLine();
					System.out.printf("내용 :");
					String body = sc.nextLine();
					
					SecSql sql = new SecSql();
					sql.append("INSERT INTO article");
					sql.append("SET regDate = NOW()");
					sql.append(", updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					
					int id = DBUtil.insert(conn, sql);
	
					System.out.printf("%d번 게시글이 생성되었습니다.\n", id);
	
				} else if (cmd.equals("article list")) {
					System.out.println("== 게시글 리스트 ==");
	
					List<Article> articles = new ArrayList<>();
					
					SecSql sql = new SecSql();
					sql.append("SELECT *");
					sql.append("FROM article");
					sql.append("ORDER BY id DESC");
					
					List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
					
					for (Map<String, Object> articleMap : articleListMap) {
						articles.add(new Article(articleMap));
					}
					
					if (articles.size() == 0) {
						System.out.println("존재하는 게시물이 없습니다");
						continue;
					}
					
					for (Article article : articles) {
						System.out.printf("%d	|	%s	|	%s\n", article.id, article.title, article.regDate);
					}
				} else if (cmd.startsWith("article detail ")) {
					String cmdBit = cmd.substring(14).trim();
					
					if (cmdBit.equals("")) {
						System.out.println("명령어가 잘못 입력됐습니다.");
						continue;
					}
					int id = Integer.parseInt(cmd.substring(14).trim());
					
					SecSql sql = SecSql.from("SELECT *");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
					
					if (articleMap.isEmpty()) {
						System.out.printf("%d번 게시물은 존재하지 않습니다.\n");
						continue;
					}
					
					Article article = new Article(articleMap);
					
					System.out.printf("== %d번 게시물 상세보기 ==\n", id);
					System.out.printf("번호 : %s\n", article.id);
					System.out.printf("작성일 : %s\n", article.regDate);
					System.out.printf("수정일 : %s\n", article.updateDate);
					System.out.printf("제목 : %s\n", article.id);
					System.out.printf("내용 : %s\n", article.id);
					
					
				} else if (cmd.startsWith("article modify ")) {
					String cmdBit = cmd.substring(14).trim();
	
					if (cmdBit.equals("")) {
						System.out.println("명령어가 잘못 입력됐습니다.");
						continue;
					}
					int id = Integer.parseInt(cmd.substring(14).trim());
					
					// 답(없는 게시물 확인)
					
					SecSql sql = SecSql.from("SELECT COUNT(*)");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					int count = DBUtil.selectRowIntValue(conn, sql);
					
					if (count == 0) {
						System.out.println("일치하는 게시물이 존재하지 않습니다.");
						continue;
					}
					
					// 내 풀이(없는 게시물 확인)
					
//					SecSql sqlSelect = new SecSql();
//					sqlSelect.append("SELECT id FROM article");
//					sqlSelect.append("WHERE id = ?", id);
//					
//					if (DBUtil.selectRowIntValue(conn, sqlSelect) == -1) {
//						System.out.println("일치하는 게시물이 존재하지 않습니다.");
//						continue;
//					}
					
					System.out.printf("== %d번 게시물 수정 ==\n", id);
					
					System.out.printf("수정할 제목 :");
					String title = sc.nextLine();
					System.out.printf("수정할 내용 :");
					String body = sc.nextLine();
					
					sql = SecSql.from("UPDATE article");
					sql.append(" SET updateDate = NOW()");
					sql.append(", title = ?", title);
					sql.append(", `body` = ?", body);
					sql.append(" WHERE id = ?", id);
					
					DBUtil.update(conn, sql);
					System.out.printf("%d번 게시물이 수정 되었습니다.\n", id);
				} else if (cmd.startsWith("article delete ")) {
					String cmdBit = cmd.substring(14).trim();
					
					if (cmdBit.equals("")) {
						System.out.println("명령어가 잘못 입력됐습니다.");
						continue;
					}
					int id = Integer.parseInt(cmd.substring(14).trim());
					
					SecSql sql = SecSql.from("SELECT COUNT(*) > 0");
					sql.append("FROM article");
					sql.append("WHERE id = ?", id);
					
					boolean isHaveArticle = DBUtil.selectRowBooleanValue(conn, sql);
					
					if (!isHaveArticle) {
						System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
						continue;
					}
					
					sql = SecSql.from("DELETE FROM article");
					sql.append("WHERE id = ?", id);
					
					DBUtil.delete(conn, sql);
					System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
				} else {
					System.out.printf("%s는 존재하지 않는 명령어입니다.\n", cmd);
				}
			}
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally {
//				try {
//					if (pstmt != null && !pstmt.isClosed()) {
//						pstmt.close();
//					}
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}	
		sc.close();
	}
}
