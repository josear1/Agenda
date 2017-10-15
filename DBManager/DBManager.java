package DBManager;

/**
 * 
 * Esta clase nos permitirá manejarnos por el servidor y bases de datos MySQL con las que vamos a trabajar.
 * @author Jose AR
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBManager
{
	private final static int DEFAULT_PORT = 3306; // esta variable almacenará el puerto por defecto del servidor MySQL
	
	private Connection connection = null; // en esta variable almacenaremos la conexión al servidor 
	
	/**
	 * Esta clase sirve para conectarnos a un SGBD de MySQL y poder operar con el servidor.
	 * @param host dirección del SGBD
	 * @param port puerto de escucha del SGBD
	 * @param user usuario del SGBD
	 * @param pass contraseña del SGBD
	 * @param dbName nombre de la base de datos a usar
	 * @throws ClassNotFoundException si no se encuentra el driver JDBC
	 * @throws SQLException si no se puede conectar al servidor
	 * @throws IllegalAccessException si no se puede acceder a un método de la clase instanciada JDBC
	 * @throws InstantiationException si no se puede instanciar la clase del driver JDBC
	 */
	public DBManager(String host, int port, String user, String pass, String dbName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		// Comprobamos que existe el driver de jdbc para conectarnos al servidor MySQL, si no se encuentra, damos ClassNotFoundException
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (ClassNotFoundException e)
		{
			throw e;
		}
		
		// Intentamos hacer la conexión al servidor MySQL y en caso de no conseguirlo dar una excepción SQLException
		try
		{
			String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";
			connection = DriverManager.getConnection(url, user, pass);
		}
		catch (SQLException e)
		{
			throw e;
		}
	}
	
	/**
	 * Otro constructor para la clase que nos conectará al SGBD de MySQL por si no pasamos el puerto, en caso de no pasarlo, 
	 * 		se tomará por defecto el 3306 (almacenado en DEFAULT_PORT).
	 * @param host dirección del SGBD
	 * @param user usuario del SGBD
	 * @param pass contraseña del SGBD
	 * @param dbName nombre de la base de datos a usar
	 * @throws ClassNotFoundException si no se encuentra el driver JDBC
	 * @throws SQLException si no se puede conectar al servidor
	 * @throws IllegalAccessException si no se puede acceder a un método de la clase instanciada JDBC
	 * @throws InstantiationException si no se puede instanciar la clase del driver JDBC
	 */
	public DBManager(String host, String user, String pass, String dbName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException
	{
		this(host, DEFAULT_PORT, user, pass, dbName);
	}
	
	/**
	 * Método que nos permite habilitar/deshabilitar el autocommit.
	 * @param enable true si se quiere activar
	 * @throws SQLException si no se puede ejecutar el setAutoCommit
	 */
	public void setAutoCommit(boolean enable) throws SQLException
	{
		connection.setAutoCommit(enable);
	}
	
	/**
	 * Método que nos permite elegir con que base de datos vamos a trabajar.
	 * @param name nombre de la base de datos
	 * @throws SQLException si no se puede seleccionar dicha base de datos
	 */
	public void selectDb(String name) throws SQLException
	{
		connection.setCatalog(name);
	}
	
	/**
	 * Método que hace commit de las operaciones realizadas.
	 * @throws SQLException si no se puede ejecutar la operación commit()
	 * @throws AutoCommitDisabledException si getAutoCommit() devuelve false
	 */
	public void commit() throws SQLException, AutoCommitDisabledException
	{
		if (connection.getAutoCommit())
			connection.commit();
		else
			throw new AutoCommitDisabledException();
	}
	
	/**
	 * Método que hace rollback de las operaciones realizadas.
	 * @throws SQLException si no se puede ejecutar la operación rollback()
	 * @throws AutoCommitDisabledException si getAutoCommit() devuelve false
	 */
	public void rollback() throws SQLException, AutoCommitDisabledException
	{
		if (connection.getAutoCommit())
			connection.rollback();
		else
			throw new AutoCommitDisabledException();
	}
	
	/**
	 * Método que sirve para hacer una consulta de tipo SELECT.
	 * @param query consulta
	 * @return resultado de la consulta
	 * @throws SQLException si no se puede ejecutar la consulta
	 */
	public HashMap<ResultSet, Statement> executeQuery(String query) throws SQLException
	{
		HashMap<ResultSet, Statement> hashmap = new HashMap<ResultSet, Statement>();
		Statement statement = connection.createStatement();
		ResultSet rs = null;
		if (statement != null)
			rs = statement.executeQuery(query);
		
		hashmap.put(rs, statement);
		return hashmap;
	}
	
	/**
	 * Método que sirve para cerrar un Statement y ResultSet de una select.
	 * @param hashmap key=ResultSet y value=Statement
	 * @throws SQLException si no se puede cerrar el ResultSet o no se puede cerrar el Statement
	 */
	public void closeQuery(HashMap<ResultSet, Statement> hashmap) throws SQLException
	{
		ResultSet rs = hashmap.keySet().iterator().next();
		if (rs != null)
			rs.close();
		
		Statement statement = hashmap.values().iterator().next();
		if (statement != null)
			statement.close();
	}
	
	/**
	 * Método que sirve para ejecutar cualquier consulta de tipo CRUD.
	 * @param query consulta
	 * @return número de filas afectadas
	 * @throws SQLException si no se puede ejecutar la consulta
	 */
	public int executeUpdate(String query) throws SQLException
	{
		Statement statement = connection.createStatement();
		int count = 0;
		if (statement != null)
		{
			count = statement.executeUpdate(query);
			statement.close();
		}
		
		return count;
	}
	
	/**
	 * Método que nos devuelve la conexión previamente abierta al servidor SGBD.
	 * @return la conexión con el SGBG
	 */
	public Connection getConnection()
	{
		return connection;
	}
	
	/**
	 * Método que cierra la conexión previamente abierta al servidor SGBD.
	 * @throws SQLException si no se puede cerrar la conexión con el SGBG
	 */
	public void closeConnection() throws SQLException
	{
		if (connection != null)
			connection.close();
	}
	
	/**
	 * Método que nos devuelve un array de dos dimensiones de ArrayList&lt;String&gt; con las filas de la tabla (solo para debug).
	 * @param rs ResultSet de una consulta
	 * @return ArrayList&lt;ArrayList&lt;String&gt;&gt; filas de la tabla
	 * @throws SQLException si no se pueden ejecutar los métodos necesarios
	 */
	public ArrayList<ArrayList<String>> printQuery(ResultSet rs) throws SQLException
	{
		ArrayList<String> row = new ArrayList<String>();
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		int count = 0;
		while (rs.next())
		{
		    for (int i = 1; i <= columns; i++)
		    	row.set(count, rs.getString(i));
		    
		    table.set(count++, row);
		    row.clear();
		}

		return table;
	}
}
