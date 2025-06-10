package cube;

public enum Move
{
    U_CW {
        @Override
        public String toString()
        {
            return "U";
        }
    },
    U_CCW {
        @Override
        public String toString()
        {
            return "U'";
        }
    },
    R_CW {
        @Override
        public String toString()
        {
            return "R";
        }
    },
    R_CCW {
        @Override
        public String toString()
        {
            return "R'";
        }
    },
    F_CW {
        @Override
        public String toString()
        {
            return "F";
        }
    },
    F_CCW {
        @Override
        public String toString()
        {
            return "F'";
        }
    },
    D_CW {
        @Override
        public String toString()
        {
            return "D";
        }
    },
    D_CCW {
        @Override
        public String toString()
        {
            return "D'";
        }
    },
    L_CW {
        @Override
        public String toString()
        {
            return "L";
        }
    },
    L_CCW {
        @Override
        public String toString()
        {
            return "L'";
        }
    },
    B_CW {
        @Override
        public String toString()
        {
            return "B";
        }
    },
    B_CCW {
        @Override
        public String toString()
        {
            return "B'";
        }
    }
}
