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

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.awt.BorderLayout;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

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
	private JPanel chartPanel = null;
	private JTextField minTempTextField;
	private JTextField maxTempTextField;
	private JTextField avgTextField;
	private JFreeChart chart;

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
		rangeSlider.setUpperValue(100);
		rangeSlider.setValue(0);
		
		rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss");
                
                long lValue = ((long) slider.getValue() + (long) 1262300400) * 1000;
                long lUpperValue = ((long) slider.getUpperValue() + (long) 1262300400) * 1000;
                
                lblFromDate.setText(sdf.format(lValue));
                lblToDate.setText(sdf.format(lUpperValue));
                
                if(!slider.getValueIsAdjusting())
                {
                	UpdateChart(lValue, lUpperValue);
                }
            }
        });
		
		chartPanel = new JPanel();
		chartPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Hodnoty", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE)
							.addGap(0, 0, Short.MAX_VALUE))
						.addComponent(rangeSlider, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblFrom)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFromDate)
							.addPreferredGap(ComponentPlacement.RELATED, 480, Short.MAX_VALUE)
							.addComponent(lblTo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblToDate)
							.addGap(20)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(panel, 0, 0, Short.MAX_VALUE)
						.addComponent(alarmPanel, GroupLayout.PREFERRED_SIZE, 84, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(chartPanel, GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrom)
						.addComponent(lblFromDate)
						.addComponent(lblTo)
						.addComponent(lblToDate))
					.addGap(15)
					.addComponent(rangeSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(19))
		);
		
		JLabel lblMinimalnaTeplota = new JLabel("Minimalna teplota");
		
		JLabel lblMaximalnaTeplota = new JLabel("Maximalna teplota");
		
		minTempTextField = new JTextField();
		minTempTextField.setEnabled(false);
		minTempTextField.setColumns(10);
		
		maxTempTextField = new JTextField();
		maxTempTextField.setEnabled(false);
		maxTempTextField.setColumns(10);
		
		JLabel lblPriemernaTeplota = new JLabel("Priemerna teplota");
		
		avgTextField = new JTextField();
		avgTextField.setEnabled(false);
		avgTextField.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblMaximalnaTeplota)
						.addComponent(lblMinimalnaTeplota))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(maxTempTextField, 0, 0, Short.MAX_VALUE)
						.addComponent(minTempTextField, GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
					.addGap(18)
					.addComponent(lblPriemernaTeplota)
					.addGap(18)
					.addComponent(avgTextField, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMinimalnaTeplota)
						.addComponent(minTempTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPriemernaTeplota)
						.addComponent(avgTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMaximalnaTeplota)
						.addComponent(maxTempTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		chartPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel lblMinValue = new JLabel("Minimal value");
		lblMinValue.setEnabled(false);
		
		JLabel lblMaximalValue = new JLabel("Maximal value");
		lblMaximalValue.setEnabled(false);
		
		minimalValueTextField = new JTextField();
		minimalValueTextField.setEnabled(false);
		minimalValueTextField.setColumns(10);
		
		textField = new JTextField();
		textField.setEnabled(false);
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
		rangeSlider.setMinimum(min);
        rangeSlider.setMaximum(max);
		rangeSlider.setValue(min);
        rangeSlider.setUpperValue(max);
        rangeSlider.setMajorTickSpacing(600);
        
        
        
        rangeSlider.setMinorTickSpacing(60);
        rangeSlider.setPaintTicks(true);
        //rangeSlider.setSnapToTicks(true);
        //rangeSlider.setPaintLabels(true);
        
        /*rangeSlider.setMinimum(0);
        rangeSlider.setMaximum(100);
		rangeSlider.setValue(0);
        rangeSlider.setUpperValue(100);
        rangeSlider.setMajorTickSpacing(10);
        rangeSlider.setPaintTicks(true);*/
	}
	
	public void DisplayChart(TimeSeriesCollection dataset)
	{
		chartPanel.removeAll();
		chart = ChartFactory.createTimeSeriesChart(
				 "Population of CSC408 Town",
				 "Date", 
				 "Population",
				 dataset,
				 true,
				 true,
				 false);
		
		ChartPanel myChartPanel = new ChartPanel(chart);
		myChartPanel.setMouseWheelEnabled(true);
        chartPanel.add(myChartPanel,BorderLayout.CENTER);
        chartPanel.validate();
	}
	
	private void UpdateChart(long value, long upperValue) 
	{
		if(chart != null)
		{
			final XYPlot plot = chart.getXYPlot();
	        ValueAxis axis = plot.getDomainAxis();
	        if(value < upperValue)
	        {
	        	axis.setRange(value, upperValue);
	        }
		}
	}

	public void SetTemperatureData(double minTemp, double maxTemp, double avgTemp) 
	{
		minTempTextField.setText(Double.toString(minTemp));
		maxTempTextField.setText(Double.toString(maxTemp));
		avgTextField.setText(String.format("%.2f", avgTemp));
	}
}
