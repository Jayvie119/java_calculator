package calculator;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class Main extends Frame implements ActionListener{

	Label display;
	String operator ="";
	BigDecimal first = BigDecimal.ZERO;
	boolean startNewNumber = true ; //Flag to start new number after operator
	
	Main() {
		//Frame title
		super("AWT Calculator");
		
		//Display
		display= new Label("0", Label.RIGHT);
		display.setBackground(Color.WHITE);
		display.setFont(new Font("Arial", Font.BOLD, 28));
		add(display, BorderLayout.NORTH);
		
		//Buttons Panel
		Panel panel = new Panel();
		panel.setLayout(new GridLayout(5,4,4,4));
		
		String[] buttons = {
				"CE", "C", "Back", "÷",
				"7", "8", "9", "x",
				"4", "5", "6", "-",
				"1", "2", "3", "+",
				"±", "0", ".", "="
		};
		
		for(String text : buttons) {
			Button b = new Button(text);
			b.setFont(new Font("Arial", Font.BOLD, 18));
			b.addActionListener(this);
			panel.add(b);
		}
		
		add(panel, BorderLayout.CENTER);
		setSize(350, 450);
		setVisible(true);
		
		//Close window properly
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		try {
			//number input
			if (cmd.matches("[0-9]")) {
				if (startNewNumber || display.getText().equals("0")) {
					display.setText(cmd);
					startNewNumber = false;
				} else {
					display.setText(display.getText() + cmd);
				}
			}
			//Decimal
			else if (cmd.equals(".")) {
				if(!display.getText().contains(".")) {
					display.setText(display.getText() + ".");
					startNewNumber = false;
				}
			}
			//Operators
			else if ("+-x÷".contains(cmd)) {
				computeIntermediate();
				operator = cmd;
				startNewNumber = true;
			}
			
			//Equals
			else if(cmd.equals("=")) {
				computeIntermediate();
				operator = "";
				startNewNumber = true;
			}
			//Clear all
			else if(cmd.equals("C")) {
				display.setText("0");
				first = BigDecimal.ZERO;
				operator = "";
				startNewNumber = true;
			}
			//Clear entry
			else if(cmd.equals("CE")) {
				display.setText("0");
				startNewNumber = true;
			}
			
			else if (cmd.equals("Back")) {
				String t = display.getText();
				if(t.length()>1) {
					display.setText(t.substring(0, t.length() - 1));
				}else {
					display.setText("0");
					startNewNumber = true;
				}
			}
		//Plus/Minus
			else if(cmd.equals("±")) {
				BigDecimal n = new BigDecimal(display.getText());
				display.setText(n.negate().toPlainString());
			}
		
		} catch (Exception ex) {
			display.setText("Error");
			startNewNumber = true;
		}
	}
	
	//Compute intermediate result for continuous operations
	private void computeIntermediate() {
		BigDecimal current = new BigDecimal(display.getText());
		if(!operator.isEmpty()) {
			switch (operator) {
			case "+": first = first.add(current); break;
			case "-": first = first.subtract(current); break;
			case "x": first= first.multiply(current); break;
			case "÷": 
				if (current.compareTo(BigDecimal.ZERO) == 0) {
					display.setText("Error");
					first = BigDecimal.ZERO;
					operator = "";
					startNewNumber = true;
					return;
				}
				first = first.divide(current, 20, BigDecimal.ROUND_HALF_UP);
				break;
			}
			display.setText(first.stripTrailingZeros().toPlainString());
		} else {
			first = current;
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
