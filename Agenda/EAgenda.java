package Agenda;

/**
 * 
 * Contiene constantes para el método anadirContacto(...) de la clase Agenda.
 * @author Jose AR
 *
 */
public enum EAgenda
{
	/**
	 * Si no se añade, error genérico
	 */
	NOT_ADDED,
	
	/**
	 * Si el número de teléfono ya existe
	 */
	DUPLICATED,
	
	/**
	 * Si los apellidos son incorrectos
	 */
	WRONG_LAST_NAME,
	
	/**
	 * Si se añade un contacto sin el número de teléfono
	 */
	ADDED_CONTACT,
	
	/**
	 * Si se añade un contacto con todos sus campos
	 */
	ADDED_BOTH
}
