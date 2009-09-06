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
//		buttonsPanel = getButtonsPanel();
		addButton(cancel);
		getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);
	}

//	protected abstract boolean save() ;
	
	/*
	private javax.swing.JPanel getButtonsPanel() { 
		javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
//		javax.swing.JButton save = new javax.swing.JButton("Save");
		javax.swing.JButton cancel = new javax.swing.JButton("Cancel");

		cancel.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
			}});
		save.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if(save())
					dispose();
			}});

		p.add(cancel);
		p.add(save);
		
		return p;
	}
	 */
	
	protected void addButtons(javax.swing.JButton[] buttons) {
		if(buttons != null && buttons.length > 0)
			for(int i = 0; i < buttons.length; i++)
				addButton(buttons[i]);
	}
	
	protected void addButton(javax.swing.JButton button) {
		buttonsPanel.add(button);
	}
	
}
