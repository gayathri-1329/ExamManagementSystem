
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ExamManagementSystem extends JFrame {
    private CardLayout cardLayout;
    private List<String> questions; // List to hold questions
    private int currentQuestionIndex; // Index of the current question
    private Timer examTimer; // Timer for the exam
    private int remainingTime; // Remaining time in seconds
    private JLabel timerLabel; // Label to display the timer

    public ExamManagementSystem() {
        setTitle("Exam Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Create login panel
        JPanel loginPanel = createLoginPanel();
        add(loginPanel, "Login");

        // Create exam creation panel
        JPanel examCreationPanel = createExamCreationPanel();
        add(examCreationPanel, "Create Exam");

        // Create questions panel
        JPanel questionsPanel = createQuestionsPanel();
        add(questionsPanel, "Questions");

        // Show login panel initially
        cardLayout.show(getContentPane(), "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle login logic here
                // If successful, show the exam creation panel
                cardLayout.show(getContentPane(), "Create Exam");
            }
        });

        return panel;
    }

    private JPanel createExamCreationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JTextField examTitleField = new JTextField();
        JTextArea questionField = new JTextArea();
        JButton createExamButton = new JButton("Create Exam");

        panel.add(new JLabel("Exam Title:"));
        panel.add(examTitleField);
        panel.add(new JLabel("Questions (separate with ';') :"));
        panel.add(new JScrollPane(questionField));
        panel.add(createExamButton);

        createExamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle exam creation logic here
                String questionsInput = questionField.getText();
                questions = new ArrayList<>();
                for (String question : questionsInput.split(";")) {
                    questions.add(question.trim());
                }

                // Show the questions panel with the created questions
                currentQuestionIndex = 0; // Reset to the first question
                updateQuestionDisplay((JTextArea) ((JScrollPane) ((JPanel) getContentPane().getComponent(2)).getComponent(0)).getViewport().getView());
                startTimer(120); // Start timer for 2 hours (120 minutes)
                cardLayout.show(getContentPane(), "Questions");
            }
        });

        return panel;
    }

    private JPanel createQuestionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea questionsArea = new JTextArea();
        questionsArea.setEditable(false);
        panel.add(new JScrollPane(questionsArea), BorderLayout.CENTER);

        timerLabel = new JLabel("Time Remaining: 02:00:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(timerLabel, BorderLayout.NORTH);

        JButton nextButton = new JButton("Next Question");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    updateQuestionDisplay(questionsArea);
                } else {
                    JOptionPane.showMessageDialog(panel, "You have reached the end of the questions.");
                }
            }
        });
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private void startTimer(int duration) {
        remainingTime = duration * 60; // Convert to seconds
        updateTimerLabel();

        examTimer = new Timer();
        examTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;
                    updateTimerLabel();
                } else {
                    examTimer.cancel();
                    JOptionPane.showMessageDialog(getContentPane(), "Time is up! The exam has ended.");
                    // Optionally, you can navigate back to the login or main menu
                }
            }
        }, 0, 1000); // Update every second
    }

    private void updateTimerLabel() {
        int hours = remainingTime / 3600;
        int minutes = (remainingTime % 3600) / 60;
        int seconds = remainingTime % 60;
        timerLabel.setText(String.format("Time Remaining: %02d:%02d:%02d", hours, minutes, seconds));
    }

    private void updateQuestionDisplay(JTextArea questionsArea) {
        if (currentQuestionIndex < questions.size()) {
            questionsArea.setText(questions.get(currentQuestionIndex));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamManagementSystem app = new ExamManagementSystem();
            app.setVisible(true);
        });
    }
}