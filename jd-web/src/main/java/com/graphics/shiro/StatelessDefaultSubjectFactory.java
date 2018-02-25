package com.graphics.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * 
 * 通过调用context.setSessionCreationEnabled(false)表示不创建会话；如果之后调用
 * Subject.getSession()将抛出DisabledSessionException异常。
 */
public class StatelessDefaultSubjectFactory extends DefaultWebSubjectFactory {
	@Override
	public Subject createSubject(SubjectContext context) {
		// 不创建session.
//		context.setSessionCreationEnabled(false);
		context.setSessionCreationEnabled(true);
		return super.createSubject(context);
	}

}