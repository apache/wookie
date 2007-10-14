package org.tencompetence.widgetservice.widgets.forum;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.tencompetence.widgetservice.beans.Post;
import org.tencompetence.widgetservice.util.hibernate.DBManagerFactory;
import org.tencompetence.widgetservice.util.hibernate.DBManagerInterface;

public class ForumManager {
	
	static Logger _logger = Logger.getLogger(ForumManager.class.getName());
	
	public List<PostNode> getNodeTree() {				
		DBManagerInterface dbManager = null;
		try {
			List<PostNode> list = new ArrayList<PostNode>();
			LinkedHashMap<Integer, PostNode> postLookupTable = new LinkedHashMap<Integer, PostNode>();
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Post.class);
			crit.addOrder( Order.desc("publishDate"));
			final List<Post> sqlReturnList =  dbManager.getObjects(Post.class, crit);
			Post[] posts = sqlReturnList.toArray(new Post[sqlReturnList.size()]);
			for(Post post : posts){									
				postLookupTable.put(post.getId(),new PostNode(post.getId(),
						post.getUserId(),post.getParentId(),post.getContent(),post.getTitle(),
						post.getPublishDate(),post.getUpdateDate()));
			}
			// Iterate thru the posts constructing a tree hierarchy
			for(Entry<Integer, PostNode> entry : postLookupTable.entrySet()) {
				PostNode post = entry.getValue();
				// Has a Post as a Parent
				if(post.getParentId()!=0) {
					PostNode parentPost = postLookupTable.get(post.getParentId());
					parentPost.getPosts().add(post);	               
				}
				// No Parent Post so it's a top-level post with the topic as parent
				else {
					list.add(post);	               
				}
			}
			return list;	        
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			ex.printStackTrace();
			return null;
		}
	}
	
	public PostNode getPost(int postId){
		DBManagerInterface dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(Post.class);
			crit.add(Restrictions.eq("id", postId));
			final List<Post> sqlReturnList =  dbManager.getObjects(Post.class, crit);
			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				Post post = (Post) sqlReturnList.get(0);
				return new PostNode(post.getId(),
						post.getUserId(),post.getParentId(),post.getContent(),post.getTitle(),
						post.getPublishDate(),post.getUpdateDate());
			}			
		} 
		catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * 	dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(WidgetInstance.class);
			crit.add(Restrictions.eq("idKey", key));
			final List<WidgetInstance> sqlReturnList = dbManager.getObjects(
					WidgetInstance.class, crit);
			if (sqlReturnList.size() != 1) {
				return null;
			} 
			else {
				return (WidgetInstance) sqlReturnList.get(0);
			}
	 */

	public boolean newPost(String parent, String username, String title, String content){
		final DBManagerInterface dbManager = DBManagerFactory.getDBManager();
		Post post = new Post();
		try {
			post.setParentId(Integer.parseInt(parent));
			post.setTitle(title);
			post.setContent(content);
			post.setUserId(username);
			post.setPublishDate(new Date());
			dbManager.saveObject(post);
			return true;
		} catch (NumberFormatException e) {
			// TODO 
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 
			e.printStackTrace();
		}	

		return false;
	}
}
