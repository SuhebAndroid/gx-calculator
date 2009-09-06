package gx.calc;

public class PluginsDialog extends FormDialog {
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel pluginsPanel; 
	
	
	public PluginsDialog (javax.swing.JFrame parent) {
		super(parent, "Plugins", false, "Close");

		javax.swing.JLabel note = new javax.swing.JLabel("Restart is required fro changes to take effect.", javax.swing.JLabel.CENTER);
		note.setForeground(java.awt.Color.RED);
		note.setFont(new java.awt.Font(note.getFont().getName(), note.getFont().getStyle(), 16));
		getContentPane().add(note, java.awt.BorderLayout.NORTH);

		pluginsPanel = new javax.swing.JPanel(); 
		
		final javax.swing.JButton add = new javax.swing.JButton("Add");
		final javax.swing.JDialog d = this;
		add.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new PluginForm(d, null);
			}});
		addButton(add);
		getContentPane().add(pluginsPanel, java.awt.BorderLayout.CENTER);
		buildRowsPane();
		setVisible(true);
	}
	
	public void buildRowsPane() {
		
		javax.swing.JPanel p = new javax.swing.JPanel(new javax.swing.SpringLayout()); 
		javax.swing.JScrollPane sc = new javax.swing.JScrollPane(p);
		 
		p.add(new javax.swing.JLabel("File"));
		p.add(new javax.swing.JLabel("Class"));
		p.add(new javax.swing.JLabel("Label"));
		p.add(new javax.swing.JLabel("Description"));
		p.add(new javax.swing.JLabel("Key"));
		p.add(new javax.swing.JLabel("Enabled"));
		p.add(new javax.swing.JLabel(""));
		p.add(new javax.swing.JLabel(""));

		for(CalculatorPluginDescriptor cp : Configuration.getPlugins()) 
			addRow(p, cp);

		SpringUtilities.makeCompactGrid(p, Configuration.getPlugins().size() + 1, 8, 2, 2, 2, 2);
		pluginsPanel.removeAll();
		pluginsPanel.add(sc);
		pack();
	}
	
	private void addRow(javax.swing.JPanel p, final CalculatorPluginDescriptor c) {
		String[] a = c.toString().split("\t");

		for(int i = 0; i < 5; i++) {
			javax.swing.JTextField f = new javax.swing.JTextField(a[i]);
			f.setEditable(false);
			if(!c.loaded)
				f.setForeground(java.awt.Color.RED);
			p.add(f);
		}
		
		javax.swing.JCheckBox enabled = new javax.swing.JCheckBox();
		enabled.setSelected(Boolean.parseBoolean(a[5]));
		enabled.addItemListener( new java.awt.event.ItemListener() {
			        public void itemStateChanged(java.awt.event.ItemEvent e) {
			            c.enabled = (e.getStateChange() == java.awt.event.ItemEvent.SELECTED);
			        } } );

		p.add(enabled);
		
		javax.swing.JButton edit = new javax.swing.JButton("Edit");
		final PluginsDialog d = this;
		edit.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				new PluginForm(d, c);
			}});
		p.add(edit);
		
		javax.swing.JButton delete = new javax.swing.JButton("Delete");
		delete.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(javax.swing.JOptionPane.showConfirmDialog(new javax.swing.JFrame(),
				        "Delete pugin " + c.descriptionValue + " ?", "Are you sure?",
				        javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION)
					Configuration.getPlugins().remove(c);
				buildRowsPane();
			}});
		p.add(delete);
	}

}
