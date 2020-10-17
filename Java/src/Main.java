// Almaraz Fabricio, Pacione Luciano

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class Main extends JFrame{

	private MainPanel contentPane;
	private JTextField tFieldUser;
	private JLabel lblUser, lblPassword;
	private JPasswordField passwordField;
	private JButton btnLogin, btnCancel;
	private DataBaseConnection connection;
	private JMenuItem mntmLogout;
	/private static JFrame INSTANCE;
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
