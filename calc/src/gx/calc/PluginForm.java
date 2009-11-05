package gx.calc;

public class PluginForm extends FormDialog {
	private static final long serialVersionUID = 1L;
	private PluginsDialog caller;
	private javax.swing.JComponent[] fields;
	private CalculatorPluginDescriptor calculatorPlugin ;

	public PluginForm(PluginsDialog parent, CalculatorKeyDescriptor c){
		super(parent, "Add new plugin", true);
		this.caller = parent;
		this.calculatorPlugin = (CalculatorPluginDescriptor)c;
		javax.swing.JButton save = new javax.swing.JButton("Save");

		save.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(save())
					dispose();
		}});
		
		addButton(save);

/*
		String[] af, al;
		String values = calculatorPlugin == null ? "Path to file\tClass name\tButton label\tDescription\tKey character\tfalse" : calculatorPlugin.toString();
		af = values.split("\t");
		al = "File\tClass\tLabel\tDescription\tKey".split("\t");
		fields = new javax.swing.JTextField[al.length];
		javax.swing.JPanel form = new javax.swing.JPanel(new javax.swing.SpringLayout());
		for (int i = 0; i < al.length; i++) {
			javax.swing.JLabel l = new javax.swing.JLabel(al[i]);
			final javax.swing.JTextField f = i == al.length - 1 ? new CharTextField(af[i]) : new javax.swing.JTextField(af[i]);
			fields[i] = f;
			l.setLabelFor(f);
			form.add(l);
			if(i == 0){
				javax.swing.JPanel p = new javax.swing.JPanel();
				p.add(f);
				javax.swing.JButton b = new javax.swing.JButton("Browse");
				final javax.swing.JDialog d = this;
				b.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						javax.swing.JFileChooser c = new javax.swing.JFileChooser(f.getText());
						c.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
							
							@Override
							public boolean accept(java.io.File f) {
								return (f.getName().endsWith(".jar") || f.getName().endsWith(".class") || f.isDirectory());
							}
							@Override
							public String getDescription() {
								return "*.jar, *.class";
							}
						});
						int rVal = c.showOpenDialog(d);
						if (rVal == javax.swing.JFileChooser.APPROVE_OPTION) {
							f.setText(c.getSelectedFile().getAbsolutePath());
						}				
					}
				}
				);
				p.add(b);
				form.add(p);
			}
			else
				form.add(f);
		}

		SpringUtilities.makeCompactGrid(form, al.length, 2, 6, 6, 2, 2);
 */

		String v = calculatorPlugin == null ? "Path to file\tClass name\tButton label\tDescription\tKey character\tfalse" : calculatorPlugin.toString();
		String[] values = v.split("\t");
		String[] labels = "File\tClass\tLabel\tDescription\tKey".split("\t");
		fields = new javax.swing.JComponent[labels.length];

		javax.swing.JPanel p = new javax.swing.JPanel();
		javax.swing.JTextField cf = new CharTextField(values[values.length - 1]);
		javax.swing.JButton b = new javax.swing.JButton("Browse");
		final javax.swing.JTextField path = new javax.swing.JTextField(values[0]);
		p.add(path);
		p.add(b);
		final javax.swing.JDialog d = this;
		b.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				javax.swing.JFileChooser c = new javax.swing.JFileChooser(path.getText());
				c.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
					
					@Override
					public boolean accept(java.io.File f) {
						return (f.getName().endsWith(".jar") || f.getName().endsWith(".class") || f.isDirectory());
					}
					@Override
					public String getDescription() {
						return "*.jar, *.class";
					}
				});
				int rVal = c.showOpenDialog(d);
				if (rVal == javax.swing.JFileChooser.APPROVE_OPTION) {
					path.setText(c.getSelectedFile().getAbsolutePath());
				}				
			}
		}
		);
		
		fields[0] = p;
		fields[labels.length - 1] = cf;

		javax.swing.JPanel form = BuildForm(labels, values, fields);
		add(form);
		getContentPane().add(form, java.awt.BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	private boolean save() {
		for(CalculatorPluginDescriptor cp : Configuration.getPlugins())
			if(cp.className.equals(((javax.swing.JTextField)fields[1]).getText()) && cp.displayValue.equals((((javax.swing.JTextField)fields[2]).getText()))) {
				javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),
						"At least the classpath and label combination must be unique", "Fields must be unique", javax.swing.JOptionPane.OK_OPTION);
				return false;
			}
		if(fields != null) {
			if(calculatorPlugin == null) {
				calculatorPlugin = new CalculatorPluginDescriptor();
				Configuration.getPlugins().add(calculatorPlugin);
			}
			if(!Configuration.getPlugins().contains(calculatorPlugin))
				Configuration.getPlugins().add(calculatorPlugin);
				
			calculatorPlugin.location = ((javax.swing.JTextField)((javax.swing.JPanel)fields[0]).getComponent(0)).getText();
			calculatorPlugin.className = ((javax.swing.JTextField)fields[1]).getText();
			calculatorPlugin.displayValue = ((javax.swing.JTextField)fields[2]).getText();
			calculatorPlugin.descriptionValue = ((javax.swing.JTextField)fields[3]).getText();
			calculatorPlugin.keyBinding = ((javax.swing.JTextField)fields[4]).getText().charAt(0);
		}
		caller.loadTables();

		Configuration.store();
		return true;
	}
}

