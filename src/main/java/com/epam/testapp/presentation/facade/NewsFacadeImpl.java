package com.epam.testapp.presentation.facade;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.epam.testapp.database.NewsDao;
import com.epam.testapp.database.exception.DaoException;
import com.epam.testapp.model.News;

@Service(value = "newsFacade")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class NewsFacadeImpl implements NewsFacade
{

	@Autowired
	private NewsDao newsDao;

	@Override
	public List<News> getNewsList() throws DaoException 
	{
		List<News> news = newsDao.getList();
		if (null != news)
		{
			Collections.sort(news);
		}
		return news;
	}

	@Override
	public News fetchById(Integer id) throws DaoException 
	{
		return newsDao.fetchById(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean save(News entity) throws DaoException 
	{
		return newsDao.save(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void remove(Integer[] ids) throws DaoException 
	{
		newsDao.remove(ids);		
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public boolean updateNews(News entity) throws DaoException 
	{
		return newsDao.updateNews(entity);
	}

}
