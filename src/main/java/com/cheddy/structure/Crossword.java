package com.cheddy.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author : Cheddy
 */
public class Crossword {

    public char[][] grid;
    public ArrayList<CrosswordClue> clues = new ArrayList<>();

    public String title, description;

    public Crossword(int width, int height) {
        if (width < 1 || height < 1)
            throw new IllegalArgumentException("Height and Width must be greater than 0!");
        this.grid = new char[height][width];
    }

    public void generate(ClueList cluelist) {
        description = cluelist.description();
        title = cluelist.title();
        int width = grid[0].length;
        int height = grid.length;

        ClueList newList = new ClueList(cluelist.title(), cluelist.description());
        for (Clue clue : cluelist.clues()) {
            if(clue.type() == ClueType.ALL || clue.type() == ClueType.CROSSWORD_ONLY) {
                int len = clue.word().length();
                if (len <= width || len <= height) {
                    clue.setWord(clue.word().toUpperCase().replaceAll("[^A-Z0-9]", ""));
                    newList.clues().add(clue);
                }
            }
        }
        cluelist = newList;

        Comparator<Clue> clueComparator = (o1, o2) -> {
            if(o1 != null && o2 != null){
                if(o1.word().length() > o2.word().length() && Math.random() > 0.3){
                    return 1;
                }
                return -1;
            }else if(o1 != null){
                return 1;
            }else if(o2 != null){
                return -1;
            }
            return 0;
        };

        Collections.shuffle(cluelist.clues());



        if (cluelist.clues().size() > 0) {
            Clue clue = cluelist.clues().get(0);
            int len = clue.word().length();
            int indent = (width - len) / 2;
            place(clue, indent, height / 2, true);
            cluelist.clues().remove(clue);

            int length = cluelist.clues().size();

            int[] orderX = new int[width];
            int[] orderY = new int[height];

            if(width % 2 == 1){
                int mid = width / 2;
                orderX[0] = mid;
                for(int i = 1; i <= mid; i++){
                    orderX[i * 2 - 1] = mid - i;
                    orderX[i * 2] = mid + i;
                }
            }else{
                int mid = width / 2;
                for(int i = 0; i < mid; i++){
                    orderX[i * 2] = mid + i;
                    orderX[i * 2 + 1] = mid - i - 1;
                }
            }

            if(height % 2 == 1){
                int mid = height / 2;
                orderY[0] = mid;
                for(int i = 1; i <= mid; i++){
                    orderY[i * 2 - 1] = mid - i;
                    orderY[i * 2] = mid + i;
                }
            }else{
                int mid = height / 2;
                for(int i = 0; i < mid; i++){
                    orderY[i * 2] = mid + i;
                    orderY[i * 2 + 1] = mid - i - 1;
                }
            }

            for (int i = 0; i < length; i++) {
                clue = cluelist.clues().get(0);
                cluelist.clues().remove(clue);

                boolean placed = false;

                for (CrosswordClue crosswordClue : clues) {
                    if (crosswordClue.clue().word().contains(clue.word()) || clue.word().contains(crosswordClue.clue().word())) {
                        placed = true;
                        break;
                    }
                }
                if (placed)
                    continue;

                String word = clue.word();
                len = word.length();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int r = orderY[y];
                        int c = orderX[x];
                        if (grid[r][c] != 0) {
                            for (int q = 0; q < len; q++) {
                                if (grid[r][c] == word.charAt(q)) {
                                    boolean left = true;
                                    boolean right = true;
                                    boolean up = true;
                                    boolean down = true;

                                    if (r - q < 0)
                                        up = false;
                                    else if ((r - q - 1) > -1 && grid[r - q - 1][c] != 0)
                                        up = false;

                                    if (c - q < 0)
                                        left = false;
                                    else if ((c - q - 1) > -1 && grid[r][c - q - 1] != 0)
                                        left = false;

                                    if (r + len - q > height)
                                        down = false;
                                    else if (r + len - q < height && grid[r + len - q][c] != 0)
                                        down = false;

                                    if (c + len - q > width)
                                        right = false;
                                    else if (c + len - q < width && grid[r][c + len - q] != 0)
                                        right = false;

                                    boolean adj1 = false;
                                    boolean adj2 = false;

                                    for (int b = 1; b < q + 1; b++) {
                                        if (up) {
                                            if (grid[r - b][c] != 0 && grid[r - b][c] != word.charAt(q - b)) {
                                                up = false;
                                            }
                                            if (grid[r - b][c] == word.charAt(q - b) && ((c + 1 < width && grid[r - b][c + 1] != 0) || (c - 1 > -1 && grid[r - b][c - 1] != 0))) {
                                                if (adj1)
                                                    up = false;
                                                adj1 = true;
                                            } else {
                                                adj1 = false;
                                                if (c + 1 < width && grid[r - b][c + 1] != 0)
                                                    up = false;
                                                if (c - 1 > -1 && grid[r - b][c - 1] != 0)
                                                    up = false;
                                            }
                                        }
                                        if (left) {
                                            if (grid[r][c - b] != 0 && grid[r][c - b] != word.charAt(q - b)) {
                                                left = false;
                                            }
                                            if (grid[r][c - b] == word.charAt(q - b) && ((r + 1 < width && grid[r + 1][c - b] != 0) || (r - 1 > -1 && grid[r - 1][c - b] != 0))) {
                                                if (adj2)
                                                    left = false;
                                                adj2 = true;
                                            } else {
                                                adj2 = false;
                                                if (r + 1 < width && grid[r + 1][c - b] != 0)
                                                    left = false;
                                                if (r - 1 > -1 && grid[r - 1][c - b] != 0)
                                                    left = false;
                                            }
                                        }
                                    }
                                    for (int b = q + 1; b < len; b++) {
                                        if (down) {
                                            if (grid[r + b - q][c] != 0 && grid[r + b - q][c] != word.charAt(b)) {
                                                down = false;
                                            }
                                            if (grid[r + b - q][c] == word.charAt(b) && ((c + 1 < width && grid[r + b - q][c + 1] != 0) || (c - 1 > -1 && grid[r + b - q][c - 1] != 0))) {
                                                if (adj1)
                                                    down = false;
                                                adj1 = true;
                                            } else {
                                                adj1 = false;
                                                if (c + 1 < width && grid[r + b - q][c + 1] != 0)
                                                    down = false;
                                                if (c - 1 > -1 && grid[r + b - q][c - 1] != 0)
                                                    down = false;
                                            }
                                        }
                                        if (right) {
                                            if (grid[r][c + b - q] != 0 && grid[r][c + b - q] != word.charAt(b)) {
                                                right = false;
                                            }
                                            if (grid[r][c + b - q] == word.charAt(b) && ((r + 1 < width && grid[r + 1][c + b - q] != 0) || (r - 1 > -1 && grid[r - 1][c + b - q] != 0))) {
                                                if (adj2)
                                                    right = false;
                                                adj2 = true;
                                            } else {
                                                adj2 = false;
                                                if (r + 1 < width && grid[r + 1][c + b - q] != 0)
                                                    right = false;
                                                if (r - 1 > -1 && grid[r - 1][c + b - q] != 0)
                                                    right = false;
                                            }
                                        }
                                    }

                                    if (left && right) {
                                        place(clue, c - q, r, true);
                                        placed = true;
                                        break;
                                    } else if (up && down) {
                                        place(clue, c, r - q, false);
                                        placed = true;
                                        break;
                                    }
                                }
                            }
                            if(placed)
                                break;
                        }
                    }
                    if(placed)
                        break;
                }
            }

            Collections.sort(clues);
            int number = 0;
            int prevX = -1;
            int prevY = -1;
            for(CrosswordClue crosswordClue : clues){
                if(crosswordClue.y() != prevY || crosswordClue.x() != prevX)
                    number++;
                crosswordClue.setNumber(number);
                prevX = crosswordClue.x();
                prevY = crosswordClue.y();
            }
        }
    }

    public void place(Clue clue, int x, int y, boolean horizontal) {
        String word = clue.word();
        int len = word.length();
        if (horizontal) {
            for (int i = 0; i < len; i++) {
                grid[y][x + i] = word.charAt(i);
            }
        } else {
            for (int i = 0; i < len; i++) {
                grid[y + i][x] = word.charAt(i);
            }
        }
        clues.add(new CrosswordClue(clue, x, y, horizontal));
    }

    public class CrosswordClue implements Comparable<CrosswordClue>{
        Clue clue;
        int number = -1, x, y;
        boolean horizontal;

        public CrosswordClue(Clue clue, int x, int y, boolean horizontal) {
            this.clue = clue;
            this.x = x;
            this.y = y;
            this.horizontal = horizontal;
        }

        public Clue clue() {
            return clue;
        }

        public void setClue(Clue clue) {
            this.clue = clue;
        }

        public int number() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int x() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int y() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean horizontal() {
            return horizontal;
        }

        public void setHorizontal(boolean horizontal) {
            this.horizontal = horizontal;
        }

        @Override
        public String toString() {
            return number() + ". " + clue().clue();
        }

        @Override
        public int compareTo(CrosswordClue o) {
            if (o == null)
                return -1;
            if (y() < o.y()){
                return -1;
            }else if(o.y() < y()) {
                return 1;
            }else{
                if (x() < o.x()){
                    return -1;
                }else if(o.x() < x()) {
                    return 1;
                }
            }
            return 0;
        }
    }

    @Override
    public String toString() {
        String string = "";
        for(int i = 0; i < grid.length; i++){
            for(int q = 0; q < grid[i].length; q++) {
                if(q != 0)
                    string += " ";
                if(grid[i][q] != 0)
                    string += grid[i][q];
                else
                    string += " ";
            }
            string += System.lineSeparator();
        }
        string += System.lineSeparator();

        for (CrosswordClue crosswordClue : clues){
            string += crosswordClue.toString();
            string += System.lineSeparator();
        }

        return string;
    }

    public String crosswordClueString(){
        String string = "";
        if(clues.size() > 0) {
            CrosswordClue first = clues.get(0);
            if(first.horizontal()) {
                string += "Across: " + System.lineSeparator() + System.lineSeparator();
                for (CrosswordClue crosswordClue : clues)
                    if(crosswordClue.horizontal())
                        string += crosswordClue.toString() + System.lineSeparator();

                string += System.lineSeparator() + System.lineSeparator() + "Down: " + System.lineSeparator() + System.lineSeparator();
                for (CrosswordClue crosswordClue : clues)
                    if(!crosswordClue.horizontal())
                        string += crosswordClue.toString() + System.lineSeparator();
            }else{
                string += "Down: " + System.lineSeparator() + System.lineSeparator();
                for (CrosswordClue crosswordClue : clues)
                    if(!crosswordClue.horizontal())
                        string += crosswordClue.toString() + System.lineSeparator();

                string += System.lineSeparator() + System.lineSeparator() + "Across: " + System.lineSeparator() + System.lineSeparator();
                for (CrosswordClue crosswordClue : clues)
                    if(crosswordClue.horizontal())
                        string += crosswordClue.toString() + System.lineSeparator();
            }
        }
        return string.trim();
    }
}
