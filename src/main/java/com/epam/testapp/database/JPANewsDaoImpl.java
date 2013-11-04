package com.epam.testapp.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.testapp.database.exception.DaoException;
import com.epam.testapp.model.News;

//@Service("newsDao")
public class JPANewsDaoImpl implements NewsDao
{
	private static final Logger log = LoggerFactory.getLogger(JPANewsDaoImpl.class);
	private final String GET_LIST = "news.getList";
	private final String REMOVE = "news.remove";
	
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<News> getList() throws DaoException 
	{
		log.debug("com.epam.testapp.database.JPANewsDaoImpl public List<News> getList() throws DaoException");
		try
		{
			log.debug("Using NamedQuery={}", GET_LIST);
			return em.createNamedQuery(GET_LIST).getResultList();
		}
		catch (Exception e)
		{
			log.debug("Some Exception catched: ", e.getMessage());
			throw new DaoException("Can't complete getList operation. Error in query or some problems with database.", e);
		}
	}

	@Override
	public News fetchById(Integer id) throws DaoException 
	{
		log.debug("com.epam.testapp.database.JPANewsDaoImpl public News fetchById(Integer id) throws DaoException: id={}", id);
		if (null == id)
		{
			return null;
		}
		try
		{
			return em.find(News.class, id);
		}
		catch (Exception e)
		{
			log.debug("Some Exception catched: ", e.getMessage());
			throw new DaoException("Can't complete fetchById operation. Error in query or some problems with database.", e);
		}
	}

	@Override
	public boolean save(News entity) throws DaoException 
	{
		log.debug("com.epam.testapp.database.JPANewsDaoImpl public boolean save(News entity) throws DaoException: entity={}", entity);
		if (null == entity)
		{
			return false;
		}
		else
		{
			try
			{
				log.debug("Attempt to save News.class: entity={}", entity);
				em.persist(entity);
				em.flush();
				log.debug("Attempt to save News.class was successfull: entity={}", entity);
				return true;
			}
			catch (Exception e)
			{
				log.debug("Some Exception catched: ", e.getMessage());
				throw new DaoException("Can't complete save operation. Error in query or some problems with database. Attemption to save " + entity.toString(), e);
			}
		}
	}

	@Override
	public void remove(Integer[] ids) throws DaoException 
	{
		log.debug("com.epam.testapp.database.JPANewsDaoImpl public void remove(Integer[] ids) throws DaoException: ids={}", ids);
		List<Integer> idsList = new ArrayList<Integer>();
		boolean isAdded = idsList.addAll(Arrays.asList(ids));
		if (isAdded) 
		{
			try 
			{
				log.debug("Using NamedQuery={}", REMOVE);
				log.debug("Attempt to remove News.class: ids={}", idsList);
				Query query = em.createNamedQuery(REMOVE).setParameter("deleteIds", idsList);			
				query.executeUpdate();
				em.flush();
				log.debug("Attempt to remove News.class was successfull: ids={}", idsList);
			} 
			catch (Exception e) 
			{
				log.debug("Some Exception catched: ", e.getMessage());
				throw new DaoException("Can't complete save operation. Error in query or some problems with database.", e);
			}
		} 
	}

	@Override
	public boolean updateNews(News entity) throws DaoException 
	{
		log.debug("com.epam.testapp.database.JPANewsDaoImpl public boolean updateNews(News entity) throws DaoException: entity={}", entity);
		if (null == entity)
		{
			return false;
		}
		else
		{
			try
			{
				log.debug("Attempt to update News.class: entity={}", entity);
				em.merge(entity);
				em.flush();
				log.debug("Attempt to update News.class was successfull: entity={}", entity);
				return true;
			}
			catch (Exception e)
			{
				log.debug("Some Exception catched: ", e.getMessage());
				throw new DaoException("Can't complete update operation. Error in query or some problems with database. Attemption to update " + entity.toString(), e);
			}
		}
	}

}
