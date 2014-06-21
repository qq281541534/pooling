package com.liuyu.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

public class ConnectionPool {
	
	private Vector<Connection> pool; 
	
	private String url;
	
	private String userName;
	
	private String passWord;
	
	private String driverClassName;
	
	//连接池的大小，也就是连接池中有多少的数据库连接
	private int poolSize = 1;
	
	private static ConnectionPool instance = null;
	
	/**
	 * 私有构造方法，禁止外部建立本类的对象（设计模式中的单例模式）
	 */
	private ConnectionPool() {
		init();
	}
	
	/**
	 * 连接池初始化方法，读取配置文件，建立连接池中的初始连接
	 */
	private void init(){
		pool = new Vector<Connection>(poolSize);
		readConfig();
		addConnection();
	}
	
	/**
	 * 返回连接到连接池中
	 */
	public synchronized void release(Connection conn){
		pool.add(conn);
	}
	
	
	/**
	 * 关闭连接池中的所有连接
	 * 
	 */
	public synchronized void closePool(){
		for(int i=0; i<pool.size();i++){
			try {
				pool.get(i).close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pool.remove(i);
		}
	}
	
	/**
	 * 返回当前连接池的对象
	 */
	public static ConnectionPool getInstance(){
		if(instance == null){
			instance = new ConnectionPool();
		}
		return instance;
	}
	
	
	/**
	 * 返回连接池中的一个数据连接
	 */
	public synchronized Connection getConnection(){
		if(pool.size() > 0){
			Connection conn = pool.get(0);
			pool.remove(conn);
			return conn;
		} else {
			return null;
		}
	}
	
	/**
	 * 往连接池中加入指定数目的连接
	 */
	public void addConnection(){
		Connection conn = null;
		for(int i=0;i<poolSize;i++){
			try {
				Class.forName(this.driverClassName);
				conn = java.sql.DriverManager.getConnection(url,userName,passWord);
				this.pool.add(conn);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * 读取设置连接池属性的文件
	 */
	public void readConfig(){
		
		String path = System.getProperty("user.dir") + "\\dbpool.properties";
		try {
			FileInputStream is = new FileInputStream(path);
			Properties pro = new Properties();
			pro.load(is);
			this.driverClassName = pro.getProperty("driverClassName");
			this.userName = pro.getProperty("userName");
			this.passWord = pro.getProperty("passWord");
			this.url = pro.getProperty("url");
			this.poolSize = Integer.parseInt(pro.getProperty("poolSize"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件没找到");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("读取文件出错");
		}
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		String path = System.getProperty("user.dir") + "\\dbpool.properties";
		System.out.println(path);
	}
	
}

