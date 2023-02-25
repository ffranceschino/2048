/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DB extends JFrame {
    
    private static DB instance = null;

    private JFrame frame;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtRegUsername;
    private JPasswordField txtRegPassword;
    private Connection con;
    private final boolean connected = false;
    private static String currentUsername = null;

    private DB() {
        createConnection();
        frame = new JFrame();
        frame.setBounds(100, 100, 650, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(null);
        
        JLabel lblLogin = new JLabel("Login");
        lblLogin.setForeground(new Color(0, 153, 153));
        lblLogin.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblLogin.setBounds(50, 11, 77, 25);
        frame.getContentPane().add(lblLogin);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(new Color(0, 153, 153));
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(50, 47, 77, 14);
        frame.getContentPane().add(lblUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(new Color(0, 153, 153));
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(50, 72, 77, 14);
        frame.getContentPane().add(lblPassword);

        txtUsername = new JTextField();
        txtUsername.setBounds(139, 45, 200, 20);
        txtUsername.setColumns(10);
        frame.getContentPane().add(txtUsername);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(139, 70, 200, 20);
        frame.getContentPane().add(txtPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener((ActionEvent e) -> {
            String username = txtUsername.getText();
            char[] password = txtPassword.getPassword();
            if (checkCredentials(username, new String(password))) {
                System.out.println("Login successful!");
                currentUsername = username;
                frame.setVisible(false);
                Game game = new Game();
                JFrame window = new JFrame("2048");
                window.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent we) {
                        int result = JOptionPane.showConfirmDialog(window,
                                "Vuoi davvero uscire?", "Conferma uscita", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION)
                            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        else if (result == JOptionPane.NO_OPTION)
                            window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    }
                });
                window.setResizable(false);
                window.add(game);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                game.start();
            } else {
                System.out.println("Login failed!");
            }
        });
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(0, 153, 153));
        btnLogin.setBounds(139, 101, 89, 23);
        frame.getContentPane().add(btnLogin);

        JLabel lblRegistration = new JLabel("Registration");
        lblRegistration.setForeground(new Color(0, 153, 153));
        lblRegistration.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblRegistration.setBounds(370, 11, 130, 25);
        frame.getContentPane().add(lblRegistration);

        JLabel lblRegUsername = new JLabel("Username:");
        lblRegUsername.setForeground(new Color(0, 153, 153));
        lblRegUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblRegUsername.setBounds(250, 47, 77, 14);
        frame.getContentPane().add(lblRegUsername);

        JLabel lblRegPassword = new JLabel("Password:");
        lblRegPassword.setForeground(new Color(0, 153, 153));
        lblRegPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblRegPassword.setBounds(250, 72, 77, 14);
        frame.getContentPane().add(lblRegPassword);

        txtRegUsername = new JTextField();
        txtRegUsername.setBounds(369, 45, 200, 20);
        frame.getContentPane().add(txtRegUsername);
        txtRegUsername.setColumns(10);

        txtRegPassword = new JPasswordField();
        txtRegPassword.setBounds(369, 70, 200, 20);
        frame.getContentPane().add(txtRegPassword);

        JButton btnRegister = new JButton("Register");
        btnRegister.addActionListener((ActionEvent e) -> {
            Register(txtRegUsername.getText(), txtRegPassword.getText());
            txtRegUsername.setText("");
            txtRegPassword.setText("");
        });
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(0, 153, 153));
        btnRegister.setBounds(369, 101, 89, 23);
        frame.getContentPane().add(btnRegister);
        frame.setVisible(true);
        
        
        
        
    }
    
    public static DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }
    
    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbex", "root", "");
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    private boolean checkCredentials(String username, String password) {
    try {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM utenti WHERE username = '" + username + "' AND password = '" + password + "'");

        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}
    
    public void Register(String username, String password) {
        try {
            String query = "insert into utenti (username, password) values (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, txtRegUsername.getText());
            pstmt.setString(2, new String(txtRegPassword.getPassword()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Inserted Successfully!");
        } catch (HeadlessException | SQLException ex) {
                ex.printStackTrace();
            }
        try {
            String query = "insert into record (utente) values (?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, txtRegUsername.getText());
            pstmt.executeUpdate();
        } catch (HeadlessException | SQLException ex) {
             ex.printStackTrace();
        }
    }
    
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
    
    public static String getCurrentUsername() {
    return currentUsername;
  }
    
    
    public void saveBestScore(int score) {
    try {
        PreparedStatement ps = con.prepareStatement("UPDATE record SET punteggio = ? WHERE utente = ?");
        ps.setInt(1, score);
        ps.setString(2, currentUsername);
        int result = ps.executeUpdate();
        if (result > 0) {
            System.out.println("Best score saved!");
        } else {
            System.out.println("Failed to save best score.");
        }
    } catch (SQLException ex) {
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getBestScore(String username) {
        int bestScore = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT punteggio FROM record WHERE utente = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bestScore = rs.getInt("punteggio");
            }
        } catch (SQLException ex) {
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bestScore;
    }
    
   public void saveBestTile(int tile) {
    try {
        PreparedStatement ps = con.prepareStatement("UPDATE record SET mattonella = ? WHERE utente = ?");
        ps.setInt(1, tile);
        ps.setString(2, currentUsername);
        int result = ps.executeUpdate();
        if (result > 0) {
            System.out.println("Best tile saved!");
        } else {
            System.out.println("Failed to save best tile.");
        }
    } catch (SQLException ex) {
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   public int getBestTile(String username) {
        int bestTile = 0;
        try {
            PreparedStatement ps = con.prepareStatement("SELECT mattonella FROM record WHERE utente = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bestTile = rs.getInt("mattonella");
            }
        } catch (SQLException ex) {
        Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bestTile;
    }
    
}

    
