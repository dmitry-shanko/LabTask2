package com.epam.testapp.presentation.action;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.MappingDispatchAction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.testapp.database.exception.DaoException;
import com.epam.testapp.model.News;
import com.epam.testapp.presentation.facade.NewsFacade;
import com.epam.testapp.presentation.form.NewsForm;
import com.epam.testapp.util.GlobalConstants;

public class NewsAction extends MappingDispatchAction
{
	private NewsFacade newsFacade;
	private static final Logger log = LoggerFactory.getLogger(NewsAction.class);

	private static final String MAIN_PAGE = "mainpage";
	private static final String NEWS_LIST_PAGE = "newsList";
	private static final String VIEW_NEWS_PAGE = "newsView";
	private static final String VIEW_NEWS_PAGE_ACTION = "newsViewAction";
	private static final String EDIT_NEWS_PAGE = "newsEdit";
	private static final String ERROR_PAGE = "error";
	private static final String PREVIOUS_PAGE = "previousPage";
	private static final String REFERER = "Referer";

	public NewsAction()
	{
		log.info("com.epam.testapp.presentation.action.NewsAction has been created");
	}

	public void setNewsFacade(NewsFacade newsFacade)
	{
		this.newsFacade = newsFacade;
	}
	/**
	 * Forwards to the News list page with list of news got from newsDao.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward newsList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward newsList(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newslist");
		String target = ERROR_PAGE;
		request.getSession().setAttribute(PREVIOUS_PAGE, MAIN_PAGE);
		NewsForm newsForm = (NewsForm) actionForm;
		if (null != newsForm)
		{
			if (newsForm.getLocale() == null) 
			{
				newsForm.setLocale(Locale.US);
				log.debug("Locale in /newslist set to Locale.US: locale={}", newsForm.getLocale());
				setLocale(request, Locale.US);
			}
			List<News> newsList = null;
			try 
			{
				newsList = newsFacade.getNewsList();
			} 
			catch (DaoException e) 
			{
				log.error("Can't complete /newslist action. DaoException catched.", e);
			}
			if (newsList != null) 
			{
				target = NEWS_LIST_PAGE;
				newsForm.setNewsList(newsList);
				log.debug("Args in /newslist action (got from database): news={}", newsList);
			}
		}
		else
		{
			log.warn("newsForm in /newslist is null.");
		}
		log.info("/newslist action finished.");
		log.debug("actionMapping.findForward(target) in /newslist action: target={}", target);
		return actionMapping.findForward(target);
	}
	/**
	 * Forwards to the View page with news in newsForm to be viewed
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction newsView(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsview");
		String target = ERROR_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			target = VIEW_NEWS_PAGE;
			News news = null;
			if (null != newsForm.getNews())
			{
				try 
				{
					log.info("Args in /newsview action: news.id={}", newsForm.getNews().getId());
					news = newsFacade.fetchById(newsForm.getNews().getId());
				} 
				catch (DaoException e) 
				{
					log.error("Can't complete /newsview action. DaoException catched.", e);
				}
				newsForm.setNews(news);
				request.getSession().setAttribute(PREVIOUS_PAGE, VIEW_NEWS_PAGE_ACTION);
				log.info("Args in /newsview action (got from database): news={}", news);
			}
			else
			{
				log.warn("news in newsForm in /newsview is null.");
			}
		}	
		else
		{
			log.warn("newsForm in /newsview is null.");
		}
		log.info("/newsview action finished.");
		log.debug("actionMapping.findForward(target) in /newsview action: target={}", target);
		return actionMapping.findForward(target);
	}
	/**
	 * Forwards to the Edit page with news in newsForm to be edited.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward newsEdit(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsedit");
		String target = EDIT_NEWS_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{			
			News news = null;
			try 
			{
				if (newsForm.getNews() != null)
				{
					log.info("Args in /newsedit action: news.id={}", newsForm.getNews().getId());
					news = newsFacade.fetchById(newsForm.getNews().getId());
				}
				else
				{
					log.warn("news in newsForm in /newsedit is null.");
				}
			} 
			catch (DaoException e) 
			{
				log.error("Can't complete /newsedit action. DaoException catched.", e);
			}
			newsForm.setNews(news);
			log.info("Args in /newsedit action (got from database): news={}", news);
		}
		else
		{
			log.warn("newsForm in /newsedit is null.");
		}
		log.info("/newsedit action finished.");
		log.debug("actionMapping.findForward(target) in /newsedit action: target={}", target);
		return actionMapping.findForward(target);
	}	
	/**
	 * Forwards to the Edit page without any news to add.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward newsAdd(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward newsAdd(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsadd");
		String target = EDIT_NEWS_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{				
			News news = new News();
			news.setDate(new java.sql.Date(System.currentTimeMillis()));
			log.debug("Args in /newsadd action: emptyNews={}", news);
			newsForm.setNews(news);
		}
		else
		{
			log.warn("newsForm in /newsadd is null.");
		}
		log.info("/newsadd action finished.");
		log.debug("actionMapping.findForward(target) in /newsadd action: target={}", target);
		return actionMapping.findForward(target);
	}	
	/**
	 * Saves changed news. Creates new if id of news from newsForm.getNews() is less then 1 and updates existing if id is 1 or more.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */

	public ActionForward newsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward newsSave(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newssave");
		String target = ERROR_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			News news = newsForm.getNews();
			log.info("Args in /newssave action: news={}", news);
			if ((null != news) && (news.getBrief() != null) && (news.getContent() != null) && (news.getDate() != null) && (news.getTitle() != null))
			{
				try 
				{					
					if (news.getId() < 1)
					{
						if (newsFacade.save(news))
						{
							target = VIEW_NEWS_PAGE_ACTION;
							log.info("News have been successfully saved: savedNews={}", news);
						}
						else
						{							
							log.warn("News were not saved: notSavedNews={}", news);
						}
					}
					else
					{
						if (newsFacade.updateNews(news))
						{
							target = VIEW_NEWS_PAGE_ACTION;
							log.info("News have been successfully updated: updatedNews={}", news);
						}
						else
						{
							log.warn("News were not updated: notUpdatedNews={}", news);
						}
					}
				}
				catch (DaoException e) 
				{
					log.error("Can't complete /newssave action. DaoException catched.", e);
				}
			}
		}	
		else
		{
			log.warn("newsForm in /newssave is null.");
		}
		log.info("/newssave action finished.");
		log.debug("ActionRedirect(actionMapping.findForward(target)) in /newssave action: target={}", target);
		return new ActionRedirect(actionMapping.findForward(target));
	}
	/**
	 * This mapped action deletes list of news with ID taken from newsForm.getSelectedItems().
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public ActionForward newsDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward newsDelete(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response): actionForm={}", actionForm);
		log.info("NewsActionServlet: action=/newsdelete");
		String target = NEWS_LIST_PAGE;
		NewsForm newsForm = (NewsForm) actionForm;
		if (newsForm != null) 
		{
			Integer[] ids = newsForm.getSelectedItems();	
			log.info("Args in /newsdelete action: ids={}", ids);
			if ((null != ids) && (ids.length > 0))
			{
				try 
				{
					newsFacade.remove(ids);
					target = MAIN_PAGE;
					log.debug("/newsdelete action completed. Id of deleted News are in NewsDao Logger");
				} 
				catch (DaoException e) 
				{
					log.error("Can't complete /newsdelete action. DaoException catched.", e);
				}
			}
		}
		else
		{
			log.warn("newsForm in /newsdelete is null.");
		}
		log.info("/newsdelete action finished.");
		log.debug("ActionRedirect(actionMapping.findForward(target)) in /newsdelete action: target={}", target);
		return new ActionRedirect(actionMapping.findForward(target));
	}

	public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward cancel(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/cancel");
		String target = (String) request.getSession().getAttribute(PREVIOUS_PAGE);
		log.debug("actionMapping.findForward(target) in /cancel action: target={}", target);
		return actionMapping.findForward(target);
	}
	/**
	 * In the case of error forwards to the Error page.
	 * @param actionMapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward error(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward error(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/error");
		log.debug("actionMapping.findForward(ERROR_PAGE) in /error action: target={}", ERROR_PAGE);
		return actionMapping.findForward(ERROR_PAGE);
	}
	/**
	 * Mapped ActionForward for changing language. Forwards to the request.getHeader("Referer") link.<p>
	 * Parameter of new locale is taking from request.
	 * @param actionMapping
	 * @param actionForm 
	 * @param request
	 * @param response
	 * @return new ActionForward(request.getHeader("Referer"), true)
	 * @throws Exception
	 */
	public ActionForward lang(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) 
	{
		log.debug("com.epam.testapp.presentation.action.NewsAction public ActionForward lang(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)");
		log.info("NewsActionServlet: action=/lang");
		String lang = request.getParameter(GlobalConstants.LANG_PARAM_REQUEST.getContent()); 	
		log.debug("request.getParameter(GlobalConstants.LANG_PARAM_REQUEST.getContent()): lang={}", lang);
		if (null != lang)
		{
			String country = GlobalConstants.COUNTRY_EN.getContent();
			lang = lang.trim().toLowerCase();
			switch (lang)
			{
			case "ru":
				country = GlobalConstants.COUNTRY_RU.getContent();
				break;
			case "en":
				country = GlobalConstants.COUNTRY_EN.getContent();
				break;
			}
			Locale locale = new Locale(lang, country);
			setLocale(request, locale);
			NewsForm newsForm = (NewsForm) actionForm;
			newsForm.setLocale(locale);	
			log.info("Locale after /lang action: locale={}", locale);
		}
		else
		{
			log.warn("Attempt to change locale to null");
		}
		String target = request.getHeader(REFERER);
		log.info("/lang action finished.");
		log.debug("ActionForward(target, true) in /lang action: target={}", target);
		return new ActionForward(target, true);
	}
}
