package com.KoreaIT.JAM.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.KoreaIT.JAM.dao.ArticleDao;

public class ArticleService {
	
	private ArticleDao articleDao;
	
	public ArticleService(Connection conn) {
		this.articleDao = new ArticleDao(conn);
	}

	public int add(String title, String body) {
		return articleDao.add(title, body);
	}

	public List<Map<String, Object>> getArticles() {
		
		return articleDao.getArticles();
	}

	public Map<String, Object> check1(int id) {
		return articleDao.check1(id);
	}

	public int check2(int id) {
		return articleDao.check2(id);
	}

	public void update(String title, String body, int id) {
		articleDao.update(title, body, id);
	}

	public boolean check3(int id) {
		return articleDao.check3(id);
	}

	public void delete(int id) {
		articleDao.delete(id);
	}

	
	

}
