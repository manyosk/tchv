package temperatureViewer;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;

import slider.RangeSlider;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;

public class ChartViewPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3699862984630885788L;
	private JTextField minimalValueTextField;
	private JTextField textField;
	private RangeSlider rangeSlider;
	private JLabel lblFromDate = new JLabel("Date1");
	private JLabel lblToDate = new JLabel("Date2");

	/**
	 * Create the panel.
	 */
	public ChartViewPanel() {
		JPanel alarmPanel = new JPanel();
		alarmPanel.setBorder(new TitledBorder(null, "Alarm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblFrom = new JLabel("From:");
		
		JLabel lblTo = new JLabel("To:");
		
		//lblFromDate = new JLabel("Date1");
		
		//final JLabel lblToDate = new JLabel("Date2");
		
		rangeSlider = new RangeSlider();
		
		rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                lblFromDate.setText(String.valueOf(slider.getValue()));
                lblToDate.setText(String.valueOf(slider.getUpperValue()));
            }
        });
		
		JPanel chartPanel = new JPanel();
		chartPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(rangeSlider, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblFrom)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFromDate)
							.addPreferredGap(ComponentPlacement.RELATED, 337, Short.MAX_VALUE)
							.addComponent(lblTo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblToDate)
							.addGap(86))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(0)
							.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrom)
						.addComponent(lblTo)
						.addComponent(lblFromDate)
						.addComponent(lblToDate))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rangeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addGap(12))
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
					.addGroup(gl_alarmPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textField, 0, 0, Short.MAX_VALUE)
						.addComponent(minimalValueTextField, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))
					.addContainerGap(32, Short.MAX_VALUE))
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
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		alarmPanel.setLayout(gl_alarmPanel);
		setLayout(groupLayout);

	}

	public void SetRangeSliderRange(int min, int max)
	{
		rangeSlider.setValue(min);
        rangeSlider.setUpperValue(max);
	}
}
