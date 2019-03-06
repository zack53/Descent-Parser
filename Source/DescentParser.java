/*File: DescentParser.java
 *Author: Zackary Scott
 *Date: 6-17-2018
 *Purpose: Creates a GUI by recursively parsing input from a text file.
 */
package descentparser;

import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**The DescentParser class scans a file to build a GUI by using the 
 * grammar given in the project file.
 */
public class DescentParser {
    //variables to get input and convert to arrayList
    private static Scanner scanFile;
    private static File file;
    private static ArrayList<String> inputList = new ArrayList<>();
    private static String buildString = "";
    private static String[] lineToSplit;
    //variables for JFrame and JPanel
    private static JFrame myJFrame;
    private static JPanel myJPanel;
    //variables to make getWidgets
    private static ButtonGroup radioButtonGroup;
    private static JRadioButton radioButton;
    private static boolean isMyJFrame = true;
    //variable to walk through arrayList
    private static int currentToken = 0;
    private final JFileChooser FILE_CHOOSER = new JFileChooser(".");
    private final JButton PARENT = new JButton();
    DescentParser(){
        //creates GUI to choose file
        FILE_CHOOSER.setDialogTitle("Seaport Program");
        FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter(
                                                "Text file", "txt"));
        if(FILE_CHOOSER.showOpenDialog(PARENT) == JFileChooser.APPROVE_OPTION){
            //tries to get file and create scanner with target file
             file = FILE_CHOOSER.getSelectedFile();
        }
    }
    private static JPanel errorPanel = new JPanel(new FlowLayout());
    //main method
    public static void main(String[] args) {
        DescentParser descentParser = new DescentParser();
        descentParser.getInputToParse();
        //creates GUI
        try{
            descentParser.createGUI();
        }catch(IndexOutOfBoundsException e){
            JOptionPane.showMessageDialog(errorPanel,
                            "Program did not run.",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
    }//end of main method
    //sets up the input from the file to be used by the createGUI method
    private void getInputToParse(){
        //tries to open a file to read and format
        try{
            scanFile = new Scanner(file);
            //scans file to create a string
            while(scanFile.hasNext()){
                buildString+= scanFile.next() +" ";
            }
            //splits the string to an array
            lineToSplit = buildString.trim().split(
                                "(?<=[,\\s+\"():.;])|(?=[,\\s+\"():.;])");
            //puts the array to a list with no spaces
            for(String words:lineToSplit){
                if(!words.equals(" ")){
                    inputList.add(words);
                }
            }
        }catch(FileNotFoundException v){
            JOptionPane.showMessageDialog(errorPanel,
                            "File Not Found",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
    }//end of getInputToParse method
    //createsGUI from file input
    private void createGUI(){
        int guiWidth;
        int guiHeight;
        //Checks to see if this is a window
        if (inputList.get(currentToken).toLowerCase().equals("window")) {
            myJFrame = new JFrame();
            myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myJFrame.setLocationRelativeTo(null);
            currentToken++;
        }else{
            JOptionPane.showMessageDialog(errorPanel,
                            "Beginning of file doesn't have Window",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //Sets title of JFrame
        if (inputList.get(currentToken).equals("\"")) {
            currentToken++;
            myJFrame.setTitle(inputList.get(currentToken));
            currentToken++;
            if(inputList.get(currentToken).equals("\"")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                                "JFrame Title improperly formmatted",
                                "Message",
                                JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }else{
            JOptionPane.showMessageDialog(errorPanel,
                            "JFrame Title improperly formmatted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //sets size of JFrame
        if(inputList.get(currentToken).equals("(")){
            currentToken++;
            if(inputList.get(currentToken).chars().allMatch(
                                                    Character::isDigit)){
                guiWidth = Integer.parseInt(inputList.get(currentToken));
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Size for JFrame isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(inputList.get(currentToken).equals(",")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Size for JFrame improperly formmatted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(inputList.get(currentToken).chars().allMatch(
                                                    Character::isDigit)){
                guiHeight = Integer.parseInt(inputList.get(currentToken));
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Size for JFrame isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(inputList.get(currentToken).equals(")")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Size for JFrame improperly formmatted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            myJFrame.setSize(guiWidth,guiHeight);
        }else{
            JOptionPane.showMessageDialog(errorPanel,
                            "Size for JFrame improperly formmatted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        //calls layoutParser method
        if(layoutParser()){
            //calls getWidgets method
            if(getWidgets()){
                if(inputList.get(currentToken).toLowerCase().equals("end")){
                    currentToken++;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Error with an End format",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                //if is end and period sets myJFrame visible
                if(inputList.get(currentToken).equals(".")){
                    myJFrame.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Error with an End format",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }//end of createGUI method
    //layoutParser the recursive method method
    private static boolean layoutParser(){
        if(inputList.get(currentToken).toLowerCase().equals("layout")){
            currentToken++;
            if(layoutType()){
                if(inputList.get(currentToken).equals(":")){
                    currentToken++;
                    return true;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Layout is missing colon",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }else{
            JOptionPane.showMessageDialog(errorPanel,
                            "Layout is misspelled",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }//end of layoutParser method
    //layoutType creates the layout
    private static boolean layoutType(){
        int numOfRow = 0;
        int numOfColumn = 0;
        int horizontalGap = 0;
        int verticalGap = 0;
        //creates a flow layout for JFrame or JPanel
        if(inputList.get(currentToken).toLowerCase().equals("flow")){
            if(isMyJFrame){
                myJFrame.setLayout(new FlowLayout());
                currentToken++;
            }else{
                myJPanel.setLayout(new FlowLayout());
                currentToken++;
            }
            return true;
        //creates grid layout for JFrame or JPanel
        }else if(inputList.get(currentToken).toLowerCase().equals("grid")){
            currentToken++;
            if(inputList.get(currentToken).equals("(")){
                currentToken++;
                if(inputList.get(currentToken).chars().allMatch( 
                                                Character::isDigit)){
                    numOfRow = Integer.parseInt(inputList.get(currentToken));
                    currentToken++;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout input isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                if(inputList.get(currentToken).equals(",")){
                    currentToken++;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout Format Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                if(inputList.get(currentToken).chars().allMatch( 
                                                Character::isDigit)){
                    numOfColumn = Integer.parseInt(
                                                inputList.get(currentToken));
                    currentToken++;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout input isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                if(inputList.get(currentToken).equals(")")){
                    currentToken++;
                    if(isMyJFrame){
                        myJFrame.setLayout(new GridLayout(
                                                    numOfRow,numOfColumn));
                    }else{
                        myJPanel.setLayout(new GridLayout(
                                                    numOfRow,numOfColumn));
                    }
                    return true;
                }else if(inputList.get(currentToken).equals(",")){
                    currentToken++;
                    if(inputList.get(currentToken).chars().allMatch(
                                                    Character::isDigit)){
                        horizontalGap = Integer.parseInt(
                                inputList.get(currentToken));
                        currentToken++;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout input isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    if(inputList.get(currentToken).equals(",")){
                        currentToken++;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout Format Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    if(inputList.get(currentToken).chars().allMatch( 
                                                    Character::isDigit)){
                        verticalGap = Integer.parseInt(
                                inputList.get(currentToken));
                        currentToken++;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout isn't numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    if(inputList.get(currentToken).equals(")")){
                        currentToken++;
                        if(isMyJFrame){
                            myJFrame.setLayout(new GridLayout(
                                    numOfRow,numOfColumn,
                                    horizontalGap,verticalGap));
                        }else{
                            myJPanel.setLayout(new GridLayout(
                                    numOfRow,numOfColumn,
                                    horizontalGap,verticalGap));
                        }
                        return true;
                    }
                } else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout Format Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Grid Layout Format Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(errorPanel,
                            "Layout can only be Flow or Grid",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }//end of layoutType
    //getWidgets is the recursive method
    private static boolean getWidgets(){
        if(getWidget()){
            if(getWidgets()){
                return true;
            }else if(inputList.get(currentToken).toLowerCase().equals("end")){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }//end of getWidgets method
    //getWidget method creates widget depending on what is parsed
    private static boolean getWidget(){
        int textFieldLength = 0;
        //creates button if parsed
        if(inputList.get(currentToken).toLowerCase().equals("button")){
            currentToken++;
            if(inputList.get(currentToken).equals("\"")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Widget Button Layout Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            buildString = inputList.get(currentToken);
            currentToken++;
            if(inputList.get(currentToken).equals("\"")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Widget Button Layout Error",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            if(inputList.get(currentToken).equals(";")){
                if(isMyJFrame){
                            JButton button = new JButton(buildString);
                            myJFrame.add(button);
                        }
                        else{
                            JButton button = new JButton(buildString);
                            myJPanel.add(button);
                        }
                        currentToken++;
                        return true;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Widget Button missing semi-colon",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        //creates group widget if parsed
        }else if(inputList.get(currentToken).toLowerCase().equals("group")){
            radioButtonGroup = new ButtonGroup();
            currentToken++;
            if(radioButtonsParser()){
                if(inputList.get(currentToken).toLowerCase().equals("end")){
                    currentToken++;
                    if(inputList.get(currentToken).equals(";")){
                        currentToken++;
                        return true;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "Widget Group missing semi-colon",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        //creates label widget if parsed
        }else if(inputList.get(currentToken).toLowerCase().equals("label")){
            currentToken++;
            if(inputList.get(currentToken).equals("\"")){
                currentToken++;
                if(inputList.get(currentToken).equals("\"")){
                    buildString = " ";
                    currentToken--;
                }else{
                    buildString = inputList.get(currentToken);
                }
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Error Label Not Formatted Properly",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            if(inputList.get(currentToken).equals("\"")){
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Error Label Not Formatted Properly",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            if(inputList.get(currentToken).equals(";")){
                if(isMyJFrame){
                    myJFrame.add(new JLabel(buildString));
                }else{
                    myJPanel.add(new JLabel(buildString));
                }
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Error Label Not Formatted Properly",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            return true;
        //creates panel widget if parsed
        }else if(inputList.get(currentToken).toLowerCase().equals("panel")){
            currentToken++;
            if(isMyJFrame){
                myJFrame.add(myJPanel = new JPanel());
            }
            else{
                myJPanel.add(myJPanel = new JPanel());
            }
            isMyJFrame = false;
             if(layoutParser()){
                if(getWidgets()){
                    if(inputList.get(currentToken).toLowerCase().equals(
                                                                "end")){
                        currentToken++;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "End of Widget improperly formatted missing End",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (inputList.get(currentToken).equals(";")) {
                        currentToken++;
                        isMyJFrame = true;
                        return true;
                    }else{
                        JOptionPane.showMessageDialog(errorPanel,
                            "End of Widget improperly formatted",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        //creates textfield widget if parsed
        }else if(inputList.get(currentToken).toLowerCase().equals(
                                                "textfield")){
            currentToken++;
            if(inputList.get(currentToken).chars().allMatch( 
                                        Character::isDigit)){
                textFieldLength = Integer.parseInt(
                                        inputList.get(currentToken));
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "TextField Length not numeric",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            if(inputList.get(currentToken).equals(";")){
                if(isMyJFrame){
                    myJFrame.add(new JTextField(textFieldLength));
                }else{
                    myJPanel.add(new JTextField(textFieldLength));
                }
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Textfield improperly formatted no colon",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            currentToken++;
            return true;
        }else if(!inputList.get(currentToken).toLowerCase().equals("end")){
            JOptionPane.showMessageDialog(errorPanel,
                            "Widget is formatted wrong:"
                                    +inputList.get(currentToken),
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }//end of getWidget method
    //radioButtonsParser the recursive method
    private static boolean radioButtonsParser(){
        if(radioButtonParser()){
            if(radioButtonsParser()){
                return true;
            }
            return true;
        }
        return false;
    }//end of radioButtonsParser method
    //radioButtonParser the radio button creator method
    private static boolean radioButtonParser(){
        //creates radio button if parsed
        if (inputList.get(currentToken).toLowerCase().equals("radio")){
            currentToken++;
            if (inputList.get(currentToken).equals("\"")){
                currentToken++;
                buildString = inputList.get(currentToken);
                currentToken++;
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Radio button is formatted wrong",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
            if (inputList.get(currentToken).equals("\"")){
                currentToken++;
                if (inputList.get(currentToken).equals(";")){
                    radioButton = new JRadioButton(buildString);
                    radioButtonGroup.add(radioButton);
                    if (isMyJFrame) {
                        myJFrame.add(radioButton);
                    }else{
                        myJPanel.add(radioButton);
                    }
                    currentToken++;
                    return true;
                }else{
                    JOptionPane.showMessageDialog(errorPanel,
                            "Radio button is formatted "
                                    + "wrong missing semi-colon",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(errorPanel,
                            "Radio button is formatted wrong",
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(!inputList.get(currentToken).toLowerCase().equals("end")){
            JOptionPane.showMessageDialog(errorPanel,
                            "Radio button is formatted wrong:"
                                    +inputList.get(currentToken),
                            "Message",
                            JOptionPane.INFORMATION_MESSAGE);
        }
        return false;
    }//end of radioButtonParser
}//end of DescentParser class