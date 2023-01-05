package Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import jwt.Tokenmanager;

import org.springframework.web.servlet.*;
import userloginmanage.*;

public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        Tokenmanager tokenmanager = new Tokenmanager("verification");
        String token = request.getParameter("token");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (token != null && principal != null) {
            String usernamefromtoken = tokenmanager.getUsernameFromToken(token);
            String usernamefromprincipal = ((MyUserDetail) principal).getUsername();
            return (usernamefromprincipal.compareTo(usernamefromtoken) == 0);
        } else
            return false;
    }
}
