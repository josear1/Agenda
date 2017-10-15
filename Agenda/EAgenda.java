package Agenda;

/**
 * 
 * Contiene constantes para el m�todo anadirContacto(...) de la clase Agenda.
 * @author Jose AR
 *
 */
public enum EAgenda
{
	/**
	 * Si no se a�ade, error gen�rico
	 */
	NOT_ADDED,
	
	/**
	 * Si el n�mero de tel�fono ya existe
	 */
	DUPLICATED,
	
	/**
	 * Si los apellidos son incorrectos
	 */
	WRONG_LAST_NAME,
	
	/**
	 * Si se a�ade un contacto sin el n�mero de tel�fono
	 */
	ADDED_CONTACT,
	
	/**
	 * Si se a�ade un contacto con todos sus campos
	 */
	ADDED_BOTH
}
