package q;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ProbaWlasciwa extends JFrame
{  			
	
	private static final long serialVersionUID = 1L;
	private JMenuBar menuBar; 
	private JMenu menu; 												 
	private JMenuItem  menuItem1, menuItem2, menuItem3, menuItem4; 								//menu 																				
	private JPanel leftPanel, rightPanel, upPanel, downPanel, centerPanel;       
	
	private JSlider slider; 	
	private JButton button3; 						//góra 
	private JLabel label; 
	
	private JButton button4, button5;              //dó³
	
	private Polygon polygon;                       //œrodek
	
	private TitledBorder PolygonTitle; 
	private ButtonGroup ButtonGroup;                //lewa strona 
	private JRadioButton button1, button2; 
	
	private JLabel labelx, labely; 					//prawa strona 
	
	private static final int SLIDER_MIN = 0; 
	private static final int SLIDER_MAX = 20; 							//zakres slidera 
	private static final int SLIDER_INIT = 0; 
	
	private GridLayout layout; 
	private static int numberOfTextFieldsRows = SLIDER_INIT;         
	private static final int numberOfTextFieldsColumns = 2; 		 // pobieramy tylko dwie wspó³rzêdne 
	private JLabel label1, label2; 
	private JTextField[][] textField;                               //bo kolumny
                                  	
	private static final int radius = 100; 
	
	String selectedPanel = "regular";
	
	private int coox[];                //koordynaty dla wierzcho³ka
	private int cooy[]; 
	
	private int lineWidth = 1; 					//domyslna grubosc linii ktora bedziemy rysowac 
	private Color lineColor = Color.black;  	//domyslny kolor linii  
	
	private JTextField textfield; 
	
 	public ProbaWlasciwa() throws HeadlessException 
 	{
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(720,620);
		this.setLayout(new BorderLayout()); 
		this.setResizable(false);
		
		JMenuBar menuBar = new JMenuBar(); 
		JMenu menu = new JMenu ("M E N U");
		JMenuItem menuItem1 = new JMenuItem("1 px");
		menuItem1.addActionListener(new ActionListener() 
		{
			@Override 
			public void actionPerformed(ActionEvent e) 
			{                          //po naciœniêciu zmieniamy gruboœæ, któr¹ rysuje siê wielok¹t
				lineWidth = 1; 
				getContentPane().repaint(); 
			}			
		});
		
		JMenuItem menuItem2 = new JMenuItem("10 px");
		menuItem2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{                       //tu te¿
				lineWidth = 10; 
				getContentPane().repaint();
			}			
		});
		
		JMenuItem menuItem3 = new JMenuItem("15 px");
		menuItem3.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{						//tutaj te¿
				lineWidth = 15; 
				getContentPane().repaint();
			}			
		});
		
		JMenuItem menuItem4 = new JMenuItem("Author"); 
		menuItem4.addActionListener(new ActionListener()
		{                                 //wyskakuje okienko dialogowe
			@Override 
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "All made by Wiktoria Kasprzak, student ID: 298109", "AUTHOR INFO", JOptionPane.INFORMATION_MESSAGE);	
			}	
		});
		
		menu.add(menuItem1); 
		menu.add(menuItem2);
		menu.add(menuItem3); 
		menu.add(menuItem4); 
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
				
		
		upPanel = new JPanel();
		upPanel.setLayout(new FlowLayout());
		label = new JLabel("Choose the number of vertices");                                          //ustawianie slidera
		slider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
		slider.setPreferredSize(new Dimension(300,50)); 
		slider.setMajorTickSpacing(3); 																	   //gestosc g³ównych przedzia³ów      
		slider.setMinorTickSpacing(1); 															        //gestosc ma³ych przedzia³ów
		slider.setPaintTicks(true); 			
		slider.setPaintLabels(true); 			
    	slider.addChangeListener(new SliderChangeListener()); 
	
    	button3 = new JButton("Draw"); 
    	button3.addActionListener(new ActionListener() 
    	{
    		@Override
    		public void actionPerformed(ActionEvent e) 
    		{
    			                                                 
    		}
    		
    		
    	});       
    	
    	upPanel.add(label);	
 		upPanel.add(slider);
		upPanel.add(button3);
		
		this.add(upPanel, BorderLayout.PAGE_START);  				//tutaj to mamy górê zaliczon¹ chyba 
		
		
		ButtonGroup = new ButtonGroup(); 
		leftPanel = new JPanel(); 
		leftPanel.setLayout(new GridLayout(2,1));
	//	PolygonTitle = BorderFactory.createTitleBorder("Drawable Polygon"); 
		JRadioButton button1 = new JRadioButton("Regular"); 
		button1.addActionListener(new ActionListener()
		{                           //przycisk, dzieki ktoremu bedziemy mogli rysowaæ foremny wielok¹t
			@Override 
			public void actionPerformed(ActionEvent e)
			{
				selectedPanel = "regular"; 
				drawRegular(numberOfTextFieldsRows); 							//tyle ma wierzcho³ków
				fillTextFields(numberOfTextFieldsRows, numberOfTextFieldsColumns); 
			}
			
		});
		button1.setSelected(true); 
		
		JRadioButton button2 = new JRadioButton("Random"); 
		button2.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{                              //przycisk, który narysuje losow¹ figurê z tyloma wierzcho³kami, z iloma chcemy
				selectedPanel = "random"; 
				drawRandom(numberOfTextFieldsRows); 
				fillTextFields(numberOfTextFieldsRows, numberOfTextFieldsColumns); 
			}
			
		});
		button2.setSelected(true);
		
		ButtonGroup.add(button1);
		ButtonGroup.add(button2);
		leftPanel.add(button1);
		leftPanel.add(button2);
		this.add(leftPanel, BorderLayout.LINE_START);
		
		setupRightPanel(4,2);                                //prawa strona - bedzie poxniej
		
		centerPanel = new JPanel()
		{
			 protected void paintComponent(Graphics g) 
			 {
				    super.paintComponent(g);
				     BasicStroke stroke = new BasicStroke(lineWidth);                              //dziêki temu mo¿emy widzieæ co siê w ogóle rysuje
				     Graphics2D g2d = (Graphics2D) g;
				     g2d.translate(this.getSize().width / 2, this.getSize().height / 2);
				     g2d.setStroke(stroke);
				     g2d.setColor(lineColor);
				     g2d.drawPolygon(polygon);
			 }
		};
		
        centerPanel.setBackground(Color.white);					//œrodek
        this.add(centerPanel, BorderLayout.CENTER);
        
        
        
        downPanel = new JPanel();
        downPanel.setLayout(new FlowLayout());                           //panel dolny
        button4 = new JButton("Background color");
        button4.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                final JColorChooser colorChooser = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(ProbaWlasciwa.this, "Choose background color", true, colorChooser, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {                                             //wybieranie koloru t³a 
                        centerPanel.setBackground(colorChooser.getColor());
                    }
                }, new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                                                                                  // w ty miejscu nic siê nie dzieje 
                    }
                });
                dialog.setVisible(true);
            }
        });
        button5 = new JButton("Polygon color");  
        button5.addActionListener(new ActionListener() 
        {
        	@Override 
        	 public void actionPerformed(ActionEvent e) 
        	{
                final JColorChooser colorChooser = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(ProbaWlasciwa.this, "Select line color", true, colorChooser, new ActionListener() {
                    @Override																					
                    public void actionPerformed(ActionEvent e)
                    {								//wybieranie koloru linii 
                        lineColor = colorChooser.getColor();
                        getContentPane().repaint();
                    }
                }, new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent e) 
                    {
                    																			//tutaj znowu siê nic nie dzieje
                    }
                });
                dialog.setVisible(true);
            }
        });
        
        downPanel.add(button4);
        downPanel.add(button5); 
        this.add(downPanel, BorderLayout.PAGE_END);  
        
		
	}
 	
 	 private void fillTextFields(int rows, int cols) 
 	 {
         for (int m = 0; m < rows; m++)
         {
             for (int n = 0; n < cols; n++) 
             {
                 if (m % 2 == 0 && n % 2 == 0)
                 {
                     coox[m] = coox[m] + this.getSize().width / 2;
                     textField[m][n].setText(String.format("%d", coox[m]));              // tutaj wype³niamy pola w prawym panelu 
                 } else if (m % 2 == 0 && n % 2 != 0) {
                     cooy[m] = cooy[m] + this.getSize().height / 2;
                     textField[m][n].setText(String.format("%d", cooy[m]));
                 } else if (m % 2 != 0 && n % 2 == 0) {
                     coox[m] = coox[m] + this.getSize().width / 2;
                     textField[m][n].setText(String.format("%d", coox[m]));
                 } else if (m % 2 != 0 && n % 2 != 0) {
                     cooy[m] = cooy[m] + this.getSize().height / 2;
                     textField[m][n].setText(String.format("%d", cooy[m]));
                 }
             }
         }
     }  

 	 
 	 
 	 
 	 private void drawRandom(int vertices) 
 	 {														 //metoda na rysowanie losowej figury o zadanej il. wierzcho³ków
 		Random generator = new Random(); 
 		coox = new int[vertices];
 		cooy = new int[vertices]; 
 		
 		for (int i=0; i < vertices; i++)
 		{
 			coox[i] = generator.nextInt(501)-350;
 			cooy[i] = generator.nextInt(301)-200; 
 		}
 		polygon = new Polygon(coox, cooy, vertices);
        this.getContentPane().repaint();
 	} 
 	 
 	private void drawRegular(int vertices) 
 	{ 														//metoda na rysowanie foremnego wielok¹tu 
 		coox = new int[vertices]; 
 		cooy = new int[vertices]; 
 		
 		for (int i=0; i < vertices; i++) 
 		{
 			
 			coox[i] = (int) (radius * Math.cos((Math.PI / 2 + 2 * Math.PI * i) / vertices));
 			cooy[i] = (int) (radius * Math.sin((Math.PI / 2 + 2 * Math.PI * i) / vertices));
 		}
 			
 		polygon = new Polygon(coox, cooy, vertices); 
 		this.getContentPane().repaint(); 
 	}
	
 	
 	 private void setupRightPanel(int rows, int cols)
 	 {
         if (rightPanel != null) 
         {
             this.remove(rightPanel);
             this.revalidate();
         }

         rightPanel = new JPanel();                                            //prawy panel, który zmienia siê w zale¿noœæi od tego 
         layout = new GridLayout(rows, cols);                                         //         ile mamy wierzcho³ków 
         rightPanel.setLayout(layout);
         labelx = new JLabel("X pos.");
         labely = new JLabel("Y pos.");
         rightPanel.add(labelx);
         rightPanel.add(labely);
         textField = new JTextField[rows][cols];
         for (int m = 0; m < rows - 1; m++) 
         {
             for (int n = 0; n < cols; n++) 
             {
                 textField[m][n] = new JTextField();
                 rightPanel.add(textField[m][n]);
             }
         }
         this.add(rightPanel, BorderLayout.LINE_END);
         this.revalidate();
     }
 	
	 private class SliderChangeListener implements ChangeListener 
	 {								//klasa implementacyjna suwaka
	        @Override
	        public void stateChanged(ChangeEvent arg0)
	        {
	            JSlider source = (JSlider) arg0.getSource();
	            numberOfTextFieldsRows = slider.getValue();
	            setupRightPanel(numberOfTextFieldsRows + 1, numberOfTextFieldsColumns);
	            if (selectedPanel == "regular") {
	                drawRegular(source.getValue());
	            } else if (selectedPanel == "random") 
	            {
	                drawRandom(source.getValue());
	            }
	            fillTextFields(numberOfTextFieldsRows, numberOfTextFieldsColumns);
	        }
	    } 
 	 
 	 
	 public static void main(String[] args) 
	    {

	        ProbaWlasciwa frame = new ProbaWlasciwa();                         //tutaj mamy maina w koñcu 
	        frame.setTitle("A simple solution for drawing a polygon!!!"); 
	        frame.fillTextFields(numberOfTextFieldsRows, numberOfTextFieldsColumns);
	        frame.setVisible(true);

	    } 
	
}
	 
	 
	

	
