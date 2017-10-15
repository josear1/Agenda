package Agenda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import DBManager.DBManager;
import Validaciones.Validaciones;

/**
 * 
 * Clase que sirve para gestionar todo el funcionamiento de la agenda.
 * @author Jose AR
 *
 */

public class Agenda
{
	private final String DB_HOST 					= "127.0.0.1";
	private final int DB_PORT 						= 3306;
	private final String DB_USER 					= "dam2";
	private final String DB_PASS 					= "dam2";
	private final String DB_NAME 					= "agenda";
	private final String TABLE_CONTACTS 			= "contactos";
	private final int MYSQL_ERROR_DB_NOT_EXISTS 	= 1049;
	
	private DBManager dbMgr 						= null;
	
	private ArrayList<Contacto> contactos;
	
	/**
	 * Constructor por defecto para nuestra agenda.
	 * @throws Exception si no se puede crear correctamente la agenda
	 */
	public Agenda() throws Exception
	{
		inicializarAgenda();
	}
	
	/**
	 * Método para inicializar todo lo relacionado con la agenda (llamada a métodos de carga de contactos, etc).
	 * @throws Exception genérico para todas las excepciones si hay algun error
	 */
	private void inicializarAgenda() throws Exception
	{
		contactos = new ArrayList<Contacto>();
		
		try
		{
			dbMgr = new DBManager(DB_HOST, DB_PORT, DB_USER, DB_PASS, "");
		}
		catch (ClassNotFoundException e)
		{
			throw new Exception("No se encuentra el driver de MySQL");
		}
		catch (SQLException e)
		{
			switch (e.getErrorCode())
			{
				case 0:
					throw new Exception("No se puede conectar con el servidor MySQL (comprobar credenciales o si el servidor esta activo).");
				default:
					throw new Exception("Error MySQL #" + e.getErrorCode() + ": " + e.getMessage());
			}
		}
		catch (Exception e)
		{
			throw new Exception("Ha ocurrido un error inesperado: " + e.getMessage());
		}
		
		try
		{
			dbMgr.selectDb(DB_NAME);
		}
		catch (SQLException e)
		{
			if (e.getErrorCode() == MYSQL_ERROR_DB_NOT_EXISTS)
			{
				try
				{
					crearDb();
				}
				catch (SQLException e1)
				{
					throw new Exception("Error al crear la base de datos: " + e1.getMessage());
				}
			}
		}
		
		try
		{
			cargarContactosDb();
		}
		catch (SQLException e)
		{
			throw new Exception("No se pueden cargar los contactos de la base de datos, error: " + e.getMessage());
		}
	}
	
	/**
	 * Método que nos crea la base de datos y la tabla que son necesarios para el uso de nuestra agenda.
	 * @throws SQLException si no se puede crear la base de datos, seleccionarla o crear la tabla necesaria para operar con la agenda
	 */
	private void crearDb() throws SQLException
	{
		dbMgr.executeUpdate("CREATE DATABASE `" + DB_NAME + "`;");
		dbMgr.selectDb(DB_NAME);
		dbMgr.executeUpdate("CREATE TABLE `" + TABLE_CONTACTS + "` (" + 
							  "`id` int(5) NOT NULL AUTO_INCREMENT," + 
							  "`nombre` varchar(40) NOT NULL," + 
							  "`apellidos` varchar(100) NULL," + 
							  "`email` varchar(60) NULL," + 
							  "`numero` int(9) NULL," + 
							  "PRIMARY KEY (`id`)" + 
							  ") DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci;");
	}
	
	/**
	 * Método que nos carga los contactos desde la base de datos.
	 * @throws SQLException si la consulta del select no es satisfactoria
	 */
	private void cargarContactosDb() throws SQLException
	{
		HashMap<ResultSet, Statement> hashmap = dbMgr.executeQuery("SELECT id, nombre, apellidos, email, numero FROM " + TABLE_CONTACTS);
		ResultSet rs = hashmap.keySet().iterator().next();
		while (rs.next())
			contactos.add(new Contacto(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellidos"), rs.getString("email"), rs.getInt("numero")));
		
		dbMgr.closeQuery(hashmap);
	}
	
	/**
	 * Método que nos guarda en la base de datos los contactos.
	 * @return true si la lista de contactos esta vacía o si se han insertado los datos en la tabla correspondiente
	 */
	public boolean guardarDb()
	{
		if (contactos.isEmpty())
			return true;
		
		try
		{
			dbMgr.executeUpdate("TRUNCATE TABLE " + TABLE_CONTACTS);
		}
		catch (SQLException e) { }
		
		String queries = "INSERT INTO `" + TABLE_CONTACTS + "` (`id`, `nombre`, `apellidos`, `email`, `numero`) VALUES ";
		for (int i = 0; i < contactos.size(); i++)
		{
			Contacto c = contactos.get(i);
			queries += "(" + (c.getId() >= 0 ? c.getId() : 0) + ", '" + c.getNombre() + "', '" + c.getApellidos() + "', ";
			
			if (c.getEmail().isEmpty())
				queries += "NULL, ";
			else
				queries += "'" + c.getEmail() + "', ";
			
			if (c.getNumero() == 0)
				queries += "0)";
			else
				queries += c.getNumero() + ")";
			
			queries += (i == contactos.size() - 1 ? ";" : ",");
		}
		
		try
		{
			dbMgr.executeUpdate(queries);
			return true;
		}
		catch (SQLException e) { e.printStackTrace(); }
		
		return false;
	}
	
	/**
	 * Método que nos añade un contacto a la agenda. Si todo es correcto nos devolverá EAgenda.ADDED_CONTACT.
	 * @param nombre el nombre del contacto
	 * @param apellidos los apellidos del contacto
	 * @param email el correo electrónico del contacto
	 * @param numero el número de teléfono del contacto
	 * @return el enum correspondiente
	 * @throws SQLException si no se puede insertar el contacto en la base de datos
	 * @see EAgenda
	 */
	public EAgenda anadirContacto(String nombre, String apellidos, String email, int numero) throws SQLException
	{
		if (numeroDuplicado(numero))
			return EAgenda.DUPLICATED;
		
		if (Validaciones.nombre(nombre))
		{
			String query = "INSERT INTO `" + TABLE_CONTACTS + "` (`nombre`, `apellidos`, `email`, `numero`) VALUES ('" + nombre + "', '" + apellidos + "', ";
			
			if (!Validaciones.apellidos(apellidos))
				return EAgenda.WRONG_LAST_NAME;
			
			if (Validaciones.email(email))
				query += "'" + email + "', ";
			else
				query += "NULL, ";
			
			if (Validaciones.telefono(numero))
			{
				query += numero + ");";
				dbMgr.executeUpdate(query);
				contactos.add(new Contacto(nombre, apellidos, email, numero));
				return EAgenda.ADDED_BOTH;
			}
			else
				query += "NULL);";
			
			dbMgr.executeUpdate(query);
			
			contactos.add(new Contacto(nombre, apellidos, email, numero));
			return EAgenda.ADDED_CONTACT;
		}
		
		return EAgenda.NOT_ADDED;
	}
	
	/**
	 * Método que borra un contacto por su id.
	 * @param id el identificador del contacto
	 */
	public void borrarContacto(int id)
	{
		contactos.remove(id);
	}
	
	/**
	 * Método para borrar un contacto de la agenda introduciendo su nombre, apellidos, email y número de teléfono.
	 * @param nombre el nombre del contacto
	 * @param apellidos los apellidos del contacto
	 * @param email el correo eletrónico del contacto
	 * @param numero el número de teléfono del contacto
	 * @return true si se encuentra y borra el contacto
	 */
	public boolean borrarContacto(String nombre, String apellidos, String email, int numero)
	{
		for (Contacto c : getContactos())
		{
			if (c.getNombre().equals(nombre) && c.getApellidos().equals(apellidos) && c.getEmail().equals(email) && c.getNumero() == numero)
			{
				contactos.remove(c);
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que nos devuelve los contactos que hay en la agenda.
	 * @return ArrayList de la clase Contacto
	 */
	public ArrayList<Contacto> getContactos()
	{
		return contactos;
	}
	
	/**
	 * Método que devuelve un array de String el cual será usado en la cabecera de las JTable.
	 * @return array de String
	 */
	public String[] getHeaderTable()
	{ 
		return new String[] { "Nombre", "Apellidos", "Email", "Teléfono" };
	}
	
	/**
	 * Método que nos genera un array bidimensional de Object para ser usado directamente en las JTable.
	 * @return array bidimensional
	 */
	public Object[][] getContactosTable()
	{
		if (contactos.isEmpty())
			return null;
		
		Object[][] data = new Object[contactos.size()][];
		for (int i = 0; i < contactos.size(); i++)
			data[i] = new Object[] { contactos.get(i).getNombre(), contactos.get(i).getApellidos(), contactos.get(i).getEmail(), contactos.get(i).getNumero() };
		
		return data;
	}
	
	/**
	 * Método que comprueba si un número de teléfono está o no duplicado.
	 * @param numero el número de telefono
	 * @return true si el número de teléfono está vacio (equivalente a -1) y si el número ya existe en algún otro contacto existente
	 */
	private boolean numeroDuplicado(int numero)
	{
		if (numero == -1)
			return false;
		
		for (int i = 0; i < contactos.size(); i++)
		{
			if (contactos.get(i).getNumero() == numero)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Método que nos importa los contactos desde un archivo .csv que estará en el escritorio con el nombre importarAgenda.csv
	 * @return array bidimensional donde la posicion 0 es la cantidad de contactos añadidos a la agenda y 1 es la cantidad total de los contactos del archivo
	 */
	public int[] importarContactos()
	{
		String filename = System.getProperty("user.home") + "\\Desktop\\importarAgenda.csv";
		File f = new File(filename);
		if (!f.exists())
			return new int[] { -1, -1 };
		
		try
		{
			int totales = 0;
			int anadidos = 0;
			String line;
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null)
			{
				String[] split = line.split(";");
				String nombre = split[0];
				String apellidos = split[1];
				String email = split[2];
				int numero = Integer.parseInt(split[3]);
				
				if (Validaciones.nombre(nombre) && Validaciones.apellidos(apellidos) && Validaciones.email(email) && Validaciones.telefono(numero) && !numeroDuplicado(numero))
				{
					anadirContacto(nombre, apellidos, email, numero);
					anadidos++;
				}
				
				totales++;
			}
			
			br.close();
			return new int[] { anadidos, totales };
		}
		catch (Exception e) { }
		
		return new int[] { -1, -1 };
	}
	
	/**
	 * Método que nos exporta los contactos a un archivo .csv que se generará en el escritorio con el nombre de agenda.csv
	 * @return true si se puede crear y escribir correctamente el archivo
	 */
	public boolean exportarContactos()
	{
		try
		{
			String filename = System.getProperty("user.home") + "\\Desktop\\agenda.csv";
			File f = new File(filename);
			if (f.exists())
				return false;
			
			FileWriter fw = new FileWriter(filename, true);
			for (Contacto c : getContactos())
				fw.write(c.getNombre() + ";" + c.getApellidos() + ";" + c.getEmail() + ";" + c.getNumero() + "\n");
			
			fw.close();
			return true;
		}
		catch (Exception e) { }
		
		return false;
	}
}
