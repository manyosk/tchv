package temperatureViewer;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

public class ChartViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3699862984630885788L;
	private JTextField minimalValueTextField;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public ChartViewPanel() {
		
		JPanel alarmPanel = new JPanel();
		alarmPanel.setBorder(new TitledBorder(null, "Alarm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(342, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(319, Short.MAX_VALUE))
		);
		
		JLabel lblMinValue = new JLabel("Minimal value");
		
		JLabel lblMaximalValue = new JLabel("Maximal value");
		
		minimalValueTextField = new JTextField();
		minimalValueTextField.setColumns(10);
		
		textField = new JTextField();
		textField.setColumns(10);
		GroupLayout gl_alarmPanel = new GroupLayout(alarmPanel);
		gl_alarmPanel.setHorizontalGroup(
			gl_alarmPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_alarmPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_alarmPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMinValue)
						.addComponent(lblMaximalValue))
					.addGap(14)
					.addGroup(gl_alarmPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minimalValueTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(338, Short.MAX_VALUE))
		);
		gl_alarmPanel.setVerticalGroup(
			gl_alarmPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_alarmPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_alarmPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinValue)
						.addComponent(minimalValueTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_alarmPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMaximalValue)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		alarmPanel.setLayout(gl_alarmPanel);
		setLayout(groupLayout);

	}
}
