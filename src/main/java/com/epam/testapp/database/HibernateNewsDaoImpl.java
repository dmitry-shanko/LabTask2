package com.epam.testapp.database;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.testapp.database.exception.DaoException;
import com.epam.testapp.model.News;

@Service("newsDao")

public class HibernateNewsDaoImpl implements NewsDao
{
	private SessionFactory sessionFactory;
	private static final Logger log = LoggerFactory.getLogger(HibernateNewsDaoImpl.class);

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl public void setSessionFactory(SessionFactory sessionFactory): sessionFactory={}", sessionFactory);
		if (null != sessionFactory)
		{
			this.sessionFactory = sessionFactory;
		}
	}

	@Override
	public List<News> getList() throws DaoException 
	{
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl public List<News> getList() throws DaoException");
		return findByCriteria();
	}

	@Override
	public News fetchById(Integer id) throws DaoException 
	{
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl public News fetchById(Integer id) throws DaoException: id={}", id);
		if (id == null) 
		{
			return null;
		}
		else
		{
			try
			{
				Session session = getSession();
				return (News) session.get(News.class, id);
			}
			catch (HibernateException e)
			{
				log.debug("HibernateException catched: ", e.getMessage());
				throw new DaoException("Can't get Entity " + News.class + " in " + getClass() + " because of " + e.getClass() + ".\n", e);
			}
		}
	}

	@Override
	public boolean save(News entity) throws DaoException 
	{
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl public boolean save(News entity) throws DaoException: entity={}", entity);
		try
		{
			if (null != entity)
			{
				log.debug("Attempt to save News.class: entity={}", entity);
				Session session = getSession();
				session.saveOrUpdate(entity);
				session.flush();
				log.debug("Attempt to save News.class was successfull: entity={}", entity);
				return (entity.getId() != null ? (entity.getId() > 0 ? true : false) : false);
			}
		}
		catch (HibernateException e)
		{
			log.debug("HibernateException catched: ", e.getMessage());
			throw new DaoException("Can't saveOrUpdate Entity " + News.class + " in " + getClass() + " because of " + e.getClass() + ".\n", e);
		}
		return false;
	}

	@Override

	public void remove(Integer[] ids) throws DaoException 
	{
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl public void remove(Integer[] ids) throws DaoException: ids={}", ids);
		if (ids != null) 
		{
			for (Integer id : ids)
			{
				try
				{
					log.debug("Attempt to delete News.class: id={}", id);
					Session session = getSession();
					News t = (News) session.get(News.class, id);
					if (null != t)
					{
						session.delete(t);
						log.debug("Attempt to delete News.class was successfull: id={}", id);
					}
					else
					{
						log.debug("Attempt to delete News.class was not successfull. Can't find news with such id: id={}", id);
					}
					session.flush();					
				}
				catch (HibernateException e)
				{
					log.debug("HibernateException catched: ", e.getMessage());
					throw new DaoException("Can't delete by id " + id + " in " + getClass() + " because of " + e.getClass() + ".\n", e);
				}
			}
		}
	}

	@Override
	public boolean updateNews(News entity) throws DaoException 
	{
		return this.save(entity);
	}

	@SuppressWarnings("unchecked")
	protected List<News> findByCriteria(Criterion... criterion) throws DaoException
	{		
		log.debug("com.epam.testapp.database.HibernateNewsDaoImpl protected List<News> findByCriteria(Criterion... criterion) throws DaoException: criterion={}", criterion);
		if (null != criterion)
		{
			try
			{
				Session session = getSession();
				Criteria crit = session.createCriteria(News.class);
				for (Criterion c : criterion) 
				{
					crit.add(c);
				}
				return crit.list();
			}
			catch (HibernateException e)
			{
				log.debug("HibernateException catched: ", e.getMessage());
				throw new DaoException("Can't find " + News.class + " in " + getClass() + " because of " + e.getClass(), e);
			}
		}
		else
		{
			return new ArrayList<News>(0);
		}
	}

	protected Session getSession() throws DaoException
	{
		Session session = null;
		if (null == sessionFactory)
		{
			throw new DaoException("SessionFactory is null, can't proceed");
		}
		else
		{
			try 
			{
				session = sessionFactory.getCurrentSession();
			} 
			catch (HibernateException e) 
			{
				try
				{
					log.warn("Can't getCurrentSession from sessionFactory=" + sessionFactory, e);
					session = sessionFactory.openSession();
				}
				catch (HibernateException e1)
				{
					log.debug("HibernateException catched: ", e1.getMessage());
					throw new DaoException("Can't get current session from " + sessionFactory + " or open new session in " + getClass(), e1);
				}
			}
		}		
		return session;
	}
}
