package org.lys.demo.jdk.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.junit.Test;

public class JDBCTest {

	@Test
	public void testDbConn() {
		String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL="jdbc:sqlserver://192.168.0.2:7777;databaseName=STUMS-CES";
		String userName="lz";
		String userPwd="Lz2017!@#";
		try{
			Class.forName(driverName);
			Connection dbConn=DriverManager.getConnection(dbURL,userName,userPwd);
		    System.out.println("连接数据库成功");
		}catch(Exception e){
		   e.printStackTrace();
		   System.out.print("连接失败");
		}    
	}
	
	
	@Test
	public void testCreateInitDBSql() {
		String sourceDB = "STUMS_CES";
		
		String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL="jdbc:sqlserver://192.168.0.2:7777;databaseName="+sourceDB;
		String userName="lz";
		String userPwd="Lz2017!@#";
		try{
			Class.forName(driverName);
			Connection conn=DriverManager.getConnection(dbURL,userName,userPwd);
			// 获取所有表名  
	        Statement statement = conn.createStatement();  
//	        getTables(conn);  
	        StringBuilder insertColumnsSb = new StringBuilder();
	        StringBuilder selectColumnsSb = new StringBuilder();
	        StringBuilder pkColumnsSb = new StringBuilder();
	        StringBuilder whereSb = new StringBuilder();
	        StringBuilder sqlSb = new StringBuilder();
	        DatabaseMetaData dbMetaData = conn.getMetaData();
	        
	        String tableName = "T_Teacher";
			ResultSet colRet = dbMetaData.getColumns(null,"%",tableName ,"%"); 
			int i = 0;
			while(colRet.next()) { 
				String columnName = colRet.getString("COLUMN_NAME");
				if(i==0){
					insertColumnsSb.append(columnName);
					selectColumnsSb.append("a."+columnName);
				}else{
					insertColumnsSb.append(","+columnName);
					selectColumnsSb.append(",");
					if(i%8==0){
						selectColumnsSb.append("\n    ");
					}
					selectColumnsSb.append("a."+columnName);
				}
				i++;
			}
			
			ResultSet pkRSet = dbMetaData.getPrimaryKeys(null, null, tableName); 
			while(pkRSet.next() ) { 
				String pkColumn = pkRSet.getObject(4).toString();
				pkColumnsSb.append("a.").append(pkColumn).append("=").append("b.").append(pkColumn);
				
				whereSb.append("b.").append(pkColumn).append(" is null");
			}
			sqlSb.append("insert into ").append(tableName).append("(").append(insertColumnsSb.toString()).append(")\n")
			    .append("select ").append("\n")
			    .append("    ").append(selectColumnsSb.toString()).append("\n")
			    .append("from ").append(sourceDB).append(".dbo.").append(tableName).append(" a\n")
			    .append("left join ").append(tableName).append(" b on ").append(pkColumnsSb.toString()).append("\n")
			    .append("where ").append(whereSb.toString()).append(";");
				
	        // 获取列名  
	            
	            
	            /*
	             insert into T_SchoolYear(schoolYearId,schoolYearName,array)
select 
	a.schoolYearId,a.schoolYearName,a.array
from STUMS_INSDEP.dbo.T_SchoolYear	a
left join T_SchoolYear b on a.schoolYearId = b.schoolYearId
where b.schoolYearId is null ; 
	             */
	            
	        System.out.println(sqlSb.toString());  
	        statement.close();  
	        conn.close();  
		}catch(Exception e){
		   e.printStackTrace();
		   System.out.print("连接失败");
		}     
	}
	
	
	
	
	@Test
	public void test() {
		String driverName="com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbURL="jdbc:sqlserver://192.168.0.2:7777;databaseName=STUMS-INSDEP";
		String userName="lz";
		String userPwd="Lz2017!@#";
		try{
			Class.forName(driverName);
			Connection conn = DriverManager.getConnection(dbURL,userName,userPwd);
			// 获取所有表名  
	        Statement statement = conn.createStatement();
	        String tableName = "t_student";
	        
//	        getTables(conn);  
	        String columnName;
	        
			getTableDesc(conn, tableName);
			getPKDesc(conn, tableName); 
	        
	        ResultSet resultSet = statement.executeQuery("select top 1 * from t_student ");  
	        // 获取列名  
	        ResultSetMetaData metaData = resultSet.getMetaData();  
	        for (int i = 0; i < metaData.getColumnCount(); i++) {  
	            // resultSet数据下标从1开始  
	            columnName = metaData.getColumnName(i + 1);  
	            int type = metaData.getColumnType(i + 1);  
	            if (Types.INTEGER == type) {  
	                // int  
	            } else if (Types.VARCHAR == type) {  
	                // String  
	            }  
	            System.out.print(columnName + "\t");  
	        }  
	        System.out.println();  
	        // 获取数据  
	        while (resultSet.next()) {  
	            for (int i = 0; i < metaData.getColumnCount(); i++) {  
	                // resultSet数据下标从1开始  
	                System.out.print(resultSet.getString(i + 1) + "\t");  
	            }  
	            System.out.println();  
	  
	        }  
	        statement.close();  
	        conn.close();  
		}catch(Exception e){
		   e.printStackTrace();
		   System.out.print("连接失败");
		}     
	}


	private void getPKDesc(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData dbMetaData = conn.getMetaData();
		ResultSet pkRSet = dbMetaData.getPrimaryKeys(null, null, tableName); 
		while(pkRSet.next() ) { 
		    System.out.println("****** Comment ******"); 
		    System.out.println("TABLE_CAT : "+pkRSet.getObject(1)); 
		    System.out.println("TABLE_SCHEM: "+pkRSet.getObject(2)); 
		    System.out.println("TABLE_NAME : "+pkRSet.getObject(3)); 
		    System.out.println("COLUMN_NAME: "+pkRSet.getObject(4)); 
		    System.out.println("KEY_SEQ : "+pkRSet.getObject(5)); 
		    System.out.println("PK_NAME : "+pkRSet.getObject(6)); 
		    System.out.println("****** ******* ******"); 
		}
	}


	private void getTableDesc(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData dbMetaData = conn.getMetaData();
		ResultSet colRet = dbMetaData.getColumns(null,"%",tableName ,"%"); 
		String columnName; 
		String columnType; 
		while(colRet.next()) { 
			columnName = colRet.getString("COLUMN_NAME"); 
			columnType = colRet.getString("TYPE_NAME"); 
			int datasize = colRet.getInt("COLUMN_SIZE"); 
			int digits = colRet.getInt("DECIMAL_DIGITS"); 
			int nullable = colRet.getInt("NULLABLE"); 
			String remarks = colRet.getString("REMARKS");
			
			System.out.println(columnName+" "+columnType+" "+datasize+" "+digits+" "+ nullable); 
		}
	}
	
	
	public static String convertDatabaseCharsetType(String in, String type) {  
        String dbUser;  
        if (in != null) {  
            if (type.equals("oracle")) {  
                dbUser = in.toUpperCase();  
            } else if (type.equals("postgresql")) {  
                dbUser = "public";  
            } else if (type.equals("mysql")) {  
                dbUser = null;  
            } else if (type.equals("mssqlserver")) {  
                dbUser = null;  
            } else if (type.equals("db2")) {  
                dbUser = in.toUpperCase();  
            } else {  
                dbUser = in;  
            }  
        } else {  
            dbUser = "public";  
        }  
        return dbUser;  
    }  
  
    private static void getTables(Connection conn) throws SQLException {  
        DatabaseMetaData dbMetData = conn.getMetaData();  
        ResultSet rs = dbMetData.getTables(null,  convertDatabaseCharsetType("root", "mssqlserver"), null,  
                new String[] { "TABLE", "VIEW" });  
  
        while (rs.next()) {  
            if (rs.getString(4) != null  
                    && (rs.getString(4).equalsIgnoreCase("TABLE") || rs  
                            .getString(4).equalsIgnoreCase("VIEW"))) {  
                String tableName = rs.getString(3).toLowerCase();  
                System.out.print(tableName + "\t");  
                // 根据表名提前表里面信息：  
                ResultSet colRet = dbMetData.getColumns(null, "%", tableName,  
                        "%");  
                while (colRet.next()) {  
                    String columnName = colRet.getString("COLUMN_NAME");  
                    String columnType = colRet.getString("TYPE_NAME");  
                    int datasize = colRet.getInt("COLUMN_SIZE");  
                    int digits = colRet.getInt("DECIMAL_DIGITS");  
                    int nullable = colRet.getInt("NULLABLE");  
                    // System.out.println(columnName + " " + columnType + " "+  
                    // datasize + " " + digits + " " + nullable);  
                }  
  
            }  
        }  
        System.out.println();  
  
        // resultSet数据下标从1开始 ResultSet tableRet =  
        //conn.getMetaData().getTables(null, null, "%", new String[] { "TABLE" });  
        //while (tableRet.next()) {  
        //  System.out.print(tableRet.getString(3) + "\t");  
        //}  
        //System.out.println();  
  
    }  

}
