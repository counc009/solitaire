package acc240.solitaire;

import acc240.solitaire.cards.Card;
import acc240.solitaire.cards.Deck;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.Random;

/**
 * The main panel of the Solitaire game
 * This class holds the decks of cards that form the game
 * It is also responsible for rendering the entire game and managing user interaction with the game
 *
 * @author Aaron Councilman
 * @version 1.00
 */
public class SolitaireBoard extends JPanel implements MouseListener, MouseMotionListener {

    private static final String fileName = "save.data";

    private static final int WIDTH = 820;
    private static final int HEIGHT = 620;

    private Deck draw, discard;
    private Deck[] rows = new Deck[7];
    private Deck[] aces = new Deck[4];

    private Deck holding;
    private int row_src;
    private double iX, iY;
    private Point press, loc;

    public SolitaireBoard() {
        reset();

        addMouseListener(this);
        addMouseMotionListener(this);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    public static SolitaireBoard load() {
        SolitaireBoard res = new SolitaireBoard();

        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            res.draw = Deck.load(in.readLine().substring(1));
            res.discard = Deck.load(in.readLine().substring(1));
            for (int i = 0; i < 7; i++) res.rows[i] = Deck.load(in.readLine().substring(1));
            for (int i = 0; i < 4; i++) res.aces[i] = Deck.load(in.readLine().substring(1));
        } catch (Exception e) {
            res = new SolitaireBoard();
        }

        return res;
    }

    public void save() {
        try {
            PrintStream out = new PrintStream(new File(fileName));
            out.println(":" + draw);
            out.println(":" + discard);
            for (int i = 0; i < 7; i++) out.println(":" + rows[i]);
            for (int i = 0; i < 4; i++) out.println(":" + aces[i]);
        } catch (FileNotFoundException e) {
        }
    }

    private void reset() {
        draw = genDeck();
        draw.faceDown();
        setupDecks();

        Deck cards = draw.removeTop(28);
        for (int i = 0; i < 7; i++) {
            rows[i].add(cards.removeTop(i + 1));
            rows[i].flipTop();
        }

        discard = new Deck();
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBackground(g);
        drawCards(g);
    }

    private void drawBackground(Graphics g) {
        g.setColor(new Color(0, 100, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.WHITE);
        g.drawRect(15, 15, Card.WIDTH, Card.HEIGHT);

        for (int i = 0; i < 7; i++)
            g.drawRect(15 + (Card.WIDTH + 15) * i, 200, Card.WIDTH, Card.HEIGHT);
        for (int i = 0; i < 4; i++)
            g.drawRect(15 + (Card.WIDTH + 15) * (i + 3), 15, Card.WIDTH, Card.HEIGHT);

        g.setColor(Color.BLUE);
        g.fillRect(280, 65, 60, 40);
        g.setColor(Color.RED);
        g.drawString("New", 297, 80);
        g.drawString("Game", 295, 95);
    }

    private void drawCards(Graphics g) {
        for (int r = 0; r < 7; r++) {
            Card[] cards = rows[r].getCards();
            for (int i = 0; i < cards.length; i++)
                g.drawImage(cards[i].getImage(), 15 + (Card.WIDTH + 15) * r, 200 + 20 * i, null);
        }
        for (int a = 0; a < 4; a++) {
            if (aces[a].getCount() > 0)
                g.drawImage(aces[a].getTop().getImage(), 15 + (Card.WIDTH + 15) * (a + 3), 15, null);
        }

        if (draw.getCount() > 0) g.drawImage(draw.getTop().getImage(), 15, 15, null);

        Card[] discards = discard.getCards();
        if (discard.getCount() >= 3) {
            g.drawImage(discards[discards.length - 3].getImage(), 130, 15, null);
            g.drawImage(discards[discards.length - 2].getImage(), 150, 15, null);
            g.drawImage(discards[discards.length - 1].getImage(), 170, 15, null);
        } else if (discard.getCount() >= 2) {
            g.drawImage(discards[0].getImage(), 150, 15, null);
            g.drawImage(discards[1].getImage(), 170, 15, null);
        } else if (discard.getCount() >= 1) {
            g.drawImage(discards[0].getImage(), 170, 15, null);
        }

        if (holding != null) {
            double x = iX + loc.getX() - press.getX();
            double y = iY + loc.getY() - press.getY();

            for (int i = 0; i < holding.getCount(); i++)
                g.drawImage(holding.get(i).getImage(), (int) x, (int) (y + 20 * i), null);
        }
    }

    private Deck genDeck() {
        Card[] cards = new Card[52];
        cards[0] = new Card(Card.Suit.DIAMOND, Card.Value.ACE, true);
        cards[1] = new Card(Card.Suit.DIAMOND, Card.Value.TWO, true);
        cards[2] = new Card(Card.Suit.DIAMOND, Card.Value.THREE, true);
        cards[3] = new Card(Card.Suit.DIAMOND, Card.Value.FOUR, true);
        cards[4] = new Card(Card.Suit.DIAMOND, Card.Value.FIVE, true);
        cards[5] = new Card(Card.Suit.DIAMOND, Card.Value.SIX, true);
        cards[6] = new Card(Card.Suit.DIAMOND, Card.Value.SEVEN, true);
        cards[7] = new Card(Card.Suit.DIAMOND, Card.Value.EIGHT, true);
        cards[8] = new Card(Card.Suit.DIAMOND, Card.Value.NINE, true);
        cards[9] = new Card(Card.Suit.DIAMOND, Card.Value.TEN, true);
        cards[10] = new Card(Card.Suit.DIAMOND, Card.Value.JACK, true);
        cards[11] = new Card(Card.Suit.DIAMOND, Card.Value.QUEEN, true);
        cards[12] = new Card(Card.Suit.DIAMOND, Card.Value.KING, true);

        cards[13] = new Card(Card.Suit.SPADE, Card.Value.ACE, true);
        cards[14] = new Card(Card.Suit.SPADE, Card.Value.TWO, true);
        cards[15] = new Card(Card.Suit.SPADE, Card.Value.THREE, true);
        cards[16] = new Card(Card.Suit.SPADE, Card.Value.FOUR, true);
        cards[17] = new Card(Card.Suit.SPADE, Card.Value.FIVE, true);
        cards[18] = new Card(Card.Suit.SPADE, Card.Value.SIX, true);
        cards[19] = new Card(Card.Suit.SPADE, Card.Value.SEVEN, true);
        cards[20] = new Card(Card.Suit.SPADE, Card.Value.EIGHT, true);
        cards[21] = new Card(Card.Suit.SPADE, Card.Value.NINE, true);
        cards[22] = new Card(Card.Suit.SPADE, Card.Value.TEN, true);
        cards[23] = new Card(Card.Suit.SPADE, Card.Value.JACK, true);
        cards[24] = new Card(Card.Suit.SPADE, Card.Value.QUEEN, true);
        cards[25] = new Card(Card.Suit.SPADE, Card.Value.KING, true);

        cards[26] = new Card(Card.Suit.HEART, Card.Value.ACE, true);
        cards[27] = new Card(Card.Suit.HEART, Card.Value.TWO, true);
        cards[28] = new Card(Card.Suit.HEART, Card.Value.THREE, true);
        cards[29] = new Card(Card.Suit.HEART, Card.Value.FOUR, true);
        cards[30] = new Card(Card.Suit.HEART, Card.Value.FIVE, true);
        cards[31] = new Card(Card.Suit.HEART, Card.Value.SIX, true);
        cards[32] = new Card(Card.Suit.HEART, Card.Value.SEVEN, true);
        cards[33] = new Card(Card.Suit.HEART, Card.Value.EIGHT, true);
        cards[34] = new Card(Card.Suit.HEART, Card.Value.NINE, true);
        cards[35] = new Card(Card.Suit.HEART, Card.Value.TEN, true);
        cards[36] = new Card(Card.Suit.HEART, Card.Value.JACK, true);
        cards[37] = new Card(Card.Suit.HEART, Card.Value.QUEEN, true);
        cards[38] = new Card(Card.Suit.HEART, Card.Value.KING, true);

        cards[39] = new Card(Card.Suit.CLUB, Card.Value.ACE, true);
        cards[40] = new Card(Card.Suit.CLUB, Card.Value.TWO, true);
        cards[41] = new Card(Card.Suit.CLUB, Card.Value.THREE, true);
        cards[42] = new Card(Card.Suit.CLUB, Card.Value.FOUR, true);
        cards[43] = new Card(Card.Suit.CLUB, Card.Value.FIVE, true);
        cards[44] = new Card(Card.Suit.CLUB, Card.Value.SIX, true);
        cards[45] = new Card(Card.Suit.CLUB, Card.Value.SEVEN, true);
        cards[46] = new Card(Card.Suit.CLUB, Card.Value.EIGHT, true);
        cards[47] = new Card(Card.Suit.CLUB, Card.Value.NINE, true);
        cards[48] = new Card(Card.Suit.CLUB, Card.Value.TEN, true);
        cards[49] = new Card(Card.Suit.CLUB, Card.Value.JACK, true);
        cards[50] = new Card(Card.Suit.CLUB, Card.Value.QUEEN, true);
        cards[51] = new Card(Card.Suit.CLUB, Card.Value.KING, true);

        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        for (int i = 0; i < 52; i++) {
            int ri = rnd.nextInt(52);

            Card temp = cards[ri];
            cards[ri] = cards[i];
            cards[i] = temp;
        }

        return new Deck(cards);
    }

    private void setupDecks() {
        for (int i = 0; i < 7; i++)
            rows[i] = new Deck();
        for (int i = 0; i < 4; i++)
            aces[i] = new Deck();
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getX() >= 280 && e.getX() <= 340) {
            if (e.getY() >= 65 && e.getY() <= 105) {
                reset();
                repaint();
            }
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getY() < 200) {
            if (e.getX() <= 115) {
                if (draw.getCount() >= 3) {
                    Deck temp = draw.removeTop(3);
                    temp.faceUp();
                    temp.reverse();
                    discard.add(temp);
                    repaint();
                } else if (draw.getCount() > 0) {
                    Deck temp = draw.removeTop(draw.getCount());
                    temp.faceUp();
                    temp.reverse();
                    discard.add(temp);
                    repaint();
                } else {
                    discard.reverse();
                    discard.faceDown();
                    draw.add(discard.removeTop(discard.getCount()));
                    repaint();
                }
            } else {
                if (e.getX() >= 170 && e.getX() < 170 + Card.WIDTH) {
                    holding = discard.removeTop(1);
                    row_src = -1;
                    press = e.getPoint();
                    loc = press;

                    iX = 170;
                    iY = 15;

                    repaint();
                }
            }
        } else {
            int level = (e.getY() - 200) / 20;
            int row = e.getX() / 115;
            if (level >= rows[row].getCount()) level = rows[row].getCount() - 1;

            Deck selected = rows[row];
            if (selected.getCount() > 0) {
                if (selected.get(level).faceUp()) {
                    holding = selected.removeTop(selected.getCount() - level);
                    row_src = row;
                    press = e.getPoint();
                    loc = press;

                    iX = 15 + (Card.WIDTH + 15) * row;
                    iY = 200 + 20 * level;

                    repaint();
                } else if (level + 1 == selected.getCount()) {
                    selected.flipTop();
                    repaint();
                }
            }
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (holding != null) {
            boolean placed = false;
            if (e.getY() > 200) {
                int row = e.getX() / 115;

                if (row >= 7) row = 6;
                if (row < 0) row = 0;

                Deck newRow = rows[row];
                if (newRow.getCount() > 0) {
                    Card bottom = newRow.get(newRow.getCount() - 1);
                    Card top = holding.get(0);
                    if (bottom.getColor() != top.getColor()) {
                        if (bottom.getValue().getInteger() == top.getValue().getInteger() + 1) {
                            newRow.add(holding);
                            placed = true;
                        }
                    }
                } else {
                    if (holding.get(0).getValue() == Card.Value.KING) {
                        newRow.add(holding);
                        placed = true;
                    }
                }
            } else if (e.getX() >= 360 && holding.getCount() == 1) {
                int val = holding.getTop().getValue().getInteger();

                int col = -1;
                switch (holding.getTop().getSuit()) {
                    case CLUB:
                        col = 0;
                        break;
                    case HEART:
                        col = 1;
                        break;
                    case SPADE:
                        col = 2;
                        break;
                    case DIAMOND:
                        col = 3;
                        break;
                }
                if (aces[col].getCount() + 1 == val) {
                    aces[col].add(holding);
                    placed = true;

                    checkWin();
                }
            }

            if (!placed) {
                if (row_src != -1) {
                    Deck oldRow = rows[row_src];
                    oldRow.add(holding);
                } else {
                    discard.add(holding);
                }
            }
            holding = null;
            repaint();
        }
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (holding != null) {
            loc = e.getPoint();
            repaint();
        }
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void checkWin() {
        boolean won = true;
        for (int i = 0; i < 4; i++)
            if (aces[i].getCount() != 13) won = false;

        if (won) {
            setEnabled(false);
            String[] options = new String[]{"Play Again!", "Exit"};
            int r = JOptionPane.showOptionDialog(this, "Congratulations! You won! You you want to play again?",
                    "Congratulations", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);

            if (r == JOptionPane.YES_OPTION) {
                setEnabled(true);
                reset();
                repaint();
            } else {
                System.exit(0);
            }
        }
    }
}
