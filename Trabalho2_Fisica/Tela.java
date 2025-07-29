package guilherme.programafisica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Tela extends JFrame {

    // Campos de texto para entrada dos valores pelo usuário
    private JTextField r1Field = new JTextField();
    private JTextField r2Field = new JTextField();
    private JTextField fonteField = new JTextField();
    private JTextField tempoField = new JTextField();

    // Área de texto para mostrar os resultados do cálculo
    private JTextArea resultadoArea = new JTextArea();

    // Modelo e componente para mostrar histórico simples dos cálculos
    private DefaultListModel<String> historicoModel = new DefaultListModel<>();
    private JList<String> historicoList = new JList<>(historicoModel);

    public Tela() {
        super("Calculadora Física"); // Define o título da janela

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa ao fechar a janela
        setSize(600, 700); 
        setLocationRelativeTo(null); 
        setResizable(true); 
        setLayout(new BorderLayout(10, 10)); // Layout principal

        // Label de título 
        JLabel tituloLabel = new JLabel("Calculadora Física", SwingConstants.CENTER);
        tituloLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        tituloLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(tituloLabel, BorderLayout.NORTH);

        // Painel para entradas com grade 5 linhas x 2 colunas e espaçamento
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Entradas do Circuito"));

        // Adiciona tooltips explicativos nos inputs
        r1Field.setToolTipText("Resistor R1 em ohms (Ω), valor mínimo 0.1 e máximo 100");
        r2Field.setToolTipText("Resistor R2 em ohms (Ω), valor mínimo 0.1 e máximo 100");
        fonteField.setToolTipText("Fonte em volts (V), entre -50 e 50, exceto 0");
        tempoField.setToolTipText("Tempo em minutos, mínimo 1 e máximo 10080 (7 dias)");

        // Resistor R1
        inputPanel.add(new JLabel("Resistor R1 (Ω, ≥ 0.1 e ≤ 100):"));
        inputPanel.add(r1Field);

        // Resistor R2
        inputPanel.add(new JLabel("Resistor R2 (Ω, ≥ 0.1 e ≤ 100):"));
        inputPanel.add(r2Field);

        // Fonte de tensão
        inputPanel.add(new JLabel("Fonte (V, ≠ 0 e |V| ≤ 50):"));
        inputPanel.add(fonteField);

        // Tempo em minutos
        inputPanel.add(new JLabel("Tempo (minutos, ≥ 1 e ≤ 10080):"));
        inputPanel.add(tempoField);

        // Botão calcular
        JButton calcularBtn = new JButton("Calcular");
        calcularBtn.setBackground(Color.GREEN);
        calcularBtn.setPreferredSize(new Dimension(100, 30));
        calcularBtn.addActionListener(this::calcularCircuito);

        // Botão limpar (limpa os campos de entrada)
        JButton limparBtn = new JButton("Limpar");
        limparBtn.setBackground(Color.BLUE);
        limparBtn.setPreferredSize(new Dimension(100, 30));
        limparBtn.addActionListener(e -> limparCampos());

        // Botão resetar (limpa tudo: entrada, resultado, histórico)
        JButton resetarBtn = new JButton("Resetar");
        resetarBtn.setBackground(Color.RED);
        resetarBtn.setPreferredSize(new Dimension(100, 30));
        resetarBtn.addActionListener(e -> resetarTudo());

        // Painel para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calcularBtn);
        buttonPanel.add(limparBtn);
        buttonPanel.add(resetarBtn);

        // Área de texto para resultados (não editável)
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultadoArea.setBackground(new Color(240, 240, 240));
        resultadoArea.setBorder(BorderFactory.createTitledBorder("Resultado"));
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);

        // Scroll para a área de resultado
        JScrollPane scrollResultado = new JScrollPane(resultadoArea);
        scrollResultado.setPreferredSize(new Dimension(550, 200));

        // Lista com histórico dos cálculos
        historicoList.setBorder(BorderFactory.createTitledBorder("Histórico de Cálculos"));
        JScrollPane scrollHistorico = new JScrollPane(historicoList);
        scrollHistorico.setPreferredSize(new Dimension(550, 140));

        // Painel central que agrupa tudo
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(inputPanel);
        centerPanel.add(Box.createVerticalStrut(10)); // espaço vertical
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(scrollResultado);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(scrollHistorico);

        add(centerPanel, BorderLayout.CENTER); // Adiciona painel central à janela
    }

    // Método que executa o cálculo quando o botão é clicado
    private void calcularCircuito(ActionEvent e) {
        try {
            // Validações de entrada para cada campo
            String r1Text = r1Field.getText().trim();
            if (r1Text.isEmpty()) throw new IllegalArgumentException("Informe o valor do resistor R1.");
            double R1 = Double.parseDouble(r1Text);
            if (R1 < 0.1 || R1 > 100)
                throw new IllegalArgumentException("R1 inválido: deve ser ≥ 0.1 e ≤ 100 Ω.");

            String r2Text = r2Field.getText().trim();
            if (r2Text.isEmpty()) throw new IllegalArgumentException("Informe o valor do resistor R2.");
            double R2 = Double.parseDouble(r2Text);
            if (R2 < 0.1 || R2 > 100)
                throw new IllegalArgumentException("R2 inválido: deve ser ≥ 0.1 e ≤ 100 Ω.");

            String fonteText = fonteField.getText().trim();
            if (fonteText.isEmpty()) throw new IllegalArgumentException("Informe o valor da fonte.");
            double fonte = Double.parseDouble(fonteText);
            if (fonte == 0 || Math.abs(fonte) > 50)
                throw new IllegalArgumentException("Fonte inválida: deve ser diferente de 0 e |V| ≤ 50.");

            String tempoText = tempoField.getText().trim();
            if (tempoText.isEmpty()) throw new IllegalArgumentException("Informe o tempo em minutos.");
            double tempoMin = Double.parseDouble(tempoText);
            if (tempoMin < 1 || tempoMin > 10080)
                throw new IllegalArgumentException("Tempo deve estar entre 1 minuto e 10080 minutos (7 dias).");

            // Cálculo da resistência equivalente com R2 em paralelo (2 resistores iguais): Rparalelo = R2 / 2
            double somaR2 = R2 + R2;
            if (somaR2 == 0) {
                resultadoArea.setText("Erro: resistores em paralelo não podem ter soma zero.");
                return;
            }
            double Rparalelo = (R2 * R2) / somaR2;
            double Rtotal = R1 + Rparalelo;

            // Cálculo da corrente total usando Lei de Ohm
            double I = fonte / Rtotal;

            // Determinar sentido da corrente com base no sinal
            String sentidoCorrente;
            if (I > 0) {
                sentidoCorrente = "Sentido convencional: do polo positivo para o negativo";
            } else if (I < 0) {
                sentidoCorrente = "Sentido inverso: do polo negativo para o positivo";
                I = Math.abs(I); // para exibir apenas o valor da corrente
            } else {
                sentidoCorrente = "Sem corrente (tensão nula ou circuito aberto)";
            }

            // Cálculo da potência (valor absoluto)
            double P = Math.abs(fonte * I);

            // Conversão de tempo para segundos
            double tempoSeg = tempoMin * 60;

            // Energia dissipada = P × t (em Joules)
            double energia = P * tempoSeg;

            // Construção do texto de saída
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Resistência total do circuito: %.2f Ω\n", Rtotal));
            sb.append(String.format("(a) Corrente total i1 = %.3f A\n\n", I));
            sb.append("(b) Sentido da corrente:\n");
            sb.append("    ").append(sentidoCorrente).append("\n\n");
            sb.append("(c) Energia dissipada:\n");
            sb.append(String.format("Potência total: |P| = |V × I| = %.2f W\n", P));
            sb.append(String.format("Tempo: %.2f minutos = %.0f segundos\n", tempoMin, tempoSeg));
            sb.append(String.format("Energia dissipada = P × t = %.3f J\n", energia));

            // Exibe resultado
            resultadoArea.setText(sb.toString());

            // Adiciona item ao histórico
            String historicoItem = String.format("R1=%.2fΩ, R2=%.2fΩ, Fonte=%.2fV, Tempo=%.2fmin → Corrente=%.3fA, Energia=%.3fJ",
                    R1, R2, fonte, tempoMin, I, energia);
            historicoModel.addElement(historicoItem);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro: digite um número válido em todos os campos.",
                    "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro: " + ex.getMessage(),
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro inesperado: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para limpar apenas os campos de entrada
    private void limparCampos() {
        r1Field.setText("");
        r2Field.setText("");
        fonteField.setText("");
        tempoField.setText("");
    }

    // Método para resetar tudo: entrada, resultado e histórico
    private void resetarTudo() {
        limparCampos();
        resultadoArea.setText("");
        historicoModel.clear();
    }
}   
