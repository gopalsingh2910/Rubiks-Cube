package cube;

import algorithm.AStar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CubieCube
{

    /**
     *
     *              | U0     U1 |
     *              | U2     U3 |
     *     ---------------------------------------
     *      L16 L17 | F8     F9 | R4 R5 | B20 B21
     *      L18 L19 | F10   F11 | R6 R7 | B22 B23
     *     ---------------------------------------
     *              | D12   D13 |
     *              | D14   D15 |
     * <p>
     *      URF-DLB
     * <p>
     * */

    private Face[] cube;

    private final AStar aStar;

    public final static Move[] ALL_MOVES = { Move.F_CW, Move.R_CW, Move.U_CW, Move.B_CW, Move.L_CW, Move.D_CW, Move.F_CCW, Move.R_CCW, Move.U_CCW, Move.B_CCW, Move.L_CCW, Move.D_CCW };
    public final static Face[] ALL_FACES = {Face.UP, Face.RIGHT, Face.FRONT, Face.DOWN, Face.LEFT, Face.BACK};

    public static final Face[][] ALL_CORNERS = {{Face.UP, Face.RIGHT, Face.FRONT}, {Face.UP, Face.FRONT, Face.LEFT}, {Face.UP, Face.LEFT, Face.BACK}, {Face.UP, Face.BACK, Face.RIGHT},
            {Face.DOWN, Face.FRONT, Face.RIGHT}, {Face.DOWN, Face.LEFT, Face.FRONT}, {Face.DOWN, Face.BACK, Face.LEFT}, {Face.DOWN, Face.RIGHT, Face.BACK}};

    public static final int[][] ALL_CORNER_INDICES = {{3, 4, 9}, {2, 8, 17}, {0, 16, 21}, {1, 20, 5}, {13, 11, 6}, {12, 19, 10}, {14, 23, 18}, {15, 7, 22}};

    public final static int[] UP_CW = {2, 0, 3, 1, 20, 21, 6, 7, 4, 5, 10, 11, 12, 13, 14, 15, 8, 9, 18, 19, 16, 17, 22, 23};
    public final static int[] UP_CCW = {1, 3, 0, 2, 8, 9, 6, 7, 16, 17, 10, 11, 12, 13, 14, 15, 20, 21, 18, 19, 4, 5, 22, 23};
    public final static int[] RIGHT_CW = {0, 9, 2, 11, 6, 4, 7, 5, 8, 13, 10, 15, 12, 22, 14, 20, 16, 17, 18, 19, 3, 21, 1, 23};
    public final static int[] RIGHT_CCW = {0, 22, 2, 20, 5, 7, 4, 6, 8, 1, 10, 3, 12, 9, 14, 11, 16, 17, 18, 19, 15, 21, 13, 23};
    public final static int[] FRONT_CW = {0, 1, 19, 17, 2, 5, 3, 7, 10, 8, 11, 9, 6, 4, 14, 15, 16, 12, 18, 13, 20, 21, 22, 23};
    public final static int[] FRONT_CCW = {0, 1, 4, 6, 13, 5, 12, 7, 9, 11, 8, 10, 17, 19, 14, 15, 16, 3, 18, 2, 20, 21, 22, 23};
    public final static int[] DOWN_CW = {0, 1, 2, 3, 4, 5, 10, 11, 8, 9, 18, 19, 14, 12, 15, 13, 16, 17, 22, 23, 20, 21, 6, 7};
    public final static int[] DOWN_CCW = {0, 1, 2, 3, 4, 5, 22, 23, 8, 9, 6, 7, 13, 15, 12, 14, 16, 17, 10, 11, 20, 21, 18, 19};
    public final static int[] LEFT_CW = {23, 1, 21, 3, 4, 5, 6, 7, 0, 9, 2, 11, 8, 13, 10, 15, 18, 16, 19, 17, 20, 14, 22, 12};
    public final static int[] LEFT_CCW = {8, 1, 10, 3, 4, 5, 6, 7, 12, 9, 14, 11, 23, 13, 21, 15, 17, 19, 16, 18, 20, 2, 22, 0};
    public final static int[] BACK_CW = {5, 7, 2, 3, 4, 15, 6, 14, 8, 9, 10, 11, 12, 13, 16, 18, 1, 17, 0, 19, 22, 20, 23, 21};
    public final static int[] BACK_CCW = {18, 16, 2, 3, 4, 0, 6, 1, 8, 9, 10, 11, 12, 13, 7, 5, 14, 17, 15, 19, 21, 23, 20, 22};

    public static final int[] FACTORIAL = {1, 1, 2, 6, 24, 120, 720, 5040};

    public static final int[] EXPONENT = {1, 3, 9, 27, 81, 243, 729, 2187};

    public CubieCube(Face[] cube)
    {
        this.cube = cube;
        this.aStar = new AStar(this);
    }

    public Face[] getState() { return cube; }

    public void setState(final Face[] face)
    {
        this.cube = face;
    }

    public void setState(final Face face, final int i)
    {
        this.cube[i] = face;
    }

    public List<String> solve()
    {
        List<String> list = this.aStar.findPath();

        for(int i = 0; i < list.size(); i++) list.set(i, (i + 1) + "." + list.get(i));
        list.add(0, "0." + this + "Cube");

        return list;
    }

    /**
     * checks if the value sent as an input is valid or not
     * @return true if input is valid, else false
     */
    public boolean isValid()
    {
        int sum = 0;
        int[][] corners = getPermutationOrientation(cube);
        for(int i = 0; i < 8; i++) sum += corners[1][i];

        return (sum%3 == 0);
    }

    /**
     * checks if the state received as an input is a goal state or not
     * @param cubeState: representation of the cube
     * @return true if the state received as an input is considered one of the possible goal states else false
     */
    public static boolean isGoal(Face[] cubeState)
    {
        int[] arr = {0, 4, 8, 12, 16, 20};

        for(int i:arr) if(cubeState[i + 1] != cubeState[i] || cubeState[i + 2] != cubeState[i] || cubeState[i + 3] != cubeState[i]) return false;
        return true;
    }

    /**
     * function applies move param2 on state param1, uses helper function rotate
     * @param1 state: representation of the cube state
     * @param2 move: char representing type of rotation to be applied
     */
    public static Face[] applyMove(final Face[] cubeState, final Move move)
    {
        if (move == Move.U_CW) return rotate(cubeState, CubieCube.UP_CW);
        if (move == Move.R_CW) return rotate(cubeState, CubieCube.RIGHT_CW);
        if (move == Move.F_CW) return rotate(cubeState, CubieCube.FRONT_CW);
        if (move == Move.D_CW) return rotate(cubeState, CubieCube.DOWN_CW);
        if (move == Move.L_CW) return rotate(cubeState, CubieCube.LEFT_CW);
        if (move == Move.B_CW) return rotate(cubeState, CubieCube.BACK_CW);
        if (move == Move.U_CCW) return rotate(cubeState, CubieCube.UP_CCW);
        if (move == Move.R_CCW) return rotate(cubeState, CubieCube.RIGHT_CCW);
        if (move == Move.F_CCW) return rotate(cubeState, CubieCube.FRONT_CCW);
        if (move == Move.D_CCW) return rotate(cubeState, CubieCube.DOWN_CCW);
        if (move == Move.L_CCW) return rotate(cubeState, CubieCube.LEFT_CCW);
        if (move == Move.B_CCW) return rotate(cubeState, CubieCube.BACK_CCW);


        return cubeState;
    }

    /** helper method to applyMove function to implement rotations
     *
     * @return a version of the cube where the full rotation has been implemented.
     */
    public static Face[] rotate(final Face[] state, final int[] rot)
    {
        Face[] temp = new Face[24];
        for(int i = 0; i < 24; i++) temp[i] = state[rot[i]];

        return temp;
    }

    public static int[][] getPermutationOrientation(final Face[] face)
    {
        int[][] corner = new int[2][8];

        for(int i = 0; i < 8; i++)
        {
            loopj:
            for(int j = 0; j < 8; j++)
            {
                for(int k = 0; k < 3; k++)
                {
                    if(face[ALL_CORNER_INDICES[i][k]] == ALL_CORNERS[j][0] && face[ALL_CORNER_INDICES[i][(k + 1)%3]] == ALL_CORNERS[j][1] && face[ALL_CORNER_INDICES[i][(k + 2)%3]] == ALL_CORNERS[j][2])
                    {
                        corner[0][i] = j;
                        corner[1][i] = k;
                        break loopj;
                    }
                }
            }
        }

        return corner;
    }

    public static Face[] scramble()
    {
        int[][] corners = new int[2][8];

        Random random = new Random();
        List<Integer> cubeCorners = new ArrayList<>();
        for(int i = 0; i < 8; i++) cubeCorners.add(i);

        int sum = 0;
        for(int i = 0; i < 8; i++)
        {
            int rn = random.nextInt(cubeCorners.size());
            corners[0][i] = cubeCorners.get(rn);
            cubeCorners.remove(rn);
        }
        for(int i = 0; i < 7; i++)
        {
            corners[1][i] = random.nextInt(3);
            sum += corners[1][i];
        }
        corners[1][7] = 3-sum%3;

        return setCorner(corners);
    }

    public static Move findMove(Face[] parent, Face[] child)
    {
        for (Move pMove : ALL_MOVES)
        {
            boolean check=true;
            Face[] temp = applyMove(parent, pMove);
            for (int j = 0; j <= 23; j++)
            {
                assert temp != null;
                if (temp[j] != child[j])
                {
                    check = false;
                    break;
                }
            }

            if (check) return pMove;
        }

        return null;
    }

    public static Face[] setCorner(final int[][] corner)
    {
        Face[] face = new Face[24];

        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 3; j++) face[ALL_CORNER_INDICES[i][(j + corner[1][i])%3]] = ALL_CORNERS[corner[0][i]][j];
        }

        return face;
    }

    public static int cubeIndex(final int[][] corners)
    {
        int index = 0;
        int[] suffix = new int[8];
        Arrays.fill(suffix, 0);

        for(int i = 0; i < 8; i++)
        {
            for(int j = i + 1; j < 8; j++) if(corners[0][i] > corners[0][j]) suffix[i]++;
        }

        for(int i = 0; i < 7; i++) index += suffix[i] * FACTORIAL[7-i];
        index *= 2187;
        for(int i = 0; i < 7; i++) index += corners[1][i] * EXPONENT[i];

        return index;
    }

    public static CubieCube toCube(final String state)
    {
        Face[] cube = new Face[24];
        char[] cubeStr = state.toCharArray();
        for(int i = 0; i < 24; i++)
        {
            switch (cubeStr[i])
            {
                case 'W' -> cube[i] = Face.UP;
                case 'R' -> cube[i] = Face.RIGHT;
                case 'G' -> cube[i] = Face.FRONT;
                case 'Y' -> cube[i] = Face.DOWN;
                case 'O' -> cube[i] = Face.LEFT;
                case 'B' -> cube[i] = Face.BACK;
            }
        }

        return new CubieCube(cube);
    }

    public String toString()
    {
        StringBuilder state = new StringBuilder();
        for(Face f:cube) state.append(f.colorString());

        return state.toString();
    }
}
