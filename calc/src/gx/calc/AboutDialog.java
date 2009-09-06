package gx.calc;

public class AboutDialog extends javax.swing.JDialog implements java.awt.event.ActionListener {
	private static final long serialVersionUID = 1L;
	javax.swing.JButton jbnOk;
	java.awt.Image img;
	
	public AboutDialog (javax.swing.JFrame parent, String title, boolean modal){
		super(parent, title, modal);

		setLocationRelativeTo(parent);
		img = new javax.swing.ImageIcon(getClass().getResource("/gx/calc/calculator.png")).getImage();

		StringBuffer text = new StringBuffer();
		text.append(Configuration.APPLICATION_NAME);
		text.append("\nDeveloper:	GX");
		text.append("\nVersion:	" + Configuration.VERSION);
		text.append("\nChanges:\n");
		text.append("\n1.2 Plugins 2009/09");
		text.append("\n    plugins are now supported with customizable buttons");
		text.append("\n    states persistant, the toggle state of extra buttons, screen location and text size is kept when closing the application");
		text.append("\n1.1 Sticky buttons 2009/09");
		text.append("\n    operator buttons ('+' '-' '/' '*' etc) toggle buttons and stay pressed until next event");
		text.append("\n    icon added");
		text.append("\n1.0 Initial release 2009/08");
		text.append("\n    first release");
		
		javax.swing.JTextArea jtAreaAbout = new javax.swing.JTextArea(){
			private static final long serialVersionUID = 1L;
		      {setOpaque(false);}  // instance initializer
		      public void paintComponent (java.awt.Graphics g) {
				g.drawImage(img, 150, 5, this);
				super.paintComponent(g);
		      }
		    };

		jtAreaAbout.setText(text.toString());
		jtAreaAbout.setEditable(false);

		javax.swing.JScrollPane sc = new javax.swing.JScrollPane(jtAreaAbout); 

		getContentPane().add(sc, java.awt.BorderLayout.CENTER);

		javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
		jbnOk = new javax.swing.JButton(" OK ");
		jbnOk.addActionListener(this);

		p.add(jbnOk);
		getContentPane().add(p, java.awt.BorderLayout.SOUTH);
		getContentPane().setPreferredSize(new java.awt.Dimension(300, 300));

		addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					java.awt.Window aboutDialog = e.getWindow();
					aboutDialog.dispose();
				}
			}
		);
		pack();
		setVisible(true);
	}

	public void actionPerformed(java.awt.event.ActionEvent e) {
		if(e.getSource() == jbnOk)	
			this.dispose();
	}

}
