package com.epam.testapp.database;

import java.io.Serializable;
import java.util.List;

import com.epam.testapp.database.exception.DaoException;

public interface GeneralDao<T, ID extends Serializable> 
{
	List<T> getList() throws DaoException;
	T fetchById(ID id) throws DaoException;
	boolean save(T entity) throws DaoException;
	void remove(ID[] ids) throws DaoException;
	boolean updateNews(T entity) throws DaoException;
}
