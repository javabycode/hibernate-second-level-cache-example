package com.javabycode.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.Statistics;

import com.javabycode.hibernate.model.Student;

public class HibernateEHCacheExample1 {

	public static void main(String[] args) {

		System.out.println("java.io.tmpdir: " + System.getProperty("java.io.tmpdir"));

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		stats.setStatisticsEnabled(true); //Enable statistics logs 

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();		
		printStats(stats);
		printLog("--------Step 1--------");		

		Student student = (Student) session.load(Student.class, 23L);
		printLog(student.getName());
		printStats(stats);

		printLog("--------Step 2--------");		
		student = (Student) session.load(Student.class, 23L);
		printLog(student.getName());
		printStats(stats);

		// clear first level cache, so that second level cache is used
		printLog("--------Step 3--------");
		session.evict(student);
		student = (Student) session.load(Student.class, 23L);
		printLog(student.getName());		
		printStats(stats);

		// Release resources
		transaction.commit();
		session.close();
	}

	private static void printStats(Statistics stats) {
		System.out.println("Fetch Count=" + stats.getEntityFetchCount());
		System.out.println("Second Level Hit Count=" + stats.getSecondLevelCacheHitCount());
		System.out.println("Second Level Miss Count=" + stats.getSecondLevelCacheMissCount());
		System.out.println("Second Level Put Count=" + stats.getSecondLevelCachePutCount());
	}
	
	private static void printLog(String msg) {
		System.out.println(msg);
	}		
}
