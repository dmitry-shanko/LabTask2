package com.epam.testapp.database.constant;

public enum NewsFields 
{
	TITLE("TITLE"),
	BRIEF("BRIEF"),
	DATE("NEWSDATE"),
	CONTENT("CONTENT"),
	ID("IDNEWS");
	private String content;
	private NewsFields (String content) 
	{
		this.content = content;
	}
	
	public String getContent()
	{
		return content;
	}
}
