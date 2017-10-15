package Agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * Formulario que se usa para crear un JDialog y en él podamos introducir los datos del contacto a buscar.
 * @author Jose AR
 *
 */

public class JDBuscarContacto extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;
	private JTextField tfEmail;
	private JTextField tfTelefono;
	private JTextField tfApellidos;
	private JTextField tfNombre;
	private JDialog dialog;

	public JDBuscarContacto(JFrame parent, Agenda agenda)
	{
		contentPanel = new JPanel();
		dialog = this;
		
		parent.setEnabled(false);
		setAlwaysOnTop(true);
		setTitle(parent.getTitle());
		
		setBounds(100, 100, 265, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setToolTipText("<html>\r\n\tDebe contener el nombre del contacto, m\u00E1ximo <b style=\"color: red\">40</b> caracteres.\r\n</html>");
		lblNombre.setBounds(10, 14, 72, 14);
		contentPanel.add(lblNombre);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(92, 67, 150, 20);
		contentPanel.add(tfEmail);
		
		tfTelefono = new JTextField();
		tfTelefono.setColumns(10);
		tfTelefono.setBounds(92, 95, 150, 20);
		contentPanel.add(tfTelefono);
		
		tfApellidos = new JTextField();
		tfApellidos.setColumns(10);
		tfApellidos.setBounds(92, 39, 150, 20);
		contentPanel.add(tfApellidos);
		
		tfNombre = new JTextField();
		tfNombre.setColumns(10);
		tfNombre.setBounds(92, 11, 150, 20);
		contentPanel.add(tfNombre);
		
		JLabel lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setToolTipText("<html>\r\n\tDebe contener los apellidos del contacto, m\u00E1ximo <b style=\"color: red\">100</b> caracteres.\r\n</html>");
		lblApellidos.setBounds(10, 42, 72, 14);
		contentPanel.add(lblApellidos);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setToolTipText("<html>\r\n\tDebe contener el correo electr\u00F3nico del contacto, m\u00E1ximo <b style=\"color: red\">60</b> caracteres.\r\n</html>");
		lblEmail.setBounds(10, 70, 72, 14);
		contentPanel.add(lblEmail);
		
		JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setToolTipText("<html>\r\n\tDebe contener el n\u00FAmero de tel\u00E9fono del contacto, maximo <b style=\"color: red\">13</b> caracteres.<br />\r\n\tFormato: <span color=\"orange\">0034</span>600000000 (la parte naranja equivale al c\u00F3digo del pa\u00EDs).\r\n</html>");
		lblTelefono.setBounds(10, 98, 72, 14);
		contentPanel.add(lblTelefono);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					int telefono = -1;
					if (!tfTelefono.getText().isEmpty())
						telefono = Integer.valueOf(tfTelefono.getText());
					
					new JDBuscarContactoTabla(dialog, agenda, tfNombre.getText(), tfApellidos.getText(), tfEmail.getText(), telefono);
				}
				catch (NumberFormatException ex)
				{
					MensajesAlerta.showWarning("El número de teléfono no es válido");
				}
			}
		});
		buttonPane.add(btnBuscar);
		getRootPane().setDefaultButton(btnBuscar);
		
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
}
