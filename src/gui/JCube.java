package gui;

import algorithm.AStar;
import cube.CubieCube;
import cube.Face;
import cube.Move;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.border.LineBorder;

public class JCube
{
    final JFrame frame;

    CubieCube cubieCube;

    public int index = 0;
    public boolean areMovesReady = false;
    List<String> moves = new ArrayList<>();

    public static final Dimension FRAME_DIMENSION = new Dimension(700, 400);
    public static final Dimension LIST_DIMENSION = new Dimension(200, 400);
    private static final Dimension SOLVE_BUTTON_DIMENSION = new Dimension(700, 30);
    private static final Dimension UP_PANEL_DIMENSION = new Dimension(700, 50);
    private static final Dimension BUTTON_DIMENSION = new Dimension(52, 40);
    public JCube(final Face[] cubeFaces)
    {
        this.cubieCube = new CubieCube(cubeFaces);
        this.frame = new JFrame("Cube Solver");
        this.frame.setSize(FRAME_DIMENSION);
        this.frame.setVisible(true);
        this.frame.setResizable(false);

        this.frame.getContentPane().setBackground(new Color(54, 53, 53));
        this.frame.getContentPane().setLayout(new BorderLayout());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        reDraw();
    }

    public void reDraw()
    {
        this.frame.getContentPane().removeAll();
        this.frame.getContentPane().setBackground(new Color(54, 53, 53));

        this.frame.getContentPane().add(upPanel(), BorderLayout.NORTH);
        this.frame.getContentPane().add(new JScrollPane(leftPanel()), BorderLayout.WEST);
        this.frame.getContentPane().add(centerPanel(), BorderLayout.CENTER);
        this.frame.getContentPane().add(rightPanel(), BorderLayout.EAST);
        this.frame.getContentPane().add(bottomPanel(), BorderLayout.SOUTH);

        this.frame.getContentPane().validate();
        this.frame.getContentPane().repaint();
    }

    public JPanel upPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setPreferredSize(UP_PANEL_DIMENSION);
        panel.setBackground(new Color(54, 53, 53));

        for(Move mv:CubieCube.ALL_MOVES)
        {
            JButton button = new JButton(mv.toString());
            button.setBackground(new Color(236, 243, 185));
            button.setPreferredSize(BUTTON_DIMENSION);

            button.addActionListener(e -> {
                cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), mv));
                reDraw();
            });
            panel.add(button);
        }

        return panel;
    }

    public JList<String> leftPanel()
    {
        if(!areMovesReady)
        {
            moves.clear();
            moves.add("empty.....");
        }

        JList<String> list = new JList<>(this.filterMoves(moves).toArray(new String[0]));
        list.setPreferredSize(LIST_DIMENSION);
        list.setBackground(new Color(54, 53, 53));
        list.setForeground(Color.WHITE);
        list.setBorder(new LineBorder(new Color(54, 53, 53), 5));

        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && areMovesReady)
            {
                String selected = list.getSelectedValue();
                index = Integer.parseInt(selected.substring(0, selected.indexOf('.')));
                String s = moves.get(index);
                cubieCube.setState(CubieCube.toCube(s.substring(s.indexOf('.') + 1, s.indexOf('.') + 25)).getState());
                reDraw();
            }
        });

        return list;
    }

    public JPanel centerPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(54, 53, 53));
        panel.setLayout(new GridLayout(2, 2));
        panel.setBorder(new LineBorder(new Color(54, 53, 53), 40));

        for(int i = 8; i < 12; i++)
        {
            JPanel p = new JPanel();
            p.setBorder(new LineBorder(Color.BLACK, 3));
            p.setBackground(cubieCube.getState()[i].color());

            int finalI = i;
            p.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    areMovesReady = false;
                    cubieCube.setState(cubieCube.getState()[finalI].next(), finalI);
                    p.setBackground(cubieCube.getState()[finalI].color());
                    reDraw();
                }
            });
            panel.add(p);
        }

        return panel;
    }

    public JPanel rightPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));
        panel.setBackground(new Color(54, 53, 53));
        String[] f = { "↑", "↓", "←", "→", "PREVIOUS", "NEXT", "RESET", "SCRAMBLE", "CLEAR", "EXIT" };

        for(int i = 0; i < f.length; i++)
        {
            JButton button = new JButton(f[i]);
            button.setBackground(new Color(236, 243, 185));
            button.setBorder(new LineBorder(new Color(54, 53, 53), 10));

            int finalI = i;
            button.addActionListener(e -> {
                if(finalI == 0)
                {
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.R_CW));
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.L_CCW));
                } else if(finalI == 1)
                {
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.R_CCW));
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.L_CW));
                } else if(finalI == 2)
                {
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.U_CW));
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.D_CCW));
                } else if(finalI == 3)
                {
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.U_CCW));
                    cubieCube.setState(CubieCube.applyMove(cubieCube.getState(), Move.D_CW));
                } else if(finalI == 4 && this.index > 0 && areMovesReady)
                {
                    String s = moves.get(--this.index);
                    cubieCube.setState(CubieCube.toCube(s.substring(s.indexOf('.') + 1, s.indexOf('.') + 25)).getState());
                }
                else if(finalI == 5 && this.index < moves.size()-1 && areMovesReady)
                {
                    String s = moves.get(++this.index);
                    cubieCube.setState(CubieCube.toCube(s.substring(s.indexOf('.') + 1, s.indexOf('.') + 25)).getState());
                }
                else if(finalI == 6)
                {
                    for(Face j:CubieCube.ALL_FACES)
                    {
                        for(int k = 0; k < 4; k++) cubieCube.setState(j, 4 * j.index() + k);
                    }
                    this.index = 0;
                    moves.clear();
                    moves.add("empty.....");
                } else if(finalI == 7) cubieCube.setState(CubieCube.scramble());
                else if (finalI == 8)
                {
                    for(int j = 0; j < 24; j++) cubieCube.setState(Face.UP, j);
                } else if(finalI == 9) System.exit(0);

                if(finalI == 6 || finalI == 7 || finalI == 8) areMovesReady = false;
                reDraw();
            });

            panel.add(button);
        }

        return panel;
    }

    public JButton bottomPanel()
    {
        JButton button = new JButton("SOLVE");
        button.setBackground(new Color(236, 243, 185));
        button.setBorder(new LineBorder(new Color(54, 53, 53), 5));
        button.setPreferredSize(SOLVE_BUTTON_DIMENSION);

        button.addActionListener(e -> {
            moves.clear();
            if(cubieCube.isValid())
            {
                areMovesReady = true;
                cubieCube = new CubieCube(cubieCube.getState());
                moves = cubieCube.solve();
                System.out.println("SOLVED!");
                reDraw();
            } else JOptionPane.showMessageDialog(new JFrame("Message"), "INVALID CUBE STATE");
        });

        return button;
    }

    private List<String> filterMoves(final List<String> moves)
    {
        if(!areMovesReady) return moves;

        List<String> list = new ArrayList<>();
        for(String s:moves)
        {
            int inx = s.indexOf('.');
            list.add(Integer.parseInt(s.substring(0, inx)) + ". " + s.substring(inx + 25));
        }
        list.add(0, "Moves");

        return list;
    }

    public static void main(String[] args)
    {
        AStar.computePruningTables();
        new JCube(CubieCube.scramble());
    }
}
