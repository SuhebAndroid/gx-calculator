package gx.calc;

public class PluginsDialog extends FormDialog {
	private static final long serialVersionUID = 1L;
	private javax.swing.JTabbedPane tabs;
	private javax.swing.JTable expressions, plugins;
	private javax.swing.JButton[] rowActions; 
	private ParsedCalculator caller;
	
	public PluginsDialog (javax.swing.JFrame parent) {
		this(parent, null);
	}
	
	public PluginsDialog (javax.swing.JFrame parent, CalculatorKeyDescriptor p) {
		super(parent, "Plugins", false, "Close");

		caller = (ParsedCalculator)parent;
		tabs = new javax.swing.JTabbedPane();
		tabs.addChangeListener(new javax.swing.event.ChangeListener() {
			@Override
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Expressions" && expressions != null && rowActions != null) {
					for(int i = 0; i < rowActions.length; i ++)
						if(rowActions[i] != null)
							rowActions[i].setEnabled(expressions.getSelectedRow() > -1);
				}
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Plugins" && plugins != null && rowActions != null) {
					for(int i = 0; i < rowActions.length; i ++)
							if(rowActions[i] != null)
								rowActions[i].setEnabled(plugins.getSelectedRow() > -1);
				}
			}
		});
		
		plugins = buildTable();
		expressions = buildTable();
/*
		plugins = new javax.swing.JTable();
		expressions = new javax.swing.JTable();
		expressions.setRowSelectionAllowed(true);
		plugins.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		expressions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		plugins.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (e.getValueIsAdjusting() || rowActions == null) 
					return;
				for(int i = 0; i < rowActions.length; i ++)
					if(rowActions[i] != null)
						rowActions[i].setEnabled(plugins.getSelectedRow() > -1);
			}
		});
 
		expressions.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (e.getValueIsAdjusting() || rowActions == null) 
	                return;
				for(int i = 0; i < rowActions.length; i ++)
					if(rowActions[i] != null)
						rowActions[i].setEnabled(expressions.getSelectedRow() > -1);
			}
		});
 */
		
		loadTables();
		javax.swing.JPanel pluginsPanel = buildTablePanel(plugins); 
		javax.swing.JPanel expressionPanel = buildTablePanel(expressions);
		
		tabs.add("Expressions", expressionPanel);
		tabs.add("Plugins", pluginsPanel);
		tabs.setEnabledAt(1, false);

		javax.swing.JLabel pnote = new javax.swing.JLabel("Restart is required for changes to take effect.", javax.swing.JLabel.CENTER);
		pnote.setForeground(java.awt.Color.RED);
		pnote.setFont(new java.awt.Font(pnote.getFont().getName(), pnote.getFont().getStyle(), 16));
		pluginsPanel.add(pnote, java.awt.BorderLayout.NORTH);
		
		javax.swing.JLabel enote = new javax.swing.JLabel("Expression may use braces and operators '(/*+-)' and 'x' to indicate the current value e.g. '1+(x*0)-1'", javax.swing.JLabel.CENTER);
		expressionPanel.add(enote, java.awt.BorderLayout.NORTH);

		final PluginsDialog d = this;

		javax.swing.JButton add = new javax.swing.JButton("Add");
		add.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Expressions")
					new ExpressionForm(d, null);
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Plugins")
					new PluginForm(d, null);
			}});
		
		addButton(add);
		
		rowActions = new javax.swing.JButton[3];
		rowActions[0] = createRowActionButton("Edit");
		rowActions[1] = createRowActionButton("Delete");
		rowActions[2] = createRowActionButton("Enable/Disable");
		addButton(rowActions[0]);
		addButton(rowActions[1]);
		addButton(rowActions[2]);

		getContentPane().add(tabs, java.awt.BorderLayout.CENTER);
		pack();
		setVisible(true);
		if(p != null) {
			if(p.type == CalculatorKeyDescriptor.TYPE.INTERFACE)
				new PluginForm(d, p);
			if(p.type == CalculatorKeyDescriptor.TYPE.EXPRESSION)
				new ExpressionForm(d, p);			
		}
		
	}
	
	private javax.swing.JPanel buildTablePanel(javax.swing.JTable table) { 
		javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.BorderLayout());
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(table);
		table.setFillsViewportHeight(true);
		panel.add(scrollPane, java.awt.BorderLayout.CENTER);
		
		return panel;
	}

	@SuppressWarnings("unchecked")
	private Object[][] getRows(java.util.ArrayList keys) {
		Object[][] data = new Object[keys.size()][];
		for(int i = 0; i < keys.size(); i++) 
			data[i] = ((CalculatorKeyDescriptor)keys.get(i)).toArray(); 
		return data;
	}
	
	private javax.swing.JButton createRowActionButton(String title) {
		javax.swing.JButton b = new javax.swing.JButton(title);
		b.setEnabled(false);
		final PluginsDialog d = this;
		b.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				javax.swing.JTable t = null;
				CalculatorKeyDescriptor.TYPE type = CalculatorKeyDescriptor.TYPE.EXPRESSION;
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Expressions"){
					t = expressions;
					type = CalculatorKeyDescriptor.TYPE.EXPRESSION;
				}
				if(tabs.getTitleAt(tabs.getSelectedIndex()) == "Plugins") {
					t = plugins;
					type = CalculatorKeyDescriptor.TYPE.INTERFACE;
				}
				if(t != null) {
					int selectedRow = -1;
					selectedRow = t.getSelectedRow();
					if(selectedRow > -1) {
						CalculatorKeyDescriptor key = type == CalculatorKeyDescriptor.TYPE.EXPRESSION ?
							key = Configuration.getExpressions().get(selectedRow) :
								type == CalculatorKeyDescriptor.TYPE.INTERFACE ?
									Configuration.getPlugins().get(selectedRow) : null;
						if(e.getSource() == rowActions[2])  // enable/disable
							key.enabled = !key.enabled;
						if(e.getSource() == rowActions[0]) { // edit
							if(type == CalculatorKeyDescriptor.TYPE.EXPRESSION)
								new ExpressionForm(d, key);
							if(type == CalculatorKeyDescriptor.TYPE.INTERFACE)
								new PluginForm(d, key);
						}
						if(e.getSource() == rowActions[1]) { // delete
							if(javax.swing.JOptionPane.showConfirmDialog(null, "Are you sure?", "Delete", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION){
								if(type == CalculatorKeyDescriptor.TYPE.INTERFACE) {
									Configuration.getPlugins().remove(selectedRow);
									plugins.clearSelection();
								}
								if(type == CalculatorKeyDescriptor.TYPE.EXPRESSION) {
									Configuration.getExpressions().remove(selectedRow);
									expressions.clearSelection();
								}
							}
						}
						loadTables();
					}
				}
				
			}});
		
		return b;
	}
	
	public javax.swing.JTable buildTable() {
		final javax.swing.JTable t = new javax.swing.JTable();
		t.setRowSelectionAllowed(true);
		t.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		t.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (e.getValueIsAdjusting() || rowActions == null) 
	                return;
				for(int i = 0; i < rowActions.length; i ++)
					if(rowActions[i] != null)
						rowActions[i].setEnabled(t.getSelectedRow() > -1);
			}

		});
		
		return t;
	}
	
	public void loadTables() {
		String[] pcols = {"Class", "File", "Description", "Display", "Key", "Enabled"};
		String[] ecols = {"Expression", "Description", "Display", "Key", "Enabled"};
		javax.swing.table.DefaultTableModel pmodel = new javax.swing.table.DefaultTableModel(getRows(Configuration.getPlugins()),pcols){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;   
			}
		};
		
		javax.swing.table.DefaultTableModel emodel = new javax.swing.table.DefaultTableModel(getRows(Configuration.getExpressions()),ecols){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;   
			}
		};
		int psind = plugins.getSelectedRow();
		int esind = expressions.getSelectedRow();
		plugins.setModel(pmodel);
		expressions.setModel(emodel);
		if(psind >= 0)
			plugins.setRowSelectionInterval(psind, psind);
		if(esind >= 0)
			expressions.setRowSelectionInterval(esind, esind);
		Configuration.store(); // in case of action performed
		caller.loadExtensionKeys();
	}
}
