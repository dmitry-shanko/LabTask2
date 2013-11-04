package com.epam.testapp.presentation.facade;

import java.util.List;

import com.epam.testapp.database.exception.DaoException;
import com.epam.testapp.model.News;

public interface NewsFacade 
{
	
	List<News> getNewsList() throws DaoException;
	News fetchById(Integer id) throws DaoException;
	boolean save(News entity) throws DaoException;
	void remove(Integer[] ids) throws DaoException;
	boolean updateNews(News entity) throws DaoException;
}
