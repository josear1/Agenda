package Agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 
 * Formulario que se usa para introducir los datos de un contacto y éste se añada a la lista de contactos.
 * @author Jose AR
 *
 */

public class JDAnadirContacto extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfNombre;
	private JTextField tfApellidos;
	private JTextField tfEmail;
	private JTextField tfTelefono;
	private JDialog dialog;
	
	public JDAnadirContacto(JFrame parent, Agenda agenda)
	{
		dialog = this;
		
		parent.setEnabled(false);
		setAlwaysOnTop(true);
		setTitle(parent.getTitle());
		
		setBounds(100, 100, 275, 208);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setToolTipText("<html>\r\n\tDebe contener el nombre del contacto, m\u00E1ximo <b style=\"color: red\">40</b> caracteres.\r\n</html>");
		lblNombre.setBounds(15, 24, 72, 14);
		contentPanel.add(lblNombre);
		
		tfNombre = new JTextField();
		lblNombre.setLabelFor(tfNombre);
		tfNombre.setBounds(97, 21, 150, 20);
		contentPanel.add(tfNombre);
		tfNombre.setColumns(10);
		
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setToolTipText("<html>\r\n\tDebe contener los apellidos del contacto, m\u00E1ximo <b style=\"color: red\">100</b> caracteres.\r\n</html>");
		lblApellidos.setBounds(15, 52, 72, 14);
		contentPanel.add(lblApellidos);
		
		tfApellidos = new JTextField();
		lblApellidos.setLabelFor(tfApellidos);
		tfApellidos.setColumns(10);
		tfApellidos.setBounds(97, 49, 150, 20);
		contentPanel.add(tfApellidos);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setToolTipText("<html>\r\n\tDebe contener el correo electr\u00F3nico del contacto, m\u00E1ximo <b style=\"color: red\">60</b> caracteres.\r\n</html>");
		lblEmail.setBounds(15, 80, 72, 14);
		contentPanel.add(lblEmail);
		
		tfEmail = new JTextField();
		lblEmail.setLabelFor(tfEmail);
		tfEmail.setColumns(10);
		tfEmail.setBounds(97, 77, 150, 20);
		contentPanel.add(tfEmail);
		
		JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setToolTipText("<html>\r\n\tDebe contener el n\u00FAmero de tel\u00E9fono del contacto, maximo <b style=\"color: red\">9</b> caracteres.<br />\r\n\tFormato: 600000000.\r\n</html>");
		lblTelefono.setBounds(15, 108, 72, 14);
		contentPanel.add(lblTelefono);
		
		tfTelefono = new JTextField();
		lblTelefono.setLabelFor(tfTelefono);
		tfTelefono.setColumns(10);
		tfTelefono.setBounds(97, 105, 150, 20);
		contentPanel.add(tfTelefono);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnAnadir = new JButton("A\u00F1adir");
		btnAnadir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					int telefono = -1;
					
					if (!tfTelefono.getText().isEmpty())
					{
						try
						{
							telefono = Integer.valueOf(tfTelefono.getText());
						}
						catch (NumberFormatException e1)
						{
							MensajesAlerta.showWarning("El número de teléfono introducido no es válido.");
							return;
						}
					}
					
					try
					{
						switch (agenda.anadirContacto(tfNombre.getText(), tfApellidos.getText(), tfEmail.getText(), telefono))
						{
							case ADDED_BOTH:
								limpiarCampos();
								MensajesAlerta.okMessage("El contacto y sus datos se han añadido correctamente.");
								break;
							case ADDED_CONTACT:
								limpiarCampos();
								MensajesAlerta.okMessage("El contacto se ha añadido correctamente.");
								break;
							case DUPLICATED:
								MensajesAlerta.showWarning("El número de teléfono \"" + tfTelefono.getText() + "\" ya existe en la agenda.");
								break;
							case WRONG_LAST_NAME:
								MensajesAlerta.showWarning("Los apellidos no son válidos.");
								break;
							case NOT_ADDED:
								MensajesAlerta.showWarning("El contacto no se ha añadido, rellena los campos.");
								break;
							default:
								break;
						}
					}
					catch (NullPointerException ex)
					{
						MensajesAlerta.showWarning("Se debe rellenar algún campo.");
						return;
					}
				}
				catch (SQLException e1)
				{
					MensajesAlerta.showWarning("Error MySQL #" + e1.getErrorCode() + ": " + e1.getMessage());
				}
			}
		});
		buttonPane.add(btnAnadir);
		getRootPane().setDefaultButton(btnAnadir);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				parent.setEnabled(true);
				dialog.dispose();
			}
		});
		buttonPane.add(btnCancelar);
		
		this.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				parent.setEnabled(true);
			}
		});
		
		setVisible(true);
	}
	
	/**
	 * Método que limpia los campos una vez que añadimos un contacto correctamente y pone en focus el campo del nombre.
	 */
	private void limpiarCampos()
	{
		tfNombre.setText("");
		tfApellidos.setText("");
		tfEmail.setText("");
		tfTelefono.setText("");
		tfNombre.requestFocus();
	}
}

