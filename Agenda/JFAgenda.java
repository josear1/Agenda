package Agenda;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import Validaciones.Validaciones;

/**
 * 
 * Frame principal de la agenda, contiene un JTable que muestra los contactos existentes, y los botones de importar/exportar contactos, añadir/buscar contacto
 * @author Jose AR
 *
 */
public class JFAgenda extends JFrame implements WindowListener
{
	private static final long serialVersionUID = 1L;
	
	private final String WINDOW_TITLE = "Agenda";
	private Agenda agenda;
	
	private JPanel contentPane;
	private static JFrame fAgenda;
	private final DefaultTableModel tModel;

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					fAgenda = new JFAgenda();
					fAgenda.setVisible(true);
				}
				catch (Exception e)
				{
					MensajesAlerta.errorFatal("No se puede crear la agenda. Error " + e.getMessage());
				}
			}
		});
	}

	public JFAgenda()
	{
		try
		{
			agenda = new Agenda();
		}
		catch (Exception e)
		{
			MensajesAlerta.errorFatal(e.getMessage());
		}
		
		setTitle(WINDOW_TITLE);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 445);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.NORTH);
		
		JButton btnImportar = new JButton("Importar");
		btnImportar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int[] importados = agenda.importarContactos();
				if (importados[0] > -1 && importados[1] > -1)
					MensajesAlerta.okMessage("Agenda importada correctamente, puede borrar el archivo \"importarAgenda.csv\".\n\nSe han añadido " + importados[0] + " de " + importados[1] + " contactos.");
				else
					MensajesAlerta.showWarning("No se ha podido importar ningún contacto, quizas no exista el archivo \"importarAgenda.csv\" en el escritorio o el formato del mismo es incorrecto.");
			}
		});
		panelNorth.add(btnImportar);
		
		JButton btnExportar = new JButton("Exportar");
		btnExportar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if (agenda.exportarContactos())
					MensajesAlerta.okMessage("Agenda exportada al escritorio con el nombre de \"agenda.csv\".");
				else
					MensajesAlerta.showWarning("La agenda no se ha exportado, comprueba que el archivo \"agenda.csv\" no existe en el escritorio.");
			}
		});
		panelNorth.add(btnExportar);
		
		// https://stackoverflow.com/questions/2561305/how-can-i-set-distance-between-elements-ordered-vertically/2561357#2561357
		panelNorth.add(Box.createHorizontalStrut(12));
		
		JButton btnAnadir = new JButton("A\u00F1adir contacto");
		btnAnadir.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new JDAnadirContacto(fAgenda, agenda);
			}
		});
		panelNorth.add(btnAnadir);
		
		JButton btnBuscar = new JButton("Buscar contacto");
		btnBuscar.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new JDBuscarContacto(fAgenda, agenda);
			}
		});
		panelNorth.add(btnBuscar);
		
		JPanel panelCenter = new JPanel();
		contentPane.add(panelCenter, BorderLayout.CENTER);
		
		tModel = new DefaultTableModel(agenda.getContactosTable(), agenda.getHeaderTable())
		{
			private static final long serialVersionUID = 1L;
			
		};
		tModel.addTableModelListener(new TableModelListener()
		{
			@Override
			public void tableChanged(TableModelEvent e)
			{
				if (e.getType() == TableModelEvent.UPDATE && e.getFirstRow() > -1)
				{
					int row = e.getFirstRow();
					int column = e.getColumn();
					String newValue = tModel.getValueAt(row, column).toString();
					switch (e.getColumn())
					{
						case 0:
							if (Validaciones.nombre(newValue))
								agenda.getContactos().get(row).setNombre(newValue);
							else
							{
								tModel.setValueAt(agenda.getContactos().get(row).getNombre(), row, column);
								MensajesAlerta.showWarning("El nombre no es válido.");
							}
							break;
						case 1:
							if (Validaciones.apellidos(newValue))
								agenda.getContactos().get(row).setApellidos(newValue);
							else
							{
								tModel.setValueAt(agenda.getContactos().get(row).getApellidos(), row, column);
								MensajesAlerta.showWarning("Los apellidos no son válidos.");
							}
							break;
						case 2:
							if (Validaciones.email(newValue))
								agenda.getContactos().get(row).setEmail(newValue);
							else
							{
								tModel.setValueAt(agenda.getContactos().get(row).getEmail(), row, column);
								MensajesAlerta.showWarning("El email no es válido.");
							}
							break;
						case 3:
						{
							int telefono = 0;
							try
							{
								telefono = Integer.parseInt(newValue);
							}
							catch (NumberFormatException ex)
							{
								MensajesAlerta.showWarning("El teléfono no es valido.");
								return;
							}
							
							if (Validaciones.telefono(telefono))
								agenda.getContactos().get(row).setNumero(telefono);
							else
							{
								tModel.setValueAt(String.valueOf(agenda.getContactos().get(row).getNumero()), row, column);
								MensajesAlerta.showWarning("El teléfono no es válido.");
							}
							break;
						}
						default:
							break;
					}
				}
			}
		});
		JTable table = new JTable(tModel);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		panelCenter.setLayout(new BorderLayout());
		panelCenter.add(scrollPane);
		
		addWindowListener(this);
	}
	
	@Override
	public void windowActivated(WindowEvent e)
	{
		tModel.setDataVector(agenda.getContactosTable(), agenda.getHeaderTable());
	}

	@Override
	public void windowClosed(WindowEvent e) { }

	@Override
	public void windowClosing(WindowEvent e)
	{
		if (!agenda.guardarDb())
			MensajesAlerta.errorFatal("Error al guardar la agenda.");
	}

	@Override
	public void windowDeactivated(WindowEvent e) { }

	@Override
	public void windowDeiconified(WindowEvent e) { }

	@Override
	public void windowIconified(WindowEvent e) { }

	@Override
	public void windowOpened(WindowEvent e) { }
}
