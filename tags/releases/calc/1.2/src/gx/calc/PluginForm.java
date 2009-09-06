package gx.calc;

public class PluginForm extends FormDialog {
	private static final long serialVersionUID = 1L;
	private PluginsDialog caller;
	private javax.swing.JTextField[] fields;
	private CalculatorPluginDescriptor calculatorPlugin ;

	public PluginForm (javax.swing.JDialog parent, CalculatorPluginDescriptor c){
		super(parent, "Add new plugin", true);
		this.caller = (PluginsDialog) parent;
		this.calculatorPlugin = c;
		javax.swing.JButton save = new javax.swing.JButton("Save");

		save.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(save())
					dispose();
			}});
		
		addButton(save);

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

		add(form);
		getContentPane().add(form, java.awt.BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	private boolean save() {
		for(CalculatorPluginDescriptor cp : Configuration.getPlugins())
			if(cp.className.equals(fields[1].getText()) && cp.displayValue.equals((fields[2].getText()))) {
				javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(),
						"At lease the classpath and label combination must be unique", "Fields must be unique",
						javax.swing.JOptionPane.OK_OPTION);
				return false;
			}
		if(fields != null) {
			if(calculatorPlugin == null) {
				calculatorPlugin = new CalculatorPluginDescriptor();
				Configuration.getPlugins().add(calculatorPlugin);
			}
			calculatorPlugin.location = fields[0].getText();
			calculatorPlugin.className = fields[1].getText();
			calculatorPlugin.displayValue = fields[2].getText();
			calculatorPlugin.descriptionValue = fields[3].getText();
			calculatorPlugin.keyBinding = fields[4].getText().charAt(0);
		}
		caller.buildRowsPane();
		Configuration.store();
		return true;
	}
}

class CharTextField extends javax.swing.JTextField implements java.awt.event.KeyListener {
	private static final long serialVersionUID = 1L;

	public CharTextField(String initialStr) {
		super(initialStr);
		addKeyListener(this);
	}

	public void keyPressed(java.awt.event.KeyEvent e) { 
		setText("");
		e.consume(); 
	}
	public void keyReleased(java.awt.event.KeyEvent e) { }
	public void keyTyped(java.awt.event.KeyEvent e) { }
}
