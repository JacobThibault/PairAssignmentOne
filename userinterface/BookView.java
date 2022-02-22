package userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.Properties;

import impresario.IModel;

public class BookView extends View {
    protected TextField barcode;
    protected TextField title;
    protected TextField author1;
    protected TextField publisher;
    protected TextField yearPublished;
    protected TextField isbn;
    protected TextField suggestedPrice;

    protected ComboBox status;

    //for error messaging
    protected MessageView statusLog;


    public BookView(IModel model, String classname) {
        super(model, classname);
    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    protected void processAction(EventObject evt) {

    }
}
