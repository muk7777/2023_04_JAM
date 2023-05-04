package com.KoreaIT.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.JAM.dto.Article;
import com.KoreaIT.JAM.service.ArticleService;

public class ArticleController extends Controller{
	private Scanner sc;
	private ArticleService articleService;
	
	public ArticleController(Scanner sc, Connection conn) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}
	
	public void doWrite() {
		
		System.out.println("== 게시물 작성 ==");
		System.out.printf("제목 :");
		String title = sc.nextLine();
		System.out.printf("내용 :");
		String body = sc.nextLine();
		
		int id = articleService.add(title, body);

		System.out.printf("%d번 게시글이 생성되었습니다.\n", id);
	}
	
	public void showList() {
		System.out.println("== 게시글 리스트 ==");
		
		List<Article> articles = new ArrayList<>();
		
		List<Map<String, Object>> articleListMap = articleService.getArticles();
		
		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}
		
		if (articles.size() == 0) {
			System.out.println("존재하는 게시물이 없습니다");
			return;
		}
		
		for (Article article : articles) {
			System.out.printf("%d	|	%s	|	%s\n", article.id, article.title, article.regDate);
		}
	}
	
	public void showDetail(String cmd) {
		String cmdBit = cmd.substring(14).trim();
		
		if (cmdBit.equals("")) {
			System.out.println("명령어가 잘못 입력됐습니다.");
			return;
		}
		int id = Integer.parseInt(cmd.substring(14).trim());
		
		Map<String, Object> articleMap = articleService.check1(id);
		
		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n");
			return;
		}
		
		Article article = new Article(articleMap);
		
		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		System.out.printf("번호 : %s\n", article.id);
		System.out.printf("작성일 : %s\n", article.regDate);
		System.out.printf("수정일 : %s\n", article.updateDate);
		System.out.printf("제목 : %s\n", article.id);
		System.out.printf("내용 : %s\n", article.id);
		
	}
	
	public void doModify(String cmd) {
		String cmdBit = cmd.substring(14).trim();
		
		if (cmdBit.equals("")) {
			System.out.println("명령어가 잘못 입력됐습니다.");
			return;
		}
		int id = Integer.parseInt(cmd.substring(14).trim());
		
		int count = articleService.check2(id); 
		
		if (count == 0) {
			System.out.println("일치하는 게시물이 존재하지 않습니다.");
			return;
		}
		
		System.out.printf("== %d번 게시물 수정 ==\n", id);
		
		System.out.printf("수정할 제목 :");
		String title = sc.nextLine();
		System.out.printf("수정할 내용 :");
		String body = sc.nextLine();
		
		articleService.update(title, body, id);
		
		System.out.printf("%d번 게시물이 수정 되었습니다.\n", id);
	}
	
	public void doDelete(String cmd) {
		String cmdBit = cmd.substring(14).trim();
		
		if (cmdBit.equals("")) {
			System.out.println("명령어가 잘못 입력됐습니다.");
			return;
		}
		int id = Integer.parseInt(cmd.substring(14).trim());

		boolean isHaveArticle = articleService.check3(id);
		
		if (!isHaveArticle) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		articleService.delete(id);
		
		System.out.printf("%d번 게시물이 삭제되었습니다.\n", id);
	}
	
}
