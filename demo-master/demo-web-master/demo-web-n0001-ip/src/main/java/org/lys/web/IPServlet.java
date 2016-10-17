package org.lys.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 
 * @copyright: 福建骏华信息有限公司 (c)2016
 * @createTime: 2016年10月17日下午2:17:19
 * @author：lys
 * @version：1.0
 */
public class IPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IPServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String remoteAddr =  request.getRemoteAddr();
		response.getWriter().append("remoteAddr: ").append(remoteAddr);
		response.getWriter().append("IP: ").append(IpUtils.getIpAddr(request));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
