package shenkar.finalProject.googleDocs.MVVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 */
public class View implements IView {
    IViewModel viewModel;

    // main frame
    private JFrame frame;

    // form labels
    private JLabel appTitle;
    private JLabel titleLabel;
    private JLabel authorNameLabel;
    private JLabel todoBulletsLabel;
    private JLabel summeryLabel;

    // messages
    private JLabel actionMessage;
    private JLabel requireFieldsMessage;

    // form inputs
    private JTextField titleField;
    private JTextField authorNameField;
    private JTextArea summeryField;
    private JTextField[] bulletsFields = new JTextField[10];

    // limit for bulletsFields array
    private int limit=5;

    // form buttons
    private JButton addTodoBulletButton;
    private JButton resetButton;
    private JButton submitButton;
    private JButton createAnotherOneButton;
    private JButton exitButton;

    // panels
    private JPanel eastPanel;
    private JPanel westPanel;
    private JPanel centerPanel;
    private JPanel northPanel;
    private JPanel southPanel;

    // listener
    private BListener listener;


    class BListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            requireFieldsMessage.setText("");
            switch (e.getActionCommand())
            {
                case "Add Todo":
                    int lastLimit=limit;
                    limit=limit*2;
                    for(int i=lastLimit;i<limit;++i){
                        centerPanel.add(bulletsFields[i]);
                    }
                    addTodoBulletButton.setVisible(false);
                    centerPanel.add(summeryLabel);
                    centerPanel.add(summeryField);
                    break;

                case "Reset":
                    cleanFrame();
                    break;

                case "Submit":
                    if(!checkInputs())
                    {
                        requireFieldsMessage.setText("Try again and fill all the fields !");
                    }
                    else {
                        requireFieldsMessage.setText("Sending..");
                        String allBullets = "";
                        String endl = "\n";
                        for(int i=0;i<limit;++i)
                        {
                            if (!bulletsFields[i].getText().equals(i + 1 + ":") && !bulletsFields[i].getText().equals(""))
                            {
                                //for the last bullet, don't need \n
                                if(i == limit-1)
                                {
                                    endl = "";
                                }
                                allBullets += bulletsFields[i].getText().substring(2) +  endl;
                            }
                        }
                        try
                        {
                            viewModel.createTemplatedDocFromModel(titleField.getText(), authorNameField.getText(),summeryField.getText(),allBullets);
                        }
                        catch (MVVMException ex)
                        {
                            MyLogger.Log.error(ex.getMessage());
                            ex.printStackTrace();
                        }
                    }
                    break;

                case "Create another one":
                    appTitle.setVisible(true);
                    actionMessage.setVisible(false);
                    centerPanel.setVisible(true);
                    createAnotherOneButton.setVisible(false);
                    exitButton.setVisible(false);
                    if(limit!=10)
                    {
                        addTodoBulletButton.setVisible(true);
                    }
                    resetButton.setVisible(true);
                    submitButton.setVisible(true);
                    cleanFrame();
                    break;

                case "No, Thanks!":
                    MyLogger.Log.trace("Program closing");
                    System.exit(0);


            }
        }
    }

    /**
     *
     */
    public void cleanFrame()
    {
        titleField.setText("");
        authorNameField.setText("");
        summeryField.setText("");
        for(int i=0;i<limit;++i)
        {
            bulletsFields[i].setText(i+1 +":");
        }
    }

    /**
     * @return
     */
    private boolean checkInputs()
    {
        String empty="";
        return !titleField.getText().equals(empty) && !authorNameField.getText().equals(empty) && !summeryField.getText().equals(empty);
    }


    /**
     *
     */
    public View()
    {
        this.appTitle = new JLabel("EasySumm");

        this.titleLabel=new JLabel("Title");
        this.authorNameLabel=new JLabel("Author Name");
        this.todoBulletsLabel=new JLabel("Todo Bullets");
        this.summeryLabel=new JLabel("Summery");

        this.titleField=new JTextField();
        this.authorNameField =new JTextField();
        this.summeryField=new JTextArea();
        for(int i=0;i<bulletsFields.length;++i)
        {
            this.bulletsFields[i]=new JTextField(i+1+":");
        }
        this.submitButton = new JButton("Submit");
        this.resetButton = new JButton("Reset");
        this.addTodoBulletButton = new JButton("More Bullets");
        this.createAnotherOneButton = new JButton("Create Another One");
        this.exitButton = new JButton("No, Thanks!");

        this.actionMessage = new JLabel("Your document is already ready please wait :)");
        this.requireFieldsMessage=new JLabel();

        this.eastPanel=new JPanel();
        this.westPanel=new JPanel();
        this.centerPanel = new JPanel();
        this.southPanel = new JPanel();
        this.northPanel = new JPanel();
        this.listener = new BListener();
    }

    /**
     * @throws MVVMException
     */
    public void start() throws MVVMException
    {
        frame = new JFrame("EasySumm");
        frame.setLayout(new BorderLayout());
        centerPanel.setLayout(new GridLayout(18,1));

        appTitle.setSize(300, 30);
        appTitle.setFont(new Font("Arial", Font.PLAIN, 30));

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        actionMessage.setFont(new Font("Arial", Font.PLAIN, 30));
        actionMessage.setVisible(false);
        authorNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        todoBulletsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        summeryLabel.setFont(new Font("Arial", Font.BOLD, 20));

        submitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        submitButton.setForeground(Color.GREEN);
        submitButton.setBackground(Color.BLACK);
        addTodoBulletButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addTodoBulletButton.setForeground(Color.WHITE);
        addTodoBulletButton.setBackground(Color.BLACK);
        createAnotherOneButton.setFont(new Font("Arial", Font.PLAIN, 14));
        createAnotherOneButton.setForeground(Color.WHITE);
        createAnotherOneButton.setBackground(Color.BLACK);
        createAnotherOneButton.setVisible(false);

        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.RED);
        exitButton.setVisible(false);

        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.setForeground(Color.red);
        resetButton.setBackground(Color.BLACK);

        northPanel.add(appTitle);
        northPanel.add(actionMessage);

        centerPanel.add(titleLabel);
        centerPanel.add(titleField);
        centerPanel.add(authorNameLabel);
        centerPanel.add(authorNameField);
        centerPanel.add(todoBulletsLabel);
        for(int i=0;i<limit;++i)
        {
            centerPanel.add(bulletsFields[i]);
        }
        centerPanel.add(summeryLabel);
        centerPanel.add(summeryField);

        southPanel.add(addTodoBulletButton);
        southPanel.add(submitButton);
        southPanel.add(resetButton);
        southPanel.add(requireFieldsMessage);
        southPanel.add(createAnotherOneButton);
        southPanel.add(exitButton);

        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(northPanel,BorderLayout.NORTH);
        frame.add(eastPanel,BorderLayout.EAST);
        frame.add(westPanel,BorderLayout.WEST);
        frame.add(southPanel,BorderLayout.SOUTH);

        addTodoBulletButton.addActionListener(listener);
        resetButton.addActionListener(listener);
        submitButton.addActionListener(listener);
        createAnotherOneButton.addActionListener(listener);
        exitButton.addActionListener(listener);

        frame.setSize(800,800);
        frame.setVisible(true);

        // fire event when user close window
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                MyLogger.Log.trace("Program closing");
                System.exit(0);
            }
        });
    }


    @Override
    public void setIViewModel(IViewModel viewModel) throws MVVMException
    {
        this.viewModel = viewModel;
    }

    /**
     * @param titleName
     * @param authorName
     * @param summary
     * @param bullets
     * @throws MVVMException
     */
    //happen after we have all values from UI
    @Override
    public void textToModel(String titleName, String authorName, String summary, String bullets) throws MVVMException
    {
        viewModel.createTemplatedDocFromModel(titleName,authorName,summary,bullets);
        // will show to client (creating doc....)
        showWaitTillDocCreate();
        return;
    }

    /**
     * @throws MVVMException
     */
    @Override
    public void showWaitTillDocCreate() throws MVVMException
    {
        // here we create a view of creating doc or something until doc done...
        actionMessage.setVisible(true);
        centerPanel.setVisible(false);
        appTitle.setVisible(false);
        addTodoBulletButton.setVisible(false);
        resetButton.setVisible(false);
        submitButton.setVisible(false);
        // wait message
    }

    /**
     * @param newDocId
     * @throws MVVMException
     */
    @Override
    public void showSuccessToCreateDoc(String newDocId) throws MVVMException
    {
        // success message + linkToNewDoc
        String linkToNewDoc = "https://docs.google.com/document/d/" + newDocId;
        actionMessage.setText("Ready! link: "+linkToNewDoc);
        appTitle.setVisible(false);
        createAnotherOneButton.setVisible(true);
        exitButton.setVisible(true);
    }


}
