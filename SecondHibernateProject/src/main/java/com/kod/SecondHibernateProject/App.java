package com.kod.SecondHibernateProject;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    { 
    	Scanner in=new Scanner(System.in);
    	SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory(); 
    	//above i have done in 1 step
    	
    	Session session= factory.openSession();
    	
    	Transaction tx = session.beginTransaction();
    	
        System.out.println( "Enter Student id to delete");
       Student obj =  session.get(Student.class, in.nextInt());
       
       if(obj!=null) {
    	   session.remove(obj); //or .delete (its deprecated old version)
    	   System.out.println("Student Deleted Successfully");
       }
       else {
    	   System.out.println("Student Does not exist to delete");
       }
       
       tx.commit();
       
       session.close();
       factory.close();
        
    }
}
