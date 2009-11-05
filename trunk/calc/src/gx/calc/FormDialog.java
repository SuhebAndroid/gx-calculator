package gx.calc;

public abstract class FormDialog extends javax.swing.JDialog {
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel buttonsPanel;
	
	public FormDialog(javax.swing.JFrame parent, String title, boolean modal) {
		super(parent, title, modal);
		init("Cancel");
		setLocationRelativeTo(parent);
	}

	public FormDialog(javax.swing.JDialog parent, String title, boolean modal) {
		super(parent, title, modal);
		init("Cancel");
		setLocationRelativeTo(parent);
	}
	
	public FormDialog(javax.swing.JFrame parent, String title, boolean modal, String buttonTitle) {
		super(parent, title, modal);
		init(buttonTitle);
		setLocationRelativeTo(parent);
	}

	public FormDialog(javax.swing.JDialog parent, String title, boolean modal, String buttonTitle) {
		super(parent, title, modal);
		init(buttonTitle);
		setLocationRelativeTo(parent);
	}
	
	private void init(String buttonTitle) {
		buttonsPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
		javax.swing.JButton cancel = new javax.swing.JButton(buttonTitle);

		cancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
			}});
		addButton(cancel);
		getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
/*
		addKeyListener(new java.awt.event.KeyListener() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if(e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE)
					dispose();
			}
			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {}
			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {}
		});
 */
	}

	protected static javax.swing.JPanel BuildForm(String[] labels, String[] values, javax.swing.JComponent[] fields) {
		javax.swing.JPanel form = new javax.swing.JPanel(new javax.swing.SpringLayout());
		String[] af = values; 
		String[] al = labels;

		for (int i = 0; i < al.length; i++) {
			javax.swing.JLabel l = new javax.swing.JLabel(al[i]);
			final javax.swing.JComponent f;
			if(fields[i] != null)
				f = fields[i];
			else {
				String s = af[i] == null ? "" : af[i];
				if(s.length() < 0 && Character.getNumericValue(s.charAt(0)) < 0)
					s = "";
				f = i == al.length - 1 ? new CharTextField(s) : new javax.swing.JTextField(s);
				fields[i] = f;				
			}
			l.setLabelFor(f);
			form.add(l);
			form.add(f);
		}

		SpringUtilities.makeCompactGrid(form, al.length, 2, 6, 6, 2, 2);
		return form;
	}

	protected void addButtons(javax.swing.JButton[] buttons) {
		if(buttons != null && buttons.length > 0)
			for(int i = 0; i < buttons.length; i++)
				addButton(buttons[i]);
	}
	
	protected void addButton(javax.swing.JButton button) {
		buttonsPanel.add(button);
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
