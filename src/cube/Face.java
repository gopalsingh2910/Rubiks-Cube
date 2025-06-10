package cube;

import java.awt.*;

public enum Face
{
    UP {
        @Override
        public String toString()
        {
            return "U";
        }

        public String colorString() { return "W"; }

        public Color color() { return Color.WHITE; }

        public Face next() { return RIGHT; }

        public short index() { return 0; }
    },
    RIGHT {
        @Override
        public String toString()
        {
            return "R";
        }

        public String colorString() { return "R"; }

        public Color color() { return Color.RED; }

        public Face next() { return FRONT; }

        public short index() { return 1; }
    },
    FRONT {
        @Override
        public String toString()
        {
            return "F";
        }

        public String colorString() { return "G"; }

        public Color color() { return Color.GREEN; }

        public Face next() { return DOWN; }

        public short index() { return 2; }
    },
    DOWN {
        @Override
        public String toString()
        {
            return "D";
        }

        public String colorString() { return "Y"; }

        public Color color() { return Color.YELLOW; }

        public Face next() { return LEFT; }

        public short index() { return 3; }
    },
    LEFT {
        @Override
        public String toString()
        {
            return "L";
        }

        public String colorString() { return "O"; }

        public Color color() { return new Color(250, 154, 0); }

        public Face next() { return BACK; }

        public short index() { return 4; }
    },
    BACK {
        @Override
        public String toString()
        {
            return "B";
        }

        public String colorString() { return "B"; }

        public Color color() { return Color.BLUE; }

        public Face next() { return UP; }

        public short index() { return 5; }
    };

    public abstract String toString();

    public abstract String colorString();

    public abstract Color color();

    public abstract Face next();

    public abstract short index();
}
