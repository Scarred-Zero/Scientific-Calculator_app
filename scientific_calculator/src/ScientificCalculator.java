import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;

// SCIENTIFIC CALCULATOR
public class ScientificCalculator {

	SciCalcHistory calcHistory = new SciCalcHistory();

	boolean bool = false;

	JFrame calculatorFrame;
	
	// RESULT VARIABLE
	String result = "";
	
	// EXPRESSION VARIABLE
	String expression = "";
	
	ArrayList<String> token = new ArrayList<String>();

    // THE ONE THAT READS SPECIFIC LINE OF A DOCUMENT
    int enter = 0;
    int enter1 = 1;

    int enter2 = 2;
    int enter3 = 3;

    int enter4 = 4;
    int enter5 = 5;

    int enter6 = 6;
    int enter7 = 7;

    // HOLDS THE DATA OF SPECIFIED LINE OF A DOCUMENT
    String line;
    String line1;

    String line2;
    String line3;

    String line4;
    String line5;

    String line6;
    String line7;

    // FLOW FOR THE DATA OF DOCUMENT THAT SHOWS IN HISTORY PANEL
    int beqenter = 0;
	
	boolean num = false;  
	
	boolean dot = false;  
	
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScientificCalculator window = new ScientificCalculator();
					window.calculatorFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ScientificCalculator() {
		initialize();
	}

	int precedence(String x) {
		int p = 10;
		switch(x) {
		case "+":
			p = 1;
			break;
		case "-":
			p = 2;
			break;
		case "x":
			p = 3;
			break;
		case "/":
			p = 4;
			break;
		case "^":
			p = 6;
			break;
		case "!":
			p = 7;
			break;
		}
	
		return p;
	}
	
	// OPERATOR CHECKING
	private boolean isoperator(String x) {
		if(x.equals("+") || x.equals("-") || x.equals("x") || x.equals("/") || x.equals("sqrt") || x.equals("%") || x.equals("^") || x.equals("!") || x.equals("sin") || x.equals("cos") || x.equals("tan") || x.equals("^3")  || x.equals("ln") || x.equals("log") || x.equals("sinh") || x.equals("cosh") || x.equals("tanh"))
			return true;
		else 
			return false;
	}
	
	// FOR INFIX TO POSTFIX CONVERSION
	private String infixTopostfix() {
		Stack<String> s = new Stack<String>();
		String y;
		int flag;
		String p = "";   
		token.add(")");
		s.push("(");
		for(String i: token) {
			if(i.equals("(")){
				s.push(i);
			}else if(i.equals(")")){
				y = s.pop();
				while(!y.equals("("))
				{
					p = p + y + ",";
					y = s.pop();
				}
			}else if(isoperator(i)){
				y = s.pop();
				flag = 0;
				if(isoperator(y) && precedence(y)>precedence(i)){
					p = p + y + ",";
					flag = 1;
				}
				if(flag == 0)
					s.push(y);
				
				s.push(i);
			}else{
				p = p + i + ",";
			}
		}
		while(!s.empty()) {
			y = s.pop();
			if(!y.equals("(") && !y.equals(")")) {
				p += y + ",";
			}
		}
		return p;
	}

	// FACTORIAL METHOD
	private double factorial(double y) {
		double fact = 1;
		if(y == 0 || y == 1) {
			fact = 1;
		}else {
			for(int i=2; i<=y; i++) {
				fact *= i;
			}
		}
		return fact;
	}
	
	// FOR ACTUAL CALCULATIONS WITH BINARY OPERATORS
	private double calculate(double x,double y,String c) {
		double res = 0;
		switch(c)
		{
			case "-":
				res = x - y;
				break;
			case "+":
				res = x + y;
				break;
			case "x":
				res = x * y;
				break;
			case "/":
				res = x / y;
				break;
			case "%":
				res = x / 100;
				break;
            case "^3":
                res = x * x * x * y;
                break;
			case "^":
				res = Math.pow(x,y);
				break;
			default :
				res = 0; 
		}
		return res;
	}

	// CALCULATION WITH UNARY OPERATORS
	private double calculate(double y,String c) {
		double res = 0;
		switch(c) {
		case "log":
			res = Math.log10(y);
			break;
		case "sin":
			res = Math.sin(y);
			break;
		case "sinh":
			res = Math.sinh(y);
			break;
		case "cos":
			res = Math.cos(y);
			break;
		case "cosh":
			res = Math.cosh(y);
			break;
		case "tan":
			res = Math.tan(y);
			break;
		case "tanh":
			res = Math.tanh(y);
			break;
		case "ln":
			res = Math.log(y);
			break;
		case "sqrt":
			res = Math.sqrt(y);
			break;
        case "^3":
            res = y * y * y;
            break;
		case "!":
			res = factorial(y);
			break;
		}
		return res;
	}

	// EVALUATION PROCESS
	private double Eval(String p) {	
		String tokens[] = p.split(",");
		ArrayList<String> token2 = new ArrayList<String>();
		for(int i=0; i<tokens.length; i++) {
			if(! tokens[i].equals("") && ! tokens[i].equals(" ") && ! tokens[i].equals("\n") && ! tokens[i].equals("  ")) {
				token2.add(tokens[i]);  // tokens from post fix form p actual tokens for calculation
			}
		}
		
		Stack<Double> s = new Stack<Double>();
		double x, y;
		for(String  i:token2) {
			if(isoperator(i)){
				//if it is unary operator or function
				if(i.equals("sin") || i.equals("cos") || i.equals("tan") || i.equals("sinh") || i.equals("cosh") || i.equals("tanh") || i.equals("log") || i.equals("ln") || i.equals("^3") || i.equals("sqrt") || i.equals("!")) {
					y = s.pop();
					s.push(calculate(y,i));
				}else {
					//for binary operators
					y = s.pop();
					x = s.pop();
					s.push(calculate(x,y,i));
				}
			}else{
				if(i.equals("pi"))
					s.push(Math.PI);
				else if(i.equals("e"))
					s.push(Math.E);
				else
					s.push(Double.valueOf(i));
			}
		}
		double res = 1;
		while(!s.empty()) {
			res *= s.pop();
		}
		return res;  //final result
	}

	// ACTUAL COMBINED METHODS FOR CALCULATIONS 
	private void calculateMain() {
		String tokens[] = expression.split(",");
		for(int i=0; i<tokens.length; i++) {
			if(! tokens[i].equals("") && ! tokens[i].equals(" ") && ! tokens[i].equals("\n") && ! tokens[i].equals("  ")) {
				token.add(tokens[i]);  //adding token to token array list from expression 
			}
		}
		try {
			double res = Eval(infixTopostfix());
			result = Double.toString(res);
		}catch(Exception e) {}
	}
	
	
	// FRAME DESIGN WITH THEIR ACTION LISTENERS
	private void initialize() {
		try {
			File history = new File("HistoryLog.doc");
			history.createNewFile();
			
			history.delete();
			
			FileWriter historyfw = new FileWriter(history, true);
		
			bool = history.exists();
		
			// CALCULATOR FRAME DESIGN
			calculatorFrame = new JFrame();
			calculatorFrame.setResizable(false);
			calculatorFrame.setTitle("Sci-Calc");
			calculatorFrame.getContentPane().setBackground(new Color(87, 126, 124));
			calculatorFrame.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 15));
			calculatorFrame.getContentPane().setForeground(SystemColor.windowBorder);
			calculatorFrame.getContentPane().setLayout(null);
			calculatorFrame.setBounds(200, 200, 390, 500);
			calculatorFrame.setSize(411,520); //gui.setSize(690,520);
			calculatorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
			
			// TEXT PANEL 
			JPanel textPanel = new JPanel();
			textPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			textPanel.setBounds(34, 25, 316, 80);
			textPanel.setLayout(null);
			calculatorFrame.getContentPane().add(textPanel);
			
			// EXPRESSION LABEL
			JLabel exprlabel = new JLabel("");
			exprlabel.setBackground(SystemColor.control);
			exprlabel.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
			exprlabel.setHorizontalAlignment(SwingConstants.RIGHT);
			exprlabel.setForeground(UIManager.getColor("Button.disabledForeground"));
			exprlabel.setBounds(2, 2, 312, 27);
			textPanel.add(exprlabel);
			
			// TEXT FIELD
			JTextField textField = new JTextField();
			exprlabel.setLabelFor(textField);
			textField.setHorizontalAlignment(SwingConstants.RIGHT);
			textField.setBackground(SystemColor.control);
			textField.setEditable(false);
			textField.setText("0");
			textField.setBorder(null);
			textField.setFont(new Font("Yu Gothic UI Light", textField.getFont().getStyle(), 32));
			textField.setBounds(2, 30, 312, 49);
			textField.setColumns(10);
			textPanel.add(textField);

			// SLIDER BUTTON TO SHOW HISTORY PANEL
			JButton slider = new JButton("|||");
			slider.setBounds(394,0,11,520);
			slider.setForeground(Color.WHITE);
			slider.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			slider.setBackground(new java.awt.Color(14, 9, 11));
			calculatorFrame.getContentPane().add(slider);
			slider.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String textFieldInput = textField.getText();
					String action = e.getActionCommand();
					textField.setText(" ");
					//Tells whether a History Panel is opened or closed.	
					int slide = 0;
					if(action.equals("|||")) {
						if(slide == 0) {
							calculatorFrame.setSize(new Dimension(690,520));
							textField.setText(textFieldInput);
							slider.setBackground(new java.awt.Color(22, 22, 22));
						slide=1;
						}
						else {
							calculatorFrame.setSize(new Dimension(411,520));
							textField.setText(textFieldInput);
							slider.setBackground(new java.awt.Color(14, 9, 11));
							slide = 0;
						}
					}
				}
			});

			// HISTORY PANEL BUTTONS AND TEXTFIELDS
			JLabel panelHistory = new JLabel("");
			panelHistory.setBounds(405,0,284,491);
			panelHistory.setForeground(Color.WHITE);
			panelHistory.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			panelHistory.setBackground(new java.awt.Color(87, 126, 124));
			panelHistory.setOpaque(true);

			// HISTORY TEXT 
			JTextField historyText = new JTextField("History");
			historyText.setBounds(420,10,70,30);
			historyText.setForeground(Color.WHITE);
			historyText.setFont(new Font("Calibri", Font.BOLD, 20));
			historyText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			historyText.setBackground(new java.awt.Color(87, 126, 124));
			historyText.setEditable(false);
			calculatorFrame.getContentPane().add(historyText);

			// HISTORY UNDERLINE 
			JTextField historyUnderline = new JTextField("");
			historyUnderline.setBounds(420,43,68,3);
			historyUnderline.setForeground(Color.WHITE);
			historyUnderline.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			historyUnderline.setBackground(new java.awt.Color(254, 250, 43));
			historyUnderline.setEditable(false);
			calculatorFrame.getContentPane().add(historyUnderline);

			// HISTORY EMPTY 
			JTextField historyEmpty = new JTextField("There's no history yet");
			historyEmpty.setBounds(420,60,155,14);
			historyEmpty.setForeground(Color.WHITE);
			historyEmpty.setFont(new Font("Calibri", Font.BOLD, 14));
			historyEmpty.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			historyEmpty.setBackground(new java.awt.Color(87, 126, 124));
			historyEmpty.setEditable(false);
			calculatorFrame.getContentPane().add(historyEmpty);
			
			// HISTORY ANSWER
			JTextField historyAns = new JTextField("");
			historyAns.setBounds(415,95,260,60);
			historyAns.setForeground(Color.WHITE);
			historyAns.setFont(new Font("Calibri", Font.BOLD, 52));
			historyAns.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			historyAns.setBackground(new java.awt.Color(87, 126, 124));
			historyAns.setHorizontalAlignment(SwingConstants.RIGHT);
			historyAns.setEditable(false);
			calculatorFrame.getContentPane().add(historyAns);

			// HISTORY EXPRESSION 
			JTextField historyExpr = new JTextField("");
			historyExpr.setBounds(415,75,260,15);
			historyExpr.setForeground(Color.WHITE);
			historyExpr.setFont(new Font("Calibri light", Font.BOLD, 15));
			historyExpr.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			historyExpr.setBackground(new java.awt.Color(87, 126, 124));
			historyExpr.setHorizontalAlignment(SwingConstants.RIGHT);
			historyExpr.setEditable(false);
			calculatorFrame.getContentPane().add(historyExpr);

			JTextField history1expr = new JTextField("");
			history1expr.setBounds(415,160,260,30);
			history1expr.setForeground(Color.GRAY);
			history1expr.setFont(new Font("Calibri", Font.BOLD, 15));
			history1expr.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history1expr.setBackground(new java.awt.Color(87, 126, 124));
			history1expr.setHorizontalAlignment(SwingConstants.RIGHT);
			history1expr.setEditable(false);
			calculatorFrame.getContentPane().add(history1expr);

			JTextField history1ans = new JTextField("");
			history1ans.setBounds(415,185,260,60);
			history1ans.setForeground(Color.WHITE);
			history1ans.setFont(new Font("Calibri", Font.BOLD, 52));
			history1ans.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history1ans.setBackground(new java.awt.Color(87, 126, 124));
			history1ans.setHorizontalAlignment(SwingConstants.RIGHT);
			history1ans.setEditable(false);
			calculatorFrame.getContentPane().add(history1ans);

			JTextField history2expr = new JTextField("");
			history2expr.setBounds(415,250,260,30);
			history2expr.setForeground(Color.GRAY);
			history2expr.setFont(new Font("Calibri", Font.BOLD, 15));
			history2expr.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history2expr.setBackground(new java.awt.Color(87, 126, 124));
			history2expr.setHorizontalAlignment(SwingConstants.RIGHT);
			history2expr.setEditable(false);
			calculatorFrame.getContentPane().add(history2expr);
			
			JTextField history2ans = new JTextField("");
			history2ans.setBounds(415,275,260,60);
			history2ans.setForeground(Color.WHITE);
			history2ans.setFont(new Font("Calibri", Font.BOLD, 52));
			history2ans.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history2ans.setBackground(new java.awt.Color(87, 126, 124));
			history2ans.setHorizontalAlignment(SwingConstants.RIGHT);
			history2ans.setEditable(false);
			calculatorFrame.getContentPane().add(history2ans);

			JTextField history3expr = new JTextField("");
			history3expr.setBounds(415,340,260,30);
			history3expr.setForeground(Color.GRAY);
			history3expr.setFont(new Font("Calibri", Font.BOLD, 15));
			history3expr.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history3expr.setBackground(new java.awt.Color(87, 126, 124));
			history3expr.setHorizontalAlignment(SwingConstants.RIGHT);
			history3expr.setEditable(false);
			calculatorFrame.getContentPane().add(history3expr);

			JTextField history3ans = new JTextField("");
			history3ans.setBounds(415,365,260,60);
			history3ans.setForeground(Color.WHITE);
			history3ans.setFont(new Font("Calibri", Font.BOLD, 52));
			history3ans.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			history3ans.setBackground(new java.awt.Color(87, 126, 124));
			history3ans.setHorizontalAlignment(SwingConstants.RIGHT);
			history3ans.setEditable(false);
			calculatorFrame.getContentPane().add(history3ans);

			JTextField fileIsOpened = new JTextField("");
			fileIsOpened.setBounds(470,430,175,25);
			fileIsOpened.setForeground(Color.WHITE);
			fileIsOpened.setFont(new Font("Calibri", Font.BOLD, 12));
			fileIsOpened.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			fileIsOpened.setBackground(new java.awt.Color(87, 126, 124));
			fileIsOpened.setEditable(false);
			calculatorFrame.getContentPane().add(fileIsOpened);
			fileIsOpened.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String textFieldInput = textField.getText();
					String action = e.getActionCommand();
					textField.setText(" ");
					if(action.equals("Open History")) {
						try {
							Desktop.getDesktop().open(new java.io.File("HistoryLog.doc"));
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						fileIsOpened.setText("File is opened! Please wait..");
						textField.setText(textFieldInput);
						
					}
				}
			});
			
			// FILE OPEN BUTTON
			JButton fileOpen = new JButton("Open History");
			fileOpen.setBounds(510,455,85,25);
			fileOpen.setForeground(Color.WHITE);
			fileOpen.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			fileOpen.setBackground(new java.awt.Color(22, 22, 22));
			calculatorFrame.getContentPane().add(fileOpen);
			fileOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String textFieldInput = textField.getText();
					String action = e.getActionCommand();
					textField.setText(" ");
					if(action.equals("Open History")) {
						try {
							Desktop.getDesktop().open(new java.io.File("HistoryLog.doc"));
						} catch (IOException e1) {
							
							e1.printStackTrace();
						}
						fileIsOpened.setText("File is opened! Please wait..");
						textField.setText(textFieldInput);
						
					}
				}
			});

			// FILE CLEAR BUTTON
			JButton fileclear = new JButton("Clear History");
			fileclear.setBounds(420,455,85,25);
			fileclear.setForeground(Color.WHITE);
			fileclear.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			fileclear.setBackground(new java.awt.Color(22, 22, 22));
			calculatorFrame.getContentPane().add(fileclear);
			fileclear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String textFieldInput = textField.getText();
					String action = e.getActionCommand();
					textField.setText(" ");	
					if(action.equals("Clear History")) {
						historyText.setText("History");
						historyUnderline.setText("  ");
						textField.setText(textFieldInput);
						historyEmpty.setText("There's no history yet");
						historyExpr.setText("");
						historyAns.setText("");
						history1expr.setText("");
						history1ans.setText("");
						history2expr.setText("");
						history2ans.setText("");
						history3expr.setText("");
						history3ans.setText("");
						fileIsOpened.setText("");
					}
				}
			});

			// BUTTON PANEL
			JPanel butttonPanel = new JPanel();
			butttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			butttonPanel.setBackground(SystemColor.cyan);
			butttonPanel.setBounds(34, 120, 316, 330);
			butttonPanel.setLayout(new GridLayout(0, 5, 0, 0));
			calculatorFrame.getContentPane().add(butttonPanel);
			
			// CLEAR BUTTON 
			JButton button1 = new JButton("<html><body><span>C</span></body></html>");
			button1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					textField.setText("0");
					exprlabel.setText("");
					expression = "";
					token.clear();
					result = "";
					num = false;
					dot = false;
				}
			});
			button1.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button1);
			
			// DELETE BUTTON
			JButton button2 = new JButton("<html><body><span>DEL</span></body></html>");
			button2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s = textField.getText();
					if(s != "0" && s.length() > 1) {
						String newString = s.substring(0,s.length()-1);
						textField.setText(newString);
						if(expression.charAt(expression.length()-1)=='.') {
							dot = false;
						}
						if(expression.charAt(expression.length()-1) == ',') {
							expression = expression.substring(0,expression.length()-2);
						}else {
							expression = expression.substring(0,expression.length()-1);
						}
					}else {
						textField.setText("0");
						expression = "";
					}
				}
			});
			button2.setFont(new Font("Calibri Light", Font.PLAIN, 14));
			butttonPanel.add(button2);
			
			// BUTTON FOR CONSTANT PI
			JButton button3 = new JButton("<html><body><span>π</span></body></html>");
			button3.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			button3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+Character.toString((char)960));
					}else {
						textField.setText(Character.toString((char)960));
					}
					expression += ",pi";
					num = false;
					dot = false;
				}
			});
			butttonPanel.add(button3);
			
			// BUTTON FOR x^Y
			JButton button4 = new JButton("<html><body><span>x^y</span></body></html>");
			button4.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"^");
						expression += ",^";
					}else {
						textField.setText("0^");
						expression += ",0,^";
					}
					num = false;
					dot = false;
				}
			});
			button4.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button4);
			
			// FACTORIAL BUTTON
			JButton buttton5 = new JButton("<html><body><span>x!</span></body></html>");
			buttton5.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"!");
						expression += ",!";
					}else {
						textField.setText("0!");
						expression += ",0,!";
					}
					num = false;
					dot = false;
				}
			});
			buttton5.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(buttton5);
		
			// OPENING BRACKET
			JButton button6 = new JButton("<html><body><span>(</span></body></html>");
			button6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"(");
					}else {
						textField.setText("(");
					}
					expression += ",(";
					num = false;
					dot = false;
				}
			});
			button6.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button6);
			
			// CLOSING BRACKET
			JButton button7 = new JButton("<html><body><span>)</span></body></html>");
			button7.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+")");
					}else {
						textField.setText(")");
					}
					expression += ",)";
					num = false;
					dot = false;
				}
			});
			button7.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button7);
			
			// EXPONENTIAL BUTTON
			JButton button8 = new JButton("<html><body><span>e</span></body></html>");
			button8.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"e");
					}else {
						textField.setText("e");
					}
					expression += ",e";
					num = false;
					dot = false;
				}
			});
			button8.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button8);
		
			// BUTTON FOR SQUARE ROOT
			JButton button9 = new JButton("<html><body><span>√</span></body></html>");
			button9.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+Character.toString((char)8730));
					}else {
						textField.setText(Character.toString((char)8730));
					}
					expression += ",sqrt";
					num = false;
					dot = false;
				}
			});
			button9.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button9);

			// BUTTON FOR %
			JButton button10 = new JButton("%");
			button10.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"/100");
					}else {
						textField.setText("%");
					}
					expression += ",/,100";
					num = false;
					dot = false;
				}
			});
			button10.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button10);

			// BUTTON FOR sinh
			JButton button11 = new JButton("sinh");
			button11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"sinh(");
					}else {
						textField.setText("sinh(");
					}
					expression += ",sinh,(,";
					num = false;
					dot = false;
				}
			});
			button11.setFont(new Font("Calibri Light", Font.PLAIN, 16));
			butttonPanel.add(button11);

			// BUTTON FOR cosh
			JButton button12 = new JButton("cosh");
			button12.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"cosh(");
					}else {
						textField.setText("cosh(");
					}
					expression += ",cosh,(";
					num = false;
					dot = false;
				}
			});
			button12.setFont(new Font("Calibri Light", Font.PLAIN, 14));
			butttonPanel.add(button12);

			// BUTTON FOR tanh
			JButton button13 = new JButton("tanh");
			button13.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"tanh(");
					}else {
						textField.setText("tanh(");
					}
					expression += ",tanh,(";
					num = false;
					dot = false;
				}
			});
			button13.setFont(new Font("Calibri Light", Font.PLAIN, 14));
			butttonPanel.add(button13);

			// BUTTON FOR x^3
			JButton button14 = new JButton("<html><body><span>X<sup>3</sup></span></body></html>");
			button14.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"^3");
						expression += ",^3";
					}else {
						textField.setText("0^3");
						expression += ",0,^3";
					}
					num = false;
					dot = false;
				}
			});
			button14.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button14);

			// BUTTON FOR DIVISION OPERATOR
			JButton button15 = new JButton("<html><body><span>÷</span></body></html>");
			button15.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s=textField.getText();
					if(s.equals("0")) {
						expression += "0";
					}
					if(s.charAt(s.length()-1) == '-' || s.charAt(s.length()-1) == 'x' || s.charAt(s.length()-1) == '+') {
						String newString = s.substring(0,s.length()-1);
						textField.setText(newString+Character.toString((char)247));
						expression = expression.substring(0,expression.length()-1);
						expression += "/";
					}else if(s.charAt(s.length()-1)!= (char)247) {	
						textField.setText(s+Character.toString((char)247));	
						expression += ",/";
					}else {
						textField.setText(s);	
					}
					num = false;
					dot = false;
				}
			});
			button15.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button15);

			// BUTTON FOR SIN
			JButton button16 = new JButton("sin");
			button16.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"sin(");
					}else {
						textField.setText("sin(");
					}
					expression += ",sin,(";
					num = false;
					dot = false;
				}
			});
			button16.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button16);
			
			// BUTTON FOR NUMBER (7)
			JButton button17 = new JButton("7");
			button17.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"7");
					}else {
						textField.setText("7");
					}
					if(num) {
						expression += "7";
					}else {
						expression += ",7";
					}
					num = true;
				}
			});
			button17.setBackground(new Color(100, 151, 177));
			button17.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button17);
			
			// BUTTON FOR NUMBER (8)
			JButton button18 = new JButton("8");
			button18.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"8");
					}else {
						textField.setText("8");
					}
					if(num) {
						expression += "8";
					}else {
						expression += ",8";
					}
					num = true;
				}
			});
			button18.setBackground(new Color(100, 151, 177));
			button18.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button18);

			// BUTTON FOR NUMBER (9)
			JButton button19 = new JButton("9");
			button19.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"9");
					}else {
						textField.setText("9");
					}
					if(num) {
						expression += "9";
					}else {
						expression += ",9";
					}
					num = true;
				}
			});
			button19.setBackground(new Color(100, 151, 177));
			button19.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button19);
			
			// BUTTON FOR MULTIPLICATION
			JButton button20 = new JButton("x");
			button20.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s=textField.getText();
					if(s.equals("0")) {
						expression += "0";
					}
					if(s.charAt(s.length()-1)== '-' || s.charAt(s.length()-1)== '+' || s.charAt(s.length()-1) == (char)(247)) {
						String newString = s.substring(0,s.length()-1);
						newString += "x";
						textField.setText(newString);
						expression = expression.substring(0,expression.length()-1);
						expression += "x";
					}else if(s.charAt(s.length()-1)!= 'x') {
						s += "x";	
						textField.setText(s);
						expression += ",x";
					}else {
						textField.setText(s);	
					}
					num = false;
					dot = false;
				}
			});
			button20.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button20);
			
			// BUTTON FOR COS
			JButton button21 = new JButton("cos");
			button21.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"cos(");
					}else {
						textField.setText("cos(");
					}
					expression += ",cos,(";
					num = false;
					dot = false;
				}
			});
			button21.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button21);
			
			// BUTTON FOR NUMBER (4)
			JButton button22 = new JButton("4");
			button22.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"4");
					}else {
						textField.setText("4");
					}
					if(num) {
						expression += "4";
					}else {
						expression += ",4";
					}
					num = true;
				}
			});
			button22.setBackground(new Color(100, 151, 177));
			button22.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button22);
			
			// BUTTON FOR NUMBER (5)
			JButton button23 = new JButton("5");
			button23.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"5");
					}else {
						textField.setText("5");
					}
					if(num) {
						expression += "5";
					}else {
						expression += ",5";
					}
					num = true;
				}
			});
			button23.setBackground(new Color(100, 151, 177));
			button23.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button23);
			
			// BUTTON FOR NUMBER (6)
			JButton button24 = new JButton("6");
			button24.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"6");
					}else {
						textField.setText("6");
					}
					if(num) {
						expression += "6";
					}else {
						expression += ",6";
					}
					num = true;
				}
			});
			button24.setBackground(new Color(100, 151, 177));
			button24.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button24);
			
			// BUTTON FOR SUBTRACTION
			JButton button25 = new JButton("-");
			button25.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s=textField.getText();
					if(s.equals("0")) {
						expression += "0";
					}
					if(s.charAt(s.length()-1)== '+') {
						String newString = s.substring(0,s.length()-1);
						newString += "-";
						expression = expression.substring(0,expression.length()-1);
						expression += "-";
						textField.setText(newString);
					}else if(s.charAt(s.length()-1)!= '-') {
						s += "-";	
						textField.setText(s);
						expression += ",-";
					}else {
						textField.setText(s);	
					}
					num = false;
					dot = false;
				}
			});
			button25.setFont(new Font("Calibri Light", Font.BOLD, 23));
			butttonPanel.add(button25);
			
			// BUTTON FOR TAN
			JButton button26 = new JButton("tan");
			button26.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"tan(");
					}else {
						textField.setText("tan(");
					}
					expression += ",tan,(";
					num = false;
					dot = false;
				}
			});
			button26.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button26);
			
			// BUTTON FOR NUMBER (1)
			JButton button27 = new JButton("1");
			button27.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"1");
					}else {
						textField.setText("1");
					}
					if(num) {
						expression += "1";
					}else {
						expression += ",1";
					}
					num = true;
				}
			});
			button27.setBackground(new Color(100, 151, 177));
			button27.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button27);
			
			// BUTTON FOR NUMBER (2)
			JButton button28 = new JButton("2");
			button28.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"2");
					}else {
						textField.setText("2");
					}
					if(num) {
						expression += "2";
					}else {
						expression += ",2";
					}
					num = true;
				}
			});
			button28.setBackground(new Color(100, 151, 177));
			button28.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button28);
			
			// BUTTON FOR NUMBER (3)
			JButton button29 = new JButton("3");
			button29.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"3");
					}else {
						textField.setText("3");
					}
					if(num) {
						expression += "3";
					}else {
						expression += ",3";
					}
					num = true;
				}
			});
			button29.setBackground(new Color(100, 151, 177));
			button29.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button29);
		
			// BUTTON FOR ADDITION
			JButton button30 = new JButton("+");
			button30.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s=textField.getText();
					if(s.equals("0")) {
						expression += "0";
					}
					if(s.charAt(s.length()-1) == '-' || s.charAt(s.length()-1) == 'x' || s.charAt(s.length()-1) == (char)(247)) {
						String newString = s.substring(0,s.length()-1);
						newString += "+";
						textField.setText(newString);
						expression = expression.substring(0,expression.length()-1);
						expression += "+";
					}else if(s.charAt(s.length()-1)!= '+') {
						s += "+";	
						textField.setText(s);
						expression += ",+";
					}else {
						textField.setText(s);	
					}
					num = false;
					dot = false;
				}
			});
			button30.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button30);

			// BUTTON FOR NATURAL LOGARITHM(ln)
			JButton button31 = new JButton("ln");
			button31.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"ln(");
					}else {
						textField.setText("ln(");
					}
					expression += ",ln,(";
					num = false;
					dot = false;
				}
			});
			button31.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button31);

			// BUTTON FOR LOGARITHM
			JButton button32 = new JButton("<html><body><span>log<sub>10</sub></span></body></html>");
			button32.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			button32.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(! "0".equals(textField.getText())) {
						textField.setText(textField.getText()+"log(");
					}else {
						textField.setText("log(");
					}
					expression += ",log,(";
					num = false;
					dot = false;
				}
			});
			butttonPanel.add(button32);
			
			// BUTTON FOR NUMBER (0)
			JButton button33 = new JButton("0");
			button33.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if("0".equals(textField.getText())) {
						textField.setText("0");
					}else {
						textField.setText(textField.getText()+"0");
						if(num) {
							expression += "0";
						}
						else {
							expression += ",0";
						}
					}
					num = true;
				}
			});
			button33.setBackground(new Color(100, 151, 177));
			button33.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button33);

			// BUTTON FOR DECIMAL POINT
			JButton button34 = new JButton(".");
			button34.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s=textField.getText();
					if(s.charAt(s.length()-1)!= '.') {
						if(num && dot==false) {
							expression += ".";
							s += ".";
						}else if(num==false && dot ==false){
							expression += ",.";
							s += ".";
						}
					}
					num = true;
					dot = true;
					textField.setText(s);	
				}
			});
			button34.setFont(new Font("Calibri Light", Font.PLAIN, 17));
			butttonPanel.add(button34);

			// ACTUAL FUNCTIONING ON CLICKING "EQUAL TO" BUTTON
			JButton button35 = new JButton("=");
			button35.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					calculateMain();
						String solving = "";
						token.remove(token.size()-1);
						for(String i: token) {
							if(i.equals("/")) {
								solving += Character.toString((char)247);
							}else if(i.equals("sqrt")) {
								solving += Character.toString((char)8730);
							}else if(i.equals("pi")) {
								solving += Character.toString((char)960);
							}else {
								solving += i;
							}
						}
						exprlabel.setText(solving + "=");
						historyExpr.setText(solving + "=");
						textField.setText(result);
						historyAns.setText(result);
						historyEmpty.setText("");

						// String action = e.getActionCommand();
						// if(action.equals("=")) {
							if(e.getSource() == "=") {
								fileIsOpened.setText("");
								historyText.setText("History");
								historyUnderline.setText("  ");
								
								// IF STATEMENT TO HELP WRITE NEW LINES FROM THE HISTORY PANEL ONTO THE "HistoryLog.doc" file
								if(beqenter == 0) {
									// historyExpr and historyAns
									try {
										line = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter);
										historyExpr.setText(line);
										line1 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter1);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									historyAns.setText(line1);
									
								} else if(beqenter == 1) {
									// historyExpr and historyAns
									enter = enter + 2;
									enter1= enter1 + 2;
									try {
										line = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter);
										historyExpr.setText(line);
										line1 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter1);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									historyAns.setText(line1);
									
									// history1expr and history1ans
									enter2 = enter2 - 2;
									enter3 = enter3 - 2;
									try {
										line2 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter2);
										history1expr.setText(line2);
										line3 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter3);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history1ans.setText(line3);
									
								}else if(beqenter == 2) {
									// historyExpr and historyAns
									enter = enter + 2;
									enter1 = enter1 + 2;
									try {
										line = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter);
										historyExpr.setText(line);
										line1 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter1);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									historyAns.setText(line1);
									
									// history1expr and history1ans
									enter2 = enter2 + 2;
									enter3 = enter3 + 2;
									try {
										line2 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter2);
										history1expr.setText(line2);
										line3 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter3);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history1ans.setText(line3);
									
									// history2expr and history2ans
									enter4 = enter4 - 4;
									enter5 = enter5 - 4;
									try {
										line4 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter4);
										history2expr.setText(line4);
										line5 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter5);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history2ans.setText(line5);
										
								} else if (beqenter == 3) {
									// historyExpr and historyAns
									enter = enter + 2;
									enter1 = enter1 + 2;
									try {
										line = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter);
										historyExpr.setText(line);
										line1 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter1);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									historyAns.setText(line1);
									
									// history1expr and history1ans
									enter2 = enter2 + 2;
									enter3 = enter3 + 2;
									try {
										line2 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter2);
										history1expr.setText(line2);
										line3 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter3);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history1ans.setText(line3);
									
									// history2expr and history2ans
									enter4 = enter4 + 2;
									enter5 = enter5 + 2;
									try {
										line4 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter4);
										history2expr.setText(line4);
										line5 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter5);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history2ans.setText(line5);
									
									// history3expr and history3ans
									enter6 = enter6 - 6;
									enter7 = enter7 - 6;
									try {
										line6 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter6);
										history3expr.setText(line6);
										line7 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter7);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history3ans.setText(line7);
									
								} else if (beqenter >= 4) {
									// historyExpr and historyAns
									enter = enter + 2;
									enter1 = enter1 + 2;
									try {
										line = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter);
										historyExpr.setText(line);
										line1 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter1);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									historyAns.setText(line1);
					
									// history1expr and history1ans
									enter2 = enter2 + 2;
									enter3 = enter3 +2;
									try {
										line2 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter2);
										history1expr.setText(line2);
										line3 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter3);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history1ans.setText(line3);
									
									// history2expr and history2ans
									enter4 = enter4 + 2;
									enter5 = enter5 + 2;
									try {
										line4 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter4);
										history2expr.setText(line4);
										line5 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter5);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history2ans.setText(line5);
									
									// history3expr and history3ans
									enter6 = enter6 + 2;
									enter7 = enter7 + 2;
									try {
										line6 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter6);
										history3expr.setText(line6);
										line7 = Files.readAllLines(Paths.get("HistoryLog.doc")).get(enter7);
									} catch (IOException e1) {									
										e1.printStackTrace();
									}
									history3ans.setText(line7);
								}
								beqenter++;
							};
						// };
						try {
							historyfw.write(solving + "=" +"\n"+ result + "\n");
							historyfw.close();
						} catch (IOException e1) {							
							e1.printStackTrace();
						}
						
						expression = result;
						dot = true;
						num = true;
					token.clear();
					
				}
			});
			button35.setBackground(Color.cyan);
			button35.setFont(new Font("Calibri Light", Font.PLAIN, 22));
			butttonPanel.add(button35);

		} catch(IOException ab) {
			// JOptionPane.showMessageDialog(null, "error");
		} catch(IndexOutOfBoundsException abc) {
			//JOptionPane.showMessageDialog(null, "error 1");
		}	
	}
}