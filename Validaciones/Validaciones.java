package Validaciones;

import java.util.regex.Pattern;

/**
 * 
 * Clase que sirve para validar los distintos campos de una agenda
 * @author Jose AR
 *
 */
public class Validaciones
{
	/**
	 * Metodo que comprueba si un nombre es v�lido, comprueba si esta vacio o si contiene algun caracter como ' , \ o "
	 * @param nombre nombre a comprobar
	 * @return true si es v�lido
	 */
	public static boolean nombre(String nombre)
	{
		if (nombre.isEmpty() || Pattern.compile("[',\\\\\"]", Pattern.CASE_INSENSITIVE).matcher(nombre).find())
			return false;
		
		return true;
	}
	
	/**
	 * Metodo que comprueba si los apellidos son v�lido, comprueba si esta vacio, si contiene espacios o si contiene algun caracter como ' , \ o "
	 * @param apellidos apellidos a comprobar
	 * @return true si son v�lidos
	 */
	public static boolean apellidos(String apellidos)
	{
		if (apellidos.isEmpty() || apellidos.contains(" ") || Pattern.compile("[',\\\\\"]", Pattern.CASE_INSENSITIVE).matcher(apellidos).find())
			return false;
		
		return true;
	}
	
	/**
	 * Comprueba si un n�mero de tel�fono es v�lido, comprueba si un numero es positivo y si tiene 9 caracteres
	 * @param numero n�mero de tel�fono a comprobar
	 * @return si es v�lido
	 */
	public static boolean telefono(int numero)
	{
		if (numero == -1)
			return true;
		
		if (numero <= -1)
			return false;
		
		String temp = String.valueOf(numero);
		if (temp.isEmpty())
			return false;
		
		if (!temp.matches("[679]{1}[0-9]{8}"))
			return false;
		
		return true;
	}
	
	/**
	 * M�todo que comprueba si un email es correcto a traves de una expresi�n regular, requiere varios caracteres alfanum�ricos, 
	 * 	luego una @, de nuevo varios caracteres alfanumericos (corresponden a servidor de correo), despu�s un . 
	 * 	y de 2 a 6 caracteres que corresponden a la extensi�n del dominio
	 * Creditos: https://stackoverflow.com/questions/8204680/java-regex-email/8204716#8204716
	 * @param email correo eletr�nico a comprobar
	 * @return si es v�lido
	 */
	public static boolean email(String email)
	{
		if (!Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email).find())
			return false;
		
		return true;
	}
}
