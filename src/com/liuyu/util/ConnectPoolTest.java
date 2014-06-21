package com.liuyu.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectPoolTest {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		
		String sql = "select * from OA_ARCHIVES";
		long start = System.currentTimeMillis();
		ConnectionPool pool = null;
		
		for(int i=0;i<100;i++){
			pool= ConnectionPool.getInstance();
			Connection conn = pool.getConnection();
			Statement sm = conn.createStatement();
			ResultSet res = sm.executeQuery(sql);
			while(res.next()){
				
			}
			res.close();
			sm.close();
			pool.release(conn);
		}
		pool.closePool();
		
		System.out.println("经过100次循环调用，使用连接池话费的时间是：" + (System.currentTimeMillis() - start) + "ms\n");
		
		String driverClassName = "oracle.jdbc.OracleDriver";
		String url = "jdbc:oracle:thin:@localhost:1521:orcl";
		String userName = "u_jocko";
		String passWord = "zkcx";
		start = System.currentTimeMillis();
		
		for(int i=0;i<100;i++){
			Class.forName(driverClassName);
			Connection conn = DriverManager.getConnection(url,userName,passWord);
			Statement sm = conn.createStatement();
			ResultSet res = sm.executeQuery(sql);
			while(res.next()){
				
			}
			res.close();
			sm.close();
			conn.close();
		}
		
		System.out.println("经过100次循环调用，不使用连接池话费的时间是：" + (System.currentTimeMillis() - start) + "ms\n");
		
		
		
		
		
		
		
		
		
		
	}

}
