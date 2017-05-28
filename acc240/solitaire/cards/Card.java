package acc240.solitaire.cards;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A playing card included in the Solitaire Game
 * The class contains methods to draw itself
 *
 * @author Aaron Councilman
 * @version 1.00
 */
public class Card {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 155;
    private static final int[][][] centers =
            {{{50, 75}},
                    {{50, 30}, {50, 120}},
                    {{50, 30}, {50, 75}, {50, 120}},
                    {{30, 30}, {70, 30}, {30, 120}, {70, 120}},
                    {{30, 30}, {70, 30}, {30, 120}, {70, 120}, {50, 75}},
                    {{30, 30}, {70, 30}, {30, 120}, {70, 120}, {30, 75}, {70, 75}},
                    {{30, 30}, {70, 30}, {30, 120}, {70, 120}, {30, 75}, {70, 75}, {50, 65}},
                    {{30, 30}, {30, 60}, {30, 90}, {30, 120}, {70, 30}, {70, 60}, {70, 90}, {70, 120}},
                    {{30, 30}, {30, 60}, {30, 90}, {30, 120}, {70, 30}, {70, 60}, {70, 90}, {70, 120}, {50, 45}},
                    {{30, 30}, {30, 60}, {30, 90}, {30, 120}, {70, 30}, {70, 60}, {70, 90}, {70, 120}, {50, 45}, {50, 110}}};
    private static final BufferedImage back = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static final Font normal = new Font("Serif", Font.PLAIN, 20);
    private static final Font fancy = new Font("Freestyle Script", Font.PLAIN, 80);
    private static boolean backRendered = false;
    private Suit suit;
    private Value value;
    private Image img;
    private boolean faceUp;
    public Card(Suit s, Value v, boolean face) {
        suit = s;
        value = v;
        faceUp = face;

        img = render();

        if (!backRendered) {
            backRendered = true;
            renderBack();
        }
    }

    public static Card load(String text) {

        Suit suit;
        Value value;
        boolean faceUp;

        switch (text.charAt(0)) {
            case 'H':
                suit = Suit.HEART;
                break;
            case 'S':
                suit = Suit.SPADE;
                break;
            case 'D':
                suit = Suit.DIAMOND;
                break;
            case 'C':
                suit = Suit.CLUB;
                break;
            default:
                throw new IllegalArgumentException("Unrecognized Card");
        }

        if (text.charAt(1) == '0') faceUp = false;
        else faceUp = true;

        if (text.length() == 4) value = Value.TEN;
        else {
            switch (text.charAt(2)) {
                case 'A':
                    value = Value.ACE;
                    break;
                case '2':
                    value = Value.TWO;
                    break;
                case '3':
                    value = Value.THREE;
                    break;
                case '4':
                    value = Value.FOUR;
                    break;
                case '5':
                    value = Value.FIVE;
                    break;
                case '6':
                    value = Value.SIX;
                    break;
                case '7':
                    value = Value.SEVEN;
                    break;
                case '8':
                    value = Value.EIGHT;
                    break;
                case '9':
                    value = Value.NINE;
                    break;
                case 'J':
                    value = Value.JACK;
                    break;
                case 'Q':
                    value = Value.QUEEN;
                    break;
                case 'K':
                    value = Value.KING;
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized Card");
            }
        }

        return new Card(suit, value, faceUp);
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    public Image getImage() {
        return img;
    }

    public char getColor() {
        if (suit == Suit.HEART || suit == Suit.DIAMOND) return 'r';
        return 'b';
    }

    public boolean faceUp() {
        return faceUp;
    }

    public void flip() {
        faceUp = !faceUp;
        img = render();
    }

    public void setFaceUp(boolean face) {
        faceUp = face;
        img = render();
    }

    private Image render() {
        if (faceUp) {
            BufferedImage r = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // This may be too big of a file, may need VolatileImage

            Graphics g = r.getGraphics();

            // background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, WIDTH, HEIGHT);

            // suit color
            if (suit == Suit.HEART || suit == Suit.DIAMOND)
                g.setColor(Color.RED);
            else
                g.setColor(Color.BLACK);

            // draw suit
            int[][] design = suit.getCoordinates(10, 10);
            g.fillPolygon(design[0], design[1], design[0].length);
            design = suit.getCoordinates(90, 145);
            g.fillPolygon(design[0], design[1], design[0].length);

            // draw value
            String text = value.getString();

            if (value != Value.TEN) {
                g.setFont(normal);
                g.drawString(text, 85, 15);
                g.drawString(text, 5, 150);
            } else {
                g.setFont(normal);
                g.drawString(text, 80, 15);
                g.drawString(text, 5, 150);
            }

            // draw card center
            g.setFont(fancy);
            switch (value) {
                case KING:
                    g.drawString("K", WIDTH / 2 - 25, HEIGHT / 2 + 25);
                    break;
                case QUEEN:
                    g.drawString("Q", WIDTH / 2 - 25, HEIGHT / 2 + 25);
                    break;
                case JACK:
                    g.drawString("J", WIDTH / 2 - 25, HEIGHT / 2 + 25);
                    break;
                default:
                    int[][] locs = centers[value.getInteger() - 1];
                    for (int i = 0; i < locs.length; i++) {
                        int[][] pattern = suit.getCoordinates(locs[i][0], locs[i][1]);
                        g.fillPolygon(pattern[0], pattern[1], pattern[0].length);
                    }
            }

            return r;
        } else {
            return back;
        }
    }

    private void renderBack() {
        Graphics2D g = (Graphics2D) back.getGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2.0f));
        for (int y = 0; y < HEIGHT * 2; y += 10) {
            g.drawLine(0, y, y, 0);
            g.drawLine(WIDTH, y, WIDTH - y, 0);
        }
    }

    public String toString() {
        String res = "";

        switch (suit) {
            case DIAMOND:
                res += "D";
                break;
            case SPADE:
                res += "S";
                break;
            case HEART:
                res += "H";
                break;
            case CLUB:
                res += "C";
                break;
        }

        if (faceUp) res += "1";
        else res += "0";

        res += value.getString();

        return res;
    }

    public enum Suit {
        HEART, SPADE, DIAMOND, CLUB;

        public int[][] getCoordinates(int cX, int cY) {
            switch (this) {
                case HEART:
                    return new int[][]{
                            {cX + 0, cX + 0, cX + 1, cX + 1, cX + 2, cX + 3, cX + 4, cX + 4, cX + 5, cX + 5, cX + 5, cX + 4, cX + 4, cX + 3, cX + 3, cX + 2, cX + 1, cX + 0, cX - 1, cX - 2, cX - 2, cX - 3, cX - 4, cX - 5, cX - 5, cX - 5, cX - 5, cX - 5, cX - 4, cX - 4, cX - 3, cX - 2, cX - 2, cX - 1, cX + 0, cX + 0},
                            {cY - 2, cY - 3, cY - 3, cY - 4, cY - 4, cY - 4, cY - 4, cY - 3, cY - 3, cY - 2, cY - 1, cY + 0, cY + 1, cY + 2, cY + 3, cY + 4, cY + 5, cY + 7, cY + 6, cY + 5, cY + 4, cY + 3, cY + 2, cY + 1, cY + 0, cY - 1, cY - 2, cY - 3, cY - 3, cY - 4, cY - 4, cY - 4, cY - 4, cY - 3, cY - 3, cY - 2}};
                case SPADE:
                    return new int[][]{
                            {cX, cX, cX + 1, cX + 1, cX + 2, cX + 3, cX + 4, cX + 4, cX + 5, cX + 5, cX + 5, cX + 5, cX + 4, cX + 4, cX + 3, cX + 2, cX + 1, cX + 1, cX, cX, cX + 1, cX + 1, cX + 1, cX + 2, cX + 3, cX, cX - 3, cX - 2, cX - 1, cX - 1, cX - 1, cX, cX, cX - 1, cX - 1, cX - 2, cX - 3, cX - 4, cX - 4, cX - 5, cX - 5, cX - 5, cX - 5, cX - 4, cX - 4, cX - 3, cX - 2, cX - 1, cX - 1, cX},
                            {cY - 6, cY - 5, cY - 5, cY - 4, cY - 4, cY - 3, cY - 3, cY - 2, cY - 2, cY - 1, cY, cY + 1, cY + 1, cY + 2, cY + 2, cY + 2, cY + 2, cY + 1, cY + 1, cY + 2, cY + 2, cY + 3, cY + 4, cY + 4, cY + 4, cY + 4, cY + 4, cY + 4, cY + 4, cY + 3, cY + 2, cY + 2, cY + 1, cY + 1, cY + 2, cY + 2, cY + 2, cY + 2, cY + 1, cY + 1, cY, cY - 1, cY - 2, cY - 2, cY - 3, cY - 3, cY - 4, cY - 4, cY - 5, cY - 5}};
                case DIAMOND:
                    return new int[][]{
                            {cX, cX - 5, cX, cX + 5},
                            {cY - 8, cY, cY + 8, cY}};
                case CLUB:
                    return new int[][]{
                            {cX, cX + 1, cX + 1, cX + 2, cX + 2, cX + 2, cX + 2, cX + 1, cX + 2, cX + 3, cX + 4, cX + 4, cX + 5, cX + 5, cX + 5, cX + 4, cX + 3, cX + 2, cX, cX, cX + 1, cX + 1, cX + 2, cX - 2, cX - 1, cX, cX, cX - 2, cX - 3, cX - 4, cX - 5, cX - 5, cX - 5, cX - 4, cX - 3, cX - 1, cX - 2, cX - 2, cX - 2, cX - 2, cX - 1},
                            {cY - 7, cY - 7, cY - 6, cY - 6, cY - 5, cY - 4, cY - 3, cY - 3, cY - 3, cY - 3, cY - 3, cY - 2, cY - 2, cY - 1, cY + 0, cY + 1, cY + 1, cY + 1, cY + 0, cY + 1, cY + 2, cY + 3, cY + 3, cY + 3, cY + 2, cY + 1, cY + 0, cY + 1, cY + 1, cY + 1, cY + 0, cY - 1, cY - 2, cY - 3, cY - 3, cY - 3, cY - 3, cY - 4, cY - 5, cY - 6, cY - 7}};
                default:
                    throw (new IllegalStateException());
            }
        }
    }

    public enum Value {
        ACE("A", 1), TWO("2", 2), THREE("3", 3), FOUR("4", 4), FIVE("5", 5), SIX("6", 6), SEVEN("7", 7),
        EIGHT("8", 8), NINE("9", 9), TEN("10", 10), JACK("J", 11), QUEEN("Q", 12), KING("K", 13);

        private String str;
        private int val;

        Value(String s, int v) {
            str = s;
            val = v;
        }

        public String getString() {
            return str;
        }

        public int getInteger() {
            return val;
        }
    }

}
