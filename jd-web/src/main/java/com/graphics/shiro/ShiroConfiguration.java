package com.graphics.shiro;

import java.util.LinkedHashMap;
import java.util.Map;
 



import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

 
/**
 * shiro配置类. 采用配置文件
 */
//@Configuration
public class ShiroConfiguration {
	@Autowired
	private Environment env;
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
       ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
       factoryBean.setSecurityManager(securityManager);
      
       
       factoryBean.getFilters().put("statelessAuthc", statelessAuthcFilter());
       
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        String adminPath = env.getProperty("adminPath");
        filterChainDefinitionMap.put(adminPath+"/system/account/login", "anon");
        filterChainDefinitionMap.put(adminPath+"/system/account/logout", "anon");
        filterChainDefinitionMap.put(adminPath+"/impl/user/login", "anon");
        filterChainDefinitionMap.put(adminPath+"/**", "statelessAuthc");
       
       return factoryBean;
    }
   
    /**
     * shiro安全管理器:
     * 主要是身份认证的管理，缓存管理，cookie管理，
     * 所以在实际开发中我们主要是和SecurityManager进行打交道的
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
       
        securityManager.setSubjectFactory(subjectFactory());
        
        securityManager.setSessionManager(sessionManager());
       
        securityManager.setRealm(statelessRealm());
        /*
         * 禁用使用Sessions 作为存储策略的实现，但它没有完全地禁用Sessions
         * 所以需要配合context.setSessionCreationEnabled(false);
         */
        ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)securityManager.getSubjectDAO()).getSessionStorageEvaluator()).setSessionStorageEnabled(false);
       
       
        return securityManager;
    }
    
    /**  
     * 凭证匹配器 
     */  
    @Bean  
    public HashedCredentialsMatcher hashedCredentialsMatcher() {  
    	HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();  
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;  
        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
        return hashedCredentialsMatcher;  
    } 
    
   
    /**
     * subject工厂管理器.
     * @return
     */
    @Bean
    public DefaultWebSubjectFactory subjectFactory(){
       StatelessDefaultSubjectFactory subjectFactory = new StatelessDefaultSubjectFactory();
       return subjectFactory;
    }
   
    /**
     * session管理器：
     * sessionManager通过sessionValidationSchedulerEnabled禁用掉会话调度器，
     * 因为我们禁用掉了会话，所以没必要再定期过期会话了。
     * @return
     */
    @Bean
    public DefaultSessionManager sessionManager(){
       DefaultSessionManager sessionManager = new DefaultSessionManager();
       sessionManager.setSessionValidationSchedulerEnabled(false);
       return sessionManager;
    }
   
   
    /**
     * 自己定义的realm.
     * @return
     */
    @Bean
    public  StatelessAuthorizingRealm statelessRealm(){
       StatelessAuthorizingRealm realm = new StatelessAuthorizingRealm();
       realm.setHashedCredentialsMatcher(hashedCredentialsMatcher());
       return realm;
    }
   
   
    /**
     * 访问控制器.
     * @return
     */
    @Bean
    public StatelessAccessControlFilter statelessAuthcFilter(){
       StatelessAccessControlFilter statelessAuthcFilter = new StatelessAccessControlFilter();
       return statelessAuthcFilter;
    }
   
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
       AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
       authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
       return authorizationAttributeSourceAdvisor;
    }
   
    /**
     *   自动代理所有的advisor:
     *   由Advisor决定对哪些类的方法进行AOP代理。
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
       DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
       daap.setProxyTargetClass(true);
       return daap;
   }
   
}
