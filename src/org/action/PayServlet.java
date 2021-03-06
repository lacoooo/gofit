package org.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.HibernateSessionFactory;
import org.been.Foodinfo;
import org.been.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;


import org.DAO.FoodinfoDAO;
import org.DAO.OrderDAO;

import com.alipay.util.UtilDate;

/*"alipayapi.jsp?" +
	"WIDout_trade_no=1111111"
	+ "&WIDsubject=test"
	+ "&WIDtotal_fee=0.3"
	+ "&WIDbody=test"
	+ "&WIDshow_url=www.baidu.com"
	*/
public class PayServlet extends HttpServlet {
	private String WIDout_trade_no = null;
	private String WIDsubject= null;
	private String WIDtotal_fee= null;
	private String WIDbody= null;
	private String WIDshow_url = "www.baidu.com";
	private List<Order> orders;
	private HttpSession sessionMap = null ;
	FoodinfoDAO foodinfoDAO = new FoodinfoDAO();
	private Foodinfo foodinfo;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sessionMap = request.getSession();
		sessionMap.setAttribute("address", request.getParameter("address"));	
		sessionMap.setAttribute("phonenum", request.getParameter("phonenum"));
		WIDout_trade_no = UtilDate.getOrderNum() ;
		sessionMap.setAttribute("ordernumber", WIDout_trade_no);
		orders = (List<Order>) sessionMap.getAttribute("order");
		Iterator<Order> iterator = orders.iterator();
		double fee =0;
		while (iterator.hasNext()) {
			Order order = (Order) iterator.next();
			
			foodinfo = order.getFoodinfo();
			fee +=order.getProductnum()*foodinfo.getPrice();
			WIDsubject+=(foodinfo.getFoodName() + "_");
		}
		WIDtotal_fee = String.valueOf(fee);
		WIDsubject = URLEncoder.encode("gofit order", "gb2312");
		response.sendRedirect("alipayapi.jsp?" +
		  		"WIDout_trade_no="+WIDout_trade_no+
		  		"&WIDsubject="+WIDsubject+
		  		"&WIDtotal_fee="+WIDtotal_fee+
		  		"&WIDbody="+WIDsubject+
		  		"&WIDshow_url="+WIDshow_url);
	}
	
	
	
}
