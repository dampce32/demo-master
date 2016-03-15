package org.lys.demo.ehcache.n0002.jgroups;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Cache
 */
public class Cache extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public Cache() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		if (action.equals("read")) {
			String value = (String) CacheLocalManager.readCache("key", "Cache_Test");
			out.print("cache data==>" + value);
		}
		if (action.equals("write")) {
			CacheLocalManager.saveCache("key", "cache test", "Cache_Test");
			out.print("write ok");
		}
		if (action.equals("clear")) {
			CacheLocalManager.removeCache("key", "Cache_Test");
			out.print("clear ok");
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
