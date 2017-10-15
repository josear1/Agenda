package Agenda;

/***
 * 
 * Clase que servirá para guardar los datos de un contacto (id de contacto y números de telefono como ints; nombre, apellidos y email como strings).
 * @author Jose AR
 *
 */

public class Contacto
{
	/**
	 * Constructor por defecto, sin parámetros
	 */
	public Contacto()
	{
		this.id = -1;
		this.nombre = "";
		this.apellidos = "";
		this.email = "";
		this.numero = 0;
	}
	
	/**
	 * Constructor por parámetros donde introducimos el id, nombre, apellidos, email y número de telefono
	 * @param id identificador del contacto
	 * @param nombre nombre del contacto
	 * @param apellidos apellidos del contacto
	 * @param email correo electrónico del contacto
	 * @param numero número de teléfono del contacto
	 */
	public Contacto(int id, String nombre, String apellidos, String email, int numero)
	{
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.numero = numero;
	}
	
	/**
	 * Constructor por parámetros donde introducimos el nombre, apellidos, email y número de telefono
	 * @param nombre nombre del contacto
	 * @param apellidos apellidos del contacto
	 * @param email correo electrónico del contacto
	 * @param numero número de teléfono del contacto
	 */
	public Contacto(String nombre, String apellidos, String email, int numero)
	{
		this.id = -1;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.numero = numero;
	}

	// Variables para almacenar los datos
	private int id;
	private String nombre;
	private String apellidos;
	private String email;
	private int numero;
	
	// Getters and setters
	/**
	 * Devuelve el id/identificador del contacto.
	 * @return identificador del contacto
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Establece el id/identificador del contacto.
	 * @param id identificador del contacto
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * Devuelve el nombre del contacto.
	 * @return nombre del contacto
	 */
	public String getNombre()
	{
		return nombre;
	}
	
	/**
	 * Establece el nombre del contacto.
	 * @param nombre nombre del contacto
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	/**
	 * Devuelve los apellidos del contacto.
	 * @return apellidos del contacto
	 */
	public String getApellidos()
	{
		return apellidos;
	}
	
	/**
	 * Establece los apellidos del contacto.
	 * @param apellidos apellidos del contacto
	 */
	public void setApellidos(String apellidos)
	{
		this.apellidos = apellidos;
	}
	
	/**
	 * Devuelve el correo electrónico del contacto.
	 * @return correo eletrónico del contacto
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Establece el correo electrónico del contacto.
	 * @param email correo eletrónico del contacto
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * Devuelve el número de teléfono del contacto.
	 * @return número de teléfono del contacto
	 */
	public int getNumero()
	{
		return numero;
	}
	
	/**
	 * Establece el número de teléfono del contacto.
	 * @param numero número de teléfono del contacto
	 */
	public void setNumero(int numero)
	{
		this.numero = numero;
	}

	/**
	 *  Método que nos servirá para imprimir los datos de un contacto (usar solo para debug)
	 */
	@Override
	public String toString()
	{
		return "[" + id + ", \"" + nombre + "\", \"" + apellidos + "\", \"" + email + "\", " + numero + "]";
	}
}
