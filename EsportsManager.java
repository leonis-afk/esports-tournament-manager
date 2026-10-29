/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


// 1. OOP STRUCTURE (Classes and Inheritance)


class Member {
    protected String nickname;

    public Member(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() { 
        return nickname; 
    }
}

class Player extends Member implements Comparable<Player> {
    private String role;
    private int score; 
    private String teamName;

    public Player(String nickname, String role, String teamName) {
        super(nickname);
        this.role = role;
        this.teamName = teamName;
        this.score = 0;
    }

    public void setScore(int score) { this.score = score; }
    public int getScore() { return score; }
    public String getRole() { return role; }
    public String getTeamName() { return teamName; }

    public String getDetails() {
        return nickname + " (" + role + ") -> Score: " + score;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.score, this.score); 
    }
}

class Team {
    private String teamName;
    private ArrayList<Player> players;
    private ImageIcon logo;

    public Team(String teamName, ImageIcon logo) {
        this.teamName = teamName;
        this.players = new ArrayList<>();
        this.logo = logo;
    }

    public void addPlayer(Player p) { players.add(p); }
    public String getTeamName() { return teamName; }
    public ArrayList<Player> getPlayers() { return players; }
    public ImageIcon getLogo() { return logo; }
}

// ==========================================
// 2. GUI INTERFACE WITH 4 TABS
// ==========================================
public class EsportsManager extends JFrame {
    private ArrayList<Team> teamList = new ArrayList<>();
    private ArrayList<Player> totalPlayerList = new ArrayList<>(); 
    private ArrayList<String> currentRound = new ArrayList<>();
    private ArrayList<String> nextRound = new ArrayList<>();
    
    // Save history of all rounds to build the bracket on Page 4
    private ArrayList<ArrayList<String>> roundStructures = new ArrayList<>();

    private JTabbedPane tabbedPane;
    private JTextArea txtAddedTeams;
    private JPanel panelBracket;
    private JLabel lblStatus;

    // Winner page elements
    private JPanel panelFinalChampion;
    private JLabel lblChampionName;
    private JLabel lblChampionLogo;
    private JPanel panelPlayerRanking;

    // 4th Page Elements (Bracket Visualization)
    private JPanel panelVisualDiagram;
    private JLabel lblDetailLogo;
    private JTextArea txtTeamDetails;

    // Gamer Palette
    private final Color COLOR_BG = new Color(20, 16, 28);        
    private final Color COLOR_PANEL = new Color(32, 26, 44);    
    private final Color COLOR_NEON_CYAN = new Color(0, 245, 255); 
    private final Color COLOR_NEON_PURPLE = new Color(186, 85, 211); 
    private final Color COLOR_TEXT_LIGHT = new Color(240, 240, 245); 

    public EsportsManager() {
        setTitle("🎮 Pro Esports Tournament Manager - Bracket History Edition");
        setSize(850, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);

        UIManager.put("TitledBorder.titleColor", COLOR_NEON_CYAN);
        UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(COLOR_NEON_PURPLE, 1));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(COLOR_PANEL);
        tabbedPane.setForeground(COLOR_NEON_CYAN);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 11));

        createRegistrationPage();
        createBracketPage();
        createWinnerPage();
        createVisualHistoryPage(); 

        add(tabbedPane);
    }

    // --- PAGE 1: REGISTRATION ---
    private void createRegistrationPage() {
        JPanel page1 = new JPanel(new BorderLayout(10, 10));
        page1.setBackground(COLOR_BG);
        page1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelInput = new JPanel(new GridLayout(3, 1, 8, 8));
        panelInput.setBackground(COLOR_PANEL);
        panelInput.setBorder(BorderFactory.createTitledBorder(" ADD NEW TEAM "));

        JTextField txtTeamNameInput = new JTextField("Squad " + (teamList.size() + 1));
        txtTeamNameInput.setBackground(COLOR_BG);
        txtTeamNameInput.setForeground(COLOR_TEXT_LIGHT);
        txtTeamNameInput.setCaretColor(COLOR_NEON_CYAN);
        txtTeamNameInput.setBorder(BorderFactory.createLineBorder(COLOR_NEON_PURPLE));

        JButton btnAddTeam = createGamerButton("UPLOAD LOGO & ENTER NAMES", COLOR_NEON_CYAN);
        JLabel lblInput = new JLabel(" Team Name:");
        lblInput.setForeground(COLOR_TEXT_LIGHT);

        panelInput.add(lblInput); panelInput.add(txtTeamNameInput); panelInput.add(btnAddTeam);

        txtAddedTeams = new JTextArea();
        txtAddedTeams.setEditable(false);
        txtAddedTeams.setBackground(COLOR_PANEL);
        txtAddedTeams.setForeground(COLOR_TEXT_LIGHT);
        txtAddedTeams.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtAddedTeams);
        scroll.setBorder(BorderFactory.createTitledBorder(" CURRENT TEAMS LIST "));

        page1.add(panelInput, BorderLayout.NORTH);
        page1.add(scroll, BorderLayout.CENTER);

        btnAddTeam.addActionListener(e -> {
            String name = txtTeamNameInput.getText().trim();
            if (name.isEmpty()) return;

            ImageIcon logoIcon = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose logo for " + name);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images (JPG, PNG)", "jpg", "jpeg", "png"));
            
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                ImageIcon originalImage = new ImageIcon(file.getAbsolutePath());
                Image scaledImage = originalImage.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(scaledImage);
            }

            JPanel panelPopUp = new JPanel(new GridLayout(4, 2, 5, 5));
            JTextField f1 = new JTextField("Player1"); JTextField f2 = new JTextField("Player2");
            JTextField f3 = new JTextField("Player3"); JTextField f4 = new JTextField("Player4");

            panelPopUp.add(new JLabel("Player 1 (Duelist):")); panelPopUp.add(f1);
            panelPopUp.add(new JLabel("Player 2 (Controller):")); panelPopUp.add(f2);
            panelPopUp.add(new JLabel("Player 3 (Sentinel):")); panelPopUp.add(f3);
            panelPopUp.add(new JLabel("Player 4 (IGL):")); panelPopUp.add(f4);

            if (JOptionPane.showConfirmDialog(this, panelPopUp, "Enter names", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                Team newTeam = new Team(name, logoIcon);
                
                Player l1 = new Player(f1.getText().trim(), "Duelist", name);
                Player l2 = new Player(f2.getText().trim(), "Controller", name);
                Player l3 = new Player(f3.getText().trim(), "Sentinel", name);
                Player l4 = new Player(f4.getText().trim(), "IGL", name);

                newTeam.addPlayer(l1); newTeam.addPlayer(l2); newTeam.addPlayer(l3); newTeam.addPlayer(l4);
                
                teamList.add(newTeam);
                totalPlayerList.add(l1); totalPlayerList.add(l2); totalPlayerList.add(l3); totalPlayerList.add(l4);
                
                updateTeamListDisplay();
                txtTeamNameInput.setText("Squad " + (teamList.size() + 1));
            }
        });

        tabbedPane.addTab("1. Registration", page1);
    }

    // --- PAGE 2: ACTIVE BRACKET ---
    private void createBracketPage() {
        JPanel page2 = new JPanel(new BorderLayout(10, 10));
        page2.setBackground(COLOR_BG);
        page2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelBracket = new JPanel();
        panelBracket.setBackground(COLOR_PANEL);
        panelBracket.setLayout(new BoxLayout(panelBracket, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(panelBracket);
        scroll.setBorder(BorderFactory.createTitledBorder(" MATCHES & BRACKET GENERATOR "));
        scroll.getViewport().setBackground(COLOR_PANEL);

        JButton btnGenerateBracket = createGamerButton("⚡ START TOURNAMENT (GENERATE SHORT) ⚡", COLOR_NEON_PURPLE);
        lblStatus = new JLabel("Add at least 2 teams on the first page to play.", SwingConstants.CENTER);
        lblStatus.setForeground(COLOR_NEON_CYAN);

        JPanel botPanel = new JPanel(new BorderLayout(5, 5));
        botPanel.setOpaque(false);
        botPanel.add(btnGenerateBracket, BorderLayout.NORTH);
        botPanel.add(lblStatus, BorderLayout.SOUTH);

        page2.add(scroll, BorderLayout.CENTER);
        page2.add(botPanel, BorderLayout.SOUTH);

        btnGenerateBracket.addActionListener(e -> {
            if (teamList.size() < 2) {
                JOptionPane.showMessageDialog(this, "At least 2 teams are required!");
                return;
            }
            currentRound.clear();
            roundStructures.clear();
            for (Team ek : teamList) currentRound.add(ek.getTeamName());
            Collections.shuffle(currentRound);
            
            // Record initial state
            roundStructures.add(new ArrayList<>(currentRound));
            
            showRound();
            refreshVisualDiagramPage4();
        });

        tabbedPane.addTab("2. Matches (Bracket)", page2);
    }

    // --- PAGE 3: CHAMPION ---
    private void createWinnerPage() {
        JPanel page3 = new JPanel(new BorderLayout(15, 15));
        page3.setBackground(COLOR_BG);
        page3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panelFinalChampion = new JPanel(new GridBagLayout());
        panelFinalChampion.setBackground(COLOR_PANEL);
        panelFinalChampion.setBorder(BorderFactory.createTitledBorder(" 🏆 OFFICIAL TOURNAMENT CHAMPION 🏆 "));
        
        lblChampionName = new JLabel("TOURNAMENT PENDING...", SwingConstants.CENTER);
        lblChampionName.setForeground(Color.GREEN);
        lblChampionName.setFont(new Font("Arial", Font.BOLD, 22));
        lblChampionLogo = new JLabel("", SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10);
        panelFinalChampion.add(lblChampionLogo, gbc);
        gbc.gridy = 1; panelFinalChampion.add(lblChampionName, gbc);

        panelPlayerRanking = new JPanel();
        panelPlayerRanking.setBackground(COLOR_PANEL);
        panelPlayerRanking.setLayout(new BoxLayout(panelPlayerRanking, BoxLayout.Y_AXIS));
        JScrollPane scrollRanking = new JScrollPane(panelPlayerRanking);
        scrollRanking.setBorder(BorderFactory.createTitledBorder(" 👑 TOP 3 BEST PLAYERS (MVP) "));
        scrollRanking.getViewport().setBackground(COLOR_PANEL);

        page3.add(panelFinalChampion, BorderLayout.NORTH);
        page3.add(scrollRanking, BorderLayout.CENTER);

        tabbedPane.addTab("3. Champion & MVP", page3);
    }

    // --- PAGE 4: INTERACTIVE HISTORICAL BRACKET ---
    private void createVisualHistoryPage() {
        JPanel page4 = new JPanel(new GridLayout(2, 1, 15, 15));
        page4.setBackground(COLOR_BG);
        page4.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Upper panel: Visual diagram with round columns (Visual bracket)
        panelVisualDiagram = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        panelVisualDiagram.setBackground(COLOR_PANEL);
        JScrollPane scrollBracketVisual = new JScrollPane(panelVisualDiagram);
        scrollBracketVisual.setBorder(BorderFactory.createTitledBorder(" 📊 JOURNEY DIAGRAM (CLICK TEAM FOR DETAILS) "));
        scrollBracketVisual.getViewport().setBackground(COLOR_PANEL);

        // Lower panel: Displaying data when team button is clicked in bracket
        JPanel panelInspection = new JPanel(new BorderLayout(10, 10));
        panelInspection.setBackground(COLOR_PANEL);
        panelInspection.setBorder(BorderFactory.createTitledBorder(" 🔍 SELECTED TEAM PROFILE "));

        lblDetailLogo = new JLabel("", SwingConstants.CENTER);
        lblDetailLogo.setPreferredSize(new Dimension(120, 120));
        lblDetailLogo.setBorder(BorderFactory.createLineBorder(COLOR_NEON_PURPLE, 2));

        txtTeamDetails = new JTextArea("\n\n Click on any team's name in the Bracket above to view its detailed profile...");
        txtTeamDetails.setEditable(false);
        txtTeamDetails.setBackground(COLOR_BG);
        txtTeamDetails.setForeground(COLOR_TEXT_LIGHT);
        txtTeamDetails.setFont(new Font("Consolas", Font.PLAIN, 14));

        panelInspection.add(lblDetailLogo, BorderLayout.WEST);
        panelInspection.add(new JScrollPane(txtTeamDetails), BorderLayout.CENTER);

        page4.add(scrollBracketVisual);
        page4.add(panelInspection);

        tabbedPane.addTab("4. History & Diagram", page4);
    }

    // --- REBUILD HISTORICAL BRACKET ON PAGE 4 ---
    private void refreshVisualDiagramPage4() {
        panelVisualDiagram.removeAll();

        // Create columns for each round played so far
        for (int r = 0; r < roundStructures.size(); r++) {
            ArrayList<String> roundTeams = roundStructures.get(r);
            
            JPanel panelRoundColumn = new JPanel();
            panelRoundColumn.setOpaque(false);
            panelRoundColumn.setLayout(new BoxLayout(panelRoundColumn, BoxLayout.Y_AXIS));
            
            String roundTitle = (r == roundStructures.size() - 1 && roundTeams.size() == 1) ? "👑 CHAMPION" : "ROUND " + (r + 1);
            JLabel lblTitle = new JLabel(roundTitle, SwingConstants.CENTER);
            lblTitle.setForeground(COLOR_NEON_CYAN);
            lblTitle.setFont(new Font("Arial", Font.BOLD, 12));
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelRoundColumn.add(lblTitle);
            panelRoundColumn.add(Box.createVerticalStrut(15));

            // Add teams as interactive buttons
            for (String teamName : roundTeams) {
                JButton btnHistoricalTeam = createGamerButton(teamName, COLOR_TEXT_LIGHT);
                if (findTeamLogo(teamName) != null) btnHistoricalTeam.setIcon(findTeamLogo(teamName));
                btnHistoricalTeam.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                // When team button is clicked inside the historical bracket
                btnHistoricalTeam.addActionListener(e -> showTeamProfileFromBracket(teamName));

                panelRoundColumn.add(btnHistoricalTeam);
                panelRoundColumn.add(Box.createVerticalStrut(10));
            }
            panelVisualDiagram.add(panelRoundColumn);
        }

        panelVisualDiagram.revalidate();
        panelVisualDiagram.repaint();
    }

    // --- DIRECT INSPECTION LOGIC FROM BRACKET CLICK ---
    private void showTeamProfileFromBracket(String targetTeamName) {
        Team ek = null;
        for (Team e : teamList) {
            if (e.getTeamName().equals(targetTeamName)) {
                ek = e;
                break;
            }
        }
        if (ek == null) return;

        // Visualizing logo in search profile (110x110)
        if (ek.getLogo() != null) {
            Image img = ek.getLogo().getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
            lblDetailLogo.setIcon(new ImageIcon(img));
            lblDetailLogo.setText("");
        } else {
            lblDetailLogo.setIcon(null);
            lblDetailLogo.setText("No Logo");
            lblDetailLogo.setForeground(COLOR_TEXT_LIGHT);
        }

        // Building detailed team profile
        StringBuilder sb = new StringBuilder();
        sb.append(" 🛡️ TEAM: ").append(ek.getTeamName().toUpperCase()).append("\n");
        sb.append(" ─────────────────────────────────────────────────────────\n");
        sb.append(" 👥 PLAYER NAMES AND CURRENT SCORES:\n\n");
        for (Player l : ek.getPlayers()) {
            sb.append("   📌 ").append(l.getDetails()).append("\n");
        }
        txtTeamDetails.setText(sb.toString());
    }

    // --- HELPER FUNCTIONS ---
    private JButton createGamerButton(String text, Color neonColor) {
        JButton btn = new JButton(text);
        btn.setBackground(COLOR_PANEL);
        btn.setForeground(neonColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createLineBorder(neonColor, 2));
        return btn;
    }

    private void updateTeamListDisplay() {
        StringBuilder sb = new StringBuilder();
        for (Team ek : teamList) {
            String hasLogo = (ek.getLogo() != null) ? "🖼️ [Logo Uploaded]" : "🏴 [No Logo]";
            sb.append("🛡️ TEAM: ").append(ek.getTeamName().toUpperCase()).append(" ").append(hasLogo).append("\n");
            for (Player l : ek.getPlayers()) {
                sb.append("   ├── 👤 ").append(l.getNickname()).append("\n");
            }
            sb.append("───────────────────────────────────────────\n");
        }
        txtAddedTeams.setText(sb.toString());
    }

    private void showRound() {
        panelBracket.removeAll();
        nextRound.clear();

        if (currentRound.size() == 1) {
            String winnerName = currentRound.get(0);
            lblStatus.setText("Tournament ended! Go to 'Champion & MVP' page.");
            
            // Update final diagram structure
            refreshVisualDiagramPage4();
            
            tabbedPane.setSelectedIndex(2);
            showOfficialWinner(winnerName);
            return;
        }

        lblStatus.setText("Click on the winning team to advance them to the next round.");

        for (int i = 0; i < currentRound.size(); i += 2) {
            if (i + 1 < currentRound.size()) {
                String team1 = currentRound.get(i);
                String team2 = currentRound.get(i + 1);

                JPanel matchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
                matchPanel.setOpaque(false);

                JButton btnTeam1 = createGamerButton(team1, COLOR_TEXT_LIGHT);
                if(findTeamLogo(team1) != null) btnTeam1.setIcon(findTeamLogo(team1));

                JButton btnTeam2 = createGamerButton(team2, COLOR_TEXT_LIGHT);
                if(findTeamLogo(team2) != null) btnTeam2.setIcon(findTeamLogo(team2));
                
                JLabel vsLabel = new JLabel(" VS ");
                vsLabel.setForeground(COLOR_NEON_CYAN);

                matchPanel.add(btnTeam1); matchPanel.add(vsLabel); matchPanel.add(btnTeam2);
                panelBracket.add(matchPanel);

                btnTeam1.addActionListener(e -> {
                    btnTeam1.setBackground(Color.GREEN);
                    determineWinner(team1, btnTeam1, btnTeam2);
                });
                btnTeam2.addActionListener(e -> {
                    btnTeam2.setBackground(Color.GREEN);
                    determineWinner(team2, btnTeam1, btnTeam2);
                });
            } else {
                String byeTeam = currentRound.get(i);
                nextRound.add(byeTeam);
                JLabel lblBye = new JLabel("➡️ " + byeTeam + " advances automatically (BYE).");
                lblBye.setForeground(COLOR_NEON_PURPLE);
                lblBye.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelBracket.add(lblBye);
            }
        }
        panelBracket.revalidate(); panelBracket.repaint();
    }

    private void determineWinner(String winner, JButton b1, JButton b2) {
        nextRound.add(winner);
        b1.setEnabled(false); b2.setEnabled(false);

        int totalMatches = currentRound.size() / 2;
        if (nextRound.size() >= totalMatches) {
            currentRound = new ArrayList<>(nextRound);
            
            // Record new round progress in our structure list
            roundStructures.add(new ArrayList<>(currentRound));
            refreshVisualDiagramPage4(); // Visually refresh Bracket on page 4
            
            Timer timer = new Timer(700, e -> showRound());
            timer.setRepeats(false); timer.start();
        }
    }

    private void showOfficialWinner(String championName) {
        lblChampionName.setText("🏆 CHAMPION: " + championName.toUpperCase() + " 🏆");
        
        ImageIcon teamLogo = findTeamLogo(championName);
        if (teamLogo != null) {
            Image img = teamLogo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblChampionLogo.setIcon(new ImageIcon(img));
            lblChampionLogo.setText("");
        } else {
            lblChampionLogo.setIcon(null);
            lblChampionLogo.setText("🏴 [No Logo]");
            lblChampionLogo.setForeground(COLOR_TEXT_LIGHT);
        }

        panelPlayerRanking.removeAll();

        JButton btnSetScores = createGamerButton("📊 SET SCORES FOR EACH PLAYER NOW", COLOR_NEON_CYAN);
        btnSetScores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPlayerRanking.add(Box.createVerticalStrut(10));
        panelPlayerRanking.add(btnSetScores);

        btnSetScores.addActionListener(e -> {
            for (Player l : totalPlayerList) {
                String input = JOptionPane.showInputDialog(this, 
                        "Enter score (0-100) for: " + l.getNickname() + " [" + l.getTeamName() + "]", "0");
                try {
                    int sc = Integer.parseInt(input);
                    l.setScore(sc);
                } catch (Exception ex) {
                    l.setScore(0);
                }
            }
            
            Collections.sort(totalPlayerList);
            
            panelPlayerRanking.removeAll();
            panelPlayerRanking.add(Box.createVerticalStrut(15));

            int playerLimit = Math.min(3, totalPlayerList.size());
            String[] podium = {"👑 [1st MVP] ", "🥈 [2nd PLACE] ", "🥉 [3rd PLACE] "};
            
            for (int i = 0; i < playerLimit; i++) {
                Player l = totalPlayerList.get(i);
                JLabel lblMvp = new JLabel(podium[i] + l.getDetails());
                lblMvp.setForeground(i == 0 ? Color.YELLOW : COLOR_TEXT_LIGHT);
                lblMvp.setFont(new Font("Arial", Font.BOLD, 14));
                lblMvp.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPlayerRanking.add(lblMvp);
                panelPlayerRanking.add(Box.createVerticalStrut(10));
            }

            panelPlayerRanking.revalidate();
            panelPlayerRanking.repaint();
        });

        panelFinalChampion.revalidate(); panelFinalChampion.repaint();
        panelPlayerRanking.revalidate(); panelPlayerRanking.repaint();
    }

    private ImageIcon findTeamLogo(String name) {
        for(Team e : teamList) {
            if(e.getTeamName().equals(name)) return e.getLogo();
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EsportsManager().setVisible(true));
    }
}