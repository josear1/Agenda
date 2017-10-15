package Agenda;

import javax.swing.JOptionPane;

/**
 * 
 * Clase que nos permite crear mensajes de alerta, informativos o de error de una manera f�cil y r�pida.
 * @author Jose AR
 *
 */
public class MensajesAlerta extends JOptionPane
{
	private static final long serialVersionUID = 1L;
	private final static String WINDOW_TITLE = "Agenda";
	
	/**
	 * Nos muestra un mensaje de error con un bot�n de Ok y nos cierra la aplicaci�n.
	 * @param mensaje mensaje a mostrar
	 */
	public static void errorFatal(String mensaje)
	{
		JOptionPane.showMessageDialog(null, mensaje, WINDOW_TITLE, JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
	
	/**
	 * Nos muestra un mensaje de advertencia con un bot�n de Ok.
	 * @param mensaje mensaje a mostrar
	 */
	public static void showWarning(String mensaje)
	{
		JOptionPane.showMessageDialog(null, mensaje, WINDOW_TITLE, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Nos muestra un mensaje informativo con un bot�n de Ok.
	 * @param mensaje mensaje a mostrar
	 */
	public static void okMessage(String mensaje)
	{
		JOptionPane.showMessageDialog(null, mensaje, WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Nos muestra una alerta con un mensaje en el cu�l debemos clickear en el boton Si o No.
	 * @param mensaje mensaje a mostrar
	 * @return dependiendo lo que pulse el usuario, bot�n Si: JOptionPane.YES_OPTION (0); bot�n No: JOptionPane.NO_OPTION (1)
	 */
	public static int showConfirmation(String mensaje)
	{
		return JOptionPane.showConfirmDialog(null, mensaje, WINDOW_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
}
