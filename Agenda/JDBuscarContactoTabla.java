package Agenda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * Formulario que sirve para crear una JTable dentro de un JDialog y ésta tabla nos muestre la información de los contactos encontrados 
 * 		según los datos introducidos a la vez que también nos permite borrar un contacto.
 * @author Jose AR
 *
 */
public class JDBuscarContactoTabla extends JDialog
{
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel;

	public JDBuscarContactoTabla(JDialog parent, Agenda agenda, String nombre, String apellidos, String email, int telefono)
	{
		contentPanel = new JPanel();
		parent.setEnabled(false);
		setTitle(parent.getTitle());
		
		setResizable(false);
		setBounds(100, 100, 472, 310);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		ArrayList<Contacto> contactos = new ArrayList<Contacto>();
		for (int i = 0; i < agenda.getContactos().size(); i++)
		{
			Contacto c = agenda.getContactos().get(i);
			if (contactos.equals(c))
				continue;
			
			if (!nombre.isEmpty() && c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
			{
				contactos.add(c);
				continue;
			}
			
			if (!apellidos.isEmpty() && c.getApellidos().toLowerCase().contains(apellidos.toLowerCase()))
			{
				contactos.add(c);
				continue;
			}
			
			if (!email.isEmpty() && c.getEmail().toLowerCase().contains(email.toLowerCase()))
			{
				contactos.add(c);
				continue;
			}
			
			if (String.valueOf(c.getNumero()).contains(String.valueOf(telefono)))
			{
				contactos.add(c);
				continue;
			}
		}
		
		if (contactos.isEmpty())
		{
			this.dispose();
			parent.setEnabled(true);
			MensajesAlerta.showWarning("No se ha encontrado ningun contacto.");
		}
		else
		{
			setAlwaysOnTop(true);
			
			Object[][] data = new Object[contactos.size()][];
			for (int i = 0; i < contactos.size(); i++)
			    data[i] = new Object[]{ contactos.get(i).getNombre(), contactos.get(i).getApellidos(), contactos.get(i).getEmail(), contactos.get(i).getNumero(), "<html><font color=\"red\">BORRAR</font></html>" };
			
			String[] columnNames = new String[agenda.getHeaderTable().length + 1];
			for (int i = 0; i < columnNames.length; i++)
				columnNames[i] = (i == columnNames.length - 1) ? "Borrar" : agenda.getHeaderTable()[i];
			
			final DefaultTableModel tModel = new DefaultTableModel(data, columnNames)
			{
				private static final long serialVersionUID = 1L;

				@Override
			    public boolean isCellEditable(int row, int column)
				{
			        return false;
			    }
			};
			JTable table = new JTable(tModel);
			table.addMouseListener(new MouseAdapter()
			{
			    @Override
			    public void mouseClicked(MouseEvent event)
			    {
			        int row = table.rowAtPoint(event.getPoint());
			        int col = table.columnAtPoint(event.getPoint());
			        if (row >= 0 && col == 4)
			        {
			        	Contacto c = contactos.get(row);
			        	if (MensajesAlerta.showConfirmation("Desea borrar a \"" + c.getNombre() + " " + c.getApellidos() + "\" con teléfono: " + c.getNumero()) == JOptionPane.YES_OPTION)
			        	{
			        		if (agenda.borrarContacto(c.getNombre(), c.getApellidos(), c.getEmail(), c.getNumero()))
			        			tModel.removeRow(row);
			        	}
			        }
			    }
			});
			table.setPreferredScrollableViewportSize(new Dimension(450, 248));
			table.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(table);
			contentPanel.add(scrollPane);
			
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
}
